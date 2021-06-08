package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public enum AdoptionPhase implements ChecksumComparable {
    UNKNOWN(-1),
    INITIAL(0),
    START_MID(1),
    MID_END(2),
    END_START(3);

    public static final List<AdoptionPhase> VALID_PHASES = Arrays.asList(INITIAL, START_MID, MID_END, END_START);

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

    public boolean isInvalid() {
        return this == UNKNOWN;
    }

    public AdoptionPhase requiresValid() {
        if(isInvalid()) {
            throw new IllegalStateException("invalid");
        }
        return this;
    }

    public boolean isInitial() {
        return this == INITIAL;
    }

    public static AdoptionPhase get(int id) {
        switch (id) {
            case -1:
                return UNKNOWN;
            case 0:
                return INITIAL;
            case 1:
                return START_MID;
            case 2:
                return MID_END;
            case 3:
                return END_START;
            default:
                throw new IllegalArgumentException("unknown id: " + id);
        }
    }
}
