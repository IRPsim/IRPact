package de.unileipzig.irpact.util.irpsim.swagger;

import de.unileipzig.irpact.util.curl.CurlException;
import de.unileipzig.irpact.util.irpsim.swagger.scenario.ScenarioMetaData;
import de.unileipzig.irpact.util.irpsim.swagger.simulation.SimulationState;
import de.unileipzig.irpact.util.scenarios.Scenario;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface SwaggerSuite {

    int INVALID_ID = -1;

    static boolean isInvalid(int id) {
        return id == INVALID_ID;
    }

    static boolean isValid(int id) {
        return id != INVALID_ID;
    }

    //=========================
    //Scenarios
    //=========================

    //==========
    //util
    //==========

    default <S extends Scenario> int findId(S scenario, BiPredicate<? super S, ? super ScenarioMetaData> filter) {
        for(ScenarioMetaData metaData: getMetaData()) {
            if(filter.test(scenario, metaData)) {
                return metaData.getId();
            }
        }
        return INVALID_ID;
    }

    default <S extends Scenario> void findAllIds(
            Iterable<? extends S> scenarios,
            BiPredicate<? super S, ? super ScenarioMetaData> filter,
            Map<S, Integer> out) {
        for(S scenario: scenarios) {
            int id = findId(scenario, filter);
            out.put(scenario, id);
        }
    }

    default <S extends Scenario> Map<S, Integer> findAllIds(
            Iterable<? extends S> scenarios,
            BiPredicate<? super S, ? super ScenarioMetaData> filter) {
        Map<S, Integer> map = new HashMap<>();
        findAllIds(scenarios, filter, map);
        return map;
    }

    //==========
    //locale
    //==========

    Stream<ScenarioMetaData> streamMetaData();

    Collection<ScenarioMetaData> getMetaData();

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

    default void addAllScenarios(Collection<? extends Scenario> scenarios) throws CurlException, IOException, InterruptedException {
        for(Scenario scenario: scenarios) {
            addScenario(scenario);
        }
    }

    boolean deleteScenario(int id) throws CurlException, IOException, InterruptedException;

    default boolean deleteAllScenarios(int[] ids) throws CurlException, IOException, InterruptedException {
        boolean changed = false;
        for(int id: ids) {
            changed |= deleteScenario(id);
        }
        return changed;
    }

    default boolean deleteAllScenarios(Predicate<? super ScenarioMetaData> filter) throws CurlException, IOException, InterruptedException {
        int[] ids = streamMetaData().filter(filter).collect(ScenarioMetaData.toIdArray());
        return deleteAllScenarios(ids);
    }

    boolean overwriteScenario(int id, Scenario scenario) throws CurlException, IOException, InterruptedException;

    default boolean overwriteAllScenarios(Map<? extends Scenario, ? extends Integer> scenarios) throws CurlException, IOException, InterruptedException {
        boolean changed = false;
        for(Map.Entry<? extends Scenario, ? extends Integer> entry: scenarios.entrySet()) {
            changed |= overwriteScenario(entry.getValue(), entry.getKey());
        }
        return changed;
    }

    default <S extends Scenario> boolean addIfAbsent(S scenario, BiPredicate<? super S, ? super ScenarioMetaData> filter) throws CurlException, IOException, InterruptedException {
        int id = findId(scenario, filter);
        if(isInvalid(id)) {
            addScenario(scenario);
            return true;
        } else {
            overwriteScenario(id, scenario);
            return false;
        }
    }

    default <S extends Scenario> void addIfAbsent(S[] scenarios, BiPredicate<? super S, ? super ScenarioMetaData> filter) throws CurlException, IOException, InterruptedException {
        for(S scenario: scenarios) {
            addIfAbsent(scenario, filter);
        }
    }

    default <S extends Scenario> void addIfAbsent(Iterable<? extends S> scenarios, BiPredicate<? super S, ? super ScenarioMetaData> filter) throws CurlException, IOException, InterruptedException {
        for(S scenario: scenarios) {
            addIfAbsent(scenario, filter);
        }
    }

    void commit() throws IOException, CurlException, InterruptedException;

    //=========================
    //Simulation
    //=========================

    GenericResult startSimulation(Scenario scenario) throws IOException, CurlException, InterruptedException;

    //==========
    //locale
    //==========

    default int findStateId(Predicate<? super SimulationState> filter) {
        for(SimulationState state: getStates()) {
            if(filter.test(state)) {
                return state.getId();
            }
        }
        return INVALID_ID;
    }

    Stream<SimulationState> streamStates();

    Collection<SimulationState> getStates();

    void localeLoadStates() throws IOException;

    void localeStoreStates() throws IOException;

    void localeClearStates();

    void backupLocaleStates() throws IOException;

    //==========
    //remote
    //==========

    void remoteLoadSimulationStates() throws IOException, CurlException, InterruptedException;

    void downloadOutput(int id, Path target) throws CurlException, IOException, InterruptedException;

    boolean hasZip(int id) throws CurlException, IOException, InterruptedException;

    void downloadZip(int id, Path target) throws CurlException, IOException, InterruptedException;
}
