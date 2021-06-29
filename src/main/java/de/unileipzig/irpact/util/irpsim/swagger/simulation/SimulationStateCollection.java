package de.unileipzig.irpact.util.irpsim.swagger.simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class SimulationStateCollection implements Iterable<SimulationState> {

    protected final Map<Integer, SimulationState> states = new TreeMap<>();
    protected final ObjectMapper mapper;

    public SimulationStateCollection() {
        this(JsonUtil.JSON);
    }

    public SimulationStateCollection(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public void clear() {
        states.clear();
    }

    public void parse(Path source) throws IOException {
        parse(source, StandardCharsets.UTF_8);
    }

    public void parse(Path source, Charset charset) throws IOException {
        ArrayNode root = JsonUtil.read(source, charset, mapper);
        if(root == null) return;
        for(JsonNode entry: Util.iterateElements(root)) {
            SimulationState state = new SimulationState(entry);
            put(state);
        }
    }

    public void store(Path target) throws IOException {
        store(target, StandardCharsets.UTF_8);
    }

    public void store(Path target, Charset charset) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        for(SimulationState data: states.values()) {
            root.set(data.getIdString(), data.getRootAsObject());
        }
        JsonUtil.write(root, target, charset, JsonUtil.DEFAULT, mapper);
    }

    public boolean hasSimulationState(int id) {
        return states.containsKey(id);
    }

    public boolean hasId(SimulationState state) {
        return states.containsKey(state.getId());
    }

    public void put(SimulationState state) {
        states.put(state.getId(), state);
    }

    public void validPut(SimulationState state) {
        if(hasId(state)) {
            throw new IRPactIllegalArgumentException("id '{}' already exists", state.getId());
        }
        put(state);
    }

    public SimulationState get(int id) {
        return states.get(id);
    }

    public SimulationState[] getAll(int[] ids) {
        return IntStream.of(ids)
                .mapToObj(this::get)
                .toArray(SimulationState[]::new);
    }

    public int[] filterIdsByBusinessModelDescription(Predicate<? super String> filter) {
        return filterIds(state -> state != null && filter.test(state.getDescription().getBusinessModelDescription()));
    }

    public int[] filterIds(Predicate<? super SimulationState> filter) {
        return stream().filter(filter)
                .collect(toIdArray());
    }

    public Collection<SimulationState> getStates() {
        return states.values();
    }

    @Override
    public Iterator<SimulationState> iterator() {
        return states.values().iterator();
    }

    public Stream<SimulationState> stream() {
        return states.values().stream();
    }

    //=========================
    //helper
    //=========================

    private static final ToIdArray INSTANCE = new ToIdArray();

    public static Collector<SimulationState, ?, int[]> toIdArray() {
        return INSTANCE;
    }

    /**
     * @author Daniel Abitz
     */
    protected static class ToIdArray implements Collector<SimulationState, List<SimulationState>, int[]> {

        @Override
        public Supplier<List<SimulationState>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<SimulationState>, SimulationState> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<SimulationState>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<List<SimulationState>, int[]> finisher() {
            return states -> states.stream()
                    .mapToInt(SimulationState::getId)
                    .toArray();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }
}
