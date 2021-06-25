package de.unileipzig.irpact.util.scenarios;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface ScenarioFileMerger {

    void merge(Path first, Path second, Path output) throws IOException;
}
