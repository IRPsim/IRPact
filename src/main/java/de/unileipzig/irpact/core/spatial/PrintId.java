package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public class PrintId implements SpatialInformationPrinter {

    public static final PrintId INSTANCE = new PrintId();

    @Override
    public String print(SpatialInformation information) {
        return "id:" + SpatialUtil.tryGetId(information);
    }
}
