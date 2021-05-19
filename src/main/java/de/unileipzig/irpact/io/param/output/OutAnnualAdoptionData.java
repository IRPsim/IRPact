package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.util.AdoptionAnalyser;
import de.unileipzig.irpact.core.util.AnnualAdoptionData;
import de.unileipzig.irpact.develop.XXXXXXXXX;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Factory;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.MapInfo;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.function.Function;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutAnnualAdoptionData implements OutEntity {

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
        putClassPath(res, thisClass(), thisName());
        addEntry(res, thisClass(), "year");
        addEntry(res, thisClass(), "adoptionsThisYear");
        addEntry(res, thisClass(), "adoptionsCumulativ");
        addEntry(res, thisClass(), "adoptionShareThisYear");
        addEntry(res, thisClass(), "adoptionShareCumulativ");
    }

    private static final String PREFIX = "year";

    public String _name;

    @FieldDefinition
    public int year;

    @FieldDefinition
    @MapInfo(key = OutConsumerAgentGroup.class, value = int.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<OutConsumerAgentGroup, Integer> adoptionsThisYear = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = OutConsumerAgentGroup.class, value = int.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<OutConsumerAgentGroup, Integer> adoptionsCumulativ = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = OutConsumerAgentGroup.class, value = double.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<OutConsumerAgentGroup, Double> adoptionShareThisYear = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = OutConsumerAgentGroup.class, value = double.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<OutConsumerAgentGroup, Double> adoptionShareCumulativ = new LinkedHashMap<>();

    public OutAnnualAdoptionData() {
    }

    public OutAnnualAdoptionData(int year) {
        setYear(year);
    }

    @Override
    public OutAnnualAdoptionData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutAnnualAdoptionData newCopy(CopyCache cache) {
        OutAnnualAdoptionData copy = new OutAnnualAdoptionData();
        copy._name = _name;
        copy.year = year;
        cache.copyMap(adoptionsThisYear, this.adoptionsThisYear);
        cache.copyMap(adoptionsCumulativ, this.adoptionsCumulativ);
        cache.copyMap(adoptionShareThisYear, this.adoptionShareThisYear);
        cache.copyMap(adoptionShareCumulativ, this.adoptionShareCumulativ);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setYear(int year) {
        _name = PREFIX + year;
        this.year = year;
    }

    public boolean hasYear() {
        return _name != null && _name.startsWith(PREFIX);
    }

    public int getYear() {
        if(hasYear()) {
            return year;
        } else {
            throw new NoSuchElementException();
        }
    }

    public Collection<? extends OutConsumerAgentGroup> getConsumerAgentGroups() {
        return adoptionsThisYear.keySet();
    }

    public void set(
            OutConsumerAgentGroup cag,
            int adoptionsThisYear,
            int adoptionsCumulativ,
            double adoptionShareThisYear,
            double adoptionShareCumulativ) {
        this.adoptionsThisYear.put(cag, adoptionsThisYear);
        this.adoptionsCumulativ.put(cag, adoptionsCumulativ);
        this.adoptionShareThisYear.put(cag, adoptionShareThisYear);
        this.adoptionShareCumulativ.put(cag, adoptionShareCumulativ);
    }

    public int getAdoptionsThisYear(OutConsumerAgentGroup cag) {
        return adoptionsThisYear.get(cag);
    }

    public int getAdoptionsCumulativ(OutConsumerAgentGroup cag) {
        return adoptionsCumulativ.get(cag);
    }

    public double getAdoptionShareThisYear(OutConsumerAgentGroup cag) {
        return adoptionShareThisYear.get(cag);
    }

    public double getAdoptionShareCumulativ(OutConsumerAgentGroup cag) {
        return adoptionShareCumulativ.get(cag);
    }

    @Override
    public String toString() {
        return "OutAnnualAdoptionData{" +
                "_name='" + _name + '\'' +
                ", year=" + year +
                ", adoptionsThisYear=" + adoptionsThisYear +
                ", adoptionsCumulativ=" + adoptionsCumulativ +
                ", adoptionShareThisYear=" + adoptionShareThisYear +
                ", adoptionShareCumulativ=" + adoptionShareCumulativ +
                '}';
    }

    @XXXXXXXXX
    public static void create(
            List<Number> years,
            Map<ConsumerAgentGroup, OutConsumerAgentGroup> outCags,
            ProductGroup pg,
            AdoptionAnalyser analyser,
            List<OutAnnualAdoptionData> out) {
        Set<Number> y = new TreeSet<>(years);
        y.addAll(analyser.getYears());
        for(Number nYear: y) {
            int year = nYear.intValue();
            OutAnnualAdoptionData outData = new OutAnnualAdoptionData(year);
            for(ConsumerAgentGroup cag: outCags.keySet()) {
                OutConsumerAgentGroup outCag = outCags.get(cag);
                AdoptionAnalyser.Result result = analyser.result(cag, pg);
                AdoptionAnalyser.Annual annual = result.get(year);
                outData.adoptionsThisYear.put(outCag, annual == null ? 0 : annual.getAdoptions());
                outData.adoptionShareThisYear.put(outCag, annual == null ? 0.0 : annual.getAdoptionShare());
                outData.adoptionsCumulativ.put(outCag, result.getAdoptions(year));
                outData.adoptionShareCumulativ.put(outCag, result.getAdoptionShare(year));
            }
            out.add(outData);
        }
    }
}
