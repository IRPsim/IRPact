package de.unileipzig.irpact.io.param.input.agent.population;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.simulation.InitializationData;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Definition
public class FixConsumerAgentPopulationSize implements PopulationSize {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubInitResSilently(thisClass(), res);
    }
    public static void applyRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
    }
    public static void applyRes0(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public int size;

    public static void initRes1(TreeAnnotationResource res) {
    }
    public static void applyRes1(TreeAnnotationResource res) {
    }
    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    public FixConsumerAgentPopulationSize() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cags, "consumerAgentGroups");
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public void setConsumerAgentGroups(Collection<? extends InConsumerAgentGroup> cags) {
        this.cags = cags.toArray(new InConsumerAgentGroup[0]);
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        InitializationData initData = ParamUtil.castTo(input, InitializationData.class);
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            if(initData.hasInitialNumberOfConsumerAgents(cag)) {
                throw new ParsingException("cag '" + cag.getName() + "' already has a population size: " + initData.getInitialNumberOfConsumerAgents(cag) + " (try to set: " + size + ")");
            }
            initData.setInitialNumberOfConsumerAgents(cag, size);
        }
    }
}
