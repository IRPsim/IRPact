package de.unileipzig.irpact.misc.toymodels;

import de.unileipzig.irpact.commons.util.Args;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.base.AnnualEntry;

/**
 * @author Daniel Abitz
 */
public abstract class ToyModel {

    public abstract InRoot createRoot() throws Exception;

    public void run(String[] argsArr) throws Exception {
        Args args = new Args()
                .setAll(argsArr);
        args.set("--testMode");

        InRoot root = createRoot();
        AnnualData<InRoot> data = new AnnualData<>(root);
        data.getConfig().setYear(root.general.firstSimulationYear);
        AnnualEntry<InRoot> entry = data.get();

        Start.start(args.toArray(), entry);
    }
}
