package de.unileipzig.irpact.util;

import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.csv.CsvValueConverter;
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
    public static final CsvValueConverter<SpatialAttribute> CSV_CONVERTER = (header, columnIndex, value) -> {
        String headerEntry = header.getLabel(columnIndex);
        switch (headerEntry) {
            case RAConstants.ID:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ADDRESS:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.ZIP:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER_STR:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.HOUSE_OWNER:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE_COUNT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SHARE_1_2_HOUSE:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.ORIENTATION:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.SLOPE:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.PURCHASE_POWER_EUR_ADDR:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.DOM_MILIEU:
                return new BasicSpatialStringAttribute(headerEntry, value);

            case RAConstants.AREA:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.X_CENT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            case RAConstants.Y_CENT:
                return new BasicSpatialDoubleAttribute(headerEntry, StringUtil.parseDoubleWithComma(value));

            default:
                throw new IllegalArgumentException("unknown header: " + headerEntry);
        }
    };

    private PVactUtil() {
    }
}
