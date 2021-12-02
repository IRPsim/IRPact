package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE)
public class InQuantileRange implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    public static final InQuantileRange QUANTILE_0_10 = new InQuantileRange("DEFAULT_QUANTILE_0_10", 0, 0.1);
    public static final InQuantileRange QUANTILE_10_25 = new InQuantileRange("DEFAULT_QUANTILE_10_25", 0.1, 0.25);
    public static final InQuantileRange QUANTILE_25_50 = new InQuantileRange("DEFAULT_QUANTILE_25_50", 0.25, 0.5);
    public static final InQuantileRange QUANTILE_50_75 = new InQuantileRange("DEFAULT_QUANTILE_50_75", 0.5, 0.75);
    public static final InQuantileRange QUANTILE_75_90 = new InQuantileRange("DEFAULT_QUANTILE_75_90", 0.75, 0.9);
    public static final InQuantileRange QUANTILE_90_100 = new InQuantileRange("DEFAULT_QUANTILE_90_100", 0.9, 1);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed01Domain = true,
            intDefault = 0
    )
    public double lowerBound = 0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed01Domain = true,
            intDefault = 1
    )
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
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
