package de.unileipzig.irpact.core.process2.modular;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.csv.CsvParser;
import de.unileipzig.irpact.commons.util.io3.xlsx.XlsxSheetWriter3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        trace3("[{}] set shared data ({})", getName(), getClass().getSimpleName());
    }

    default void traceNewInput(Object input) {
        trace3("[{}] initalize new input '{}' ({})", getName(), printInputInfo(input), getClass().getSimpleName());
    }

    default void traceModuleCall(Object input) {
        trace3("[{}]@[{}] call module ({})", getName(), printInputInfo(input), getClass().getSimpleName());
    }

    default void traceReevaluatorInfo(Object input) {
        trace3("[{}]@[{}] call reevaluator ({})", getName(), printInputInfo(input), getClass().getSimpleName());
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
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        writer.setCellHandler(XlsxSheetWriter3.forJson());
        writer.write(path, sheetData);
    }

    default void storeXlsxWithTime(
            Path path,
            DateTimeFormatter formatter,
            Map<String, JsonTableData3> sheetData) throws IOException {
        XlsxSheetWriter3<JsonNode> writer = new XlsxSheetWriter3<>();
        XSSFWorkbook book = writer.newBook();
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
