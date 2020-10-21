package de.unileipzig.irpact.experimental.gis.inspire;

import de.unileipzig.irpact.experimental.TestFiles;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
class TestInspire {

    public static final Path inspire2 = TestFiles.resolveGis("inspire2");

    public static final Path adressen = inspire2.resolve("Adressen");
    public static final Path adressenDbf = adressen.resolve("Adressen.dbf");
    public static final Path adressenShp = adressen.resolve("Adressen.shp");

    public static final Path leipzig = inspire2.resolve("Stadt Leipzig (14713000)");
    public static final Path leipzigDbf = leipzig.resolve("Stadt Leipzig (14713000).dbf");
    public static final Path leipzigShp = leipzig.resolve("Stadt Leipzig (14713000).shp");

    public static final Path adressenMod = inspire2.resolve("Adressen_modified");
    public static final Path adressenModDbf = adressenMod.resolve("Adressen_modified.dbf");

    private static void setCharset(FileDataStore fds, Charset charset) {
        ((ShapefileDataStore) fds).setCharset(charset);
    }

    private static List<Map<String, Object>> parseAll(Path path, Charset charset) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, charset)) {
            DbaseFileHeader header = reader.getHeader();
            while(reader.hasNext()) {
                Map<String, Object> map = new LinkedHashMap<>();
                Object[] entry = reader.readEntry();
                for(int i = 0; i < header.getNumFields(); i++) {
                    map.put(header.getFieldName(i), entry[i]);
                }
                data.add(map);
            }
        }
        return data;
    }

    @Test
    void testRead() throws IOException {
        List<Map<String, Object>> adressenData = parseAll(adressenDbf, StandardCharsets.UTF_8);
        adressenData.stream()
                .sorted(Comparator.comparing(o -> o.get("ID_LOCALID").toString()))
                .limit(10)
                .forEach(System.out::println);
        System.out.println("===");
        List<Map<String, Object>> leipzigData = parseAll(leipzigDbf, StandardCharsets.UTF_8);
        leipzigData.stream()
                .sorted(Comparator.comparing(o -> o.get("ID_LOCALID").toString()))
                .limit(10)
                .forEach(System.out::println);
    }

    private static DbaseFileHeader copyHeader(Path path, Charset charset) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, charset)) {
            return reader.getHeader();
        }
    }

    private static Object[] toRecord(Map<String, Object> entry) {
        Object[] row = new Object[entry.size()];
        int i = 0;
        for(Object value: entry.values()) {
            row[i] = value;
            i++;
        }
        return row;
    }

    private static Map<String, Map<String, Object>> toIdMap(List<Map<String, Object>> list) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        for(Map<String, Object> m: list) {
            String key = m.get("ID_LOCALID").toString();
            map.put(key, m);
        }
        return map;
    }

    private static Map<String, Object> replace(Map<String, Object> faulty, Map<String, Object> ok) {
        Map<String, Object> map = new LinkedHashMap<>(faulty);
        map.put("STR", ok.get("STR"));
        map.put("ORTST_NAME", ok.get("ORTST_NAME"));
        return map;
    }

    @Test
    void combineData_v1() throws IOException {
        Path target = inspire2.resolve("Adressen_v1").resolve("Adressen_v1.dbf");

        List<Map<String, Object>> adressenData = parseAll(adressenDbf, StandardCharsets.UTF_8);
        Map<String, Map<String, Object>> adressenDataMap = toIdMap(adressenData);
        List<Map<String, Object>> leipzigData = parseAll(leipzigDbf, StandardCharsets.UTF_8);
        Map<String, Map<String, Object>> leipzigDataMap = toIdMap(leipzigData);

        DbaseFileHeader header = copyHeader(adressenDbf, StandardCharsets.UTF_8);
        DbaseFileWriter writer = new DbaseFileWriter(header, FileChannel.open(target, StandardOpenOption.CREATE, StandardOpenOption.WRITE), StandardCharsets.UTF_8);
        try {
            for(String id: adressenDataMap.keySet()) {
                Map<String, Object> adressEntry = adressenDataMap.get(id);
                Map<String, Object> leipzigEntry = leipzigDataMap.get(id);
                Map<String, Object> replaced = replace(adressEntry, leipzigEntry);
                writer.write(toRecord(replaced));
            }
        } finally {
            writer.close();
        }
    }

    void testEquals(Map<String, Object> faulty, Map<String, Object> ok, boolean skipStuff) {
        if(skipStuff) {
            Map<String, Object> faultyCopy = new LinkedHashMap<>(faulty);
            faultyCopy.remove("STR");
            faultyCopy.remove("ORTST_NAME");
            Map<String, Object> okCopy = new LinkedHashMap<>(faulty);
            okCopy.remove("STR");
            okCopy.remove("ORTST_NAME");
            assertEquals(faultyCopy, okCopy);
        } else {
            assertEquals(faulty, ok);
        }
    }

    @Test
    void validateNewData() throws IOException {
        Path newDataPath = inspire2.resolve("Adressen_v1").resolve("Adressen_v1.dbf");

        List<Map<String, Object>> adressenData = parseAll(adressenDbf, StandardCharsets.UTF_8); //order
//        Map<String, Map<String, Object>> adressenDataMap = toIdMap(adressenData);
        Map<String, Map<String, Object>> leipzigDataMap = toIdMap(parseAll(leipzigDbf, StandardCharsets.UTF_8)); //content
        List<Map<String, Object>> newData = parseAll(newDataPath, StandardCharsets.UTF_8); //order
        Map<String, Map<String, Object>> newDataMap = toIdMap(newData); //content

        //order
        assertEquals(adressenData.size(), newData.size());
        for(int i = 0; i < adressenData.size(); i++) {
            testEquals(adressenData.get(i), newData.get(i), true);
        }

        //content
        assertEquals(leipzigDataMap.size(), newDataMap.size());
        for(String id: leipzigDataMap.keySet()) {
            testEquals(leipzigDataMap.get(id), newDataMap.get(id), false);
        }
    }

    private static List<Map<String, Object>> parseAllShp(Path path, Charset charset, String x) throws IOException {
        List<Map<String, Object>> data = new ArrayList<>();
        FileDataStore store = FileDataStoreFinder.getDataStore(path.toFile());
        setCharset(store, charset);

        SimpleFeatureType type = store.getSchema(x);
        Query query = new Query(x);
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                Map<String, Object> map = new LinkedHashMap<>();
                for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                    String name = descriptor.getName().toString();
                    if("the_geom".equals(name)) {
                        continue;
                    }
                    map.put(name, feature.getAttribute(descriptor.getName()).toString());
                }
                data.add(map);
            }
        } finally {
            store.dispose(); //!!! sonst lock error
            //https://stackoverflow.com/questions/19882341/why-this-code-how-read-a-shapefile-using-geotools-throws-this-exception
        }
        return data;
    }

    @Test
    void printShp() throws IOException {
        Path newDataPath = inspire2.resolve("Adressen_v1_full").resolve("Adressen.shp");

        List<Map<String, Object>> data = parseAllShp(
                adressenShp,
                StandardCharsets.UTF_8,
                "Adressen"
        );
        data.stream()
                .limit(10)
                .forEach(System.out::println);
    }
}

/*
{ID_LOCALID=0033-1442438297, HNR=27, HNR_Z=a, STR=Friedrich-List-Straße, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02767}
{ID_LOCALID=0033-1442438351, HNR=5, HNR_Z=, STR=Kesselgrund, PLZ=04288, ORT=Stadt Leipzig, ORTST_NAME=Holzhausen, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0350, STRSCHL=03472}
{ID_LOCALID=0033-1442438073, HNR=62, HNR_Z=, STR=Jahnweg, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Engelsdorf, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0270, STRSCHL=02733}
{ID_LOCALID=0033-1442438544, HNR=23, HNR_Z=, STR=Freundschaftsring, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02766}
{ID_LOCALID=0033-1442438545, HNR=21, HNR_Z=, STR=Freundschaftsring, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02766}
{ID_LOCALID=0033-1442438080, HNR=4, HNR_Z=, STR=Schöfflerweg, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Engelsdorf, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0270, STRSCHL=02858}
{ID_LOCALID=0033-1442438079, HNR=64, HNR_Z=, STR=Jahnweg, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Engelsdorf, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0270, STRSCHL=02733}
{ID_LOCALID=0033-1441842866, HNR=5, HNR_Z=a, STR=Althener Anger, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02768}
{ID_LOCALID=0033-1441842867, HNR=6, HNR_Z=a, STR=Althener Anger, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02768}
{ID_LOCALID=0033-1441842872, HNR=9, HNR_Z=, STR=Althener Anger, PLZ=04319, ORT=Stadt Leipzig, ORTST_NAME=Althen-Kleinpösna, LAND=14, REGBEZ=7, KREIS=13, GEMEINDE=000, ORTSTEIL=0290, STRSCHL=02768}
 */