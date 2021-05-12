package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.commons.Nameable;

import java.util.concurrent.TimeUnit;

/**
 * Interface for agent interaction.
 *
 * @author Daniel Abitz
 */
//erklaeren das attention und data zwei locks sind und attention > data ist von prio
public interface Acting {

    int getMaxNumberOfActions();

    long getActingOrder();

    void allowAttention();

    boolean tryAquireAttention();

    void actionPerformed();

    void releaseAttention();

    void aquireDataAccess();

    boolean tryAquireDataAccess();

    boolean tryAquireDataAccess(long time, TimeUnit unit) throws InterruptedException;

    void releaseDataAccess();

    //=========================
    //util
    //=========================

    /**
     * @author Daniel Abitz
     */
    enum AttentionResult {
        SUCCESS,
        TARGET_OCCUPIED,
        SELF_OCCUPIED;

        //Ansatz fuer dual-locks -> nutze eindeutige Lock-Reihenfolge
        //https://stackoverflow.com/questions/9555413/acquiring-multiple-locks-atomically-in-java
        private static AttentionResult aquireDoubleSidedAttention(Acting self, Acting target, boolean dataAccess) {
            if(self.getActingOrder() == target.getActingOrder()) {
                throw new IllegalStateException("same acting order");
            }

            final Acting first;
            final Acting second;
            if(self.getActingOrder() < target.getActingOrder()) {
                first = self;
                second = target;
            } else {
                first = target;
                second = self;
            }

            if(first.tryAquireAttention()) {
                if(second.tryAquireAttention()) {
                    if(dataAccess) {
                        first.aquireDataAccess(); //data lock
                        second.aquireDataAccess(); //data lock
                    }
                    return SUCCESS;
                } else {
                    first.releaseAttention();
                    return first == self
                            ? SELF_OCCUPIED
                            : TARGET_OCCUPIED;
                }
            } else {
                return first == self
                        ? SELF_OCCUPIED
                        : TARGET_OCCUPIED;
            }
        }

        private static AttentionResult aquireDoubleSidedAction(Acting self, Acting target, boolean dataAccess) {
            AttentionResult result = aquireDoubleSidedAttention(self, target, dataAccess);
            if(result == SUCCESS) {
                try {
                    self.actionPerformed();
                    target.actionPerformed();
                } finally {
                    target.releaseAttention();
                    self.releaseAttention();
                }
            }
            return result;
        }
    }

    static AttentionResult aquireDoubleSidedAttention(Acting self, Acting target) {
        return AttentionResult.aquireDoubleSidedAttention(self, target, false);
    }

    //Schreiben, dass bei SUCCESS die Locks noch vorhanden sind
    static AttentionResult aquireDoubleSidedAttentionAndDataAccess(Acting self, Acting target) {
        return AttentionResult.aquireDoubleSidedAttention(self, target, true);
    }

    static AttentionResult aquireDoubleSidedAction(Acting self, Acting target) {
        return AttentionResult.aquireDoubleSidedAction(self, target, false);
    }

    static AttentionResult aquireDoubleSidedActionAndDataAccess(Acting self, Acting target) {
        return AttentionResult.aquireDoubleSidedAction(self, target, true);
    }

    //Um deadlocks zu vermeiden, versuchen wir beide Locks zu erhalten.
    //Solange ein Lock fehl schlaegt, wird das andere freigelassen und der Versuch startet neu.
    //Erst wenn beide Locks erhalten wurden, bleiben sie bestehen.
    //Aus Sicherheit brechen wir bei interrupt sofort ab.

    static boolean waitForDoubleSidedDataAccess(Acting self, Acting target) {
        while(true) {
            if(self.tryAquireDataAccess()) {
                if(target.tryAquireDataAccess()) {
                    return true;
                } else {
                    self.releaseDataAccess();
                }
            }
        }
    }

    static boolean waitForDoubleSidedDataAccess(Acting self, Acting target, long time, TimeUnit unit) {
        while(true) {
            try {
                if(self.tryAquireDataAccess(time, unit)) {
                    try {
                        if(target.tryAquireDataAccess(time, unit)) {
                            return true;
                        } else {
                            self.releaseDataAccess();
                        }
                    } catch (InterruptedException e) {
                        self.releaseDataAccess();
                        return false;
                    }
                }
            } catch (InterruptedException e) {
                return false;
            }
        }
    }
}
