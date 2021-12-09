package de.unileipzig.irpact.core.process2.modular;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.csv.CsvParser;
import de.unileipzig.irpact.commons.util.io3.xlsx.DefaultXlsxSheetWriter3;
import de.unileipzig.irpact.commons.util.io3.xlsx.StreamingXlsxSheetWriter3;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetWriter3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface HelperAPI2 extends Nameable, LoggingHelper {

    Object REEVALUATOR_CALL = new Object();

    //=========================
    //general
    //=========================

    SharedModuleData getSharedData();

    default void traceReevaluatorInitalization() {
        trace1("[{}] initalize reevaluator", getName());
    }

    default void traceModuleInitalization() {
        trace1("[{}] initalize module", getName());
    }

    default void traceModuleSetup() {
        trace1("[{}] setup module", getName());
    }

    default void traceModuleValidation() {
        trace1("[{}] validate module", getName());
    }

    default void traceSetSharedData() {
        trace3("[{}] set shared data ({})", getName(), printThisClass());
    }

    default void traceNewInput(Object input) {
        trace3("[{}] initalize new input '{}' ({})", getName(), printInputInfo(input), printThisClass());
    }

    default void traceModuleCall(Object input) {
        trace3("[{}]@[{}] call module ({})", getName(), printInputInfo(input), printThisClass());
    }

    default void traceModuleCall(Object input, String msg) {
        trace3("[{}]@[{}] call module ({}): {}", getName(), printInputInfo(input), printThisClass(), msg);
    }

    default void traceReevaluatorInfo(Object input) {
        trace3("[{}]@[{}] call reevaluator ({})", getName(), printInputInfo(input), printThisClass());
    }

    default void checkAndWarnNaN(Object input, double value, String msg) {
        if(Double.isNaN(value)) {
            warn("[{}]@[{}] ({}) NaN detected (msg: '{}') ", getName(), printInputInfo(input), printThisClass(), msg);
        }
    }

    default void checkAndWarnNaN(Object input, double value, Module2<?, ?> submodule, String msg) {
        if(Double.isNaN(value)) {
            warn("[{}]@[{}] ({}) NaN detected (submodule: '{}'@'{}): '{}'", getName(), printInputInfo(input), printThisClass(), submodule.getName(), submodule.getClass().getSimpleName(), msg);
        }
    }

    default String printThisClass() {
        return getClass().getSimpleName();
    }

    default String printInputInfo(Object obj) {
        if(obj instanceof ConsumerAgentData2) {
            return ((ConsumerAgentData2) obj).getAgentName();
        }

        if(obj instanceof Nameable) {
            return ((Nameable) obj).getName();
        }

        return Objects.toString(obj);
    }

    default void startReevaluatorCall() {
        getSharedData().put(REEVALUATOR_CALL, Boolean.TRUE);
    }

    default void finishReevaluatorCall() {
        getSharedData().remove(REEVALUATOR_CALL);
    }

    default boolean isReevaluatorCall() {
        return getSharedData().contains(REEVALUATOR_CALL);
    }

    //=========================
    //Attribute
    //=========================

    default long getLong(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName) {
        return environment.getAttributeHelper().getLong(agent, product, attributeName, true);
    }

    default double getDouble(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName) {
        return environment.getAttributeHelper().getDouble(agent, product, attributeName, true);
    }

    default void setDouble(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName, double value) {
        environment.getAttributeHelper().setDouble(agent, product, attributeName, value, true);
    }

    default boolean getBoolean(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName) {
        return environment.getAttributeHelper().getBoolean(agent, product, attributeName, true);
    }

    default void setBoolean(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName, boolean value) {
        environment.getAttributeHelper().setBoolean(agent, product, attributeName, value, true);
    }

    //=========================
    //File
    //=========================

    default JsonTableData3 loadCsv(Path path, String delimiter) throws IOException {
        CsvParser<JsonNode> parser = new CsvParser<>();
        parser.setValueGetter(CsvParser.forJson());
        parser.setDelimiter(delimiter);
        return new JsonTableData3(parser.parseToList(path, StandardCharsets.UTF_8));
    }

    default void storeXlsx(Path path, Map<String, JsonTableData3> sheetData) throws IOException {
        DefaultXlsxSheetWriter3<JsonNode> writer = new DefaultXlsxSheetWriter3<>();
        writer.setCellHandler(DefaultXlsxSheetWriter3.forJson());
        writer.write(path, sheetData);
    }

    default void storeXlsxWithTime(
            Path path,
            DateTimeFormatter formatter,
            Map<String, JsonTableData3> sheetData) throws IOException {
        storeXlsxWithTime(path, formatter, sheetData, new DefaultXlsxSheetWriter3<>());
    }

    default void storeXlsxWithTime_Streaming(
            Path path,
            DateTimeFormatter formatter,
            Map<String, JsonTableData3> sheetData) throws IOException {
        StreamingXlsxSheetWriter3<JsonNode> writer = new StreamingXlsxSheetWriter3<>();
        writer.setSheetInitalizer(sheet -> {
            sheet.setRandomAccessWindowSize(100);
            return sheet;
        });
        storeXlsxWithTime(path, formatter, sheetData, writer);
    }

    default void storeXlsxWithTime(
            Path path,
            DateTimeFormatter formatter,
            Map<String, JsonTableData3> sheetData,
            XlsxSheetWriter3<JsonNode> writer) throws IOException {
        Workbook book = writer.newBook();
        CellStyle dateStyle = XlsxSheetWriter3.createDefaultDateStyle(book);

        writer.setCellHandler(
                XlsxSheetWriter3.forJson(
                        XlsxSheetWriter3.testTime(formatter),
                        XlsxSheetWriter3.toTime(formatter),
                        XlsxSheetWriter3.toCellStyle(dateStyle)
                )
        );

        writer.write(
                path,
                book,
                sheetData
        );
    }
}
