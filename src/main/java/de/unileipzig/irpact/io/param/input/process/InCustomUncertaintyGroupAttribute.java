package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@Definition
public class InCustomUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InCustomUncertaintyGroupAttribute.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Uncertainty"),
                res.getCachedElement("UncertaintyCustom")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-KGs")
                .setGamsDescription("-")
                .store(InCustomUncertaintyGroupAttribute.class, "cags");

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-Attribute")
                .setGamsDescription("-")
                .store(InCustomUncertaintyGroupAttribute.class, "names");

        res.newEntryBuilder()
                .setGamsIdentifier("Unsicherheit")
                .setGamsDescription("-")
                .store(InCustomUncertaintyGroupAttribute.class, "uncertDist");

        res.newEntryBuilder()
                .setGamsIdentifier("Konvergenz")
                .setGamsDescription("-")
                .store(InCustomUncertaintyGroupAttribute.class, "convergenceDist");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InCustomUncertaintyGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InAttributeName[] names;

    @FieldDefinition
    public InUnivariateDoubleDistribution uncertDist;

    @FieldDefinition
    public InUnivariateDoubleDistribution convergenceDist;

    public InCustomUncertaintyGroupAttribute() {
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

    public InAttributeName[] getNames() {
        return names;
    }

    public InUnivariateDoubleDistribution getUncertaintyDistribution() {
        return uncertDist;
    }

    public InUnivariateDoubleDistribution getConvergenceDistribution() {
        return convergenceDist;
    }
}
