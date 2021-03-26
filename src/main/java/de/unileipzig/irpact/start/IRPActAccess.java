package de.unileipzig.irpact.start;

import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.AnnualEntry;

/**
 * @author Daniel Abitz
 */
public interface IRPActAccess {

    CommandLineOptions getCommandLineOptions();

    AnnualEntry<InRoot> getInput();

    AnnualData<OutRoot> getOutput();
}
