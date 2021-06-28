package de.unileipzig.irpact.util.irpsim.swagger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.unileipzig.irpact.commons.util.FileUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.util.curl.Curl;
import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.*;
import de.unileipzig.irpact.util.irpsim.swagger.simulation.SimulationState;
import de.unileipzig.irpact.util.scenarios.Scenario;
import de.unileipzig.irptools.io.swagger.UploadableSwaggerFile;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Daniel Abitz
 */
public final class Swagger {

    public Swagger() {
    }
    
    //==================================================
    //general
    //==================================================

    private Path tempDir;
    public void setTempDir(Path tempDir) {
        this.tempDir = tempDir;
    }
    public Path getTempDir() {
        return tempDir;
    }

    private Charset charset = StandardCharsets.UTF_8;
    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    public Charset getCharset() {
        return charset;
    }

    private ObjectMapper mapper = JsonUtil.JSON;
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public ObjectMapper getMapper() {
        return mapper;
    }

    private int modeldefinition = -1;
    public void setModeldefinition(int modeldefinition) {
        this.modeldefinition = modeldefinition;
    }
    public int getModeldefinition() {
        return modeldefinition;
    }

    private int defaulModelIndex = -1;
    public void setDefaulModelIndex(int defaulModelIndex) {
        this.defaulModelIndex = defaulModelIndex;
    }
    public int getDefaulModelIndex() {
        return defaulModelIndex;
    }

    private String user;
    public void setUser(String user) {
        this.user = user;
    }
    public String getUser() {
        return user;
    }

    private String password;
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    //==================================================
    //util - static
    //==================================================

    public static Optional<JsonNode> executeToJson(
            Curl curl,
            Path outPath,
            Path errPath,
            Charset charset,
            ObjectMapper mapper,
            boolean deleteFiles) throws IOException, InterruptedException, CurlException {
        curl.execute(outPath, errPath);
        try {
            if(Files.exists(errPath) && Files.size(errPath) > 0L) {
                String content = FileUtil.readString(errPath, charset);
                throw new CurlException(content);
            } else {
                if(Files.notExists(outPath) || Files.size(outPath) == 0L) {
                    return Optional.empty();
                } else {
                    try {
                        JsonNode root = JsonUtil.read(outPath, charset, mapper);
                        return Optional.of(root);
                    } catch (JsonParseException e) {
                        String content = FileUtil.readString(outPath, charset);
                        throw new CurlException(content);
                    }
                }
            }
        } finally {
            if(deleteFiles) {
                FileUtil.deleteIfExists(outPath, errPath);
            }
        }
    }

    public static int execute(
            Curl curl,
            Path outPath,
            Path errPath,
            Charset charset,
            boolean deleteFiles) throws IOException, InterruptedException, CurlException {
        int result = curl.execute(outPath, errPath);
        try {
            if(Files.exists(errPath) && Files.size(errPath) > 0L) {
                String content = FileUtil.readString(errPath, charset);
                throw new CurlException(content);
            } else {
                return result;
            }
        } finally {
            if(deleteFiles) {
                FileUtil.deleteIfExists(outPath, errPath);
            }
        }
    }

    //==================================================
    //util
    //==================================================

    public Path createTempFile() throws IOException {
        return FileUtil.createTempFile(getTempDir(), "", "");
    }

    private Charset getCharsetOrDefault() {
        Charset cs = getCharset();
        return cs == null ? StandardCharsets.UTF_8 : cs;
    }

    private int execute(Curl curl) throws IOException, CurlException, InterruptedException {
        Path outTemp = null;
        Path errTemp = null;
        try {
            outTemp = createTempFile();
            errTemp = createTempFile();

            return execute(
                    curl,
                    outTemp,
                    errTemp,
                    getCharsetOrDefault(),
                    true
            );
        } finally {
            FileUtil.deleteIfExists(outTemp, errTemp);
        }
    }

    private JsonNode executeToJson(Curl curl) throws IOException, CurlException, InterruptedException, NoSuchElementException {
        Path outTemp = null;
        Path errTemp = null;
        try {
            outTemp = createTempFile();
            errTemp = createTempFile();

            Optional<JsonNode> result = executeToJson(
                    curl,
                    outTemp,
                    errTemp,
                    getCharsetOrDefault(),
                    getMapper(),
                    true
            );

            if(result.isPresent()) {
                return result.get();
            } else {
                throw new NoSuchElementException();
            }
        } finally {
            FileUtil.deleteIfExists(outTemp, errTemp);
        }
    }

    public void validate() {
    }

    //==================================================
    //scenario
    //==================================================

    //==========
    //GET /scenarien
    //==========

    private String getScenariosUrl;
    public void setGetScenariosUrl(String getScenariosUrl) {
        this.getScenariosUrl = getScenariosUrl;
    }
    public String getGetScenariosUrl() {
        return getScenariosUrl;
    }
    public String buildGetScenariosUrl(int modeldefinition) {
        return StringUtil.format(getGetScenariosUrl(), modeldefinition);
    }

    public List<ScenarioMetaData> getAllScenarios() throws IOException, CurlException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetScenariosUrl(getModeldefinition()))
                .GET()
                .acceptJson()
                .user(getUser(), getPassword());

        JsonNode root = executeToJson(curl);
        List<ScenarioMetaData> list = new ArrayList<>();
        for(JsonNode entryNode: Util.iterateElements(root)) {
            ScenarioMetaData entry = new ScenarioMetaData(entryNode);
            list.add(entry);
        }
        return list;
    }

    public int storeAllScenarios(Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetScenariosUrl(getModeldefinition()))
                .GET()
                .acceptJson()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //GET /scenarien/id
    //==========

    private String getScenarioUrl;
    public void setGetScenarioUrl(String getScenarioUrl) {
        this.getScenarioUrl = getScenarioUrl;
    }
    public String getGetScenarioUrl() {
        return getScenarioUrl;
    }
    public String buildGetScenarioUrl(int id) {
        return StringUtil.format(getGetScenarioUrl(), id);
    }

    public ScenarioData getScenarioData(ScenarioMetaData metaData) throws CurlException, IOException, InterruptedException {
        return getScenarioData(metaData.getId());
    }

    public ScenarioData getScenarioData(int id) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetScenarioUrl(id))
                .GET()
                .acceptJson()
                .user(getUser(), getPassword());

        JsonNode result = executeToJson(curl);
        return new ScenarioData(result, id);
    }

    public int storeScenarioData(Path target, int id) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetScenarioUrl(id))
                .GET()
                .acceptJson()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    public ValidScenario getScenario(ScenarioMetaData metaData) throws CurlException, IOException, InterruptedException {
        ScenarioData data = getScenarioData(metaData);
        return new ValidScenario(metaData, data);
    }

    //==========
    //DELETE /scenarien/id
    //==========

    private String deleteScenarioUrl;
    public void setDeleteScenarioUrl(String deleteScenarioUrl) {
        this.deleteScenarioUrl = deleteScenarioUrl;
    }
    public String getDeleteScenarioUrl() {
        return deleteScenarioUrl;
    }
    public String buildDeleteScenarioUrl(int id) {
        return StringUtil.format(getDeleteScenarioUrl(), id);
    }

    public int deleteScenario(ScenarioMetaData metaData) throws CurlException, IOException, InterruptedException {
        return deleteScenario(metaData.getId());
    }

    public int deleteScenario(int id) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildDeleteScenarioUrl(id))
                .DELETE()
                .acceptJson()
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //put /scenarien
    //==========

    private String putScenarioUrl;
    public void setPutScenarioUrl(String putScenarioUrl) {
        this.putScenarioUrl = putScenarioUrl;
    }
    public String getPutScenarioUrl() {
        return putScenarioUrl;
    }

    public PutResult storeScenario(Path validSource) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(getPutScenarioUrl())
                .PUT()
                .acceptJson()
                .contentTypeJson()
                .fileContent(validSource)
                .user(getUser(), getPassword());

        JsonNode result = executeToJson(curl);
        return new PutResult(result);
    }

    public PutResult storeScenario(UploadableSwaggerFile data) throws IOException, CurlException, InterruptedException {
        Path dataTemp = null;
        try {
            dataTemp = createTempFile();
            JsonUtil.writeJson(data.root(), dataTemp, JsonUtil.MINIMAL);
            return storeScenario(dataTemp);
        } finally {
            FileUtil.deleteIfExists(dataTemp);
        }
    }

    public PutResult storeScenario(Scenario scenario) throws IOException, CurlException, InterruptedException {
        Path dataTemp = null;
        try {
            dataTemp = createTempFile();
            scenario.storeUploadableTo(dataTemp, false);
            return storeScenario(dataTemp);
        } finally {
            FileUtil.deleteIfExists(dataTemp);
        }
    }

    //==========
    //put /scenarien/id
    //==========

    private String putScenarioIdUrl;
    public void setPutScenarioIdUrl(String putScenarioIdUrl) {
        this.putScenarioIdUrl = putScenarioIdUrl;
    }
    public String getPutScenarioIdUrl() {
        return putScenarioIdUrl;
    }
    public String buildPutScenarioIdUrl(int id) {
        return StringUtil.format(getPutScenarioIdUrl(), id);
    }

    public PutResult replaceScenario(int id, Path replacement) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildPutScenarioIdUrl(id))
                .PUT()
                .acceptJson()
                .contentTypeJson()
                .fileContent(replacement)
                .user(getUser(), getPassword());

        JsonNode result = executeToJson(curl);
        return new PutResult(result);
    }

    public PutResult replaceScenario(int id, UploadableSwaggerFile replacement) throws IOException, CurlException, InterruptedException {
        Path dataTemp = null;
        try {
            dataTemp = createTempFile();
            JsonUtil.writeJson(replacement.root(), dataTemp, JsonUtil.MINIMAL);
            return replaceScenario(id, dataTemp);
        } finally {
            FileUtil.deleteIfExists(dataTemp);
        }
    }

    //==================================================
    //simulation
    //==================================================

    //==========
    //GET /simulations/states
    //==========

    private String getSimulationStatesUrl;
    public void setGetSimulationStatesUrl(String getSimulationStatesUrl) {
        this.getSimulationStatesUrl = getSimulationStatesUrl;
    }
    public String getGetSimulationStatesUrl() {
        return getSimulationStatesUrl;
    }
    public String buildGetSimulationStatesUrl(int id) {
        return StringUtil.format(getGetSimulationStatesUrl(), id);
    }

    public List<SimulationState> getAllSimulationStates() throws IOException, CurlException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetSimulationStatesUrl(getModeldefinition()))
                .GET()
                .acceptJson()
                .user(getUser(), getPassword());

        JsonNode root = executeToJson(curl);
        List<SimulationState> list = new ArrayList<>();
        for(JsonNode entryNode: Util.iterateElements(root)) {
            SimulationState entry = new SimulationState(entryNode);
            list.add(entry);
        }
        return list;
    }

    public int storeAllSimulationStates(Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetSimulationStatesUrl(getModeldefinition()))
                .GET()
                .acceptJson()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //GET /simulations/{simulationid}/{year}/{modelindex}/gdxresultfile
    //==========

    private String getGdxresultfileUrl;
    public void setGetGdxresultfileUrl(String getSimulationStatesUrl) {
        this.getSimulationStatesUrl = getSimulationStatesUrl;
    }
    public String getGetGdxresultfileUrl() {
        return getSimulationStatesUrl;
    }
    public String buildGetGdxresultfileUrl(int simulationId, int yearIndex, int modelIndex) {
        return StringUtil.format(getGetGdxresultfileUrl(), simulationId, yearIndex, modelIndex);
    }

    public JsonNode getGdxresultfileAsJson(int simulationId, int yearIndex) throws CurlException, IOException, InterruptedException {
        return getGdxresultfileAsJson(simulationId, yearIndex, getDefaulModelIndex());
    }
    public JsonNode getGdxresultfileAsJson(int simulationId, int yearIndex, int modelIndex) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetGdxresultfileUrl(simulationId, yearIndex, modelIndex))
                .GET()
                .acceptOctetStream()
                .user(getUser(), getPassword());

        return executeToJson(curl);
    }

    public int storeGdxresultfile(int simulationId, int yearIndex, Path target) throws CurlException, IOException, InterruptedException {
        return storeGdxresultfile(simulationId, yearIndex, getDefaulModelIndex(), target);
    }
    public int storeGdxresultfile(int simulationId, int yearIndex, int modelIndex, Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetGdxresultfileUrl(simulationId, yearIndex, modelIndex))
                .GET()
                .acceptOctetStream()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //GET /simulations/{simulationid}/{year}/{modelindex}/lstfile
    //==========

    private String getLstfileUrl;
    public void setGetLstfileUrl(String getLstfileUrl) {
        this.getLstfileUrl = getLstfileUrl;
    }
    public String getGetLstfileUrl() {
        return getLstfileUrl;
    }
    public String buildGetLstfileUrl(int simulationId, int yearIndex, int modelIndex) {
        return StringUtil.format(getGetLstfileUrl(), simulationId, yearIndex, modelIndex);
    }

    public int storeLogfile(int simulationId, int yearIndex, Path target) throws CurlException, IOException, InterruptedException {
        return storeLstfile(simulationId, yearIndex, target);
    }
    public int storeLogfile(int simulationId, int yearIndex, int modelIndex, Path target) throws CurlException, IOException, InterruptedException {
        return storeLstfile(simulationId, yearIndex, modelIndex, target);
    }
    public int storeLstfile(int simulationId, int yearIndex, Path target) throws CurlException, IOException, InterruptedException {
        return storeGdxresultfile(simulationId, yearIndex, getDefaulModelIndex(), target);
    }
    public int storeLstfile(int simulationId, int yearIndex, int modelIndex, Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetLstfileUrl(simulationId, yearIndex, modelIndex))
                .GET()
                .acceptPlainText()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //DEV
    //GET /simulations/{simulationid}/{year}/{modelindex}/{imagename}
    //==========

    private String getImageUrl;
    public void setGetImageUrl(String getImageUrl) {
        this.getImageUrl = getImageUrl;
    }
    public String getGetImageUrl() {
        return getImageUrl;
    }
    public String buildGetImageUrl(int simulationId, int yearIndex, int modelIndex, String imageName) {
        return StringUtil.format(getGetImageUrl(), simulationId, yearIndex, modelIndex, imageName);
    }

    public int storeImage(int simulationId, int yearIndex, String imageName, Path target) throws CurlException, IOException, InterruptedException {
        return storeImage(simulationId, yearIndex, getDefaulModelIndex(), imageName, target);
    }
    public int storeImage(int simulationId, int yearIndex, int modelIndex, String imageName, Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetImageUrl(simulationId, yearIndex, modelIndex, imageName))
                .GET()
                .acceptPng()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }

    //==========
    //DEV
    //GET /simulations/{simulationid}/{year}/{modelindex}/bulkImages
    //==========

    private String getBulkImagesUrl;
    public void setGetBulkImagesUrl(String getBulkImagesUrl) {
        this.getBulkImagesUrl = getBulkImagesUrl;
    }
    public String getGetBulkImagesUrl() {
        return getBulkImagesUrl;
    }
    public String buildGetBulkImagesUrl(int simulationId, int yearIndex, int modelIndex) {
        return StringUtil.format(getGetBulkImagesUrl(), simulationId, yearIndex, modelIndex);
    }

    public int storeImages(int simulationId, int yearIndex, Path target) throws CurlException, IOException, InterruptedException {
        return storeImages(simulationId, yearIndex, getDefaulModelIndex(), target);
    }
    public int storeImages(int simulationId, int yearIndex, int modelIndex, Path target) throws CurlException, IOException, InterruptedException {
        Curl curl = new Curl()
                .silent()
                .showError()
                .target(buildGetBulkImagesUrl(simulationId, yearIndex, modelIndex))
                .GET()
                .acceptOctetStream()
                .output(target)
                .user(getUser(), getPassword());

        return execute(curl);
    }
}
