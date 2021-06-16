package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.util.scenarios.toymodels.ToyModelUtil;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactScenario;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractScenario implements IRPactScenario {


    public AbstractScenario() {
    }

    public abstract String getName();

    public abstract InRoot createInRoot();

    protected void updateArgs(IRPArgs args) {
    }

    public void run() throws Throwable {
        IRPArgs args = new IRPArgs();
        updateArgs(args);
        InRoot inRoot = createInRoot();
        AnnualEntry<InRoot> entry = ToyModelUtil.buildEntry(inRoot);
        run(args, entry);
    }

    protected abstract void run(IRPArgs args, AnnualEntry<InRoot> entry) throws Throwable;

    public void store(Path target) throws IOException {
        store(target, StandardCharsets.UTF_8);
    }

    public void store(Path target, Charset charset) throws IOException {
        InRoot root = createInRoot();
        PerennialData<InRoot> data = new PerennialData<>();
        data.add(2015, root);
        PerennialFile file = data.serialize(IRPact.getInputConverter(null));
        file.store(target, charset);
    }
}
