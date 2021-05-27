package de.unileipzig.irpact.io.param.output.xDEP;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.util.AdoptionAnalyser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InGeneralConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Map;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Deprecated
@Definition(copy = InGeneralConsumerAgentGroup.class)
public class OutGeneralConsumerAgentGroup implements OutConsumerAgentGroup {

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
        addEntry(res, thisClass(), "adoptionsThisYear");
        addEntry(res, thisClass(), "adoptionsCumulativ");
        addEntry(res, thisClass(), "adoptionShareThisYear");
        addEntry(res, thisClass(), "adoptionShareCumulativ");
    }

    public String _name;

    @FieldDefinition
    public int adoptionsThisYear;

    @FieldDefinition
    public int adoptionsCumulativ;

    @FieldDefinition
    public double adoptionShareThisYear;

    @FieldDefinition
    public double adoptionShareCumulativ;

    public OutGeneralConsumerAgentGroup() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
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

    @Override
    public OutGeneralConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutGeneralConsumerAgentGroup newCopy(CopyCache cache) {
        OutGeneralConsumerAgentGroup copy = new OutGeneralConsumerAgentGroup();
        copy._name = _name;

        return copy;
    }

    @Override
    public String toString() {
        return "OutGeneralConsumerAgentGroup{" +
                "_name='" + _name + '\'' +
                ", adoptionsThisYear=" + adoptionsThisYear +
                ", adoptionsCumulativ=" + adoptionsCumulativ +
                ", adoptionShareThisYear=" + adoptionShareThisYear +
                ", adoptionShareCumulativ=" + adoptionShareCumulativ +
                '}';
    }

    public static void create(
            Collection<ConsumerAgentGroup> cags,
            int year,
            ProductGroup pg,
            AdoptionAnalyser analyser,
            Map<ConsumerAgentGroup, OutConsumerAgentGroup> out) {
        for(ConsumerAgentGroup cag: cags) {
            OutGeneralConsumerAgentGroup outCag = new OutGeneralConsumerAgentGroup();
            outCag.setName(cag.getName());

            AdoptionAnalyser.Annual annual = analyser.annual(year, cag, pg);
            outCag.setAdoptionsThisYear(annual.getAdoptions());
            outCag.setAdoptionShareThisYear(annual.getAdoptionShare());

            AdoptionAnalyser.Result result = analyser.result(cag, pg);
            outCag.setAdoptionsCumulativ(result.getAdoptions(year));
            outCag.setAdoptionShareCumulativ(result.getAdoptionShare(year));

            out.put(cag, outCag);
        }
    }
}
