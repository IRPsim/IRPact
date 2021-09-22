package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public final class DataToVisualizeHandler {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DataToVisualizeHandler.class);

    private DataToVisualizeHandler() {
    }

    public static void handleImage(ImageProcessor processor, InOutputImage image) throws IOException, ParsingException {
        SupportedEngine engine = image.getEngine();
        if(engine == null) {
            throw new NoSuchElementException("missing engine");
        }

        switch (engine) {
            case GNUPLOT:
                handleGnuPlotImage(processor, image);
                break;

            case R:
                handleRscriptImage(processor, image);
                break;
        }
    }

    public static void handleGnuPlotImage(ImageProcessor processor, InOutputImage image) throws IOException, ParsingException {
        DataToVisualize data = image.getMode();
        if(data == null) {
            throw new NullPointerException("missing data to visualize");
        }

        switch (data) {
            case ANNUAL_ZIP:
                AnnualZipWithGnuPlot az = new AnnualZipWithGnuPlot(processor);
                az.handleImage(image);
                break;

            case COMPARED_ANNUAL_ZIP:
                ComparedAnnualZipWithGnuPlotVersionLess528 caz = new ComparedAnnualZipWithGnuPlotVersionLess528(processor);
                caz.handleImage(image);
                break;

            case CUMULATIVE_ANNUAL_PHASE:
                CumulativeAnnualPhaseWithGnuPlot cap = new CumulativeAnnualPhaseWithGnuPlot(processor);
                cap.handleImage(image);
                break;

            case CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL:
                CumulativeAnnualPhaseWithInitialAdopterWithGnuPlot cap2 = new CumulativeAnnualPhaseWithInitialAdopterWithGnuPlot(processor);
                cap2.handleImage(image);
                break;

            case ANNUAL_INTEREST_2D:
                if(processor.getEnvironment().getPostAnalysisData().isLogAnnualInterest()) {
                    AnnualInterestWithGnuPlot2D ai2d = new AnnualInterestWithGnuPlot2D(processor);
                    ai2d.handleImage(image);
                } else {
                    LOGGER.info("annual interest disabled");
                }
                break;

            case ANNUAL_PHASE_OVERVIEW:
                if(processor.getEnvironment().getPostAnalysisData().isLogPhaseTransition()) {
                    AnnualPhaseOverviewWithGnuPlot apo = new AnnualPhaseOverviewWithGnuPlot(processor);
                    apo.handleImage(image);
                } else {
                    LOGGER.info("annual phase overview disabled");
                }
                break;

            default:
                throw new IllegalArgumentException("unsupported data to visualize: " + data);
        }
    }

    public static void handleRscriptImage(ImageProcessor processor, InOutputImage image) throws IOException, ParsingException {
        DataToVisualize data = image.getMode();
        if(data == null) {
            throw new NullPointerException("missing data to visualize");
        }


        switch (data) {
            case ANNUAL_ZIP:
                AnnualZipWithRscript az = new AnnualZipWithRscript(processor);
                az.handleImage(image);
                break;

            case COMPARED_ANNUAL_ZIP:
                ComparedAnnualZipWithRscript caz = new ComparedAnnualZipWithRscript(processor);
                caz.handleImage(image);
                break;

            case CUMULATIVE_ANNUAL_PHASE:
                CumulativeAnnualPhaseWithRscript cap = new CumulativeAnnualPhaseWithRscript(processor);
                cap.handleImage(image);
                break;

            default:
                throw new IllegalArgumentException("unsupported data to visualize: " + data);
        }
    }
}
