package de.unileipzig.irpact.util.irpsim.swagger.simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class SimulationState extends Base {

    public static final String STATE_FINISHED = "FINISHED";
    public static final String STATE_FINISHEDERROR = "FINISHEDERROR";
    public static final String STATE_UNDEFINED = "UNDEFINED";

    public SimulationState(JsonNode root) {
        super(root);
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    @Override
    public String toString() {
        return "SimulationState{" +
                "id=" + getId() +
                ", state=" + getState() +
                '}';
    }

    //=========================
    //access
    //=========================

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }

    public boolean isRunning() {
        return JsonUtil.getBoolean(root, "running", false);
    }

    public boolean isError() {
        return JsonUtil.getBoolean(root, "error", false);
    }

    public String getState() {
        return JsonUtil.getText(root, "state", STATE_UNDEFINED);
    }

    public long getCreation() {
        return JsonUtil.getLong(root, "creation", Long.MIN_VALUE);
    }

    public long getStart() {
        return JsonUtil.getLong(root, "start", Long.MIN_VALUE);
    }

    public long getEnd() {
        return JsonUtil.getLong(root, "end", Long.MIN_VALUE);
    }

    protected Description description;
    public Description getDescription() {
        if(description == null) {
            description = new Description(JsonUtil.computeObjectIfAbsent(getRootAsObject(), "description"));
        }
        return description;
    }

    public int getYearIndex() {
        return JsonUtil.getInt(root, "yearIndex", -1);
    }

    public int numberOfYearStates() {
        JsonNode arr = root.get("yearStates");
        return arr == null ? 0 : arr.size();
    }

    public int[] getYearStatesIndexArray() {
        return IntStream.range(0, numberOfYearStates()).toArray();
    }

    public YearState getYearState(int index) {
        JsonNode arr = root.get("yearStates");
        if(arr == null || index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundsException();
        }
        JsonNode child = arr.get(index);
        return new YearState(child);
    }

    public YearState getFirstYearState() {
        return getYearState(0);
    }

    public YearState getLastYearState() {
        return getYearState(numberOfYearStates() - 1);
    }

    public int findIndexOfYear(int year) {
        for(int i = 0; i < numberOfYearStates(); i++) {
            YearState state = getYearState(i);
            if(state.getYear() == year) {
                return i;
            }
        }
        return -1;
    }

    //=========================
    //sub components
    //=========================

    /**
     * @author Daniel Abitz
     */
    public static class Description extends Base {

        protected Description(JsonNode root) {
            super(root);
        }

        @Override
        public ObjectNode getRootAsObject() {
            return super.getRootAsObject();
        }

        public String getSupportiveYears() {
            return JsonUtil.getText(root, "supportiveYears", null);
        }

        public String getBusinessModelDescription() {
            return JsonUtil.getText(root, "businessModelDescription", null);
        }

        public String getInvestmentCustomerSide() {
            return JsonUtil.getText(root, "investmentCustomerSide", null);
        }

        public String getParameterAttention() {
            return JsonUtil.getText(root, "parameterAttention", null);
        }

        public String getCreator() {
            return JsonUtil.getText(root, "creator", null);
        }
    }

    /**
     * @author Daniel Abitz
     */
    public static class YearState extends Base {

        protected YearState(JsonNode root) {
            super(root);
        }

        @Override
        public ArrayNode getRootAsArray() {
            return super.getRootAsArray();
        }

        public int getYear() {
            return JsonUtil.getInt(root, "year", Integer.MIN_VALUE);
        }
    }

    //=========================
    //filter
    //=========================

    public static Predicate<SimulationState> filterCreator(String creator) {
        return state -> Objects.equals(state.getDescription().getCreator(), creator);
    }

    public static Predicate<SimulationState> filterModelDescription(String description) {
        return state -> Objects.equals(state.getDescription().getBusinessModelDescription(), description);
    }

    public static Predicate<SimulationState> filterModelDescriptionStartsWith(String prefix) {
        return state -> {
            String desc = state.getDescription().getBusinessModelDescription();
            return desc != null && desc.startsWith(prefix);
        };
    }
}
