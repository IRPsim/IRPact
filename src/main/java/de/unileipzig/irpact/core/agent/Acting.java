package de.unileipzig.irpact.core.agent;

import de.unileipzig.irpact.develop.PotentialProblem;

import java.util.concurrent.TimeUnit;

/**
 * Interface for agent interaction.
 *
 * @author Daniel Abitz
 */
//erklaeren das attention und data zwei locks sind und attention > data ist von prio
public interface Acting {

    int getMaxNumberOfActions();

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

        @PotentialProblem("endlos-lock, falls beim self.attention ein fehler auftritt?")
        private static AttentionResult aquireDoubleSidedAttention(Acting self, Acting target, boolean dataAccess) {
            if(target.tryAquireAttention()) {
                if(self.tryAquireAttention()) {
                    if(dataAccess) {
                        self.aquireDataAccess(); //data lock
                        target.aquireDataAccess(); //data lock
                    }
                    return SUCCESS;
                } else {
                    target.releaseAttention();
                    return SELF_OCCUPIED;
                }
            } else {
                return TARGET_OCCUPIED;
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
