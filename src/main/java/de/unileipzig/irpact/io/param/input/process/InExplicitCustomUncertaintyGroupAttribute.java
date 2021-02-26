package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.I_InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public class InExplicitCustomUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InExplicitCustomUncertaintyGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public I_InConsumerAgentGroupAttribute[] cagAttrs;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] uncertDist;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] convergenceDist;

    public InExplicitCustomUncertaintyGroupAttribute() {
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

        for(I_InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            InConsumerAgentGroup inCag = parser.getRoot().findConsumerAgentGroup(inCagAttr.getConsumerAgentGroupName());
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            String attrName = inCagAttr.getAttributeName();

            processModel.getUncertaintySupplier().add(
                    cag,
                    attrName,
                    uncert,
                    conv
            );
            LOGGER.debug(
                    IRPSection.INITIALIZATION_PARAMETER,
                    "add UncertaintySupplier for group '{}', attribute '{}', uncertainity '{}', convergence '{}'",
                    cag.getName(),
                    attrName,
                    uncert.getName(),
                    conv.getName()
            );
        }
    }

    public I_InConsumerAgentGroupAttribute[] getAttributes() throws ParsingException {
        return InUtil.getArray(cagAttrs, "ConsumerAgentGroupAttribute");
    }

    public InUnivariateDoubleDistribution getUncertaintyDistribution() throws ParsingException {
        return InUtil.getInstance(uncertDist, "UncertaintyDistribution");
    }

    public InUnivariateDoubleDistribution getConvergenceDistribution() throws ParsingException {
        return InUtil.getInstance(convergenceDist, "ConvergenceDistribution");
    }
}
