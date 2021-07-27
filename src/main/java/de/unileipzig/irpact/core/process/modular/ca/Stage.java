package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;

/**
 * @author Daniel Abitz
 */
public enum Stage implements ChecksumComparable {
    PRE_INITIALIZATION(0),
    AWARENESS(1),
    FEASIBILITY(2),
    DECISION_MAKING(3),
    IMPEDED(4),
    ADOPTED(5);

    private final int ID;

    Stage(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int getChecksum() {
        return ID;
    }

    public static Stage get(int id) {
        switch (id) {
            case 0:
                return PRE_INITIALIZATION;
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
