package de.unileipzig.irpact.commons.distribution;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by daniel on 18.01.2022.
 * @since 0.0
 */
public enum RoundingMode {
    NONE(0) {
        @Override
        public double apply(double value) {
            return value;
        }
    },
    FLOOR(1) {
        @Override
        public double apply(double value) {
            return Math.floor(value);
        }
    },
    CEIL(2) {
        @Override
        public double apply(double value) {
            return Math.ceil(value);
        }
    },
    ROUND(3) {
        @Override
        public double apply(double value) {
            return Math.round(value);
        }
    };

    private final int ID;

    RoundingMode(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public abstract double apply(double value);

    public static RoundingMode get(int id) {
        for(RoundingMode mode: values()) {
            if(mode.getID() == id) {
                return mode;
            }
        }

        throw new IllegalArgumentException("unsupported id: " + id);
    }

    public static RoundingMode get(
            boolean modeNoRounding,
            boolean modeFloor,
            boolean modeCeil,
            boolean modeRound) {
        List<RoundingMode> modes = new ArrayList<>();
        if(modeNoRounding) modes.add(NONE);
        if(modeFloor) modes.add(FLOOR);
        if(modeCeil) modes.add(CEIL);
        if(modeRound) modes.add(ROUND);

        switch (modes.size()) {
            case 0:
                throw new IllegalArgumentException("no mode selected");

            case 1:
                return modes.get(0);

            default:
                List<String> names = modes.stream()
                        .map(Enum::name)
                        .collect(Collectors.toList());
                throw new IllegalArgumentException("multiple modes selected: " + names);
        }
    }
}
