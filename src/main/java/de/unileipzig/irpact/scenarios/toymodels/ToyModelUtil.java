package de.unileipzig.irpact.scenarios.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.AnnualEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public final class ToyModelUtil {

    protected static final Map<String, InAttributeName> NAMES = new HashMap<>();

    private ToyModelUtil() {
    }

    public static InAttributeName getAttribute(String name) {
        return NAMES.computeIfAbsent(name, InAttributeName::new);
    }

    public static AnnualData<InRoot> buildData(InRoot root) {
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.general.firstSimulationYear);
        return data;
    }

    public static AnnualEntry<InRoot> buildEntry(InRoot root) {
        return buildData(root).get();
    }
}
