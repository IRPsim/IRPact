package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedConsumerGroups;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualMilieuImage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualMilieuImageHandler
        extends AbstractImageHandler<InAnnualMilieuImage>
        implements LoggingHelper {

    public AbstractAnnualMilieuImageHandler(ImageProcessor2 processor, InAnnualMilieuImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    protected Double getMinYOrDefault(Double ifMissing) {
        return imageConfiguration.isUseCustomYRange()
                ? imageConfiguration.getMinY()
                : ifMissing;
    }

    protected Double getMaxYOrDefault(Double ifMissing) {
        return imageConfiguration.isUseCustomYRange()
                ? imageConfiguration.getMaxY()
                : ifMissing;
    }

    protected String getCsvDelimiter() {
        return getLocalizedString("sep");
    }

    protected AnnualEnumeratedConsumerGroups getAdoptionData() {
        if(getGlobalData().contains(AnnualEnumeratedConsumerGroups.class)) {
            return getGlobalData().getAuto(AnnualEnumeratedConsumerGroups.class);
        } else {
            AnnualEnumeratedConsumerGroups data = new AnnualEnumeratedConsumerGroups();
            data.analyse(getEnvironment());
            getGlobalData().put(AnnualEnumeratedConsumerGroups.class, data);
            return data;
        }
    }

    protected JsonTableData3 createData(
            String yearLabel,
            boolean showPreYear) {
        List<Integer> years = processor.getAllSimulationYears();
        Product product = processor.getSingletonProduct();
        List<ConsumerAgentGroup> groups = new ArrayList<>(getEnvironment().getAgents().getConsumerAgentGroups());
        AnnualEnumeratedConsumerGroups simuData = getAdoptionData();

        JsonTableData3 data = new JsonTableData3();
        int rowIndex = 0;
        int columnIndex = 0;

        //header
        data.setString(rowIndex, columnIndex++, yearLabel);
        for(ConsumerAgentGroup cag: groups) {
            data.setString(rowIndex, columnIndex++, cag.getName());
        }

        //init
        if(showPreYear) {
            rowIndex++;
            columnIndex = 0;
            data.setInt(rowIndex, columnIndex++, processor.getPreFirstSimulationYear());
            for(ConsumerAgentGroup cag: groups) {
                data.setInt(rowIndex, columnIndex++, simuData.getInitialCount(product, cag));
            }
        }

        //rest
        for(int year: years) {
            rowIndex++;
            columnIndex = 0;
            data.setInt(rowIndex, columnIndex++, year);
            for(ConsumerAgentGroup cag: groups) {
                data.setInt(rowIndex, columnIndex++, simuData.getCount(year, product, cag));
            }
        }

        return data;
    }

    public static void findMinMax(JsonTableData3 csvData, MutableDouble min, MutableDouble max) {
        for(int c = 1; c < csvData.getNumberOfColumns(); c++) {
            csvData.getMinMax(c, 1, min, max);
        }
    }
}
