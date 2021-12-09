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

    /**
     * @author Daniel Abitz
     */
    public enum LoggingMode {
        DISABLED,
        OPINION,
        UNCERTAINTY,
        OPINION_UNCERTAINTY;

        public static LoggingMode get(boolean logOpionion, boolean logUncertainty) {
            if(logOpionion) {
                if(logUncertainty) {
                    return OPINION_UNCERTAINTY;
                } else {
                    return OPINION;
                }
            } else {
                if(logUncertainty) {
                    return UNCERTAINTY;
                } else {
                    return DISABLED;
                }
            }
        }

        int map(int index) {
            switch (this) {
                case OPINION:
                    switch (index) {
                        case TIME_INDEX: return 0;
                        case ATTR_INDEX: return 1;
                        case DESC_INDEX: return 2;
                        case CHANGED_INDEX: return 3;
                        case NI_INDEX: return 4;
                        case XI_INDEX: return 5;
                        case UI_INDEX: return  6;
                        case NEW_XI_INDEX: return 7;
                        case NEW_UI_INDEX: throw new IllegalArgumentException("new ui not supported");
                        case NJ_INDEX: return 8;
                        case XJ_INDEX: return 9;
                        case UJ_INDEX: return 10;
                        case NEW_XJ_INDEX: return 11;
                        case NEW_UJ_INDEX: throw new IllegalArgumentException("new uj not supported");

                        default:
                            throw new IllegalArgumentException("unsupported: " + index);
                    }

                case UNCERTAINTY:
                    switch (index) {
                        case TIME_INDEX: return 0;
                        case ATTR_INDEX: return 1;
                        case DESC_INDEX: return 2;
                        case CHANGED_INDEX: return 3;
                        case NI_INDEX: return 4;
                        case XI_INDEX: return 5;
                        case UI_INDEX: return 6;
                        case NEW_XI_INDEX: throw new IllegalArgumentException("new xi not supported");
                        case NEW_UI_INDEX: return 7;
                        case NJ_INDEX: return 8;
                        case XJ_INDEX: return 9;
                        case UJ_INDEX: return 10;
                        case NEW_XJ_INDEX: throw new IllegalArgumentException("new xj not supported");
                        case NEW_UJ_INDEX: return 11;

                        default:
                            throw new IllegalArgumentException("unsupported: " + index);
                    }

                case OPINION_UNCERTAINTY:
                    switch (index) {
                        case TIME_INDEX:
                        case ATTR_INDEX:
                        case DESC_INDEX:
                        case CHANGED_INDEX:
                        case NI_INDEX:
                        case XI_INDEX:
                        case UI_INDEX:
                        case NEW_XI_INDEX:
                        case NEW_UI_INDEX:
                        case NJ_INDEX:
                        case XJ_INDEX:
                        case UJ_INDEX:
                        case NEW_XJ_INDEX:
                        case NEW_UJ_INDEX:
                            return index;

                        default:
                            throw new IllegalArgumentException("unsupported: " + index);
                    }

                default:
                    throw new IllegalArgumentException("unsupported: " + index);
            }
        }
    }

    public static final int TIME_INDEX = 0;
    public static final int ATTR_INDEX = 1;
    public static final int DESC_INDEX = 2;
    public static final int CHANGED_INDEX = 3;
    public static final int NI_INDEX = 4;
    public static final int XI_INDEX = 5;
    public static final int UI_INDEX = 6;
    public static final int NEW_XI_INDEX = 7;
    public static final int NEW_UI_INDEX = 8;
    public static final int NJ_INDEX = 9;
    public static final int XJ_INDEX = 10;
    public static final int UJ_INDEX = 11;
    public static final int NEW_XJ_INDEX = 12;
    public static final int NEW_UJ_INDEX = 13;


    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    protected LoggingMode loggingMode = LoggingMode.OPINION_UNCERTAINTY;
    protected Path dir;
    protected String baseName;
    protected SimplifiedLogger valueLogger;
    protected JsonResource resource;
    protected boolean printHeader;
    protected boolean storeXlsx;
    protected boolean keepCsv;
    protected boolean loggingEnabled = false;

    public void setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public boolean isLoggingDisabled() {
        return !loggingEnabled;
    }

    protected boolean isLoggingDisabled(Object time) {
        return time == null || isLoggingDisabled() || loggingMode == LoggingMode.DISABLED;
    }

    public void setLoggingMode(LoggingMode loggingMode) {
        this.loggingMode = loggingMode;
    }

    public LoggingMode getLoggingMode() {
        return loggingMode;
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        trace("register on close: {}", environment.registerIfNotRegistered(this));
        setDir(environment.getSettings().getDownloadDir());
        resource = load(environment);
        if(resource == null) {
            throw new NullPointerException("resource not found");
        }
        if(isLoggingEnabled()) {
            createCsvLogger(dir, baseName);
        }
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
                "[{}] create logger '{}' (printHeader={}, storeXlsx={}), mode={}, target={}",
                getName(),
                getName(),
                isPrintHeader(), isStoreXlsx(),
                getLoggingMode().name(),
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

    public void setKeepCsv(boolean keepCsv) {
        this.keepCsv = keepCsv;
    }

    public boolean isKeepCsv() {
        return keepCsv;
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

        double gap = gab(xi, xj);

        if(gap < getAttitudeGap()) {
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

        if(mapping.isEmpty()) {
            throw new IllegalStateException("empty weight mapping");
        }

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
        if(isLoggingDisabled(time)) return;

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
        if(isLoggingDisabled(time)) return;

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
        if(isLoggingDisabled(time)) return;

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
        if(isLoggingDisabled(time)) return;

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
        if(isLoggingDisabled(time)) return;

        double newXi = influence[INDEX_XI];
        double newUi = influence[INDEX_UI];
        double newXj = influence[INDEX_XJ];
        double newUj = influence[INDEX_UJ];

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
        if(isLoggingDisabled(time)) return;

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
        if(isLoggingDisabled(time)) return;

        switch (loggingMode) {
            case OPINION:
                getValueLogger().log(
                        "{};{};{};{};{};{};{};{};{};{};{};{}",
                        time,
                        attr,
                        desc,
                        changed,
                        ni, xi, ui, newXi,
                        nj, xj, uj, newXj
                );
                break;

            case UNCERTAINTY:
                getValueLogger().log(
                        "{};{};{};{};{};{};{};{};{};{};{};{}",
                        time,
                        attr,
                        desc,
                        changed,
                        ni, xi, ui, newUi,
                        nj, xj, uj, newUj
                );
                break;

            case OPINION_UNCERTAINTY:
                getValueLogger().log(
                        "{};{};{};{};{};{};{};{};{};{};{};{};{};{}",
                        time,
                        attr,
                        desc,
                        changed,
                        ni, xi, ui, newXi, newUi,
                        nj, xj, uj, newXj, newUj
                );
                break;
        }
    }

    @Override
    public void closeEntity() {
        if(valueLogger != null) {
            valueLogger.stop();
            boolean xlsxSuccessful = true;
            if(storeXlsx) {
                xlsxSuccessful = storeXlsx();
            }
            if(xlsxSuccessful && !keepCsv) {
                deleteCsv();
            }
        }
    }

    protected boolean storeXlsx() {
        try {
            trace("[{}] store xlsx", getBaseName());
            storeXlsx0();
            return true;
        } catch (Throwable t) {
            error("store xlsx failed", t);
            return false;
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
            storeXlsxWithTime_Streaming(xlsxPath, FORMATTER, xlsxData);
            trace("stored '{}': {}", xlsxPath, Files.exists(xlsxPath));
        } else {
            info("file '{}' not found", csvPath);
        }
    }

    protected Map<String, JsonTableData3> toXlsxData(JsonTableData3 csvData) {
        Map<String, JsonTableData3> xlsxSheetData = new HashMap<>();

        JsonTableData3 xlsxData = csvData.copy();
        int from = isPrintHeader() ? 1 : 0;

        if(loggingMode == LoggingMode.OPINION || loggingMode == LoggingMode.OPINION_UNCERTAINTY) {
            xlsxData.mapStringColumnToDouble(loggingMode.map(NEW_XJ_INDEX), from, Double::parseDouble);
            xlsxData.mapStringColumnToDouble(loggingMode.map(NEW_XI_INDEX), from, Double::parseDouble);
        }

        if(loggingMode == LoggingMode.UNCERTAINTY || loggingMode == LoggingMode.OPINION_UNCERTAINTY) {
            xlsxData.mapStringColumnToDouble(loggingMode.map(NEW_UJ_INDEX), from, Double::parseDouble);
            xlsxData.mapStringColumnToDouble(loggingMode.map(NEW_UI_INDEX), from, Double::parseDouble);
        }

        xlsxData.mapStringColumnToDouble(loggingMode.map(XJ_INDEX), from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(loggingMode.map(UJ_INDEX), from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(loggingMode.map(XI_INDEX), from, Double::parseDouble);
        xlsxData.mapStringColumnToDouble(loggingMode.map(UI_INDEX), from, Double::parseDouble);
        xlsxData.mapStringColumnToBoolean(loggingMode.map(CHANGED_INDEX), from, Boolean::parseBoolean);

        xlsxSheetData.put(getLocalizedString("sheetName"), xlsxData);

        return xlsxSheetData;
    }

    protected void deleteCsv() {
        Path csvPath = dir.resolve(baseName + ".csv");
        try {
            trace("[{}] delete '{}': {}", getName(), csvPath, Files.deleteIfExists(csvPath));
        } catch (Throwable t) {
            error("deleting '" + csvPath + "' failed", t);
        }
    }
}
