package de.unileipzig.irpact.util.irpsim.swagger.scenario;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;
import de.unileipzig.irpact.util.scenarios.Scenario;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

/**
 * @author Daniel Abitz
 */
public class ScenarioMetaData extends Base {

    public ScenarioMetaData() {
        super(JsonUtil.JSON.createObjectNode());
    }

    public ScenarioMetaData(JsonNodeCreator creator) {
        super(creator.objectNode());
    }

    public ScenarioMetaData(JsonNode root) {
        super(root);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }
    public String getIdString() {
        return String.valueOf(getId());
    }

    public String getName() {
        return JsonUtil.getText(root, "name", null);
    }
    public void setName(String name) {
        getRootAsObject().put("name", name);
    }

    public String getCreator() {
        return JsonUtil.getText(root, "creator", null);
    }
    public void setCreator(String creator) {
        getRootAsObject().put("creator", creator);
    }

    public String getDescription() {
        return JsonUtil.getText(root, "description", null);
    }
    public void setDescription(String description) {
        getRootAsObject().put("description", description);
    }

    public int getModeldefinition() {
        return JsonUtil.getInt(root, "modeldefinition", -1);
    }

    public boolean isDeletable() {
        return JsonUtil.getBoolean(root, "deletable", false);
    }

    public boolean isNotDeletable() {
        return !JsonUtil.getBoolean(root, "deletable", true);
    }

    public long getDate() {
        return JsonUtil.getLong(root, "date", -1L);
    }

    public boolean isEqualsId(ScenarioMetaData other) {
        return getId() == other.getId();
    }

    public boolean isEqualsData(ScenarioMetaData other) {
        return getId() == other.getId()
                && Objects.equals(getName(), other.getName())
                && Objects.equals(getCreator(), other.getCreator())
                && Objects.equals(getDescription(), other.getDescription());
    }

    public boolean isEqualsData(String name, String creator, String description) {
        return Objects.equals(getName(), name)
                && Objects.equals(getCreator(), creator)
                && Objects.equals(getDescription(), description);
    }

    @Override
    protected String printPrefix() {
        return "ScenarioMetaData";
    }

    @Override
    public String toString() {
        return "ScenarioMetaData{" +
                "id=" + getId() +
                '}';
    }

    //=========================
    //filter
    //=========================

    private static final ToIdArray INSTANCE = new ToIdArray();

    public static Collector<ScenarioMetaData, ?, int[]> toIdArray() {
        return INSTANCE;
    }

    /**
     * @author Daniel Abitz
     */
    protected static class ToIdArray implements Collector<ScenarioMetaData, List<ScenarioMetaData>, int[]> {

        @Override
        public Supplier<List<ScenarioMetaData>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<ScenarioMetaData>, ScenarioMetaData> accumulator() {
            return List::add;
        }

        @Override
        public BinaryOperator<List<ScenarioMetaData>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<List<ScenarioMetaData>, int[]> finisher() {
            return states -> states.stream()
                    .mapToInt(ScenarioMetaData::getId)
                    .toArray();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Collections.emptySet();
        }
    }

    public static Predicate<ScenarioMetaData> filterCreator(Predicate<? super String> filter) {
        return state -> state != null && filter.test(state.getCreator());
    }

    public static Predicate<ScenarioMetaData> filterCreator(String creator) {
        return filterCreator(modelCreator -> Objects.equals(modelCreator, creator));
    }

    public static BiPredicate<AbstractScenario, ScenarioMetaData> filterNameCreatorDescription() {
        return (scenario, metaData) -> Objects.equals(scenario.getName(), metaData.getName())
                && Objects.equals(scenario.getCreator(), metaData.getCreator())
                && Objects.equals(scenario.getDescription(), metaData.getDescription());
    }
}
