package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL_RA_UNCERT;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNameBasedUncertaintyWithConvergenceGroupAttribute implements InUncertaintyGroupAttribute {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, InRAProcessModel.thisName(), PROCESS_MODEL_RA_UNCERT, thisName());
        addEntry(res, thisClass(), "uncertDist");
        addEntry(res, thisClass(), "convergenceDist");
        addEntry(res, thisClass(), "names");
        addEntry(res, thisClass(), "cags");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InAttributeName[] names;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] uncertDist;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] convergenceDist;

    public InNameBasedUncertaintyWithConvergenceGroupAttribute() {
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        RAProcessModel processModel = (RAProcessModel) input;

        UnivariateDoubleDistribution uncert = parser.parseEntityTo(getUncertaintyDistribution());
        UnivariateDoubleDistribution conv = parser.parseEntityTo(getConvergenceDistribution());

        for(InConsumerAgentGroup inCag: getGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            for(InAttributeName attrName: getNames()) {
                processModel.getUncertaintySupplier().add(
                        cag,
                        attrName.getName(),
                        uncert,
                        conv
                );
                LOGGER.debug(
                        IRPSection.INITIALIZATION_PARAMETER,
                        "add UncertaintySupplier for group '{}', attribute '{}', uncertainity '{}', convergence '{}'",
                        cag.getName(),
                        attrName.getName(),
                        uncert.getName(),
                        conv.getName()
                );
            }
        }
    }

    public InConsumerAgentGroup[] getGroups() {
        return cags;
    }

    public void setGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public InAttributeName[] getNames() {
        return names;
    }

    public void setNames(InAttributeName[] names) {
        this.names = names;
    }

    public void setUncertaintyDistribution(InUnivariateDoubleDistribution uncertDist) {
        this.uncertDist = new InUnivariateDoubleDistribution[]{uncertDist};
    }

    public InUnivariateDoubleDistribution getUncertaintyDistribution() throws ParsingException {
        return ParamUtil.getInstance(uncertDist, "UncertaintyDistribution");
    }

    public void setConvergenceDistribution(InUnivariateDoubleDistribution convergenceDist) {
        this.convergenceDist = new InUnivariateDoubleDistribution[]{convergenceDist};
    }

    public InUnivariateDoubleDistribution getConvergenceDistribution() throws ParsingException {
        return ParamUtil.getInstance(convergenceDist, "ConvergenceDistribution");
    }
}
