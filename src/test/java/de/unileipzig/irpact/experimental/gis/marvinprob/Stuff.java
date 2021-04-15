package de.unileipzig.irpact.experimental.gis.marvinprob;

import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Disabled
class Stuff {

    private static final Path dir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");

    private static void setCharset(FileDataStore fds, Charset charset) {
        ((ShapefileDataStore) fds).setCharset(charset);
    }

    private static String getExtension(String name) {
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(dot + 1);
    }

    private static String removeExtension(String name) {
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(0, dot);
    }

    private static Set<String> collectNames(Path dir) throws IOException {
        Set<String> set = new TreeSet<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path p: stream) {
                String fileName = p.getFileName().toString();
                if(fileName.endsWith(".csv")) {
                    continue;
                }
                String name = removeExtension(fileName);
                set.add(name);
            }
        }
        return set;
    }

    @Test
    void asd() throws IOException {
        Map<String, Integer> map = new TreeMap<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path p: stream) {
                String fileName = p.getFileName().toString();
                String extension = getExtension(fileName);
                int current = map.computeIfAbsent(extension, _e -> 0);
                map.put(extension, current + 1);
            }
        }
        map.forEach((k, c) -> System.out.println(k + ": " + c));
        int total = map.values().stream().mapToInt(i -> i).sum();
        System.out.println("total:" + total);
        System.out.println("names: " + collectNames(dir).size());
    }

    @Test
    void testMiniShp() throws IOException {
        Path path = dir.resolve("3D_LoD2_33278_5602_2_sn.shp");
        assertTrue(Files.exists(path), "> " + path);
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(path.toFile());
        System.out.println(Arrays.toString(store.getTypeNames()));

        SimpleFeatureType type = store.getSchema("3D_LoD2_33278_5602_2_sn");
        Query query = new Query("3D_LoD2_33278_5602_2_sn");

        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            String name = descriptor.getName().toString();
            System.out.println(name);
        }

//        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
//            while(reader.hasNext()) {
//                SimpleFeature feature = reader.next();
//                for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
//                    String name = descriptor.getName().toString();
//
//                }
//            }
//        } finally {
//            store.dispose(); //!!! sonst lock error
//            //https://stackoverflow.com/questions/19882341/why-this-code-how-read-a-shapefile-using-geotools-throws-this-exception
//        }
    }

    @Test
    void testShpCustom() throws IOException {
        Path path = dir.resolve("3D_LoD2_33278_5602_2_sn.shp");
        byte[] data = Files.readAllBytes(path);
        byte[] b = {data[32], data[33], data[34], data[35]};
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(bb.getInt());
    }
}
