package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Level;

/**
 * @author Daniel Abitz
 */
public enum IRPLevel {
    OFF(0) {
        @Override
        public Level toLogbackLevel() {
            return Level.OFF;
        }
    },
    TRACE(1) {
        @Override
        public Level toLogbackLevel() {
            return Level.OFF;
        }
    },
    DEBUG(2) {
        @Override
        public Level toLogbackLevel() {
            return Level.DEBUG;
        }
    },
    INFO(3) {
        @Override
        public Level toLogbackLevel() {
            return Level.INFO;
        }
    },
    WARN(4) {
        @Override
        public Level toLogbackLevel() {
            return Level.WARN;
        }
    },
    ERROR(5) {
        @Override
        public Level toLogbackLevel() {
            return Level.ERROR;
        }
    },
    ALL(6) {
        @Override
        public Level toLogbackLevel() {
            return Level.ALL;
        }
    };

    public static final int OFF_ID = 0;
    public static final int TRACE_ID = 1;
    public static final int INFO_ID = 2;
    public static final int DEBUG_ID = 3;
    public static final int WARN_ID = 4;
    public static final int ERROR_ID = 5;
    public static final int ALL_ID = 6;

    private final int LEVEL_ID;

    IRPLevel(int id) {
        LEVEL_ID = id;
    }

    public int getLevelId() {
        return LEVEL_ID;
    }

    public abstract Level toLogbackLevel();

    public static String getDefaultAsString() {
        return Integer.toString(getDefault().getLevelId());
    }

    public static IRPLevel getDefault() {
        return ALL;
    }

    public static IRPLevel get(int levelId) {
        switch (levelId) {
            case OFF_ID:
                return OFF;
            case TRACE_ID:
                return TRACE;
            case DEBUG_ID:
                return DEBUG;
            case INFO_ID:
                return INFO;
            case WARN_ID:
                return WARN;
            case ERROR_ID:
                return ERROR;
            case ALL_ID:
                return ALL;
            default:
                return null;
        }
    }
}
