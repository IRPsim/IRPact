package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class ScenarioMetaDataCollection implements Iterable<ScenarioMetaData> {

    protected final Map<Integer, ScenarioMetaData> cache = new TreeMap<>();
    protected final ObjectMapper mapper;

    public ScenarioMetaDataCollection() {
        this(JsonUtil.JSON);
    }

    public ScenarioMetaDataCollection(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public boolean hasId(ScenarioMetaData metaData) {
        return cache.containsKey(metaData.getId());
    }

    public boolean hasScenario(int id) {
        return cache.containsKey(id);
    }

    public void validate(ScenarioMetaData metaData) throws IllegalArgumentException {
        ScenarioMetaData containd = cache.get(metaData.getId());
        if(containd != null) {
            if(containd.getId() != metaData.getId()) {
                throw new IRPactIllegalArgumentException("id mismatch: (contained) '{}' != '{}' (other)", containd.getId(), metaData.getId());
            }
            if(!Objects.equals(containd.getName(), metaData.getName())) {
                throw new IRPactIllegalArgumentException("name mismatch: (contained) '{}' != '{}' (other)", containd.getName(), metaData.getName());
            }
            if(!Objects.equals(containd.getCreator(), metaData.getCreator())) {
                throw new IRPactIllegalArgumentException("creator mismatch: (contained) '{}' != '{}' (other)", containd.getCreator(), metaData.getCreator());
            }
            if(!Objects.equals(containd.getDescription(), metaData.getDescription())) {
                throw new IRPactIllegalArgumentException("description mismatch: (contained) '{}' != '{}' (other)", containd.getDescription(), metaData.getDescription());
            }
        }
    }

    public void clear() {
        cache.clear();
    }

    public void put(ScenarioMetaData metaData) {
        cache.put(metaData.getId(), metaData);
    }

    public void validPut(ScenarioMetaData metaData) {
        if(hasId(metaData)) {
            throw new IRPactIllegalArgumentException("id '{}' already exists", metaData.getId());
        }
        put(metaData);
    }

    public void putAll(Collection<? extends ScenarioMetaData> metaDatas) {
        for(ScenarioMetaData metaData: metaDatas) {
            put(metaData);
        }
    }

    public void parse(Path source) throws IOException {
        parse(source, StandardCharsets.UTF_8);
    }

    public void parse(Path source, Charset charset) throws IOException {
        ObjectNode root = JsonUtil.read(source, charset, mapper);
        if(root == null) return;
        for(JsonNode entry: Util.iterateElements(root)) {
            ScenarioMetaData metaData = new ScenarioMetaData(entry);
            put(metaData);
        }
    }

    public void store(Path target) throws IOException {
        store(target, StandardCharsets.UTF_8);
    }

    public void store(Path target, Charset charset) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        for(ScenarioMetaData data: cache.values()) {
            root.set(data.getIdString(), data.getRootAsObject());
        }
        JsonUtil.write(root, target, charset, JsonUtil.DEFAULT, mapper);
    }

    public Collection<ScenarioMetaData> getMetaData() {
        return cache.values();
    }

    @Override
    public Iterator<ScenarioMetaData> iterator() {
        return cache.values().iterator();
    }

    public Stream<ScenarioMetaData> stream() {
        return cache.values().stream();
    }
}
