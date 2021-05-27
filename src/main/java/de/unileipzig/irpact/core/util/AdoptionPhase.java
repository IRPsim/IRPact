package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

/**
 * @author Daniel Abitz
 */
public enum AdoptionPhase implements ChecksumComparable {
    UNKNOWN(-1),
    START_MID(0),
    MID_END(1),
    END_START(2);

    private final int ID;

    AdoptionPhase(int id) {
        ID = id;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(ID);
    }

    public int getId() {
        return ID;
    }

    public boolean isValid() {
        return this != UNKNOWN;
    }

    public static AdoptionPhase get(int id) {
        switch (id) {
            case -1:
                return UNKNOWN;
            case 0:
                return START_MID;
            case 1:
                return MID_END;
            case 2:
                return END_START;
            default:
                throw new IllegalArgumentException("unknown id: " + id);
        }
    }
}
