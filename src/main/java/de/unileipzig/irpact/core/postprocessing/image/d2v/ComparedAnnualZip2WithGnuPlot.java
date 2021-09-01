package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.*;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZip2WithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZip2WithGnuPlot.class);

    public ComparedAnnualZip2WithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.COMPARED_ANNUAL_ZIP;
    }

    @Override
    protected void handleImage(InOutputImage image, boolean engineUsable) throws IOException {
        MutableInt numberOfEntries = MutableInt.empty();
        ImageData data = createData(image, numberOfEntries);
        if(data == null) {
            return;
        }

        GnuPlotBuilder builder = getBuilder(image, numberOfEntries);
        if(builder == null) {
            return;
        }

        execute(image, builder, data, engineUsable);
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        throw new UnsupportedOperationException();
    }

    protected GnuPlotBuilder getBuilder(InOutputImage image, MutableInt numberOfEntries) {
        return GnuPlotFactory.interactionLineChartForVersionLess528(
                getSettings(image, numberOfEntries.get())
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        throw new UnsupportedOperationException();
    }

    protected ImageData createData(InOutputImage image, MutableInt numberOfEntries) {
        AnnualAdoptionsZip2 data = createAnnualAdoptionZipData();
        return map(image, data, numberOfEntries);
    }

    protected ImageData map(InOutputImage image, AnnualAdoptionsZip2 input, MutableInt numberOfEntries) {
        RealAdoptionData realData = imageProcessor.getRealAdoptionData(image);

        Set<Integer> years = new TreeSet<>();
        Map<String, Map<Integer, Integer>> zipData = new LinkedHashMap<>();
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 data = AnnualAdoptionsZip2.getData(entry);

            years.add(year);
            zipData.computeIfAbsent(zip, _zip -> new TreeMap<>()).put(year, data.getValue());
        }

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(map("years"));
        numberOfEntries.set(zipData.keySet().size());
        LOGGER.trace(IRPSection.RESULT, "number of entries: {}", numberOfEntries.get());
        for(String zip: zipData.keySet()) {
            header.add(map(zip));
            header.add(map(zip + "-R"));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(map(Integer.toString(year)));
            for(Map.Entry<String, Map<Integer, Integer>> zipEntry: zipData.entrySet()) {
                row.add(map(Integer.toString(zipEntry.getValue().get(year))));
                row.add(map(Integer.toString(realData.get(year, zipEntry.getKey()))));
            }
            csvData.add(row);
        }

        BuilderSettings settings = getSettings(image, numberOfEntries.get());
        return new CsvBasedImageData(settings.getSep(), csvData);
    }

    protected BuilderSettings currentSettings;
    protected InOutputImage currentImage;

    protected BuilderSettings getSettings(InOutputImage image, int numberOfEntries) {
        if(image != currentImage || currentSettings == null) {
            LocalizedImageData localized = getLocalizedImageData();
            currentImage = image;
            currentSettings = new BuilderSettings()
                    //general
                    .setTitle(localized.getTitle(getMode()))
                    .setWidth(image == null ? imageProcessor.getDefaultWidth() : image.getImageWidth())
                    .setHeight(image == null ? imageProcessor.getDefaultHeight() : image.getImageHeight())
                    .setXArg(localized.getXArg(getMode()))
                    .setXLab(localized.getXLab(getMode()))
                    .setYArg(localized.getYArg(getMode()))
                    .setYLab(localized.getYLab(getMode()))
                    .setGrpArg(localized.getGrpArg(getMode()))
                    .setGrpLab(localized.getGrpLab(getMode()))
                    .setDistinctArg(localized.getDistinctArg(getMode()))
                    .setDistinctLab(localized.getDistinctLab(getMode()))
                    .setDistinct0Label(localized.getDistinct0Lab(getMode()))
                    .setDistinct1Label(localized.getDistinct1Lab(getMode()))
                    .setSep(localized.getSep(getMode()))
                    .setLineWidth(image == null ? imageProcessor.getDefaultLinewidth() : image.getLinewidth())
                    .setUseArgsFlag(true)
                    .setUsageFlag(BuilderSettings.USAGE_ARG2)
                    .setCenterTitle(true)
                    //gnuplot
                    .setXYRangeWildCard()
                    .setNumberOfEntries(numberOfEntries)
                    ;
        }
        return currentSettings;
    }
}
