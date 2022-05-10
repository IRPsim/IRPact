package de.unileipzig.irpact.core.postprocessing.data4;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionZips;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AdoptionAnalysis extends AbstractGeneralDataHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionAnalysis.class);

    public AdoptionAnalysis(DataProcessor4 processor, String baseName) {
        super(processor, baseName);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected String getResourceKey() {
        return "ADOPTION_ANALYSIS";
    }

    @Override
    public void init() throws Throwable {
    }

    @Override
    public void execute() throws Throwable {
        Path path = getTargetFile(FileType.JSON);
        ObjectNode rootNode = createOutput();
        JsonUtil.writeJson(rootNode, path, JsonUtil.DEFAULT);
    }

    protected AnnualEnumeratedAdoptionZips getCumulatedAdoptionData() {
        return (AnnualEnumeratedAdoptionZips) getAdoptionData().cumulate(processor.getAllSimulationYears());
    }

    protected AnnualEnumeratedAdoptionZips getAdoptionData() {
        if(getGlobalData().contains(AnnualEnumeratedAdoptionZips.class)) {
            return getGlobalData().getAuto(AnnualEnumeratedAdoptionZips.class);
        } else {
            AnnualEnumeratedAdoptionZips data = new AnnualEnumeratedAdoptionZips();
            data.analyse(getEnvironment());
            getGlobalData().put(AnnualEnumeratedAdoptionZips.class, data);
            return data;
        }
    }

    protected ObjectNode createOutput() {
        Product product = processor.getUniqueProduct();
        List<Integer> years = processor.getAllSimulationYears();
        List<String> zips = processor.getAllZips(RAConstants.ZIP);
        AnnualEnumeratedAdoptionZips data = getCumulatedAdoptionData();

        ObjectNode rootNode = JsonUtil.JSON.createObjectNode();
        ObjectNode cumulatedNode = rootNode.putObject("cumulated");
        cumulatedNode.putNull("total"); //placeholder

        int totalAll = 0;
        for(int year: years) {

            ObjectNode yearNode = cumulatedNode.putObject(Integer.toString(year));
            yearNode.putNull("total"); //placeholder
            int totalYear = 0;
            for(String zip: zips) {
                int adoptions = data.getCount(year, product, zip);

                totalYear += adoptions;
                yearNode.put(zip, adoptions);
            }
            yearNode.put("total", totalYear);
            totalAll = Math.max(totalAll, totalYear);
        }
        cumulatedNode.put("total", totalAll);

        return rootNode;
    }
}
