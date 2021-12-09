package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.AnnualEnumeratedAdoptionPhases;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAdoptionPhaseOverviewImage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAdoptionPhaseOverviewImageHandler
        extends AbstractQuantilRangeImageHandler<InAdoptionPhaseOverviewImage>
        implements LoggingHelper {

    public AbstractAdoptionPhaseOverviewImageHandler(
            ImageProcessor2 processor,
            InAdoptionPhaseOverviewImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    protected AnnualEnumeratedAdoptionPhases getAdoptionData() {
        if(getGlobalData().contains(AnnualEnumeratedAdoptionPhases.class)) {
            return getGlobalData().getAuto(AnnualEnumeratedAdoptionPhases.class);
        } else {
            AnnualEnumeratedAdoptionPhases data = new AnnualEnumeratedAdoptionPhases();
            data.initalize(
                    processor.getAllSimulationYears(),
                    processor.getAllProducts(),
                    AdoptionPhase.VALID_PHASES
            );
            data.analyse(getEnvironment());
            getGlobalData().put(AnnualEnumeratedAdoptionPhases.class, data);
            return data;
        }
    }

    protected AnnualEnumeratedAdoptionPhases getCumulatedAdoptionData() {
        AnnualEnumeratedAdoptionPhases data = getAdoptionData();
        return (AnnualEnumeratedAdoptionPhases) data.cumulate();
    }

    protected ColorPalette getPaletteOrNull() {
        try {
            return imageConfiguration.hasColorPalette()
                    ? imageConfiguration.getColorPalette().toPalette()
                    : null;
        } catch (ParsingException e) {
            error("color platte not usable, use default colors", e);
            return null;
        }
    }

    protected List<String> getHexRGBPaletteOrNull() {
        ColorPalette palette = getPaletteOrNull();
        return palette == null
                ? null
                : palette.stream()
                .map(ColorPalette::printRGBHex)
                .collect(Collectors.toList());
    }

    protected String getNameForPhase(AdoptionPhase phase) {
        switch (phase) {
            case INITIAL:
                return getLocalizedString("initial");
            case START_MID:
                return getLocalizedString("startMid");
            case MID_END:
                return getLocalizedString("midEnd");
            case END_START:
                return getLocalizedString("endStart");

            default:
                throw new IllegalArgumentException("unsupported phase: " + phase);
        }
    }

    protected int getInitialAdopterCount() {
        Product product = processor.getSingletonProduct();
        AnnualEnumeratedAdoptionPhases cumulatedData = getCumulatedAdoptionData();
        return cumulatedData.getInitialCount(product, AdoptionPhase.INITIAL);
    }

    protected int getTotalAdopterCount() {
        Product product = processor.getSingletonProduct();
        AnnualEnumeratedAdoptionPhases data = getAdoptionData();
        return data.sumAll(product);
    }

    protected JsonTableData3 getTableData() {
        Product product = processor.getSingletonProduct();
        List<Integer> years = processor.getAllSimulationYears();
        AnnualEnumeratedAdoptionPhases cumulatedData = getCumulatedAdoptionData();

        JsonTableData3 tableData = new JsonTableData3();
        int rowIndex = 0;
        //header
        tableData.setString(rowIndex, 0, "years");
        tableData.setString(rowIndex, 1, getNameForPhase(AdoptionPhase.INITIAL));
        tableData.setString(rowIndex, 2, getNameForPhase(AdoptionPhase.START_MID));
        tableData.setString(rowIndex, 3, getNameForPhase(AdoptionPhase.MID_END));
        tableData.setString(rowIndex, 4, getNameForPhase(AdoptionPhase.END_START));
        //data
        rowIndex++;
        for(int year: years) {
            int initial = cumulatedData.getCount(year, product, AdoptionPhase.INITIAL);
            int startMid = cumulatedData.getCount(year, product, AdoptionPhase.START_MID);
            int midEnd = cumulatedData.getCount(year, product, AdoptionPhase.MID_END);
            int endStart = cumulatedData.getCount(year, product, AdoptionPhase.END_START);

            tableData.setInt(rowIndex, 0, year);
            tableData.setInt(rowIndex, 1, initial);
            tableData.setInt(rowIndex, 2, startMid);
            tableData.setInt(rowIndex, 3, midEnd);
            tableData.setInt(rowIndex, 4, endStart);

            rowIndex++;
        }

        return tableData;
    }
}
