package de.unileipzig.irpact.util.irpsim.swagger.simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.util.irpsim.swagger.Base;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class SimulationState extends Base {

    public static final String STATE_FINISHED = "FINISHED";
    public static final String STATE_FINISHEDERROR = "FINISHEDERROR";
    public static final String STATE_UNDEFINED = "UNDEFINED";

    public static final int DEFAULT_MODEL_INDEX = 0;

    protected int modelIndex = DEFAULT_MODEL_INDEX;

    public SimulationState(JsonNode root) {
        super(root);
    }

    @Override
    public JsonNode getRoot() {
        return super.getRoot();
    }

    @Override
    public ObjectNode getRootAsObject() {
        return super.getRootAsObject();
    }

    @Override
    protected String printPrefix() {
        return "SimulationState";
    }

    @Override
    public String toString() {
        return "SimulationState{" +
                "id=" + getId() +
                ", state=" + getState() +
                '}';
    }

    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }

    public int getModelIndex() {
        return modelIndex;
    }

    //=========================
    //access
    //=========================

    public int getId() {
        return JsonUtil.getInt(root, "id", -1);
    }

    public String getIdString() {
        return Integer.toString(getId());
    }

    public boolean isRunning() {
        return JsonUtil.getBoolean(root, "running", false);
    }

    public boolean isError() {
        return JsonUtil.getBoolean(root, "error", false);
    }

    public boolean isSuccessful() {
        return JsonUtil.getBoolean(root, "error", false);
    }

    public String getState() {
        return JsonUtil.getText(root, "state", STATE_UNDEFINED);
    }

    public boolean isFinished() {
        String state = getState();
        switch (state) {
            case STATE_FINISHED:
            case STATE_FINISHEDERROR:
                return true;

            default:
                return false;
        }
    }

    public boolean isNotFinished() {
        return !isFinished();
    }

    public String getStateDescription() {
        return JsonUtil.getText(root, "stateDesc", null);
    }

    protected static Optional<ZonedDateTime> msToTime(long ms) {
        return ms == Long.MIN_VALUE
                ? Optional.empty()
                : Optional.of(TimeUtil.msToTime(ms));
    }

    public long getCreation() {
        return JsonUtil.getLong(root, "creation", Long.MIN_VALUE);
    }

    public Optional<ZonedDateTime> getCreationTime() {
        return msToTime(getCreation());
    }

    public long getStart() {
        return JsonUtil.getLong(root, "start", Long.MIN_VALUE);
    }

    public Optional<ZonedDateTime> getStartTime() {
        return msToTime(getStart());
    }

    public long getEnd() {
        return JsonUtil.getLong(root, "end", Long.MIN_VALUE);
    }

    public Optional<ZonedDateTime> getEndTime() {
        return msToTime(getEnd());
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

    public int getFirstYearIndex() {
        return numberOfYearStates() > 0 ? 0 : -1;
    }

    public int getLastYearIndex() {
        return numberOfYearStates() - 1;
    }

    public int[] getYearStatesIndexArray() {
        return IntStream.rangeClosed(getFirstYearIndex(), getLastYearIndex()).toArray();
    }

    public YearState getYearState(int index) {
        JsonNode arr = root.get("yearStates");
        if(arr == null) {
            return null;
        }
        if(index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundsException();
        }
        JsonNode child = arr.get(index);
        return new YearState(child);
    }

    public int getYear(int index) {
        YearState state = getYearState(index);
        return state == null
                ? -1
                : state.getYear();
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

        public String[] splitSupportiveYears() {
            String years = getSupportiveYears();
            return years == null
                    ? null
                    : years.split(";");
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

        public long getStart() {
            return JsonUtil.getLong(root, "start", Long.MIN_VALUE);
        }

        public JsonNode getMessagesNode() {
            return root.get("messages");
        }

        public JsonNode getStateNode() {
            return root.get("state");
        }
    }

    //=========================
    //filter
    //=========================

    public static Predicate<SimulationState> finished() {
        return SimulationState::isFinished;
    }

    public static Predicate<SimulationState> successfully() {
        return SimulationState::isSuccessful;
    }

    public static Predicate<SimulationState> error() {
        return SimulationState::isError;
    }

    public static Predicate<SimulationState> filterCreationTime(Predicate<? super ZonedDateTime> filter) {
        return state -> {
            if(state == null) return false;
            Optional<ZonedDateTime> time = state.getCreationTime();
            return time.isPresent() && filter.test(time.get());
        };
    }

    public static Predicate<SimulationState> filterCreateTimeSameDay(ZonedDateTime time) {
        return filterCreationTime(stateTime -> stateTime.toLocalDate().equals(time.toLocalDate()));
    }

    public static Predicate<SimulationState> filterCreator(Predicate<? super String> filter) {
        return state -> state != null && filter.test(state.getDescription().getCreator());
    }

    public static Predicate<SimulationState> filterCreator(String creator) {
        return filterCreator(modelCreator -> Objects.equals(modelCreator, creator));
    }

    public static Predicate<SimulationState> filterModelDescription(Predicate<? super String> filter) {
        return state -> state != null && filter.test(state.getDescription().getBusinessModelDescription());
    }

    public static Predicate<SimulationState> filterModelDescription(String description) {
        return filterModelDescription(modelDescription -> Objects.equals(modelDescription, description));
    }

    public static Predicate<SimulationState> filterModelDescriptionStartsWith(String prefix) {
        return filterModelDescription(modelDescription -> modelDescription != null && modelDescription.startsWith(prefix));
    }
}
