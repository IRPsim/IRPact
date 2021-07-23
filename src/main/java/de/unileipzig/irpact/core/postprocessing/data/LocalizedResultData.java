package de.unileipzig.irpact.core.postprocessing.data;

import de.unileipzig.irpact.commons.locale.LocalizedData;

/**
 * @author Daniel Abitz
 */
public interface LocalizedResultData extends LocalizedData {

    String getSheetName(FileExtension extension, DataToAnalyse mode);

    String[] getInformations(FileExtension extension, DataToAnalyse mode);

    String[] getColumns(FileExtension extension, DataToAnalyse mode);

    String getCharset(FileExtension extension, DataToAnalyse mode);

    String getDelimiter(FileExtension extension, DataToAnalyse mode);

    String getCommentPrefix(FileExtension extension, DataToAnalyse mode);
}
