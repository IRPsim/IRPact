package de.unileipzig.irpact.start.irpact;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.data.AnnualEntry;

/**
 * @author Daniel Abitz
 */
public interface IRPActAccess {

    MainCommandLineOptions getCommandLineOptions();

    AnnualEntry<InRoot> getInput();

    AnnualData<OutRoot> getOutput();
}
