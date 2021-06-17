package de.unileipzig.irpact.util.scenarios.toymodels;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.data.AnnualEntry;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public final class ToyModelUtil {

    public static final BiConsumer<InRoot, OutRoot> IGNORE = (inRoot, outRoot) -> {};

    private ToyModelUtil() {
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
