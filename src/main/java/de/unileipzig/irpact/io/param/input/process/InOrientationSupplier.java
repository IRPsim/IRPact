package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InOrientationSupplier implements InEntity {

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
                res.getCachedElement("Datenerweiterung"),
                res.getCachedElement("Orientierung")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion für die Orientierung")
                .setGamsDescription("Verteilungsfunktion")
                .store(thisClass(), "distInOrientation");

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-KGs für Orientierung")
                .setGamsDescription("Gruppen, denen die Werte hinzugefügt werden sollen.")
                .store(thisClass(), "oriCags");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] distInOrientation;

    @FieldDefinition
    public InConsumerAgentGroup[] oriCags;

    public InOrientationSupplier() {
    }

    public InOrientationSupplier(String name, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        setDistribution(distribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setDistribution(InUnivariateDoubleDistribution distribution) {
        this.distInOrientation = new InUnivariateDoubleDistribution[]{distribution};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return InUtil.getInstance(distInOrientation, "Distribution");
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() {
        return oriCags;
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        RAProcessModel model = (RAProcessModel) input;

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            model.getOrientationSupplier().put(cag, dist);
        }
    }
}
