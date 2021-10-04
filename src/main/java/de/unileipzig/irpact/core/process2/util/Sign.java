package de.unileipzig.irpact.core.process2.util;

/**
 * @author Daniel Abitz
 */
public enum Sign {
    UNKNOWN(-1) {
        @Override
        public boolean eval(double x, double y) {
            throw new UnsupportedOperationException("unknown");
        }
    },
    EQUALS(0) {
        @Override
        public boolean eval(double x, double y) {
            return x == y;
        }
    },
    NOT_EQUALS(1) {
        @Override
        public boolean eval(double x, double y) {
            return x != y;
        }
    },
    LESS_THAN(2) {
        @Override
        public boolean eval(double x, double y) {
            return x < y;
        }
    },
    LESS_THAN_OR_EQUAL(3) {
        @Override
        public boolean eval(double x, double y) {
            return x <= y;
        }
    },
    GREATER_THAN(4) {
        @Override
        public boolean eval(double x, double y) {
            return x > y;
        }
    },
    GREATER_THAN_OR_EQUAL(5) {
        @Override
        public boolean eval(double x, double y) {
            return x >= y;
        }
    };

    private final int ID;

    Sign(int id) {
        ID = id;
    }

    public static Sign get(int id) {
        for(Sign sign: values()) {
            if(sign.id() == id) {
                return sign;
            }
        }
        return UNKNOWN;
    }

    public static boolean isValid(Sign sign) {
        return sign != null && sign != UNKNOWN;
    }

    public static boolean isNotValid(Sign sign) {
        return !isValid(sign);
    }

    public int id() {
        return ID;
    }

    public abstract boolean eval(double x, double y);
}
