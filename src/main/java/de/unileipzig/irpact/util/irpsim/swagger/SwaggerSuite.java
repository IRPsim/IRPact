package de.unileipzig.irpact.util.irpsim.swagger;

import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.ScenarioMetaData;
import de.unileipzig.irpact.util.scenarios.Scenario;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SwaggerSuite {

    //=========================
    //Scenarios
    //=========================

    //==========
    //locale
    //==========

    Stream<ScenarioMetaData> streamMetaData();

    void localeLoad() throws IOException;

    void localeStore() throws IOException;

    void backupLocale() throws IOException;

    void localeClear();

    int getScenarioId(String name);

    //==========
    //remote
    //==========

    void remoteLoadScenarioMetaData() throws IOException, CurlException, InterruptedException;

    void remoteDeleteAllScenarios() throws CurlException, IOException, InterruptedException;

    void remoteLoadAllScenarios(Path targetDir) throws IOException, CurlException, InterruptedException;

    //==========
    //changes
    //==========

    void addScenario(Scenario scenario) throws CurlException, IOException, InterruptedException;

    boolean deleteScenario(int id) throws CurlException, IOException, InterruptedException;

    boolean overwriteScenario(int id, Scenario scenario) throws CurlException, IOException, InterruptedException;

    void commit() throws IOException, CurlException, InterruptedException;

    //=========================
    //Simulation
    //=========================


}
