package de.unileipzig.irpact.io.param.irpopt;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.HashMap;
import java.util.Map;

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

    @FieldDefinition(
            name = "S_DS_growth_absolute",
            gams = @GamsParameter(
                    description = "Bitte geben Sie hier das absolute j\u00e4hrliche Wachstum der Kundengruppe an",
                    identifier = "Absolutes Wachstum der Kundengruppe pro Jahr"
            )
    )
    @MapInfo(key = SideCustom.class, value = double.class, factory = @Factory(clazz = HashMap.class))
    public Map<SideCustom, Double> growthAbsolute = new HashMap<>();

    public ATotal() {
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ATotal copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public ATotal newCopy(CopyCache cache) {
        return new ATotal();
    }
}
