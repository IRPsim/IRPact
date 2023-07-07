package de.unileipzig.irpact.core.postprocessing.data4;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionZips;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * @author Daniel Abitz
 */
public class AdoptionAnalysis extends AbstractGeneralDataHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionAnalysis.class);
    private NavigableMap<Integer, Integer> annualDataCumulated = new TreeMap<>();
    private NavigableMap<Integer, Integer> annualDataUncumulated = new TreeMap<>();

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
        AnnualEnumeratedAdoptionData<String> uncumulated = getAdoptionData();

        ObjectNode rootNode = JsonUtil.JSON.createObjectNode();
        ObjectNode cumulatedNode = rootNode.putObject("cumulated");
        cumulatedNode.putNull("total"); //placeholder

        int totalAll = 0;
        for(int year: years) {

            ObjectNode yearNode = cumulatedNode.putObject(Integer.toString(year));
            yearNode.putNull("total"); //placeholder
            int totalYear = 0;
            int totalYearUncumulated = 0;
            for(String zip: zips) {
                int adoptions = data.getCount(year, product, zip);
                int uncumulatedAdoptions = uncumulated.getCount(year, product, zip);

                totalYear += adoptions;
                totalYearUncumulated += uncumulatedAdoptions;
                yearNode.put(zip, adoptions);
            }
            yearNode.put("total", totalYear);
            annualDataCumulated.put(year, totalYear);
            annualDataUncumulated.put(year, totalYearUncumulated);
            totalAll = Math.max(totalAll, totalYear);
        }
        cumulatedNode.put("total", totalAll);

        return rootNode;
    }

    protected ObjectNode getVerboseInformation() {
        ObjectNode rootNode = JsonUtil.JSON.createObjectNode();
        for (Map.Entry<Integer, Integer> entry : annualDataUncumulated.entrySet()) {
            rootNode.put(Integer.toString(entry.getKey()), entry.getValue());
        }
        return rootNode;
    }

    protected ObjectNode getVerboseCumulatedInformation() {
        ObjectNode rootNode = JsonUtil.JSON.createObjectNode();
        for (Map.Entry<Integer, Integer> entry : annualDataCumulated.entrySet()) {
            rootNode.put(Integer.toString(entry.getKey()), entry.getValue());
        }
        return rootNode;
    }

    protected ArrayNode getSimpleInformation() {
        ArrayNode rootNode = JsonUtil.JSON.createArrayNode();
        for (Map.Entry<Integer, Integer> entry : annualDataUncumulated.entrySet()) {
            rootNode.add(entry.getValue());
        }
        return rootNode;
    }

    protected ArrayNode getSimpleCumulatedInformation() {
        ArrayNode rootNode = JsonUtil.JSON.createArrayNode();
        for (Map.Entry<Integer, Integer> entry : annualDataCumulated.entrySet()) {
            rootNode.add(entry.getValue());
        }
        return rootNode;
    }
}
