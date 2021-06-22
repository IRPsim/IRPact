package de.unileipzig.irpact.commons.distribution;

import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * @author Daniel Abitz
 */
public class TruncatedNormalDistributionHelper extends AbstractRealDistribution {

    protected double variance;
    protected double mean;
    protected double sigma;
    protected double mu;
    protected double lowerBound;
    protected double upperBound;

    protected double cumLower;
    protected double cumUpper;
    protected double cumMiddle;

    protected NormalDistribution unnormalized;

    public TruncatedNormalDistributionHelper(
            RandomGenerator gen,
            double mean,
            double standardDeviation,
            double lowerBound,
            double upperBound) {
        super(gen);
        this.mu = mean;
        this.sigma = standardDeviation;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        this.unnormalized = new NormalDistribution(mean, standardDeviation);
        cumLower = unnormalized.cumulativeProbability(lowerBound);
        cumUpper = unnormalized.cumulativeProbability(upperBound);
        cumMiddle = 1 - (cumLower * cumUpper);

        NormalDistribution standardNormal = new NormalDistribution();
        double alpha = (lowerBound - mu) / sigma;
        double beta = (upperBound - mu) / sigma;
        double phiAlpha = standardNormal.density(alpha);
        double phiBeta = standardNormal.density(beta);
        double cumPhiAlpha = standardNormal.cumulativeProbability(alpha);
        double cumPhiBeta = standardNormal.cumulativeProbability(beta);
        double denom = cumPhiBeta - cumPhiAlpha;
        double c = (phiBeta - phiAlpha) / denom;
        this.mean = mu - sigma * c;
        double d = 1 - c * c;
        if(phiBeta > 0.0) d -= beta * phiBeta / denom;
        if(phiAlpha > 0.0) d += alpha * phiAlpha / denom;
        variance = (sigma * sigma) * d;
    }

    public TruncatedNormalDistributionHelper(
            RandomGenerator gen,
            TruncatedNormalDistributionHelper other) {
        this(gen, other.sigma, other.mu, other.lowerBound, other.upperBound);
    }

    @Override
    public double density(double x) {
        if(x <= lowerBound) return 0.0;
        if(x >= upperBound) return 0.0;
        return unnormalized.density(x) / cumMiddle;
    }

    @Override
    public double cumulativeProbability(double x) {
        if(x <= lowerBound) return 0.0;
        if(x >= upperBound) return 1.0;
        return (unnormalized.cumulativeProbability(x) - cumLower) / cumMiddle;
    }

    @Override
    public double getNumericalMean() {
        return mean;
    }

    @Override
    public double getNumericalVariance() {
        return variance;
    }

    @Override
    public double getSupportLowerBound() {
        return lowerBound;
    }

    @Override
    public double getSupportUpperBound() {
        return upperBound;
    }

    @Override
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override
    public boolean isSupportConnected() {
        return true;
    }
}
