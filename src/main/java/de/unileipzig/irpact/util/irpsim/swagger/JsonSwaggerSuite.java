package de.unileipzig.irpact.util.irpsim.swagger;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.io.FileUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.PutResult;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.ScenarioMetaData;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.ScenarioMetaDataCollection;
import de.unileipzig.irpact.util.irpsim.swagger.simulation.SimulationState;
import de.unileipzig.irpact.util.irpsim.swagger.simulation.SimulationStateCollection;
import de.unileipzig.irpact.util.scenarios.Scenario;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public final class JsonSwaggerSuite implements SwaggerSuite {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(JsonSwaggerSuite.class);

    protected final Swagger swagger;

    protected final ScenarioMetaDataCollection metaDataCache = new ScenarioMetaDataCollection();
    protected Path metaDataPath;
    protected Charset metaDataCharset = StandardCharsets.UTF_8;
    protected final SimulationStateCollection stateCollection = new SimulationStateCollection();
    protected Path statePath;
    protected Charset stateCharset = StandardCharsets.UTF_8;

    protected Charset dataCharset = StandardCharsets.UTF_8;
    protected Path backupDir;
    protected boolean autoBackup = true;
    protected boolean autoCommit = false;
    protected PrettyPrinter printer = JsonUtil.DEFAULT;

    protected final Map<Integer, Scenario> overwriteScenarios = new TreeMap<>();
    protected final Set<Integer> deleteScenarios = new TreeSet<>();
    protected final List<Scenario> addScenarios = new ArrayList<>();

    public JsonSwaggerSuite(
            Swagger swagger,
            Path metaDataPath,
            Path statePath,
            Path backupDir) {
        this.swagger = swagger;
        setMetaDataPath(metaDataPath);
        setStatePath(statePath);
        setBackupDir(backupDir);
    }

    public Swagger getSwagger() {
        return swagger;
    }

    public void setMetaDataCharset(Charset metaDataCharset) {
        this.metaDataCharset = metaDataCharset;
    }

    public Charset getMetaDataCharset() {
        return metaDataCharset;
    }

    public void setMetaDataPath(Path metaDataPath) {
        this.metaDataPath = metaDataPath;
    }

    public Path getMetaDataPath() {
        return metaDataPath;
    }

    public void setStateCharset(Charset stateCharset) {
        this.stateCharset = stateCharset;
    }

    public Charset getStateCharset() {
        return stateCharset;
    }

    public void setStatePath(Path statePath) {
        this.statePath = statePath;
    }

    public Path getStatePath() {
        return statePath;
    }

    public void setAutoBackup(boolean autoBackup) {
        this.autoBackup = autoBackup;
    }

    public boolean isAutoBackup() {
        return autoBackup;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setBackupDir(Path backupDir) {
        this.backupDir = backupDir;
    }

    public Path getBackupDir() {
        return backupDir;
    }

    //=========================
    //util
    //=========================

    protected Path createTempFile() throws IOException {
        return swagger.createTempFile();
    }

    protected Path createBackupPath(Path input, long timestamp) {
        String suffix = "-backup-" + timestamp;
        Path newPath =  FileUtil.changeFileName(input, "", suffix);
        return backupDir.resolve(newPath.getFileName());
    }

    protected Path toJson(Path dir, String fileName) throws IOException {
        if(fileName == null) {
            return FileUtil.createTempFile(dir, "", ".json");
        } else {
            String name = fileName.endsWith(".json") ? fileName : fileName + ".json";
            return dir.resolve(name);
        }
    }

    protected void makeMinimal(Path input, Charset charset) throws IOException {
        changePrettyPrinter(input, charset, JsonUtil.MINIMAL);
    }

    protected void makeDefault(Path input, Charset charset) throws IOException {
        changePrettyPrinter(input, charset, JsonUtil.DEFAULT);
    }

    protected void changePrettyPrinter(Path input, Charset charset, PrettyPrinter printer) throws IOException {
        Path temp = swagger.createTempFile();
        try {
            JsonNode root = JsonUtil.readJson(input, charset);
            JsonUtil.writeJson(root, temp, charset, printer);
        } catch (IOException e) {
            LOGGER.error("changing pretty printer failed, file content:\n{}", FileUtil.tryReadString(input, charset));
            throw e;
        }
        Files.move(temp, input, StandardCopyOption.REPLACE_EXISTING);
    }

    //=========================
    //Scenarios
    //=========================

    //==========
    //locale
    //==========

    @Override
    public Stream<ScenarioMetaData> streamMetaData() {
        return metaDataCache.stream();
    }

    @Override
    public Collection<ScenarioMetaData> getMetaData() {
        return metaDataCache.getMetaData();
    }

    @Override
    public void localeLoad() throws IOException {
        LOGGER.trace("load locale meta data: {}", metaDataPath);
        metaDataCache.parse(metaDataPath, metaDataCharset);
        LOGGER.trace("load finished");
    }

    @Override
    public void localeStore() throws IOException {
        LOGGER.trace("store locale meta data: {}", metaDataPath);
        metaDataCache.store(metaDataPath, metaDataCharset);
        LOGGER.trace("store finished");
    }

    @Override
    public void localeClear() {
        metaDataCache.clear();
    }

    @Override
    public void backupLocale() throws IOException {
        long now = System.currentTimeMillis();
        Path metaBackupPath = createBackupPath(metaDataPath, now);

        LOGGER.trace("create scenario meta data backup: {}", metaBackupPath);
        metaDataCache.store(metaBackupPath, metaDataCharset);
        LOGGER.trace("backup finished");
    }

    @Override
    public int getScenarioId(String name) {
        for(ScenarioMetaData metaData: metaDataCache) {
            if(Objects.equals(name, metaData.getName())) {
                return metaData.getId();
            }
        }
        return -1;
    }

    //==========
    //remote
    //==========

    @Override
    public void remoteLoadScenarioMetaData() throws IOException, CurlException, InterruptedException {
        if(autoBackup) {
            backupLocale();
        }

        LOGGER.trace("load all scenario meta data");
        swagger.storeAllScenarios(metaDataPath);
        LOGGER.trace("stored: {}", metaDataPath);
        changePrettyPrinter(metaDataPath, metaDataCharset, printer);
        LOGGER.trace("formatting changed");

        metaDataCache.clear();
        metaDataCache.parse(metaDataPath, metaDataCharset);
    }

    @Override
    public void remoteDeleteAllScenarios() throws CurlException, IOException, InterruptedException {
        for(ScenarioMetaData metaData: metaDataCache) {
            if(metaData.isNotDeletable()) {
                continue;
            }

            int id = metaData.getId();
            LOGGER.trace("delete id {}", id);
            swagger.deleteScenario(id);
            LOGGER.trace("deleted: {}", id);
        }
    }

    @Override
    public void remoteLoadAllScenarios(Path targetDir) throws IOException, CurlException, InterruptedException {
        for(ScenarioMetaData metaData: metaDataCache) {
            int id = metaData.getId();
            Path outPath = toJson(targetDir, Integer.toString(id));
            LOGGER.trace("loading scenario {} ({})", id, outPath);
            swagger.storeScenarioData(outPath, id);
            LOGGER.trace("scenario stored");
            changePrettyPrinter(outPath, dataCharset, printer);
            LOGGER.trace("formatting changed");
        }
    }

    //==========
    //changes
    //==========

    @Override
    public void addScenario(Scenario scenario) throws CurlException, IOException, InterruptedException {
        addScenarios.add(scenario);
        tryCommit();
    }

    @Override
    public boolean deleteScenario(int id) throws CurlException, IOException, InterruptedException {
        if(metaDataCache.hasScenario(id)) {
            deleteScenarios.add(id);
            tryCommit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean overwriteScenario(int id, Scenario scenario) throws CurlException, IOException, InterruptedException {
        if(metaDataCache.hasScenario(id)) {
            overwriteScenarios.put(id, scenario);
            tryCommit();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void commit() throws IOException, CurlException, InterruptedException {
        commitOverwriteScenarios();
        commitDeleteScenarios();
        commitAddScenarios();

        remoteLoadScenarioMetaData();
    }
    protected void tryCommit() throws CurlException, IOException, InterruptedException {
        if(autoCommit) {
            commit();
        }
    }

    protected void commitOverwriteScenarios() throws IOException, CurlException, InterruptedException {
        Map<Integer, Path> tempFiles = new TreeMap<>();
        for(Map.Entry<Integer, Scenario> entry: overwriteScenarios.entrySet()) {
            int id = entry.getKey();
            Scenario scenario = entry.getValue();
            Path tempPath = swagger.createTempFile();
            scenario.storeUploadableTo(tempPath, dataCharset, false);
            tempFiles.put(id, tempPath);
        }

        for(Map.Entry<Integer, Path> entry: tempFiles.entrySet()) {
            int id = entry.getKey();
            Path tempPath = entry.getValue();
            LOGGER.trace("replace id {} with '{}'", id, tempPath);
            PutResult result = swagger.replaceScenario(id, tempPath);
            LOGGER.trace("result: {}", result.getRootAsObject());
        }

        overwriteScenarios.clear();
    }

    protected void commitDeleteScenarios() throws CurlException, IOException, InterruptedException {
        for(Integer id: deleteScenarios) {
            LOGGER.trace("delete id {}", id);
            int result = swagger.deleteScenario(id);
            LOGGER.trace("deleted: {}", result);
        }

        deleteScenarios.clear();
    }

    protected void commitAddScenarios() throws IOException, CurlException, InterruptedException {
        Map<Path, Scenario> tempFiles = new LinkedHashMap<>();
        for(Scenario scenario: addScenarios) {
            Path tempPath = swagger.createTempFile();
            scenario.storeUploadableTo(tempPath, dataCharset, false);
            tempFiles.put(tempPath, scenario);
        }

        for(Path scenarioPath: tempFiles.keySet()) {
            LOGGER.trace("upload scenario {}", scenarioPath);
            PutResult result = swagger.storeScenario(scenarioPath);
            LOGGER.trace("uploaded: {}", result.print());
        }
    }

    //==================================================
    //simulation
    //==================================================

    protected static Path createSubDir(Path dir, String name) throws IOException {
        Path subDir = dir.resolve(name);
        return Files.createDirectories(subDir);
    }

    protected static Path createYearSubDir(Path dir, int yearIndex, SimulationState state) throws IOException {
        int year = state.getYear(yearIndex);
        if(year == -1) {
            throw new IllegalArgumentException("missing year");
        }
        return createSubDir(dir, Integer.toString(year));
    }

    @Override
    public GenericResult startSimulation(Scenario scenario) throws IOException, CurlException, InterruptedException {
        return swagger.startSimulation(scenario);
    }

    //==========
    //locale
    //==========

    @Override
    public Stream<SimulationState> streamStates() {
        return stateCollection.stream();
    }

    @Override
    public Collection<SimulationState> getStates() {
        return stateCollection.getStates();
    }

    @Override
    public void localeLoadStates() throws IOException {
        LOGGER.trace("load states: {}", statePath);
        stateCollection.parse(statePath, stateCharset);
        LOGGER.trace("load finished");
    }

    @Override
    public void localeStoreStates() throws IOException {
        LOGGER.trace("store states: {}", statePath);
        stateCollection.store(statePath, stateCharset);
        LOGGER.trace("store finished");
    }

    @Override
    public void localeClearStates() {
        stateCollection.clear();
    }

    @Override
    public void backupLocaleStates() throws IOException {
        long now = System.currentTimeMillis();
        Path metaBackupPath = createBackupPath(statePath, now);

        LOGGER.trace("create states backup: {}", metaBackupPath);
        stateCollection.store(metaBackupPath, stateCharset);
        LOGGER.trace("backup finished");
    }

    //==========
    //remote
    //==========

    @Override
    public void remoteLoadSimulationStates() throws IOException, CurlException, InterruptedException {
        if(autoBackup) {
            backupLocaleStates();
        }

        LOGGER.trace("load all simulation states");
        swagger.storeAllSimulationStates(statePath);
        LOGGER.trace("stored: {}", statePath);
        changePrettyPrinter(statePath, stateCharset, printer);
        LOGGER.trace("formatting changed");

        stateCollection.clear();
        stateCollection.parse(statePath, stateCharset);
    }

    @Override
    public void downloadOutput(int id, Path target) throws CurlException, IOException, InterruptedException {
        SimulationState state = stateCollection.get(id);
        if(state == null) {
            throw new IRPactIllegalArgumentException("state with id '{}' not found", id);
        }

        LOGGER.trace("download gdx result file for: {}/{}/{}", state.getId(), state.getLastYearIndex(), state.getModelIndex());
        swagger.storeGdxresultfile(
                state.getId(),
                state.getLastYearIndex(),
                state.getModelIndex(),
                target
        );
        LOGGER.trace("saved to: {}", target);
        changePrettyPrinter(target, dataCharset, printer);
    }

    @Override
    public boolean hasZip(int id) throws CurlException, IOException, InterruptedException {
        SimulationState state = stateCollection.get(id);
        if(state == null) {
            throw new IRPactIllegalArgumentException("state with id '{}' not found", id);
        }

        LOGGER.trace("has zip for: {}/{}/{}", state.getId(), state.getLastYearIndex(), state.getModelIndex());
        GenericResult result = swagger.hasImages(
                state.getId(),
                state.getLastYearIndex(),
                state.getModelIndex()
        );
        LOGGER.trace("result: {}", result.print());

        if(result.isError()) {
            throw new CurlException(result.getErrorMessage());
        } else {
            return result.isTrue();
        }
    }

    @Override
    public void downloadZip(int id, Path target) throws CurlException, IOException, InterruptedException {
        SimulationState state = stateCollection.get(id);
        if(state == null) {
            throw new IRPactIllegalArgumentException("state with id '{}' not found", id);
        }

        LOGGER.trace("download zip for: {}/{}/{}", state.getId(), state.getLastYearIndex(), state.getModelIndex());
        swagger.storeImages(
                state.getId(),
                state.getLastYearIndex(),
                state.getModelIndex(),
                target
        );
        LOGGER.trace("saved to: {}", target);
        changePrettyPrinter(target, dataCharset, printer);
    }
}
