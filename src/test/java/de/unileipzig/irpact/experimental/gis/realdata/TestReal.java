package de.unileipzig.irpact.experimental.gis.realdata;

import de.unileipzig.irpact.develop.TestFiles;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Disabled
class TestReal {

    public static final Path dir = TestFiles.gis.resolve("DatenBig");

    private static int getIndex(DbaseFileHeader header, String name) {
        for(int i = 0; i < header.getNumFields(); i++) {
            String n = header.getFieldName(i);
            if(Objects.equals(n, name)) {
                return i;
            }
        }
        return -1;
    }

    private static int getIndex2(DbaseFileHeader header, String name) {
        int index = getIndex(header, name);
        if(index == -1) {
            throw new IllegalArgumentException(name);
        }
        return index;
    }

    private static String getNumber(Object input) {
        return input == null
                ? ""
                : input.toString().replace('.', ',');

    }

    private static boolean isEmpty(Object data) {
        return data instanceof String && data.toString().isEmpty();
    }

    @Test
    void printDbf() throws IOException {
        Path path = dir.resolve("Stand_19.11..dbf");
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, StandardCharsets.UTF_8)) {
            DbaseFileHeader header = reader.getHeader();
            while(reader.hasNext()) {
                Object[] entry = reader.readEntry();
                for(int i = 0; i < header.getNumFields(); i++) {
                    System.out.println(header.getFieldName(i) + ": " + entry[i]);
                }
                if(true) break;
            }
        }
    }

    @Test
    void printDachDatenFuerEmily() throws IOException {
        Path path = dir.resolve("Stand_19.11..dbf");
        Path out = dir.resolve("test_voll.csv");

        boolean skipEmpty = false;

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            DbaseFileHeader header = reader.getHeader();
            int markId = getIndex2(header, "FS_Gmk");
            int adresseId1 = getIndex2(header, "INSP_Adr");
            int adresseId2 = getIndex2(header, "INSP_PLZ");
            int adresseId3 = getIndex2(header, "INSP_Ortst");
            int orientierungId = getIndex2(header, "LoD2_Dacho");
            int neigungId = getIndex2(header, "LoD2_Dachn");

            int count = header.getNumRecords();
            int i = 1;
            writer.write("Adresse;Postleitzahl;Ortsteil;Dachorientierung;Dachneigung\n");
            while(reader.hasNext()) {
                reader.read(); //!!!
                Object mark = reader.readField(markId);
                Object adresse1 = reader.readField(adresseId1);
                Object adresse2 = reader.readField(adresseId2);
                Object adresse3 = reader.readField(adresseId3);
                Object orientierung = reader.readField(orientierungId);
                Object neigung = reader.readField(neigungId);

                if(!(skipEmpty && (isEmpty(adresse1) || orientierung == null))) {
                    String outtext = "\"" + adresse1 + "\""
                            + ";" + "\"" + adresse2 + "\""
                            + ";" + "\"" + adresse3 + "\""
                            + ";" + getNumber(orientierung)
                            + ";" + getNumber(neigung)
                            + "\n";
                    writer.write(outtext);
                    writer.flush();

                    if(!isEmpty(adresse1) && orientierung == null) {
                        System.out.println(mark + " -> " + outtext);
                    }
                }

                if(i % 100 == 0 || i == 1 || i == count) {
                    System.out.println(i + "/" + count);
                }
                i++;
            }
        }
    }

    @Test
    void printDachDatenFuerEmilyX() throws IOException {
        Path path = dir.resolve("Stand_19.11..dbf");
        Path out = dir.resolve("test_voll.csv");

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, StandardCharsets.UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
            DbaseFileHeader header = reader.getHeader();
            int markId = getIndex2(header, "Mic_Mil_Do");
            int count = header.getNumRecords();
            int i = 1;
            writer.write("Adresse;Postleitzahl;Ortsteil;Dachorientierung;Dachneigung\n");
            while(reader.hasNext()) {
                reader.read(); //!!!
                Object mark = reader.readField(markId);
                System.out.println(mark);

                if(i == 100) {
                    break;
                }
                i++;
            }
        }
    }

    @Test
    void printShp() throws IOException {
        Path path = dir.resolve("Stand_19.11..shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(path.toFile());
        ((ShapefileDataStore) store).setCharset(StandardCharsets.UTF_8);

        SimpleFeatureType type = store.getSchema("Stand_19.11.");
        Query query = new Query("Stand_19.11.");

        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                    Name n = descriptor.getName();
                    String name = n.toString();
                    Object attr = feature.getAttribute(n);
                    if("the_geom".equals(name)) {
                        System.out.println("> " + attr);
                        MultiPolygon mp = (MultiPolygon) attr;
                        System.out.println(">>> " + Arrays.toString(mp.getCoordinates()));
                        for(int i = 0; i < mp.getCoordinates().length; i++) {
                            System.out.println(" !!! " + mp.getCoordinates()[i] + " " + mp.getCoordinates()[i].getClass());
                        }
                        for(int i = 0; i < mp.getNumGeometries(); i++) {
                            System.out.println(" !!! " + mp.getGeometryN(i) + " " + mp.getGeometryN(i).getClass());
                        }
                        System.out.println("center: " + mp.getCentroid());
                        System.out.println("area: " + mp.getArea());
                        continue;
                    }
                    System.out.println(name + ": " + attr);
                }
                if(true) return;
            }
        } finally {
            store.dispose(); //!!! sonst lock error
            //https://stackoverflow.com/questions/19882341/why-this-code-how-read-a-shapefile-using-geotools-throws-this-exception
        }
    }
}
