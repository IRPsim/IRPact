package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.irpsim.swagger.Swagger;
import de.unileipzig.irpact.util.scenarios.Scenario;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ScenarioManager {

    protected final Swagger swagger;
    protected final ScenarioMetaDataCollection cache;

    public ScenarioManager(Swagger swagger) {
        this(swagger, new ScenarioMetaDataCollection());
    }

    public ScenarioManager(Swagger swagger, ScenarioMetaDataCollection cache) {
        this.swagger = swagger;
        this.cache = cache;
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public ScenarioMetaDataCollection getCache() {
        return cache;
    }

    //=========================
    //cache
    //=========================

    public void loadCache(Path source, Charset charset) throws IOException {
        getCache().parse(source, charset);
    }

    public void saveCache(Path target, Charset charset) throws IOException {
        getCache().store(target, charset);
    }

    public int[] findIds(String name) {
        return cache.stream()
                .filter(entry -> Objects.equals(entry.getName(), name))
                .mapToInt(ScenarioMetaData::getId)
                .toArray();
    }

    public int[] findIds(String name, String creator, String description) {
        return cache.stream()
                .filter(entry -> entry.isEqualsData(name, creator, description))
                .mapToInt(ScenarioMetaData::getId)
                .toArray();
    }

    //=========================
    //store
    //=========================

    public ScenarioMetaData storeScenario(Path validSource) throws CurlException, IOException, InterruptedException {
        PutResult result = swagger.storeScenario(validSource);
        return updateCache(result);
    }
    public ScenarioMetaData storeScenario(Scenario scenario) throws CurlException, IOException, InterruptedException {
        PutResult result = swagger.storeScenario(scenario);
        return updateCache(result);
    }
    protected ScenarioMetaData updateCache(PutResult result) throws IOException, CurlException, InterruptedException {
        List<ScenarioMetaData> metaDataList = swagger.getAllScenarios();
        for(ScenarioMetaData metaData: metaDataList) {
            if(metaData.getId() == result.getId()) {
                cache.validPut(metaData);
                return metaData;
            }
        }
        throw new IllegalStateException("meta data for '" + result.getId() + "' not found");
    }
}
