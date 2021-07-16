package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.locale.LocalizedData;
import de.unileipzig.irpact.commons.time.TimeUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Daniel Abitz
 */
public final class WriterLocalizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WriterLocalizer.class);

    protected static final String CSV = "csv";
    protected static final String CHARSET = "charset";
    protected static final String DELIMITER = "delimiter";
    protected static final String COMMENT_PREFIX = "commentPrefix";


    protected static final String XLSX = "xlsx";
    protected static final String SHEETNAME = "sheetname";

    protected static final String INFOS = "informations";
    protected static final String COLUMNS = "columns";

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
