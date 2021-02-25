package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAutoUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InAutoUncertaintyGroupAttribute.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Uncertainty"),
                res.getCachedElement("UncertaintyAuto")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("---")
                .setGamsDescription("Platzhalter")
                .store(InAutoUncertaintyGroupAttribute.class, "placeholderAutoUncert");
    }

    public String _name;

    @FieldDefinition
    public double placeholderAutoUncert;

    public InAutoUncertaintyGroupAttribute() {
    }

    @Override
    public String getName() {
        return _name;
    }
}
