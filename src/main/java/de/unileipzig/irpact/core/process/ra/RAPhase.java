package de.unileipzig.irpact.core.process.ra;

/**
 * @author Daniel Abitz
 */
public enum RAPhase {
    NONE(0),
    AWARENESS(1),
    FEASIBILITY(2),
    DECISION_MAKING(3),
    IMPEDED(4),
    ADOPTED(5);

    private final int ID;

    RAPhase(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public static RAPhase get(int id) {
        switch (id) {
            case 0:
                return NONE;
            case 1:
                return AWARENESS;
            case 2:
                return FEASIBILITY;
            case 3:
                return DECISION_MAKING;
            case 4:
                return IMPEDED;
            case 5:
                return ADOPTED;

            default:
                throw new IllegalArgumentException("unknown id: " + id);
        }
    }
}
