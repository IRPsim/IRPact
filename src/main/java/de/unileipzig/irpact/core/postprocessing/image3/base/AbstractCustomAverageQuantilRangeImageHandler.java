package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
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
