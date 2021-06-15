package de.unileipzig.irpact.scenarios.toymodels;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.scenarios.AbstractScenario;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.irpact.callbacks.GetResult;
import de.unileipzig.irptools.io.base.AnnualEntry;

import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractScenario {

    protected BiConsumer<InRoot, OutRoot> resultConsumer;

    public AbstractToyModel(BiConsumer<InRoot, OutRoot> resultConsumer) {
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
    }

    protected abstract void validate(InRoot inRoot, OutRoot outRoot);

    @Override
    protected void run(IRPArgs args, AnnualEntry<InRoot> entry) throws Throwable {
        GetResult callback = new GetResult("toymodel");
        Start.start(args.toArray(), entry, callback);
        resultConsumer.accept(callback.getInRoot(), callback.getOutRoot());
    }
}
