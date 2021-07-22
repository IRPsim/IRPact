package de.unileipzig.irpact.core.postprocessing.data;

import de.unileipzig.irpact.commons.locale.LocalizedData;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.XlsxVarCollectionWriter;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AllAdoptions2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class DataProcessor extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DataProcessor.class);

    protected static final String ALL_ADOPTIONS_XLSX = "Alle_Adoptionen.xlsx";

    public DataProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        super(metaData, clOptions, inRoot, environment);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void execute() {
        try {
            execute0();
        } catch (Throwable t) {
            error("error while executing DataProcessor", t);
        } finally {
            cleanUp();
        }
    }

    protected void execute0() {
        trace("isLogResultAdoptionsZip: {}", getSettings().isLogResultAdoptionsZip());

        trace("isLogResultAdoptionsZipPhase: {}", getSettings().isLogResultAdoptionsZipPhase());

        trace("isLogResultAdoptionsAll: {}", getSettings().isLogResultAdoptionsAll());
        if(getSettings().isLogResultAdoptionsAll()) {
            logAllAdoptionsXlsx();
        }
    }

    protected void cleanUp() {
    }

    //=========================
    //out
    //=========================

    protected void logAllAdoptionsXlsx() {
        try {
            logAllAdoptionsXlsx0();
        } catch (Throwable t) {
            error("error while running 'logAllAdoptionsXlsx'", t);
        }
    }

    protected void logAllAdoptionsXlsx0() throws IOException {
        AllAdoptions2 analyser = new AllAdoptions2();
        analyser.apply(environment);
        analyser.setLocalizedData(getLocalizedData());
        analyser.setYears(getAllSimulationYears());

        XlsxVarCollectionWriter writer = new XlsxVarCollectionWriter();
        writer.setTarget(getTargetDir().resolve(ALL_ADOPTIONS_XLSX));
        analyser.writeXlsx(writer);
    }

    //=========================
    //util
    //=========================

    protected LocalizedData getLocalizedData() {
        return Dev.throwException();
    }
}
