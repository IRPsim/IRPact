package de.unileipzig.irpact.core.log;

import ch.qos.logback.classic.Level;

/**
 * @author Daniel Abitz
 */
public enum IRPLevel {
    OFF(IRPLevelID.OFF_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.OFF;
        }
    },
    TRACE(IRPLevelID.TRACE_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.OFF;
        }
    },
    DEBUG(IRPLevelID.DEBUG_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.DEBUG;
        }
    },
    INFO(IRPLevelID.INFO_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.INFO;
        }
    },
    WARN(IRPLevelID.WARN_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.WARN;
        }
    },
    ERROR(IRPLevelID.ERROR_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.ERROR;
        }
    },
    ALL(IRPLevelID.ALL_ID) {
        @Override
        public Level toLogbackLevel() {
            return Level.ALL;
        }
    };

    private final int LEVEL_ID;

    IRPLevel(int id) {
        LEVEL_ID = id;
    }

    public int getLevelId() {
        return LEVEL_ID;
    }

    public abstract Level toLogbackLevel();

    public static String getDomain() {
        return "[" + OFF.getLevelId() + ", " + + ALL.LEVEL_ID + "]";
    }

    public static String getDefaultAsString() {
        return Integer.toString(getDefault().getLevelId());
    }

    public static IRPLevel getDefault() {
        return ALL;
    }

    public static IRPLevel get(int levelId) {
        switch (levelId) {
            case IRPLevelID.OFF_ID:
                return OFF;
            case IRPLevelID.TRACE_ID:
                return TRACE;
            case IRPLevelID.DEBUG_ID:
                return DEBUG;
            case IRPLevelID.INFO_ID:
                return INFO;
            case IRPLevelID.WARN_ID:
                return WARN;
            case IRPLevelID.ERROR_ID:
                return ERROR;
            case IRPLevelID.ALL_ID:
                return ALL;
            default:
                return null;
        }
    }
}
