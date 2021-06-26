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
public class ScenarioDataCollection implements Iterable<ScenarioData> {

    protected final Map<Integer, ScenarioData> cache = new TreeMap<>();
    protected final ObjectMapper mapper;

    public ScenarioDataCollection() {
        this(JsonUtil.JSON);
    }

    public ScenarioDataCollection(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public boolean hasId(ScenarioData data) {
        return cache.containsKey(data.getId());
    }

    public void validate(ScenarioData data) throws IllegalArgumentException {
        ScenarioData containd = cache.get(data.getId());
        if(containd != null) {
            if(containd.getId() != data.getId()) {
                throw new IRPactIllegalArgumentException("id mismatch: (contained) '{}' != '{}' (other)", containd.getId(), data.getId());
            }
        }
    }

    public void clear() {
        cache.clear();
    }

    public void put(ScenarioData data) {
        cache.put(data.getId(), data);
    }

    public void validPut(ScenarioData data) {
        if(hasId(data)) {
            throw new IRPactIllegalArgumentException("id '{}' already exists", data.getId());
        }
        put(data);
    }

    public void putAll(Collection<? extends ScenarioData> datas) {
        for(ScenarioData data: datas) {
            put(data);
        }
    }

    public void parse(Path source) throws IOException {
        parse(source, StandardCharsets.UTF_8);
    }

    public void parse(Path source, Charset charset) throws IOException {
        ObjectNode root = JsonUtil.read(source, charset, mapper);
        if(root == null) return;
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(root)) {
            int id = Integer.parseInt(entry.getKey());
            ScenarioData data = new ScenarioData(entry.getValue(), id);
            put(data);
        }
    }

    public void store(Path target) throws IOException {
        store(target, StandardCharsets.UTF_8);
    }

    public void store(Path target, Charset charset) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        for(ScenarioData data: cache.values()) {
            root.set(data.getIdString(), data.getRootAsObject());
        }
        JsonUtil.write(root, target, charset, JsonUtil.DEFAULT, mapper);
    }

    @Override
    public Iterator<ScenarioData> iterator() {
        return cache.values().iterator();
    }

    public Stream<ScenarioData> stream() {
        return cache.values().stream();
    }
}
