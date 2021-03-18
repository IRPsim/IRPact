package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.util.Placeholder;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Placeholder
@Definition
public class InAutoUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

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
