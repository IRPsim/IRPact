package de.unileipzig.irpact.commons.util;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;

/**
 * @author Daniel Abitz
 */
public final class RndGen implements RandomGenerator {

    protected Rnd rnd;

    public RndGen(Rnd rnd) {
        this.rnd = rnd;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    @Override
    public void setSeed(int[] seed) {
        setSeed(RandomGeneratorFactory.convertToLong(seed));
    }

    @Override
    public void setSeed(long seed) {
        rnd.setInitialSeed(seed);
    }

    @Override
    public void nextBytes(byte[] bytes) {
        rnd.nextBytes(bytes);
    }

    @Override
    public int nextInt() {
        return rnd.nextInt();
    }

    @Override
    public int nextInt(int n) {
        return rnd.nextInt();
    }

    @Override
    public long nextLong() {
        return rnd.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return rnd.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return rnd.nextFloat();
    }

    @Override
    public double nextDouble() {
        return rnd.nextDouble();
    }

    @Override
    public double nextGaussian() {
        return rnd.nextGaussian();
    }
}
