package de.unileipzig.irpact.core.util.img;

import de.unileipzig.irpact.commons.locale.LocalizedData;

/**
 * @author Daniel Abitz
 */
public interface LocalizedImage extends LocalizedData {

    String getSep(int mode);

    String getEncoding(int mode);

    String getTitle(int mode);

    String getXArg(int mode);

    String getYArg(int mode);

    String getGrpArg(int mode);

    String getDistinctArg(int mode);

    String getFillArg(int mode);

    String getXLab(int mode);

    String getYLab(int mode);

    String getGrpLab(int mode);

    String getDistinctLab(int mode);

    String getDistinct0Lab(int mode);

    String getDistinct1Lab(int mode);

    String getFillLab(int mode);
}
