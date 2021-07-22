package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.locale.LocalizedData;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.FileExtension;
import de.unileipzig.irpact.core.postprocessing.data.LocalizedResultDataYaml;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Daniel Abitz
 */
public final class WriterLocalizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WriterLocalizer.class);

    public static final String CSV = FileExtension.CSV.getText();
    public static final String CHARSET = LocalizedResultDataYaml.CHARSET;
    public static final String DELIMITER = LocalizedResultDataYaml.DELIMITER;
    public static final String COMMENT_PREFIX = LocalizedResultDataYaml.COMMENT_PREFIX;

    public static final String XLSX = FileExtension.XLSX.getText();
    public static final String SHEETNAME = LocalizedResultDataYaml.SHEETNAME;

    public static final String INFOS = LocalizedResultDataYaml.INFOS;
    public static final String COLUMNS = LocalizedResultDataYaml.COLUMNS;

    protected WriterLocalizer() {
    }

    protected static Charset getCharset(LocalizedData data, String[] path) {
        try {
            String charsetName = data.getString(path);
            return Charset.forName(charsetName);
        } catch (Exception e) {
            LOGGER.error("invalid charset, using UTF-8", e);
            return StandardCharsets.UTF_8;
        }
    }

    //=========================
    //xlsx
    //=========================

    protected static String[] buildXlsxPath(String type, String target) {
        return new String[] {XLSX, type, target};
    }

    public static void localizeXlsx(LocalizedData data, XlsxVarCollectionWriter writer, String type) {
        writer.setSheetName(data.getString(buildXlsxPath(type, SHEETNAME)));
        applyInfos(data, writer, type);
        writer.setColumns(data.getStringArray(buildXlsxPath(type, COLUMNS)));
    }

    protected static void applyInfos(LocalizedData data, XlsxVarCollectionWriter writer, String type) {
        String[] infos = data.getStringArray(buildXlsxPath(type, INFOS));
        if(infos.length > 0) {
            infos[0] = StringUtil.format(infos[0], TimeUtil.printNowWithoutMs());
        }
        writer.setInfos(infos);
    }

    //=========================
    //csv
    //=========================

    protected static String[] buildCsvPath(String type, String target) {
        return new String[] {CSV, type, target};
    }

    public static void localizeCsv(LocalizedData data, CsvVarCollectionWriter writer, String type) {
        writer.setCharset(getCharset(data, buildCsvPath(type, CHARSET)));
        writer.setDelimiter(data.getString(buildCsvPath(type, DELIMITER)));
        applyInfos(data, writer, type);
        writer.setColumns(data.getStringArray(buildXlsxPath(type, COLUMNS)));
        writer.setCommentPrefix(data.getString(buildCsvPath(type, COMMENT_PREFIX)));
    }

    protected static void applyInfos(LocalizedData data, CsvVarCollectionWriter writer, String type) {
        String[] infos = data.getStringArray(buildXlsxPath(type, INFOS));
        if(infos.length > 0) {
            infos[0] = StringUtil.format(infos[0], TimeUtil.printNowWithoutMs());
        }
        writer.setInfos(infos);
    }


}
