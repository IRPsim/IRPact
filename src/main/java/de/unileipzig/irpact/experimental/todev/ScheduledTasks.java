package de.unileipzig.irpact.experimental.todev;

import de.unileipzig.irpact.experimental.annotation.ToDevelop;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Verwaltet (chronologisch) sortierte (bzgl. K) Aufgaben (V).
 *
 * @param <K> Typ nach dem die Aufgaben (V) sortiert werden sollen
 * @param <V> Aufgabentyp
 * @author Daniel Abitz
 */
@ToDevelop
public final class ScheduledTasks<K, V> {

    private final Lock LOCK = new ReentrantLock();
    private final SortedMap<K, List<V>> scheduledTasks;

    /**
     * Erstellt eine neue Instanz, bei der alle verwendeten
     * Schluessel {@link Comparable} implementieren haben muessen.
     * 
     * @since 0.0
     */
    public ScheduledTasks() {
        this(null);
    }

    /**
     * Erstellt eine neue Instanz und nutzt den uebergebenden Comparator fuer Vergleiche.
     *
     * @param comparator Der zu nutzende Comparator. Falls null, muessen die Schluessel das
     *        {@link Comparable}-Interface implementieren.
     * @since 0.0
     */
    public ScheduledTasks(Comparator<? super K> comparator) {
        scheduledTasks = new TreeMap<>(comparator);
    }

    private void lock() {
        LOCK.lock();
    }

    private void unlock() {
        LOCK.unlock();
    }

    private List<V> getTasksFor(K pointInTime) {
        return scheduledTasks.computeIfAbsent(pointInTime, _pointInTime -> new ArrayList<>());
    }

    private static int count(Map<?, ? extends List<?>> map) {
        return map.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    private static <E> void transferToList(Map<?, ? extends List<E>> map, List<E> targetList) {
        for(List<E> list: map.values()) {
            targetList.addAll(list);
        }
        map.clear();
    }

    @SuppressWarnings("unchecked")
    private int compare(K first, K second) {
        Comparator<? super K> comp = scheduledTasks.comparator();
        if(comp == null) {
            Comparable<? super K> compFirst = (Comparable<? super K>) first;
            return compFirst.compareTo(second);
        } else {
            return comp.compare(first, second);
        }
    }

    private boolean hasPendingTasks(K current, boolean inclusive) {
        if(scheduledTasks.size() > 0) {
            int result = compare(scheduledTasks.firstKey(), current);
            if(result < 0) {
                return true;
            } else {
                return inclusive && result == 0;
            }
        } else {
            return false;
        }
    }

    private void transferFirstIfPending(List<V> pendingTasks, K current) {
        if(hasPendingTasks(current, true)) {
            K firstKey = scheduledTasks.firstKey();
            pendingTasks.addAll(scheduledTasks.remove(firstKey));
        }
    }

    private List<V> _removeAllPendingTasks(K current, boolean inclusive) {
        if(!hasPendingTasks(current, inclusive)) {
            return Collections.emptyList();
        }
        SortedMap<K, List<V>> pendingHeap = scheduledTasks.headMap(current);
        List<V> pendingTasks = new ArrayList<>(count(pendingHeap) + 1);
        transferToList(pendingHeap, pendingTasks);
        if(inclusive) {
            //heap arbeitet mit <, fuer <= neuen firstkey testen
            transferFirstIfPending(pendingTasks, current);
        }
        return pendingTasks;
    }

    /**
     * Entfernt alle anstehenden Aufgaben.
     *
     * @param current Zeitpunkt bis zu dem alle Aufgaben entfernt werden sollen (inklusive).
     * @return Liste aller anstehenden Aufgaben, kann leer sein aber nie null.
     * @since 0.0
     */
    public List<V> removeAllPendingTasks(K current) {
        return removeAllPendingTasks(current, true);
    }

    /**
     * Entfernt alle anstehenden Aufgaben.
     *
     * @param current Zeitpunkt bis zu dem alle Aufgaben entfernt werden sollen.
     * @param inclusive Ob der uebergebende Zeitpunkt ebenfalls mit beachtet werden soll (inklusiv vs exklusiv).
     * @return Liste aller anstehenden Aufgaben, kann leer sein aber nie null.
     * @since 0.0
     */
    public List<V> removeAllPendingTasks(K current, boolean inclusive) {
        lock();
        try {
            return _removeAllPendingTasks(current, inclusive);
        } finally {
            unlock();
        }
    }

    private List<V> _removeAllPendingTasks() {
        if(scheduledTasks.isEmpty()) {
            return Collections.emptyList();
        }
        List<V> pendingTasks = new ArrayList<>(numberOfTasks());
        transferToList(scheduledTasks, pendingTasks);
        return pendingTasks;
    }

    /**
     * Entfernt alle noch verbliebenden Aufgaben.
     *
     * @return Liste aller verbliebenden Aufgaben
     * @since 0.0
     */
    public List<V> removeAllPendingTasks() {
        lock();
        try {
            return _removeAllPendingTasks();
        } finally {
            unlock();
        }
    }

    private void _register(K pointInTime, V task) {
        getTasksFor(pointInTime).add(task);
    }

    /**
     * Regitriert die neue Aufgabe.
     *
     * @param pointInTime Zeitpunkt an dem die Aufgabe bearbeitet werden soll.
     * @param task zu bearbeitende Aufgabe
     * @since 0.0
     */
    public void register(K pointInTime, V task) {
        lock();
        try {
            _register(pointInTime, task);
        } finally {
            unlock();
        }
    }

    private boolean _remove(V task) {
        for(List<V> tasks: scheduledTasks.values()) {
            if(tasks.remove(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Entfernt die registrierte Aufgabe.
     *
     * @param task zu entfernende Aufgabe
     * @return true: entfernt, false: nicht gefunden
     * @since 0.0
     */
    public boolean remove(V task) {
        lock();
        try {
            return _remove(task);
        } finally {
            unlock();
        }
    }

    /**
     * Zaehlt die Anzahl der ausstehenden Aufgaben.
     *
     * @return Anzahl der noch ausstehenden Aufgaben, mindestens 0.
     * @since 0.0
     */
    public int numberOfTasks() {
        return scheduledTasks.values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }
}
