package de.unileipzig.irpact.util;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialStringAttribute;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.csv.CsvPartConverter;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PVactUtil {

    public static  final InPVFile pvFile = new InPVFile("Barwertrechner");
    public static  final InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");

    public static final List<String> MILIEUS = CollectionUtil.arrayListOf(
            "PRA", "PER", "SOK", "BUM", "PRE", "EPE", "TRA", "KET", "LIB", "HED", "G"
    );

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static final CsvPartConverter<SpatialAttribute> CSV_CONVERTER = (columnIndex, header, value) -> {
        String headerEntry = header[columnIndex];
        switch (headerEntry) {
            case RAConstants.ID:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ADDRESS:
                return new SpatialStringAttribute(headerEntry, value);

            case RAConstants.ZIP:
                return new SpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER_STR:
                return new SpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE_COUNT:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ORIENTATION:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SLOPE:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR_ADDR:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.DOM_MILIEU:
                return new SpatialStringAttribute(headerEntry, value);

            case RAConstants.AREA:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.X_CENT:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.Y_CENT:
                return new SpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            default:
                throw new IllegalArgumentException("unknown header: " + headerEntry);
        }
    };

    private PVactUtil() {
    }
}
