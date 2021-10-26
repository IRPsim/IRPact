package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InCsvValueLoggingModule_calcloggraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InMinimalCsvValueLoggingModule_calcloggraphnode2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCustomAverageQuantilRangeImage;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCustomAverageQuantilRangeImageHandler
        extends AbstractQuantilRangeImageHandler<InCustomAverageQuantilRangeImage>
        implements LoggingHelper {

    public AbstractCustomAverageQuantilRangeImageHandler(
            ImageProcessor2 processor,
            InCustomAverageQuantilRangeImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    protected void validate() throws Throwable {
        InConsumerAgentCalculationLoggingModule2 module = imageConfiguration.getLoggingModule();
        if(!(module instanceof InCsvValueLoggingModule_calcloggraphnode2
                || module instanceof InMinimalCsvValueLoggingModule_calcloggraphnode2)) {
            throw new IllegalArgumentException("unsupported module type for '" + module.getName() + "': " + module.getClass());
        }
    }

    public static void updateNames(
            JsonTableData3 data,
            String years) {
        data.setString(0, 0, years);
    }

    public static void updateNames(
            JsonTableData3 data,
            String years,
            String avg) {
        data.setString(0, 0, years);
        data.setString(0, 1, avg);
    }
}
