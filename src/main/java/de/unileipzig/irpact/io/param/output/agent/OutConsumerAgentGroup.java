package de.unileipzig.irpact.io.param.output.agent;

import de.unileipzig.irpact.io.param.output.OutIRPactEntity;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition(template = DefinitionType.OUTPUT)
public class OutConsumerAgentGroup implements OutIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), thisName());
        addEntry(res, thisClass(), "adoptionsThisPeriod");
        addEntry(res, thisClass(), "initialAdoptionsThisPeriod");
        addEntry(res, thisClass(), "adoptionsCumulative");
        addEntry(res, thisClass(), "initialAdoptionsCumulative");
    }

    public String _name;

    @FieldDefinition
    public int adoptionsThisPeriod;

    @FieldDefinition
    public int initialAdoptionsThisPeriod;

    @FieldDefinition
    public int adoptionsCumulative;

    @FieldDefinition
    public int initialAdoptionsCumulative;

    public OutConsumerAgentGroup() {
    }

    public OutConsumerAgentGroup(String name) {
        setName(name);
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setAdoptionsThisPeriod(int adoptionsThisPeriod) {
        this.adoptionsThisPeriod = adoptionsThisPeriod;
    }

    public int getAdoptionsThisPeriod() {
        return adoptionsThisPeriod;
    }

    public void setInitialAdoptionsThisPeriod(int initialAdoptionsThisPeriod) {
        this.initialAdoptionsThisPeriod = initialAdoptionsThisPeriod;
    }

    public int getInitialAdoptionsThisPeriod() {
        return initialAdoptionsThisPeriod;
    }

    public void setAdoptionsCumulative(int adoptionsCumulative) {
        this.adoptionsCumulative = adoptionsCumulative;
    }

    public int getAdoptionsCumulative() {
        return adoptionsCumulative;
    }

    public void setInitialAdoptionsCumulative(int initialAdoptionsCumulative) {
        this.initialAdoptionsCumulative = initialAdoptionsCumulative;
    }

    public int getInitialAdoptionsCumulative() {
        return initialAdoptionsCumulative;
    }

    @Override
    public OutConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutConsumerAgentGroup newCopy(CopyCache cache) {
        OutConsumerAgentGroup copy = new OutConsumerAgentGroup();
        copy._name = _name;
        copy.adoptionsThisPeriod = adoptionsThisPeriod;
        copy.initialAdoptionsThisPeriod = initialAdoptionsThisPeriod;
        copy.adoptionsCumulative = adoptionsCumulative;
        copy.initialAdoptionsCumulative = initialAdoptionsCumulative;
        return copy;
    }
}
