package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Daniel Abitz
 */
public class AdoptionOverview extends AbstractGeneralDataHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionOverview.class);

    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public AdoptionOverview(DataProcessor4 processor, String baseName) {
        super(processor, baseName);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected String getResourceKey() {
        return "ADOPTION_OVERVIEW";
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
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("utility"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("product"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("time"));
        data.setString(rowIndex, columnIndex++, getLocalizedXlsxString("phase"));

        //data
        for(ConsumerAgent agent: getEnvironment().getAgents().iterableConsumerAgents()) {
            for(AdoptedProduct product: agent.getAdoptedProducts()) {
                rowIndex++;
                columnIndex = 0;
                data.setString(rowIndex, columnIndex++, getName(agent));
                data.setLong(rowIndex, columnIndex++, getID(agent));
                data.setString(rowIndex, columnIndex++, getMilieu(agent));
                data.setString(rowIndex, columnIndex++, getAdress(agent));
                data.setString(rowIndex, columnIndex++, getZIP(agent));
                data.setDouble(rowIndex, columnIndex++, getPP(agent));
                data.setDouble(rowIndex, columnIndex++, getUtility(product));
                data.setString(rowIndex, columnIndex++, getProduct(product));
                data.setString(rowIndex, columnIndex++, getTime(product));
                data.setString(rowIndex, columnIndex++, getAdoptionPhase(product));
            }
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

    protected double getUtility(AdoptedProduct product) {
        return product.getUtility();
    }

    protected String getProduct(AdoptedProduct product) {
        return product.getProduct().getName();
    }

    protected String getTime(AdoptedProduct product) {
        LocalDateTime ldt;

        if(product.hasTimestamp()) {
            ldt = product.getTimestamp().getTime().toLocalDateTime();
        } else {
            if(product.isInitial()) {
                int firstYear = getEnvironment().getTimeModel().getFirstSimulationYear();
                ldt = TimeUtil.lastDayOfYear(firstYear - 1).toLocalDateTime();
            } else {
                throw new IllegalArgumentException("missing time");
            }
        }

        return ldt.format(FORMATTER);
    }

    protected String getAdoptionPhase(AdoptedProduct product) {
        return product.getPhase().name();
    }
}
