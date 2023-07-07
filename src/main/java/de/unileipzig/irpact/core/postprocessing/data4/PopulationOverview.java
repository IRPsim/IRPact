package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class PopulationOverview extends AbstractGeneralDataHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PopulationOverview.class);

    public PopulationOverview(DataProcessor4 processor, String baseName) {
        super(processor, baseName);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected String getResourceKey() {
        return "POPULATION_OVERVIEW";
    }

    @Override
    public void init() throws Throwable {
    }

    @Override
    public void execute() throws Throwable {
        JsonTableData3 data = createTableData();
        Path xlsxPath = getTargetFile(FileType.XLSX);
        processor.storeXlsx(
                xlsxPath,
                getLocalizedXlsxString("sheet"),
                data
        );
    }

    @SuppressWarnings("UnusedAssignment")
    protected JsonTableData3 createTableData() {
        JsonTableData3 data = new JsonTableData3();

        int columnIndex = 0;
        int rowIndex = 0;
        //header
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("agent"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("id"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("milieu"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("adress"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("zip"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("pp"));

        //data
        for(ConsumerAgent agent: getEnvironment().getAgents().iterableConsumerAgents()) {
            rowIndex++;
            columnIndex = 0;
            data.setString(rowIndex, columnIndex++, getName(agent));
            data.setLong(rowIndex, columnIndex++, getID(agent));
            data.setString(rowIndex, columnIndex++, getMilieu(agent));
            data.setString(rowIndex, columnIndex++, getAdress(agent));
            data.setString(rowIndex, columnIndex++, getZIP(agent));
            data.setDouble(rowIndex, columnIndex++, getPP(agent));
        }

        return data;
    }

    protected long getID(ConsumerAgent agent) {
        return agent.findAttribute(RAConstants.ID).asValueAttribute().getLongValue();
    }

    protected String getName(ConsumerAgent agent) {
        return agent.getName();
    }

    protected String getMilieu(ConsumerAgent agent) {
        return agent.getGroup().getName();
    }

    protected String getAdress(ConsumerAgent agent) {
        return agent.findAttribute(RAConstants.ADDRESS).asValueAttribute().getStringValue();
    }

    protected String getZIP(ConsumerAgent agent) {
        return agent.findAttribute(RAConstants.ZIP).asValueAttribute().getStringValue();
    }

    protected double getPP(ConsumerAgent agent) {
        return agent.findAttribute(RAConstants.PURCHASE_POWER_EUR).asValueAttribute().getDoubleValue();
    }
}
