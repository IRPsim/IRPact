package de.unileipzig.irpact.experimental;

import de.unileipzig.irptools.util.Util;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Daniel Abitz
 */
@Disabled
class GeoStuff {

    //https://docs.geotools.org/latest/userguide/tutorial/datastore/read.html
    @Test
    void asd() throws IOException {
        Path source = TestPaths.tesResources
                .resolve("geo")
                .resolve("cities")
                .resolve("ne_50m_populated_places.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(source.toFile());
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);

        SimpleFeatureType type = store.getSchema("ne_50m_populated_places");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for (AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }

        GeometryDescriptor geomDesc = type.getGeometryDescriptor();
        System.out.println("default geom    name: " + geomDesc.getName());
        System.out.println("default geom    type: " + geomDesc.getType().toString());
        System.out.println("default geom binding: " + geomDesc.getType().getBinding());
        System.out.println("default geom     crs: " + CRS.toSRS(geomDesc.getCoordinateReferenceSystem()));

        Query query = new Query("ne_50m_populated_places");
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("  " + feature.getID() + " " + feature.getAttribute("NAME") + " " + feature.getAttribute("LATITUDE") + " " + feature.getAttribute("LONGITUDE"));
                count++;
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
        }
    }

    @Test
    void dresdenBuildings() throws IOException {
        Path source = TestPaths.tesResources
                .resolve("geo")
                .resolve("dresden")
                .resolve("gis_osm_buildings_a_07_1.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(source.toFile());
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);

        SimpleFeatureType type = store.getSchema("gis_osm_buildings_a_07_1");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for (AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }

        GeometryDescriptor geomDesc = type.getGeometryDescriptor();
        System.out.println("default geom    name: " + geomDesc.getName());
        System.out.println("default geom    type: " + geomDesc.getType().toString());
        System.out.println("default geom binding: " + geomDesc.getType().getBinding());
        System.out.println("default geom     crs: " + CRS.toSRS(geomDesc.getCoordinateReferenceSystem()));

        Query query = new Query("gis_osm_buildings_a_07_1");
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                MultiPolygon theGeom = (MultiPolygon) feature.getAttribute("the_geom");
                System.out.println("  " + feature.getID() + " " + feature.getAttribute("name") + " " + theGeom.getCentroid());
                count++;
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
        }
    }

    @Test
    void testGisStuff() throws IOException {
        Path p = Paths.get("E:\\Temp\\SUSIC_GIS\\QGIS", "buildings_osm_repaired.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(p.toFile());
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);

        SimpleFeatureType type = store.getSchema("buildings_osm_repaired");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }
    }

    @Test
    void testGisStuffDbf() throws IOException {
        //Path p = Paths.get("E:\\Temp\\SUSIC_GIS\\QGIS", "LoD2_fixed.dbf");
        Path p = Paths.get("E:\\Temp\\SUSIC_GIS\\QGIS", "Buildings_merged.dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, StandardCharsets.ISO_8859_1)) {
            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                break;
            }
        }
    }

    @Test
    void testOrtsTeile() throws IOException {
        //Path p = Paths.get("E:\\Temp\\SUSIC_GIS\\QGIS", "LoD2_fixed.dbf");
        Path p = Paths.get("E:\\Temp\\SUSIC_GIS\\QGIS\\Ortsteile_Stadt_Leipzig", "Ortsteile_Stadt_Leipzig.dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, StandardCharsets.UTF_8)) {
            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
            }
        }
    }

    @Test
    void testInspireX() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("x-MacRoman");
        charset = StandardCharsets.ISO_8859_1;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, charset)) {

            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            if(true) return;
            int c = 0;
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                c++;
            }
            System.out.println(c);
        }
    }

    @Test
    void testInspire() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("x-MacRoman");
        charset = StandardCharsets.ISO_8859_1;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.dbf");

        String firstName = null;

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, charset)) {

            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            int c = 0;
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                c++;

                System.out.println(fields[3].getClass());
                firstName = fields[3].toString();
                break;
            }
            System.out.println(c);
        }

        System.out.println(firstName);
        byte[] b = firstName.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(Arrays.toString(b));
        String x = new String(b, StandardCharsets.UTF_8);
        System.out.println(x);
    }
    //-61, -125, -59, -72

    @Test
    void testCharsetStuff() {
        byte[] b = "ß".getBytes(StandardCharsets.UTF_8);
        String s = new String(b, StandardCharsets.ISO_8859_1);
        byte[] b2 = s.getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(b));
        System.out.println(Arrays.toString(b2));

//        byte[] bb = "ÃŸ".getBytes(StandardCharsets.ISO_8859_1);
//        System.out.println(Arrays.toString(bb));
//        System.out.println(Arrays.toString("ÃŸ".getBytes(StandardCharsets.UTF_8)));
//        byte[] x = "ÃŸ".getBytes(StandardCharsets.UTF_8);
//        String xs = new String(x, StandardCharsets.UTF_8);
//        System.out.println((int) xs.charAt(0));
//        System.out.println((int) xs.charAt(1));
//        byte[] x2 = xs.getBytes(StandardCharsets.US_ASCII);
//        System.out.println(Arrays.toString(x2));
//
//        String y3 = new String(new byte[]{(byte) 159}, StandardCharsets.ISO_8859_1);
//        String y2 = new String(new byte[]{-97}, StandardCharsets.ISO_8859_1);
//        String y = new String(new byte[]{-59, -72}, StandardCharsets.UTF_8);
//        System.out.println(y);
//        System.out.println(y2);
//        System.out.println(y3);
//        System.out.println(Arrays.toString(y.getBytes(StandardCharsets.ISO_8859_1)));
    }

    // -61, -125, -59, -72,
    //C5B8

    @Test
    void testInspire2() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("windows-1252");
        charset = StandardCharsets.UTF_8;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\Stadt Leipzig (14713000)", "Stadt Leipzig (14713000).dbf");

        String firstName = null;

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, charset)) {

            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            int c = 0;
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                c++;

//                System.out.println(fields[3].getClass());
//                firstName = fields[3].toString();
                break;
            }
            System.out.println(c);
        }

//        System.out.println(firstName);
//
//        String str = "Friedrich-List-Straße";
//        byte[] b = str.getBytes(StandardCharsets.ISO_8859_1);
//        System.out.println("> " + Arrays.toString(b));
//        System.out.println("> " + Arrays.toString(firstName.getBytes(StandardCharsets.ISO_8859_1)));
//        String str2 = new String(b, StandardCharsets.UTF_8);
//        System.out.println(str2);
    }

    @Test
    void testInspireMarvin() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("windows-1252");
        charset = StandardCharsets.UTF_8;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "sn_ad_25833.dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, charset)) {

            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            int c = 0;
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                c++;
                if(c == 1000) break;
            }
            System.out.println(c);
        }
    }

    @Test
    void testInspireMarvin2() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("windows-1252");
        charset = StandardCharsets.UTF_8;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.dbf");
        p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\Stadt Leipzig (14713000)", "Stadt Leipzig (14713000).dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, charset)) {

            DbaseFileHeader header = reader.getHeader();
            for(int i = 0; i < header.getNumFields(); i++) {
                System.out.println(header.getFieldName(i));
            }

            int c = 0;
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                System.out.println(Arrays.toString(fields));
                c++;

                String x = fields[0].toString();
                if("0033-1442438297".equals(x)) {
                    break;
                }
            }
            System.out.println(c);
        }
    }

    @Test
    void testInspireMarvinXXX() throws IOException {
//        Charset charset = StandardCharsets.ISO_8859_1;
        Charset charset = Charset.forName("windows-1252");
        charset = StandardCharsets.UTF_8;

        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "test.txt");

        String x = Util.readString(p, charset);
        System.out.println(x);

        String str = "ß";
        System.out.println(Arrays.toString(str.getBytes(StandardCharsets.UTF_8)));

        String str2 = "ÃƒÅ¸";
        System.out.println(Arrays.toString(str2.getBytes(StandardCharsets.UTF_8)));

        String str3 = "ÃŸ";
        System.out.println(Arrays.toString(str3.getBytes(StandardCharsets.UTF_8)));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isEquals(Object[] data0, Object[] data1, int i) {
        return Objects.equals(data0[i], data1[i]);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean startsSame(Object[] data0, Object[] data1, int i) {
        String str0 = data0[i].toString();
        String str1 = data1[i].toString();
        if(str0.equals(str1)) return true;
        if(str0.isEmpty() && !str1.isEmpty() || !str0.isEmpty() && str1.isEmpty()) return false;
        char c0 = str0.charAt(0);
        char c1 = str1.charAt(0);
        return c0 == c1;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean isEquals(Object[] data0, Object[] data1) {
        if(!isEquals(data0, data1, 0)) return false;
        if(!isEquals(data0, data1, 1)) return false;
        if(!isEquals(data0, data1, 2)) return false;
        //if(!startsSame(data0, data1, 3)) return false;    //NAME
        if(!isEquals(data0, data1, 4)) return false;
        if(!isEquals(data0, data1, 5)) return false;
        //if(!startsSame(data0, data1, 6)) return false;    //NAME
        if(!isEquals(data0, data1, 7)) return false;
        if(!isEquals(data0, data1, 8)) return false;
        if(!isEquals(data0, data1, 9)) return false;
        if(!isEquals(data0, data1, 10)) return false;
        if(!isEquals(data0, data1, 11)) return false;
        if(!isEquals(data0, data1, 12)) return false;
        return true;
    }
/*
ID_LOCALID
HNR
HNR_Z
STR
PLZ
ORT
ORTST_NAME
LAND
REGBEZ
KREIS
GEMEINDE
ORTSTEIL
STRSCHL
 */
//[0033-1442438088, 6, , Amselhöhe, 04288, Stadt Leipzig, Liebertwolkwitz, 14, 7, 13, 000, 0340, 03504]

    @Test
    void testSameStuff() throws IOException {
        Map<String, Object[]> faultData = new TreeMap<>();
        Map<String, Object[]> correctData = new TreeMap<>();

        Path faultPath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.dbf");
        Path correctPath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\Stadt Leipzig (14713000)", "Stadt Leipzig (14713000).dbf");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(faultPath), false, StandardCharsets.UTF_8)) {
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                String id = fields[0].toString();
                faultData.put(id, fields);
            }
        }

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(correctPath), false, StandardCharsets.UTF_8)) {
            while(reader.hasNext()) {
                Object[] fields = reader.readEntry();
                String id = fields[0].toString();
                correctData.put(id, fields);
            }
        }

        int okCount = 0;
        int faultCount = 0;
        System.out.println("size: " + faultData.size() + " " + correctData.size());
        System.out.println("keys: " + Objects.equals(faultData.keySet(), correctData.keySet()));
        for(String id: faultData.keySet()) {
            Object[] faulty = faultData.get(id);
            Object[] correct = correctData.get(id);
            boolean isEquals = isEquals(faulty, correct);
            if(isEquals) {
                okCount++;
            } else {
                System.out.println(Arrays.toString(faulty));
                System.out.println(Arrays.toString(correct));
                faultCount++;
            }
        }
        System.out.println("ok: " + okCount);
        System.out.println("faulty: " + faultCount);
    }

    @Test
    void testGisStuffFaulty() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(p.toFile());
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);

        SimpleFeatureType type = store.getSchema("Adressen");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }

        double totalLen = 0;
        Query query = new Query("Adressen");
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                MultiPoint asd = (MultiPoint) feature.getAttribute("the_geom");
                totalLen += asd.getLength();
                count++;
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
            System.out.println("totalLen " + totalLen);
        }
    }

    @Test
    void testGisStuffTEST() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\test", "Adressen.shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(p.toFile());
        ((ShapefileDataStore) store).setCharset(StandardCharsets.UTF_8);
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);

        SimpleFeatureType type = store.getSchema("Adressen");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }

        double totalLen = 0;
        Query query = new Query("Adressen");
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                String id = (String) feature.getAttribute("ID_LOCALID");
                String name = (String) feature.getAttribute("STR");
                String ortst = (String) feature.getAttribute("ORTST_NAME");
                System.out.println(id + " " + name + " " + ortst);
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
            System.out.println("totalLen " + totalLen);
        }
    }

    @Test
    void testGisStuffMod2() throws IOException {
        Path namePath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\Stadt Leipzig (14713000)", "Stadt Leipzig (14713000).shp");
        FileDataStore nameStore = FileDataStoreFinder.getDataStore(namePath.toFile());
        Query nameQuery = new Query("Stadt%20Leipzig%20(14713000)");

        Path faultyPath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen.shp");
        FileDataStore faultyStore = FileDataStoreFinder.getDataStore(faultyPath.toFile());
        SimpleFeatureType faultyType = faultyStore.getSchema("Adressen");
        Query faultyQuery = new Query("Adressen");

        Path modPath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\inspire", "Adressen_modified.shp");
        Files.deleteIfExists(modPath);
        FileDataStoreFactorySpi fac = new ShapefileDataStoreFactory();
        FileDataStore modStore = fac.createDataStore(modPath.toUri().toURL());
        modStore.createSchema(faultyType);

        //read correct stuff
        Map<String, String[]> namesMap = new HashMap<>();
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = nameStore.getFeatureReader(nameQuery, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                String id = (String) feature.getAttribute("ID_LOCALID");
                String name = (String) feature.getAttribute("STR");
                String ortst = (String) feature.getAttribute("ORTST_NAME");
                if(namesMap.containsKey(id)) {
                    throw new IllegalArgumentException(id);
                }
                namesMap.put(id, new String[]{name, ortst});
            }
        }

        //faulty
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = faultyStore.getFeatureReader(faultyQuery, Transaction.AUTO_COMMIT);
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = modStore.getFeatureWriter("Adressen_modified", Transaction.AUTO_COMMIT)) {

            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                String id = (String) feature.getAttribute("ID_LOCALID");

                SimpleFeature newFeature = writer.next();
                newFeature.setAttributes(feature.getAttributes());
                String[] names = namesMap.get(id);
                newFeature.setAttribute("STR", names[0]);
                newFeature.setAttribute("ORTST_NAME", names[1]);
            }
        }
    }



    @Test
    void testGisStuffCorrect2() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\experimental\\gis\\Stadt Leipzig (14713000)", "Stadt Leipzig (14713000).shp");

        FileDataStore store = FileDataStoreFinder.getDataStore(p.toFile());
        ((ShapefileDataStore) store).setCharset(StandardCharsets.UTF_8);
        String[] names = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);
        System.out.println(Arrays.toString(store.getTypeNames()));

        SimpleFeatureType type = store.getSchema("Stadt%20Leipzig%20(14713000)");
        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(" (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }

        Query query = new Query("Stadt%20Leipzig%20(14713000)");
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                Object str = feature.getAttribute("ID_LOCALID");
                System.out.println("  " + str);
                count++;
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
        }
    }
}

/*
[0033-1442495811, 38, , AlexanderstraÃŸe, 04109, Stadt Leipzig, Zentrum-West, 14, 7, 13, 000, 0040, 05012]
[0033-1442495814, 41, , AlexanderstraÃŸe, 04109, Stadt Leipzig, Zentrum-West, 14, 7, 13, 000, 0040, 05012]
70979
 */

/*
ID_LOCALID
HNR
HNR_Z
STR
PLZ
ORT
ORTST_NAME
LAND
REGBEZ
KREIS
GEMEINDE
ORTSTEIL
STRSCHL
[0033-1442438088, 6, , Amselhöhe, 04288, Stadt Leipzig, Liebertwolkwitz, 14, 7, 13, 000, 0340, 03504]
1


Process finished with exit code 0

 */

/*
ID_LOCALID
HNR
HNR_Z
STR
PLZ
ORT
ORTST_NAME
LAND
REGBEZ
KREIS
GEMEINDE
ORTSTEIL
STRSCHL
[0033-1442438297, 27, a, Friedrich-List-StraÃŸe, 04319, Stadt Leipzig, Althen-KleinpÃ¶sna, 14, 7, 13, 000, 0290, 02767]
[0033-1442438297, 27, a, Friedrich-List-Straße, 04319, Stadt Leipzig, Althen-Kleinpösna, 14, 7, 13, 000, 0290, 02767]
1


Process finished with exit code 0

 */