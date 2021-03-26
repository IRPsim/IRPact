package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
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
public class InIndividualAttributeBasedUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
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
        addEntry(res, thisClass(), "cagAttrs");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InIndividualAttributeBasedUncertaintyGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroupAttribute[] cagAttrs;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] uncertDist;

    public InIndividualAttributeBasedUncertaintyGroupAttribute() {
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

        for(InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            InConsumerAgentGroup inCag = parser.getRoot().findConsumerAgentGroup(inCagAttr);
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            String attrName = inCagAttr.getAttributeName();

            processModel.getUncertaintySupplier().add(
                    cag,
                    attrName,
                    uncert
            );
            LOGGER.debug(
                    IRPSection.INITIALIZATION_PARAMETER,
                    "add UncertaintySupplier for group '{}', attribute '{}', uncertainity '{}'",
                    cag.getName(),
                    attrName,
                    uncert.getName()
            );
        }
    }

    public InConsumerAgentGroupAttribute[] getAttributes() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cagAttrs, "ConsumerAgentGroupAttribute");
    }

    public void setAttributes(InConsumerAgentGroupAttribute[] cagAttrs) {
        this.cagAttrs = cagAttrs;
    }

    public InUnivariateDoubleDistribution getUncertaintyDistribution() throws ParsingException {
        return ParamUtil.getInstance(uncertDist, "UncertaintyDistribution");
    }

    public void setUncertaintyDistribution(InUnivariateDoubleDistribution uncertDist) {
        this.uncertDist = new InUnivariateDoubleDistribution[]{uncertDist};
    }
}