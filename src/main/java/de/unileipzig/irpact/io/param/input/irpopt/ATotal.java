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
        name = "a_total",
        gams = @Gams(
                description = "Einlesen der zu optimierenden Jahre",
                identifier = "Jahreszahl",
                type = Constants.GAMS_INT,
                hidden = Constants.TRUE1
        )
)
public class ATotal implements InIRPactEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    public String _name;

    public ATotal() {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ATotal copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public ATotal newCopy(CopyCache cache) {
        ATotal copy = new ATotal();
        copy._name = _name;
        return copy;
    }
}
