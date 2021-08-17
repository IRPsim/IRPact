package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implements a binary task with predefined tasks.
 *
 * @author Daniel Abitz
 */
public abstract class PredefinedBinaryTask extends JsonBasedBinaryTask {

    public static final String TASK_KEY = "t";
    public static final String ID_KEY = "d";

    public static final int NO_TASK = -1;

    public PredefinedBinaryTask(ObjectNode node) {
        super(node);
        setID();
    }

    protected void setID() {
        root.put(ID_KEY, getID());
    }

    public void setTask(int task) {
        root.put(TASK_KEY, task);
    }

    public int getTask() {
        JsonNode n = root.get(TASK_KEY);
        if(n == null || !n.isNumber()) {
            return NO_TASK;
        } else {
            return n.intValue();
        }
    }
}
