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
import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.DIR_ConsumerAgentGroups;

/**
 * @author Daniel Abitz
 */
public class SpecificationData {

    private static final DirectoryStream.Filter<Path> FILTER = path -> {
        if(path == null) {
            return false;
        }
        if(Files.isDirectory(path)) {
            return true;
        }
        String fileName = path.getFileName().toString();
        return fileName.endsWith(JSON_EXTENSION_WITH_DOT);
    };

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpecificationData.class);

    protected final ObjectMapper mapper;

    protected final Map<String, FileEntry> fileEntries = new HashMap<>();
    protected final Map<String, DirEntry> dirEntries = new HashMap<>();

    public SpecificationData() {
        this(IRPactJson.JSON);
    }

    public SpecificationData(ObjectMapper mapper) {
        this.mapper = mapper;
        init();
    }

    private void init() {
        //dir
        addDir(DIR_Distributions);
        addDir(DIR_SpatialDistributions);
        addDir(DIR_ProductGroups);
        addDir(DIR_ConsumerAgentGroups);
        //file
        addFile(FILE_General);
        addFile(FILE_TimeModel);
        addFile(FILE_SpatialModel);
        addFile(FILE_BinaryData);
        addFile(FILE_SocialNetwork);
        addFile(FILE_Files);
        addFile(FILE_Affinities);
        addFile(FILE_ProcessModel);
    }

    private void addDir(String name) {
        addDir(new DirEntry(mapper, name));
    }

    private void addDir(DirEntry entry) {
        if(dirEntries.containsKey(entry.getDirKey())) {
            throw new IllegalArgumentException("key '" + entry.getDirKey() + "' already exists");
        } else {
            dirEntries.put(entry.getDirKey(),entry);
        }
    }

    private void addFile(String name) {
        addFile(new FileEntry(name, mapper.createObjectNode()));
    }

    private void addFile(FileEntry entry) {
        if(fileEntries.containsKey(entry.getFileName())) {
            throw new IllegalArgumentException("key '" + entry.getFileName() + "' already exists");
        } else {
            fileEntries.put(entry.getFileName(),entry);
        }
    }

    //=========================
    //general
    //=========================

    private static String toJson(String input) {
        return input.endsWith(JSON_EXTENSION_WITH_DOT)
                ? input
                : input + JSON_EXTENSION_WITH_DOT;
    }

    private static String removeJson(String input) {
        return input.endsWith(JSON_EXTENSION_WITH_DOT)
                ? input.substring(0, input.length() - JSON_EXTENSION_WITH_DOT.length())
                : input;
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

    private String getDirKey(Path dir) {
        String name = dir.getFileName().toString();
        if(dirEntries.containsKey(name)) {
            return name;
        } else {
            return DIR_NONE;
        }
    }

    private void loadFile(String dirKey, Path file) throws IOException {
        String fileName = file.getFileName().toString();
        String fileNameWithoutJson = removeJson(fileName);
        ObjectNode root = IRPactJson.readJson(file, StandardCharsets.UTF_8);

        if(Objects.equals(dirKey, DIR_NONE)) {
            FileEntry entry = fileEntries.get(fileName);
            if(entry != null) {
                entry.set(root);
            } else {
                LOGGER.warn("unsupported file: '{}'", fileName);
            }
        } else {
            DirEntry entry = dirEntries.get(dirKey);
            if(entry != null) {
                entry.put(fileNameWithoutJson, root);
            } else {
                LOGGER.warn("unsupported dir: '{}'", dirKey);
            }
        }
    }

    //=========================
    //store
    //=========================

    public void store(Path dir) throws IOException {
        store(dir, IRPactJson.JSON, Util.defaultPrinter, true);
    }

    public void store(Path dir, ObjectMapper mapper, PrettyPrinter printer, boolean skipIfEmpty) throws IOException {
        if(Files.notExists(dir)) {
            LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "create '{}'", dir);
            Files.createDirectories(dir);
        }

        for(FileEntry entry: fileEntries.values()) {
            entry.store(dir, mapper, printer, skipIfEmpty);
        }

        for(DirEntry entry: dirEntries.values()) {
            entry.store(dir, mapper, printer, skipIfEmpty);
        }
    }

    //=========================
    //access
    //=========================

    public FileEntry getFileEntry(String key) {
        FileEntry entry = fileEntries.get(key);
        if(entry == null) {
            throw new IllegalArgumentException("key '" + key + "' not found");
        }
        return entry;
    }

    public FileEntry getGeneral() {
        return getFileEntry(FILE_General);
    }

    public FileEntry getTimeModel() {
        return getFileEntry(FILE_TimeModel);
    }

    public FileEntry getSpatialModel() {
        return getFileEntry(FILE_SpatialModel);
    }

    public FileEntry getBinaryData() {
        return getFileEntry(FILE_BinaryData);
    }

    public FileEntry getSocialNetwork() {
        return getFileEntry(FILE_SocialNetwork);
    }

    public FileEntry getFiles() {
        return getFileEntry(FILE_Files);
    }

    public FileEntry getAffinities() {
        return getFileEntry(FILE_Affinities);
    }

    public FileEntry getProcessModel() {
        return getFileEntry(FILE_ProcessModel);
    }

    public DirEntry getDirEntry(String key) {
        DirEntry entry = dirEntries.get(key);
        if(entry == null) {
            throw new IllegalArgumentException("key '" + key + "' not found");
        }
        return entry;
    }

    public DirEntry getDistributions() {
        return getDirEntry(DIR_Distributions);
    }

    public DirEntry getSpatialDistributions() {
        return getDirEntry(DIR_SpatialDistributions);
    }

    public DirEntry getProductGroups() {
        return getDirEntry(DIR_ProductGroups);
    }

    public DirEntry getConsumerAgentGroups() {
        return getDirEntry(DIR_ConsumerAgentGroups);
    }

//    public DirEntry getProductInterests() {
//        return getDirEntry(DIR_ConsumerAgentGroups);
//    }

    //=========================
    //helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    @SuppressWarnings("FieldMayBeFinal")
    public static class FileEntry {

        private final String NAME;
        private ObjectNode root;

        public FileEntry(String name, ObjectNode root) {
            this.NAME = name;
            this.root = root;
        }

        public String getFileName() {
            return NAME;
        }

        private void store(Path dir, ObjectMapper mapper, PrettyPrinter printer, boolean skipIfEmpty) throws IOException {
            if(skipIfEmpty && isEmpty()) {
                return;
            }

            Path path = dir.resolve(getFileName());
            LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "write '{}'", path);
            IRPactJson.write(
                    root,
                    path,
                    StandardCharsets.UTF_8,
                    printer,
                    mapper
            );
        }

        public boolean isEmpty() {
            return root.isEmpty();
        }

        public void set(ObjectNode root) {
            this.root = root;
        }

        public ObjectNode get() {
            return root;
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class DirEntry {

        private final String KEY;
        private final ObjectMapper MAPPER;
        private final Map<String, ObjectNode> ENTRIES = new HashMap<>();

        public DirEntry(ObjectMapper mapper, String dirKey) {
            this.MAPPER = mapper;
            this.KEY = dirKey;
        }

        public String getDirKey() {
            return KEY;
        }

        public boolean isEmpty() {
            int count = 0;
            for(ObjectNode node: getAll().values()) {
                count += node.size();
            }
            return count == 0;
        }

        private void store(Path dir, ObjectMapper mapper, PrettyPrinter printer, boolean skipIfEmpty) throws IOException {
            if(skipIfEmpty && isEmpty()) {
                return;
            }

            Path thisDir = dir.resolve(KEY);
            if(Files.notExists(thisDir)) {
                LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "create '{}'", thisDir);
                Files.createDirectories(thisDir);
            }

            for(Map.Entry<String, ObjectNode> entry: getAll().entrySet()) {
                Path path = thisDir.resolve(toJson(entry.getKey()));
                LOGGER.debug(IRPSection.SPECIFICATION_CONVERTER, "write '{}'", path);
                IRPactJson.write(
                        entry.getValue(),
                        path,
                        StandardCharsets.UTF_8,
                        printer,
                        mapper
                );
            }
        }

        public Map<String, ObjectNode> getAll() {
            return ENTRIES;
        }

        public void put(String nameWithoutExtension, ObjectNode node) {
            ENTRIES.put(nameWithoutExtension, node);
        }

        public ObjectNode get(String name) {
            return ENTRIES.computeIfAbsent(name, _name -> MAPPER.createObjectNode());
        }

        public boolean has(String name) {
            return ENTRIES.containsKey(name);
        }

        public boolean hasNot(String name) {
            return !has(name);
        }
    }
}
