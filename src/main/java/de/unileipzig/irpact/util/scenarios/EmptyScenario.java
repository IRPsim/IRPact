package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class EmptyScenario extends AbstractScenario {

    public EmptyScenario(String name, String creator, String description) {
        super(name, creator, description);
    }

    public EmptyScenario(String name, String creator, String description, Path logPath, Path outputDir, Path downloadDir) {
        super(name, creator, description, logPath, outputDir, downloadDir);
    }

    @Override
    public List<InRoot> createInRoots() {
        InRoot root = new InRoot();
        root.general = new InGeneral();
        root.general.setFirstSimulationYear(DEFAULT_INITIAL_YEAR);
        return Collections.singletonList(root);
    }
}
