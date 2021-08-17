package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.twodim.Point2D;

/**
 * @author Daniel Abitz
 */
public class PrintAll implements SpatialInformationPrinter {

    public static final PrintAll INSTANCE = new PrintAll();

    @Override
    public String print(SpatialInformation information) {
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(SpatialUtil.tryGetId(information));
        if(information instanceof Point2D) {
            Point2D p2d = (Point2D) information;
            sb.append(",x:").append(p2d.getX());
            sb.append(",y:").append(p2d.getY());
        }
        sb.append(",[");
        if(information.getAttributes().size() > 0) {
            for(SpatialAttribute attr: information.getAttributes()) {
                sb.append(attr.getName()).append("=").append(attr.asValueAttribute().getValueAsString());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("]");
        return sb.toString();
    }
}
