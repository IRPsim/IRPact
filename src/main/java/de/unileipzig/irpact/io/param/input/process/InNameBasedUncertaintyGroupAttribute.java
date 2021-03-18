package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNameBasedUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Uncertainty"),
                res.getCachedElement("UncertaintyCustom")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-KGs")
                .setGamsDescription("-")
                .store(thisClass(), "cags");

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-Attribute")
                .setGamsDescription("-")
                .store(thisClass(), "names");

        res.newEntryBuilder()
                .setGamsIdentifier("Unsicherheit")
                .setGamsDescription("-")
                .store(thisClass(), "uncertDist");

        res.newEntryBuilder()
                .setGamsIdentifier("Konvergenz")
                .setGamsDescription("-")
                .store(thisClass(), "convergenceDist");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InAttributeName[] names;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] uncertDist;

    public InNameBasedUncertaintyGroupAttribute() {
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

        for(InConsumerAgentGroup inCag: getGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            for(InAttributeName attrName: getNames()) {
                processModel.getUncertaintySupplier().add(
                        cag,
                        attrName.getName(),
                        uncert
                );
                LOGGER.debug(
                        IRPSection.INITIALIZATION_PARAMETER,
                        "add UncertaintySupplier for group '{}', attribute '{}', uncertainity '{}'",
                        cag.getName(),
                        attrName.getName(),
                        uncert.getName()
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
}
