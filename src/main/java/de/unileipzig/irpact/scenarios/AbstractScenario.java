package de.unileipzig.irpact.scenarios;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.scenarios.toymodels.ToyModelUtil;
import de.unileipzig.irpact.start.irpact.IRPactScenario;
import de.unileipzig.irptools.io.base.AnnualEntry;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractScenario implements IRPactScenario {


    public AbstractScenario() {
    }

    public abstract int getId();

    public abstract String getName();

    public abstract InRoot createInRoot();

    protected IRPArgs initArgs() {
        return new IRPArgs();
    }

    protected void updateArgs(IRPArgs args) {
    }

    protected abstract void run(IRPArgs args, AnnualEntry<InRoot> entry) throws Throwable;

    public void run() throws Throwable {
        IRPArgs args = initArgs();
        updateArgs(args);
        InRoot inRoot = createInRoot();
        AnnualEntry<InRoot> entry = ToyModelUtil.buildEntry(inRoot);
        run(args, entry);
    }
}
