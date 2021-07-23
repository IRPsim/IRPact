package de.unileipzig.irpact.util.pvact;

import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class PVDataFactory {

    private PVDataFactory() {
    }

    public static List<SpatialAttribute> buildRow(
            long id,
            String adress, String zip,
            String houseOwnerStr, boolean houseOwner,
            int anzahlHH,
            double dachOrient, double dachNeig,
            double kkIndex, double kkEuro, double KKEuroAdress,
            String milieu,
            double area, double x, double y) {
        List<SpatialAttribute> row = new ArrayList<>();

        row.add(new BasicSpatialDoubleAttribute(RAConstants.ID, id));

        row.add(new BasicSpatialStringAttribute(RAConstants.ADDRESS, adress));
        row.add(new BasicSpatialStringAttribute(RAConstants.ZIP, zip));

        row.add(new BasicSpatialStringAttribute(RAConstants.HOUSE_OWNER_STR, houseOwnerStr));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.HOUSE_OWNER, houseOwner));

        row.add(new BasicSpatialDoubleAttribute(RAConstants.SHARE_1_2_HOUSE_COUNT, anzahlHH));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.SHARE_1_2_HOUSE, anzahlHH == 1 || anzahlHH == 2));

        row.add(new BasicSpatialDoubleAttribute(RAConstants.ORIENTATION, dachOrient));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.SLOPE, dachNeig));

        row.add(new BasicSpatialDoubleAttribute(RAConstants.PURCHASE_POWER, kkIndex));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.PURCHASE_POWER_EUR, kkEuro));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.PURCHASE_POWER_EUR_ADDR, KKEuroAdress));

        row.add(new BasicSpatialStringAttribute(RAConstants.DOM_MILIEU, milieu));

        row.add(new BasicSpatialDoubleAttribute(RAConstants.AREA, area));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.X_CENT, x));
        row.add(new BasicSpatialDoubleAttribute(RAConstants.Y_CENT, y));

        return row;
    }
}
