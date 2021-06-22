package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public class DefaultScenario extends AbstractScenario implements DefaultScenarioFactory {

    public DefaultScenario(String name, String creator, String description) {
        super(name, creator, description);
    }

    public DefaultScenario(String name, String creator, String description, Path logPath, Path outputDir, Path downloadDir) {
        super(name, creator, description, logPath, outputDir, downloadDir);
    }

    @Override
    public List<InRoot> createInRoots() {
        InRoot root = new InRoot();
        root.general = new InGeneral();
        root.getGeneral().seed = 42;
        root.getGeneral().timeout = TimeUnit.MINUTES.toMillis(1);
        root.getGeneral().runOptActDemo = false;
        root.getGeneral().runPVAct = true;
        root.getGeneral().logLevel = IRPLevel.ALL.getLevelId();
        root.getGeneral().logAllIRPact = true;
        root.getGeneral().enableAllDataLogging();
        root.getGeneral().enableAllResultLogging();
        root.getGeneral().setFirstSimulationYear(2015);
        root.getGeneral().lastSimulationYear = 2015;
        return Collections.singletonList(root);
    }

    @Override
    public InRoot createDefaultScenario() {
        return createInRoots().get(0);
    }
}
