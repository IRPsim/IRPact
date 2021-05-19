package de.unileipzig.irpact.core.simulation.tasks;

import de.unileipzig.irpact.commons.Nameable;

import java.util.Comparator;

/**
 * Marker interface for tasks.
 *
 * @author Daniel Abitz
 */
public interface Task extends Nameable {

    Comparator<Task> PRIORITY_COMPARATOR = Comparator.comparingInt(Task::priority);

    int DEFAULT_PRIORITY = 0;
    int MAX_PRIORITY = Integer.MIN_VALUE;

    default int priority() {
        return DEFAULT_PRIORITY;
    }
}
