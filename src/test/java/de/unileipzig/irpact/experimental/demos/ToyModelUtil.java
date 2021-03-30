package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.AnnualEntry;
import org.junit.jupiter.api.Disabled;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModelUtil {

    public static AnnualData<InRoot> buildData(InRoot root) {
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.general.firstSimulationYear);
        return data;
    }

    public static AnnualEntry<InRoot> buildEntry(InRoot root) {
        return buildData(root).get();
    }
}
