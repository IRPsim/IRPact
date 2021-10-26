package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InQuantileRange implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE);
        addEntryWithDefaultAndDomain(res, thisClass(), "lowerBound", VALUE_0, DOMAIN_CLOSED_0_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "upperBound", VALUE_1, DOMAIN_CLOSED_0_1);
    }

    public static final InQuantileRange QUANTILE_0_10 = new InQuantileRange("DEFAULT_QUANTILE_0_10", 0, 0.1);
    public static final InQuantileRange QUANTILE_10_25 = new InQuantileRange("DEFAULT_QUANTILE_10_25", 0.1, 0.25);
    public static final InQuantileRange QUANTILE_25_50 = new InQuantileRange("DEFAULT_QUANTILE_25_50", 0.25, 0.5);
    public static final InQuantileRange QUANTILE_50_75 = new InQuantileRange("DEFAULT_QUANTILE_50_75", 0.5, 0.75);
    public static final InQuantileRange QUANTILE_75_90 = new InQuantileRange("DEFAULT_QUANTILE_75_90", 0.75, 0.9);
    public static final InQuantileRange QUANTILE_90_100 = new InQuantileRange("DEFAULT_QUANTILE_90_100", 0.9, 1);

    public String _name;

    @FieldDefinition
    public double lowerBound = 0;

    @FieldDefinition
    public double upperBound = 1;

    public InQuantileRange() {
    }

    public InQuantileRange(String name, double lowerBound, double upperBound) {
        setName(name);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    @Override
    public InQuantileRange copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InQuantileRange newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getUpperBound() {
        return upperBound;
    }
}
