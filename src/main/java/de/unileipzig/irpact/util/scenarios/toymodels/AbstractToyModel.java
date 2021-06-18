package de.unileipzig.irpact.util.scenarios.toymodels;

import de.unileipzig.irpact.commons.util.IRPArgs;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.irpact.callbacks.GetInputAndOutput;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.io.base.data.AnnualEntry;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractToyModel extends AbstractScenario {

    protected static final String DEFAULT_PV_DATA_NAME = "Barwertrechner";
    protected static final String DEFAULT_SPATIAL_DATA_NAME = "Datensatz_210322";

    protected Map<String, InAttributeName> nameCache = new HashMap<>();
    protected BiConsumer<InRoot, OutRoot> resultConsumer;
    protected Path logPath;
    protected Path outputDir;
    protected Path downloadDir;

    protected int totalAgents = -1;

    public AbstractToyModel(BiConsumer<InRoot, OutRoot> resultConsumer) {
        this(resultConsumer, null, null, null);
    }

    public AbstractToyModel(
            BiConsumer<InRoot, OutRoot> resultConsumer,
            Path logPath,
            Path outputDir,
            Path downloadDir) {
        this.resultConsumer = Objects.requireNonNull(resultConsumer);
        this.logPath = logPath;
        this.outputDir = outputDir;
        this.downloadDir = downloadDir;
    }

    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }

    protected String getPVDataName() {
        return DEFAULT_PV_DATA_NAME;
    }

    protected String getSpatialFileName() {
        return DEFAULT_SPATIAL_DATA_NAME;
    }

    protected InPVFile pvFile;
    protected InPVFile getPVFile() {
        if(pvFile == null) {
            pvFile = new InPVFile(getPVDataName());
        }
        return pvFile;
    }

    protected InSpatialTableFile spatialTableFile;
    protected InSpatialTableFile getSpatialFile() {
        if(spatialTableFile == null) {
            spatialTableFile = new InSpatialTableFile(getSpatialFileName());
        }
        return spatialTableFile;
    }

    protected InAttributeName getAttribute(String text) {
        return nameCache.computeIfAbsent(text, InAttributeName::new);
    }

    protected InFileBasedPVactMilieuSupplier createSpatialDistribution(String name) {
        InFileBasedPVactMilieuSupplier supplier = new InFileBasedPVactMilieuSupplier();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        return supplier;
    }

    protected InFileBasedPVactConsumerAgentPopulation createPopulation(String name, int agents, InConsumerAgentGroup... cags) {
        InFileBasedPVactConsumerAgentPopulation supplier = new InFileBasedPVactConsumerAgentPopulation();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        supplier.setRequiresDesiredSize(true);
        supplier.setConsumerAgentGroups(cags);
        if(agents < 1) {
            supplier.setUseAll(true);
            supplier.setDesiredSize(-1);
        } else {
            supplier.setUseAll(false);
            supplier.setDesiredSize(agents);
        }
        return supplier;
    }

    @Override
    protected void updateArgs(IRPArgs args) {
        if(logPath != null) args.setLogPathWithConsole(logPath);
        if(outputDir != null) args.setOutputDir(outputDir);
        if(downloadDir != null) args.setDownloadDir(downloadDir);
    }

    @Override
    protected void run(IRPArgs args, AnnualEntry<InRoot> entry) throws Throwable {
        GetInputAndOutput callback = new GetInputAndOutput("result_" + getName());
        Start.start(args.toArray(), entry, callback);
        resultConsumer.accept(callback.getInRoot(), callback.getOutRoot());
    }
}
