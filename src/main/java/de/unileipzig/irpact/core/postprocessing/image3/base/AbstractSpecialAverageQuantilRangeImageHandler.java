package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InQuantileRange;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InSpecialAverageQuantilRangeImage;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpecialAverageQuantilRangeImageHandler
        extends AbstractQuantilRangeImageHandler<InSpecialAverageQuantilRangeImage>
        implements LoggingHelper {

    public AbstractSpecialAverageQuantilRangeImageHandler(
            ImageProcessor2 processor,
            InSpecialAverageQuantilRangeImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    protected void validate() throws Throwable {
    }

    public static List<InQuantileRange> getDefaultRanges() {
        return Arrays.asList(
                InQuantileRange.QUANTILE_0_10,
                InQuantileRange.QUANTILE_10_25,
                InQuantileRange.QUANTILE_25_50,
                InQuantileRange.QUANTILE_50_75,
                InQuantileRange.QUANTILE_75_90,
                InQuantileRange.QUANTILE_90_100
        );
    }

    public static void updateNames(
            JsonTableData3 data,
            String years,
            String avg,
            String q0,
            String q1,
            String q2,
            String q3,
            String q4,
            String q5) {
        data.setString(0, 0, years);
        data.setString(0, 1, avg);
        data.setString(0, 2, q0);
        data.setString(0, 3, q1);
        data.setString(0, 4, q2);
        data.setString(0, 5, q3);
        data.setString(0, 6, q4);
        data.setString(0, 7, q5);
    }

    public static void updateNames(
            JsonTableData3 data,
            String years,
            String q0,
            String q1,
            String q2,
            String q3,
            String q4,
            String q5) {
        data.setString(0, 0, years);
        data.setString(0, 1, q0);
        data.setString(0, 2, q1);
        data.setString(0, 3, q2);
        data.setString(0, 4, q3);
        data.setString(0, 5, q4);
        data.setString(0, 6, q5);
    }
}
