package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public final class SpecificationManager {

    private static final DirectoryStream.Filter<Path> FILTER = path -> {
        if(path == null) {
            return false;
        }
        if(Files.isDirectory(path)) {
            return true;
        }
        String fileName = path.getFileName().toString();
        return fileName.endsWith(JSON_EXTENSION);
    };

    private static final IRPLogger logger = IRPLogging.getLogger(SpecificationManager.class);

    protected ObjectMapper mapper;

    protected Map<String, ObjectNode> cagMap = new HashMap<>();
    protected Map<String, ObjectNode> awarenessMap = new HashMap<>();
    protected Map<String, ObjectNode> distributionMap = new HashMap<>();
    protected Map<String, ObjectNode> spatialDistributionMap = new HashMap<>();
    protected Map<String, ObjectNode> evalMap = new HashMap<>();
    protected Map<String, ObjectNode> productMap = new HashMap<>();

    protected ObjectNode generalRoot;
    protected ObjectNode affinityRoot;
    protected ObjectNode topologyRoot;
    protected ObjectNode processRoot;
    protected ObjectNode spatialRoot;
    protected ObjectNode timeRoot;

    public SpecificationManager(ObjectMapper mapper) {
        this.mapper = mapper;
        init();
    }

    protected void init() {
        generalRoot = mapper.createObjectNode();
        affinityRoot = mapper.createObjectNode();
        topologyRoot = mapper.createObjectNode();
        processRoot = mapper.createObjectNode();
        spatialRoot = mapper.createObjectNode();
        timeRoot = mapper.createObjectNode();
    }

    //=========================
    //load
    //=========================

    public void load(Path dir) throws IOException {
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, FILTER)) {
            for(Path path: stream) {
                if(Files.isDirectory(path)) {
                    load(path);
                } else {
                    loadFile(getDirKey(dir), path);
                }
            }
        }
    }

    private static String getDirKey(Path dir) {
        String name = dir.getFileName().toString();
        switch (name) {
            case DIR_ConsumerAgentGroups:
            case DIR_Awareness:
            case DIR_Distributions:
            case DIR_SpatialDistributions:
            case DIR_DistanceEvaluators:
            case DIR_ProductGroups:
                return name;

            default:
                return DIR_NONE;
        }
    }

    private void loadFile(String dirKey, Path file) throws IOException {
        String fileName = file.getFileName().toString();
        String fileNameWithoutJson = removeJson(fileName);
        ObjectNode root = IRPactJson.readJson(file, StandardCharsets.UTF_8);

        switch (dirKey) {
            case DIR_ConsumerAgentGroups:
                cagMap.put(fileNameWithoutJson, root);
                break;

            case DIR_Awareness:
                awarenessMap.put(fileNameWithoutJson, root);
                break;

            case DIR_Distributions:
                distributionMap.put(fileNameWithoutJson, root);
                break;

            case DIR_SpatialDistributions:
                spatialDistributionMap.put(fileNameWithoutJson, root);
                break;

            case DIR_DistanceEvaluators:
                evalMap.put(fileNameWithoutJson, root);
                break;

            case DIR_ProductGroups:
                productMap.put(fileNameWithoutJson, root);
                break;

            case DIR_NONE:
                switch (fileName) {
                    case FILE_General:
                        generalRoot = root;
                        break;

                    case FILE_Affinities:
                        affinityRoot = root;
                        break;

                    case FILE_SocialNetwork:
                        topologyRoot = root;
                        break;

                    case FILE_ProcessModel:
                        processRoot = root;
                        break;

                    case FILE_SpatialModel:
                        spatialRoot = root;
                        break;

                    case FILE_TimeModel:
                        timeRoot = root;
                        break;

                    default:
                        throw new IllegalArgumentException("unknown file: " + fileName);
                }
                break;

            default:
                throw new IllegalArgumentException("unknown key: " + dirKey);
        }
    }

    //=========================
    //store
    //=========================

    public void store(Path dir) throws IOException {
        if(Files.notExists(dir)) {
            logger.debug(IRPSection.SPECIFICATION_CONVERTER, "create '{}'", dir);
            Files.createDirectories(dir);
        }

        createFiles(dir.resolve(DIR_ConsumerAgentGroups), cagMap);
        createFiles(dir.resolve(DIR_Awareness), awarenessMap);
        createFiles(dir.resolve(DIR_Distributions), distributionMap);
        createFiles(dir.resolve(DIR_SpatialDistributions), spatialDistributionMap);
        createFiles(dir.resolve(DIR_DistanceEvaluators), evalMap);
        createFiles(dir.resolve(DIR_ProductGroups), productMap);

        createFile(dir.resolve(FILE_General), generalRoot);
        createFile(dir.resolve(FILE_Affinities), affinityRoot);
        createFile(dir.resolve(FILE_SocialNetwork), topologyRoot);
        createFile(dir.resolve(FILE_ProcessModel), processRoot);
        createFile(dir.resolve(FILE_SpatialModel), spatialRoot);
        createFile(dir.resolve(FILE_TimeModel), timeRoot);
    }

    private static String toJson(String input) {
        return input.endsWith(JSON_EXTENSION)
                ? input
                : input + JSON_EXTENSION;
    }

    private static String removeJson(String input) {
        return input.endsWith(JSON_EXTENSION)
                ? input.substring(0, input.length() - JSON_EXTENSION.length())
                : input;
    }

    private static void createFile(Path path, ObjectNode node) throws IOException {
        createFile(IRPactJson.JSON, Util.defaultPrinter, path, node);
    }

    @SuppressWarnings("SameParameterValue")
    private static void createFile(
            ObjectMapper mapper,
            PrettyPrinter printer,
            Path path,
            ObjectNode node) throws IOException {
        logger.debug(IRPSection.SPECIFICATION_CONVERTER, "write '{}'", path);
        IRPactJson.write(
                node,
                path,
                StandardCharsets.UTF_8,
                printer,
                mapper
        );
    }

    private static void createFiles(
            Path dir,
            Map<String, ObjectNode> map) throws IOException {
        createFiles(IRPactJson.JSON, Util.defaultPrinter, dir, map);
    }

    @SuppressWarnings("SameParameterValue")
    private static void createFiles(
            ObjectMapper mapper,
            PrettyPrinter printer,
            Path dir,
            Map<String, ObjectNode> map) throws IOException {
        if(Files.notExists(dir)) {
            logger.debug(IRPSection.SPECIFICATION_CONVERTER, "create '{}'", dir);
            Files.createDirectories(dir);
        }
        for(Map.Entry<String, ObjectNode> entry: map.entrySet()) {
            Path path = dir.resolve(toJson(entry.getKey()));
            logger.debug(IRPSection.SPECIFICATION_CONVERTER, "write '{}'", path);
            IRPactJson.write(
                    entry.getValue(),
                    path,
                    StandardCharsets.UTF_8,
                    printer,
                    mapper
            );
        }
    }

    //=========================
    //util
    //=========================

    public ObjectNode getGeneralSettings() {
        return generalRoot;
    }

    public ObjectNode getAffinities() {
        return affinityRoot;
    }

    public ObjectNode getTopology() {
        return topologyRoot;
    }

    public ObjectNode getProcess() {
        return processRoot;
    }

    public ObjectNode getSpatial() {
        return spatialRoot;
    }

    public ObjectNode getTime() {
        return timeRoot;
    }

    public ObjectNode getAwareness(String name) {
        return awarenessMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasAwareness(String name) {
        return awarenessMap.containsKey(name);
    }

    public ObjectNode getConsumerAgentGroup(String name) {
        return cagMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasConsumerAgentGroup(String name) {
        return cagMap.containsKey(name);
    }

    public ObjectNode getDistribution(String name) {
        return distributionMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasDistribution(String name) {
        return distributionMap.containsKey(name);
    }

    public ObjectNode getEvaluator(String name) {
        return evalMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasEvaluator(String name) {
        return evalMap.containsKey(name);
    }

    public ObjectNode getProductGroup(String name) {
        return productMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasProductGroup(String name) {
        return productMap.containsKey(name);
    }

    public ObjectNode getSpatialDistribution(String name) {
        return spatialDistributionMap.computeIfAbsent(name, _name -> mapper.createObjectNode());
    }
    public boolean hasSpatialDistribution(String name) {
        return spatialDistributionMap.containsKey(name);
    }
}
