package de.unileipzig.irpact.util.scenarios.pvact.toymodels;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.Start3;
import de.unileipzig.irpact.start.irpact.callbacks.GetInputAndOutput;
import de.unileipzig.irpact.util.scenarios.pvact.AbstractPVactScenario;
import de.unileipzig.irptools.io.perennial.PerennialData;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractPVactScenario {

    public static final BiConsumer<InRoot, OutRoot> IGNORE = (in, out) -> {};

    protected BiConsumer<InRoot, OutRoot> resultConsumer;

    public AbstractToyModel(
            String name,
            String creator,
            String description,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        this(name, creator, description, null, null, null, resultConsumer);
    }

    public AbstractToyModel(
            String name,
            String creator,
            String description,
            Path logPath,
            Path outputDir,
            Path downloadDir,
            BiConsumer<InRoot, OutRoot> resultConsumer) {
        super(name, creator, description, logPath, outputDir, downloadDir);
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
    }

    @Override
    protected void run(IRPArgs args, PerennialData<InRoot> data) throws Throwable {
        GetInputAndOutput callback = new GetInputAndOutput("result_" + getName());
        Start3.start(args.toArray(), data, callback);
        resultConsumer.accept(callback.getInRoot(), callback.getOutRoot());
    }
}
