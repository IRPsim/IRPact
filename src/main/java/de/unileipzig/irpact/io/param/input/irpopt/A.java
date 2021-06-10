package de.unileipzig.irpact.io.param.input.irpopt;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Gams;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "a",
        gams = @Gams(
                description = "Einlesen des zu optimierenden Jahres",
                identifier = "Jahreszahl",
                type = Constants.GAMS_INT,
                hidden = Constants.TRUE1
        )
)
public class A implements InIRPactEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    public String _name;

    public A() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public A copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public A newCopy(CopyCache cache) {
        A copy = new A();
        copy._name = _name;
        return copy;
    }
}
