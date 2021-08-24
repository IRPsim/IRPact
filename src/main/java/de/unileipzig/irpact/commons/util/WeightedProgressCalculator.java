package de.unileipzig.irpact.commons.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
public class WeightedProgressCalculator implements ProgressCalculator {

    protected final Lock LOCK = new ReentrantLock();
    protected final Map<Integer, Double> phaseWeights = new HashMap<>();
    protected final Map<Integer, Double> phaseProgress = new HashMap<>();

    public WeightedProgressCalculator() {
        this(1);
    }

    public WeightedProgressCalculator(int phases) {
        setPhases(phases);
    }

    public void setPhases(int count) {
        phaseWeights.clear();
        phaseProgress.clear();
        for(int i = 1; i <= count; i++) {
            phaseWeights.put(i, 1.0);
            phaseProgress.put(i, 0.0);
        }
    }

    public int numberOfPhases() {
        return phaseWeights.size();
    }

    public boolean hasPhase(int phase) {
        return phaseWeights.containsKey(phase);
    }

    public boolean setWeight(int phase, double weight) {
        if(hasPhase(phase)) {
            if(weight < 0) weight = 0;
            phaseWeights.put(phase, weight);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void lock() {
        LOCK.lock();
    }

    @Override
    public void unlock() {
        LOCK.unlock();
    }

    @Override
    public void reset() {
        for(int phase = 1; phase <= numberOfPhases(); phase++) {
            phaseProgress.put(phase, 0.0);
        }
    }

    @Override
    public boolean setProgress(int phase, double progress) {
        if(hasPhase(phase)) {
            if(progress < 0) progress = 0;
            if(progress > 1) progress = 1;
            phaseProgress.put(phase, progress);
            return true;
        } else {
            return false;
        }
    }

    protected double getTotalWeight() {
        return phaseWeights.values()
                .stream()
                .mapToDouble(w -> w)
                .sum();
    }

    @Override
    public double getProgress() {
        double totalWeight = getTotalWeight();
        if(totalWeight == 0.0) {
            return 0.0;
        }

        double total = 0.0;
        for(int phase = 1; phase <= numberOfPhases(); phase++) {
            double phaseProgress = this.phaseProgress.get(phase);
            double weight = phaseWeights.get(phase);
            total += weight * phaseProgress;
        }
        return Math.min(1.0, total / totalWeight);
    }
}
