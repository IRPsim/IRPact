package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Factory;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.MapInfo;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutAnnualData implements OutEntity {

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
        addEntry(res, thisClass());
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
    @MapInfo(key = InConsumerAgentGroup.class, value = int.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<InConsumerAgentGroup, Integer> adoptionsThisYear = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = InConsumerAgentGroup.class, value = int.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<InConsumerAgentGroup, Integer> adoptionsCumulativ = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = InConsumerAgentGroup.class, value = double.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<InConsumerAgentGroup, Double> adoptionShareThisYear = new LinkedHashMap<>();

    @FieldDefinition
    @MapInfo(key = InConsumerAgentGroup.class, value = double.class, factory = @Factory(clazz = LinkedHashMap.class))
    public Map<InConsumerAgentGroup, Double> adoptionShareCumulativ = new LinkedHashMap<>();

    public OutAnnualData() {
    }

    public OutAnnualData(int year) {
        setYear(year);
    }

    @Override
    public OutAnnualData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutAnnualData newCopy(CopyCache cache) {
        OutAnnualData copy = new OutAnnualData();
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

    public void set(
            InConsumerAgentGroup cag,
            int adoptionsThisYear,
            int adoptionsCumulativ,
            double adoptionShareThisYear,
            double adoptionShareCumulativ) {
        this.adoptionsThisYear.put(cag, adoptionsThisYear);
        this.adoptionsCumulativ.put(cag, adoptionsCumulativ);
        this.adoptionShareThisYear.put(cag, adoptionShareThisYear);
        this.adoptionShareCumulativ.put(cag, adoptionShareCumulativ);
    }
}
