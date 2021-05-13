package de.unileipzig.irpact.start.utilities;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class Utilities {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Utilities.class);

    protected UtilitiesCommandLineOptions options;

    public Utilities() {
    }

    public Utilities(UtilitiesCommandLineOptions options) {
        setOptions(options);
    }

    public void setOptions(UtilitiesCommandLineOptions options) {
        this.options = options;
    }

    public void run() {
        if(options.isPrintCumulativeAdoptions()) {
            printCumulativeAdoptions();
        }
    }

    //=========================
    //input printing
    //=========================

    /*

        if(CL_OPTIONS.hasInputOutPath()) {
            printInput();
            if(CL_OPTIONS.isNoSimulationAndNoImage()) {
                LOGGER.info(IRPSection.GENERAL, "no simulation");
                return;
            }
        }

    private void printInput() throws IOException {
        LOGGER.trace(IRPSection.GENERAL, "print input file to {} (using {})", CL_OPTIONS.getInputOutPath(), CL_OPTIONS.getInputOutCharset().name());
        AnnualData<InRoot> inData = new AnnualData<>(inEntry);
        AnnualFile aFile = inData.serialize(getInputConverter());
        aFile.store(CL_OPTIONS.getInputOutPath(), CL_OPTIONS.getInputOutCharset());
    }
     */

    //=========================
    //spec <> param
    //=========================

    /*
    private void convertParamToSpec(ObjectNode root) throws Exception {
        LOGGER.trace("convert parameter to specification");
        AnnualEntry<InRoot> entry = IRPact.convert(clOptions, root);
        InRoot inRoot = entry.getData();
        inRoot.general.firstSimulationYear = entry.getConfig().getYear(); //!
        SpecificationConverter converter = new SpecificationConverter();
        SpecificationData data = converter.toSpec(inRoot);
        data.store(clOptions.getSpecOutputDirPath());
    }

    private void convertSpecToParam() throws Exception {
        LOGGER.trace("convert specification to parameter");
        SpecificationConverter converter = new SpecificationConverter();
        InRoot root =  converter.toParam(clOptions.getSpecInputDirPath());
        PerennialData<InRoot> pData = new PerennialData<>();
        pData.add(root.general.firstSimulationYear, root);
        PerennialFile pFile = pData.serialize(IRPact.getInputConverter(clOptions));
        pFile.store(clOptions.getOutputPath(), StandardCharsets.UTF_8);
        LOGGER.trace(IRPSection.SPECIFICATION_CONVERTER, "param file stored: '{}'", clOptions.getOutputPath());
    }
     */

    //=========================
    //print cumulative adoptions
    //=========================

    private void printCumulativeAdoptions() {

    }

    //=========================
    //start
    //=========================

    public static void main(String[] args) {

    }
}
