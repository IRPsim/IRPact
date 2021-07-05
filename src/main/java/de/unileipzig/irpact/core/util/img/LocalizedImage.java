package de.unileipzig.irpact.core.util.img;

import de.unileipzig.irpact.commons.locale.LocalizedData;

/**
 * @author Daniel Abitz
 */
public interface LocalizedImage extends LocalizedData {

    String getSep(DataToVisualize mode);

    String getEncoding(DataToVisualize mode);

    String getTitle(DataToVisualize mode);

    String getXArg(DataToVisualize mode);

    String getYArg(DataToVisualize mode);

    String getGrpArg(DataToVisualize mode);

    String getDistinctArg(DataToVisualize mode);

    String getFillArg(DataToVisualize mode);

    String getXLab(DataToVisualize mode);

    String getYLab(DataToVisualize mode);

    String getGrpLab(DataToVisualize mode);

    String getDistinctLab(DataToVisualize mode);

    String getDistinct0Lab(DataToVisualize mode);

    String getDistinct1Lab(DataToVisualize mode);

    String getFillLab(DataToVisualize mode);
}
