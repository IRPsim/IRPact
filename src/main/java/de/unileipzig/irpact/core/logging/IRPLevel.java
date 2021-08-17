package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Level;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.ChecksumValue;
import de.unileipzig.irpact.commons.checksum.Checksums;

/**
 * @author Daniel Abitz
 */
public enum IRPLevel implements ChecksumComparable {
    OFF(0, Level.OFF, null),
    TRACE(1, Level.TRACE, org.slf4j.event.Level.TRACE),
    DEBUG(2, Level.DEBUG, org.slf4j.event.Level.DEBUG),
    INFO(3, Level.INFO, org.slf4j.event.Level.INFO),
    WARN(4, Level.WARN, org.slf4j.event.Level.WARN),
    ERROR(5, Level.ERROR, org.slf4j.event.Level.ERROR),
    ALL(6, Level.ALL, null);

    @ChecksumValue
    private final int LEVEL_ID;
    private final Level LOGBACK_LEVEL;
    private final org.slf4j.event.Level SLF4J_LEVEL;

    IRPLevel(int id, Level logbackLevel, org.slf4j.event.Level slf4jLevel) {
        LEVEL_ID = id;
        LOGBACK_LEVEL = logbackLevel;
        SLF4J_LEVEL = slf4jLevel;
    }

    public int getLevelId() {
        return LEVEL_ID;
    }

    public Level toLogbackLevel() {
        return LOGBACK_LEVEL;
    }

    public org.slf4j.event.Level toSlf4jLevel() {
        if(SLF4J_LEVEL == null) {
            throw new IllegalStateException("level '" + name() + "' not supported");
        } else {
            return SLF4J_LEVEL;
        }
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(LEVEL_ID);
    }

    public static IRPLevel getDefault() {
        return ALL;
    }

    public static IRPLevel get(int levelId) {
        for(IRPLevel level: values()) {
            if(level.getLevelId() == levelId) {
                return level;
            }
        }
        return null;
    }
}
