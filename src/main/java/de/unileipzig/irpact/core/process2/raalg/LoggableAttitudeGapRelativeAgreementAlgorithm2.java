package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.logging.simplified.SimplifiedLogger;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.LoggingResourceAccess;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.CloseableSimulationEntity;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static de.unileipzig.irpact.core.process2.raalg.RelativeAgreementAlgorithm2Util.gab;
import static de.unileipzig.irpact.core.process2.raalg.RelativeAgreementAlgorithm2Util.validate;

/**
 * @author Daniel Abitz
 */
public class LoggableAttitudeGapRelativeAgreementAlgorithm2
        extends AttitudeGapRelativeAgreementAlgorithm2
        implements LoggingResourceAccess, LoggableRelativeAgreementAlgorithm2, CloseableSimulationEntity, HelperAPI2 {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    protected Path dir;
    protected String baseName;
    protected SimplifiedLogger valueLogger;
    protected JsonResource resource;
    protected boolean printHeader;
    protected boolean storeXlsx;

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        trace("register on close: {}", environment.registerIfNotRegistered(this));
        resource = load(environment);
        if(resource == null) {
            throw new NullPointerException("resource not found");
        }
        createCsvLogger(dir, baseName);
    }

    @Override
    public SharedModuleData getSharedData() {
        throw new UnsupportedOperationException();
    }

    protected void createCsvLogger(Path dir, String baseName) throws IOException {
        Path target = dir.resolve(baseName + ".csv");
        logClassInfo(target);
        SimplifiedLogger valueLogger = new SimplifiedFileLogger(
                baseName + "_LOGGER",
                SimplifiedFileLogger.createNew(
                        baseName + "_APPENDER",
                        "%msg%n",
                        target
                )
        );
        valueLogger.start();
        this.valueLogger = valueLogger;
        trace("[{}] print header: {}", getName(), isPrintHeader());
        if(isPrintHeader()) {
            writeHeader();
        }
    }

    protected void logClassInfo(Path target) {
        trace(
                "[{}] create logger '{}' (printHeader={}, storeXlsx={}), target: {}",
                getName(),
                getName(),
                isPrintHeader(), isStoreXlsx(),
                target
        );
    }

    protected void writeHeader() {
        log(
                getLocalizedString("ni"),
                getLocalizedString("xi"),
                getLocalizedString("ui"),
                getLocalizedString("newXi"),
                getLocalizedString("newUi"),
                getLocalizedString("nj"),
                getLocalizedString("xj"),
                getLocalizedString("uj"),
                getLocalizedString("newXj"),
                getLocalizedString("newUj"),
                getLocalizedString("attr"),
                getLocalizedString("desc"),
                getLocalizedString("changed"),
                getLocalizedString("time")
        );
    }

    public static final int NI_INDEX = 0;
    public static final int XI_INDEX = 1;
    public static final int UI_INDEX = 2;
    public static final int NEW_XI_INDEX = 3;
    public static final int NEW_UI_INDEX = 4;
    public static final int NJ_INDEX = 5;
    public static final int XJ_INDEX = 6;
    public static final int UJ_INDEX = 7;
    public static final int NEW_XJ_INDEX = 8;
    public static final int NEW_UJ_INDEX = 9;
    public static final int ATTR_INDEX = 10;
    public static final int DESC_INDEX = 11;
    public static final int CHANGED_INDEX = 12;
    public static final int TIME_INDEX = 13;

    public void setDir(Path dir) {
        this.dir = dir;
    }

    public Path getDir() {
        return dir;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setPrintHeader(boolean printHeader) {
        this.printHeader = printHeader;
    }

    public boolean isPrintHeader() {
        return printHeader;
    }

    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }

    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    public SimplifiedLogger getValueLogger() {
        return valueLogger;
    }

    @Override
    public JsonResource getLocalizedData() {
        return resource;
    }

    @Override
    public String getResourceType() {
        return "LoggableAttitudeGapRelativeAgreementAlgorithm2";
    }

    @Override
    public boolean calculateInfluence(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            double[] influence,
            Timestamp time) {
        validate(influence);

        double gab = gab(xi, xj);

        if(gab < getAttitudeGap()) {
            return calculateWithinGab(ni, xi, ui, nj, xj, uj, attr, influence, time);
        } else {
            return calculateWithMode(ni, xi, ui, nj, xj, uj, attr, influence, time);
        }
    }

    protected boolean calculateWithinGab(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            double[] influence,
            Timestamp time) {
        boolean changed = calculateConvergence(xi, ui, xj, uj, influence);
        logDefault(ni, xi, ui, nj, xj, uj, attr, changed, influence, time);
        return changed;
    }

    protected boolean calculateWithMode(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            double[] influence,
            Timestamp time) {
        WeightedMapping<AttitudeGap> mapping = getMapping();
        AttitudeGap mode = mapping.getWeightedRandom(getRnd());
        switch (mode) {
            case NEUTRAL:
                boolean changedNeutral = calculateNeutral(xi, ui, xj, uj, influence);
                logNeutral(ni, xi, ui, nj, xj, uj, attr, time);
                return changedNeutral;

            case CONVERGENCE:
                boolean changedPositive = calculateConvergence(xi, ui, xj, uj, influence);
                logPositive(ni, xi, ui, nj, xj, uj, attr, changedPositive, influence, time);
                return changedPositive;

            case DIVERGENCE:
                boolean changedNegative = calculateDivergence(xi, ui, xj, uj, influence);
                logNegative(ni, xi, ui, nj, xj, uj, attr, changedNegative, influence, time);
                return changedNegative;

            default:
                throw new IllegalStateException("unsupported attide gab mode: " + mode);
        }
    }

    protected void logDefault(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            boolean changed,
            double[] influence,
            Timestamp time) {
        if(time == null) return;

        doLog(
                ni, xi, ui,
                nj, xj, uj,
                attr,
                getDefaultDescription(),
                changed,
                influence,
                time
        );
    }

    protected String getDefaultDescription() {
        return getLocalizedString("defaultDescription");
    }

    protected void logNeutral(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            Timestamp time) {
        if(time == null) return;

        doLog(
                ni, xi, ui, xi, ui,
                nj, xj, uj, xj, uj,
                attr,
                getNeutralDescription(),
                false,
                time
        );
    }

    protected String getNeutralDescription() {
        return getLocalizedString("neutralDescription");
    }

    protected void logPositive(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            boolean changed,
            double[] influence,
            Timestamp time) {
        if(time == null) return;

        doLog(
                ni, xi, ui,
                nj, xj, uj,
                attr,
                getPositiveDescription(),
                changed,
                influence,
                time
        );
    }

    protected String getPositiveDescription() {
        return getLocalizedString("positiveDescription");
    }

    protected void logNegative(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            boolean changed,
            double[] influence,
            Timestamp time) {
        if(time == null) return;

        doLog(
                ni, xi, ui,
                nj, xj, uj,
                attr,
                getNegativeDescription(),
                changed,
                influence,
                time
        );
    }

    protected String getNegativeDescription() {
        return getLocalizedString("negativeDescription");
    }

    protected void doLog(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            String desc,
            boolean changed,
            double[] influence,
            Timestamp time) {
        if(time == null) return;

        double newXj = influence[0];
        double newUj = influence[1];
        double newXi = influence[2];
        double newUi = influence[3];
        doLog(
                ni, xi, ui, newXi, newUi,
                nj, xj, uj, newXj, newUj,
                attr,
                desc,
                changed,
                time
        );
    }

    protected void doLog(
            String ni, double xi, double ui, double newXi, double newUi,
            String nj, double xj, double uj, double newXj, double newUj,
            String attr,
            String desc,
            boolean changed,
            Timestamp time) {
        if(time == null) return;

        log(ni, xi, ui, newXi, newUi, nj, xj, uj, newXj, newUj, attr, desc, changed, printTime(time));
    }

    protected String printTime(Timestamp time) {
        return time == null ? null : FORMATTER.format(time.getTime().toLocalDateTime());
    }

    protected void log(
            Object ni, Object xi, Object ui, Object newXi, Object newUi,
            Object nj, Object xj, Object uj, Object newXj, Object newUj,
            Object attr,
            Object desc,
            Object changed,
            Object time) {
        if(time == null) return;

        getValueLogger().log(
                "{},{},{},{},{},{},{},{},{},{},{},{},{},{}",
                time,
                attr,
                desc,
                changed,
                ni, xi, ui, newXi, newUi,
                nj, xj, uj, newXj, newUj
        );
    }

    @Override
    public void closeEntity() {
        if(valueLogger != null) {
            valueLogger.stop();
            if(storeXlsx) {
                storeXlsx();
            }
        }
    }

    protected void storeXlsx() {
        try {
            trace("[{}] store xlsx", getBaseName());
            storeXlsx0();
        } catch (Throwable t) {
            error("store xlsx failed", t);
        }
    }

    protected void storeXlsx0() throws IOException {
        Path csvPath = getDir().resolve(getBaseName() + ".csv");
        trace("try load '{}'", csvPath);
        if(Files.exists(csvPath)) {
            JsonTableData3 csvData = loadCsv(csvPath, ";");
            Map<String, JsonTableData3> xlsxData = toXlsxData(csvData);

            Path xlsxPath = getDir().resolve(getBaseName() + ".xlsx");
            trace("try store '{}'", xlsxPath);
            storeXlsxWithTime(xlsxPath, FORMATTER, xlsxData);
            trace("stored '{}': {}", xlsxPath, Files.exists(xlsxPath));
        } else {
            info("file '{}' not found", csvPath);
        }
    }

    protected Map<String, JsonTableData3> toXlsxData(JsonTableData3 csvData) {
        Map<String, JsonTableData3> xlsxSheetData = new HashMap<>();

        JsonTableData3 xlsxData = csvData.copy();
        int from = isPrintHeader() ? 1 : 0;
        xlsxData.mapStringColumnToDouble(XJ_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(UJ_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(NEW_XJ_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(NEW_UJ_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(XI_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(UI_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(NEW_XI_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(NEW_UI_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToBoolean(CHANGED_INDEX, from, Boolean::parseBoolean);

        xlsxSheetData.put(getLocalizedString("sheetName"), xlsxData);

        return xlsxSheetData;
    }
}
