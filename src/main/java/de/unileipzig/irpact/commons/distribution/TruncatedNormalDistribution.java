package de.unileipzig.irpact.commons.distribution;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.RndGen;
import org.apache.commons.math3.distribution.AbstractRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * @author Daniel Abitz
 */
public class TruncatedNormalDistribution
        extends AbstractRealDistribution
        implements Nameable, BoundedUnivariateDoubleDistribution, ChecksumComparable {

    protected String name;

    protected final NormalDistribution normal;
    protected double mu;
    protected double sigma;
    protected double a;
    protected double b;
    protected boolean changed = true;

    protected double mean;
    protected double variance;

    protected double Z;
    protected double PhiAlpha;

    public TruncatedNormalDistribution() {
        super(new RndGen(null));
        this.normal = new NormalDistribution(getRandomGenerator(), 0, 1);
    }

    public TruncatedNormalDistribution(
            Rnd rnd,
            double mu,
            double sigma,
            double a,
            double b) {
        this(new RndGen(rnd), mu, sigma, a, b);
    }

    protected TruncatedNormalDistribution(
            RndGen gen,
            double mu,
            double sigma,
            double a,
            double b) {
        super(gen);
        this.normal = new NormalDistribution(getRandomGenerator(), 0, 1);
        this.mu = mu;
        this.sigma = sigma;
        this.a = a;
        this.b = b;
        initalize();
    }

    //=========================
    //AbstractRealDistribution
    //=========================

    public Rnd getRandom() {
        return getRandomGenerator().getRnd();
    }

    public void setRandom(Rnd rnd) {
        getRandomGenerator().setRnd(rnd);
        resetInitalized();
    }

    public RndGen getRandomGenerator() {
        return (RndGen) random;
    }

    public void initalize() {
        if(isInitalized()) {
            return;
        }

        if(getRandomGenerator().getRnd() == null) {
            throw new NullPointerException("missing random instance");
        }

        double alpha = (a - mu) / sigma;
        double beta = (b - mu) / sigma;
        double phiAlpha = normal.density(alpha);
        double phiBeta = normal.density(beta);
        this.PhiAlpha = normal.cumulativeProbability(alpha);
        double PhiBeta = normal.cumulativeProbability(beta);
        this.Z = PhiBeta - PhiAlpha;
        this.mean = mu + (((phiAlpha - phiBeta) / Z) * sigma);

        double temp0 = (alpha*phiAlpha - beta*phiBeta) / Z;
        double temp1 = (phiAlpha - phiBeta) / Z;
        this.variance = sigma*sigma * (1 + temp0 - temp1*temp1);

        this.Z = normal.cumulativeProbability(beta) - normal.cumulativeProbability(alpha);
    }

    public boolean isInitalized() {
        return !changed;
    }

    public boolean isNotInitalized() {
        return changed;
    }

    protected void checkInitalized() {
        if(isNotInitalized()) {
            throw new IllegalStateException("not initalized");
        }
    }

    protected void resetInitalized() {
        changed = true;
    }

    protected double xi(double x) {
        return (x - mu) / sigma;
    }

    @Override
    public double density(double x) {
        checkInitalized();
        if(x < a) return 0.0;
        if(x > b) return 0.0;
        return normal.density(xi(x)) / (sigma * Z);
    }

    @Override
    public double cumulativeProbability(double x) {
        checkInitalized();
        if(x < a) return 0.0;
        if(x > b) return 1.0;
        return (normal.cumulativeProbability(xi(x)) - PhiAlpha) / Z;
    }

    public double getMu() {
        return mu;
    }

    public void setMu(double mu) {
        this.mu = mu;
        resetInitalized();
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        resetInitalized();
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
        return a;
    }

    public void setSupportLowerBound(double a) {
        this.a = a;
        resetInitalized();
    }

    @Override
    public double getSupportUpperBound() {
        return b;
    }

    public void setSupportUpperBound(double b) {
        this.b = b;
        resetInitalized();
    }

    @Override
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    @Override
    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    @Override
    public boolean isSupportConnected() {
        return true;
    }

    @Override
    public double sample() {
        checkInitalized();
        return super.sample();
    }

    //=========================
    //IRPact implementation
    //=========================

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getLowerBound() {
        return getSupportLowerBound();
    }

    @Override
    public double getUpperBound() {
        return getSupportUpperBound();
    }

    @Override
    public boolean isLowerBoundInclusive() {
        return isSupportLowerBoundInclusive();
    }

    @Override
    public boolean isUpperBoundInclusive() {
        return isSupportUpperBoundInclusive();
    }

    @Override
    public double drawDoubleValue() {
        checkInitalized();
        return sample();
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                name,
                getRandom(),
                mu,
                sigma,
                a,
                b
        );
    }
}
