package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.output.agent.OutConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutAdoptionResult implements OutEntity {

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
        addEntry(res, thisClass(), "cag");
        addEntry(res, thisClass(), "adoptionsThisYear");
        addEntry(res, thisClass(), "adoptionsCumulativ");
        addEntry(res, thisClass(), "adoptionShareThisYear");
        addEntry(res, thisClass(), "adoptionShareCumulativ");
    }

    private static final String PREFIX = "Result";

    public String _name;

    @FieldDefinition
    public OutConsumerAgentGroup[] cag;

    @FieldDefinition
    public int adoptionsThisYear;

    @FieldDefinition
    public int adoptionsCumulativ;

    @FieldDefinition
    public double adoptionShareThisYear;

    @FieldDefinition
    public double adoptionShareCumulativ;

    public OutAdoptionResult() {
    }

    @Override
    public OutAdoptionResult copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutAdoptionResult newCopy(CopyCache cache) {
        OutAdoptionResult copy = new OutAdoptionResult();
        copy._name = _name;
        copy.cag = cache.copyArray(cag);
        copy.adoptionsThisYear = adoptionsThisYear;
        copy.adoptionsCumulativ = adoptionsCumulativ;
        copy.adoptionShareThisYear = adoptionShareThisYear;
        copy.adoptionShareCumulativ = adoptionShareCumulativ;
        return copy;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setName(OutConsumerAgentGroup cag) {
        setName(PREFIX + cag.getName());
    }

    public void setConsumerAgentGroup(OutConsumerAgentGroup cag) {
        this.cag = new OutConsumerAgentGroup[]{cag};
    }

    public OutConsumerAgentGroup getConsumerAgentGroup() throws ParsingException {
        return ParamUtil.getInstance(cag, "cag");
    }

    public void setAdoptionsThisYear(int adoptionsThisYear) {
        this.adoptionsThisYear = adoptionsThisYear;
    }

    public int getAdoptionsThisYear() {
        return adoptionsThisYear;
    }

    public void setAdoptionsCumulativ(int adoptionsCumulativ) {
        this.adoptionsCumulativ = adoptionsCumulativ;
    }

    public int getAdoptionsCumulativ() {
        return adoptionsCumulativ;
    }

    public void setAdoptionShareThisYear(double adoptionShareThisYear) {
        this.adoptionShareThisYear = adoptionShareThisYear;
    }

    public double getAdoptionShareThisYear() {
        return adoptionShareThisYear;
    }

    public void setAdoptionShareCumulativ(double adoptionShareCumulativ) {
        this.adoptionShareCumulativ = adoptionShareCumulativ;
    }

    public double getAdoptionShareCumulativ() {
        return adoptionShareCumulativ;
    }
}
