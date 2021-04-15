package de.unileipzig.irpact.util.gis;

import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class Gis {

    private static String getSchema(Path target) {
        return CityGjm2Gis.removeExtension(target.getFileName().toString());
    }

    public static void peekDbf(Path target, Charset charset) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(target), false, charset)) {
            DbaseFileHeader header = reader.getHeader();

            if(reader.hasNext()) {
                Object[] entry = reader.readEntry();
                for(int i = 0; i < header.getNumFields(); i++) {
                    System.out.println(header.getFieldName(i) + ": " + entry[i]);
                }
            } else {
                System.out.println("EMPTY");
            }
        }
    }

    public static void peekShp(Path target) throws IOException {
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(target.toFile());
        String schema = getSchema(target);

        SimpleFeatureType type = store.getSchema(schema);
        Query query = new Query(schema);


        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            if(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                    String name = descriptor.getName().toString();
                    Object obj = feature.getAttribute(descriptor.getName());
                    System.out.println(name + ": " + obj);
                }
            } else {
                System.out.println("EMPTY");
            }
        }
    }


    public static void appendShpTo(Path source, Path appendTarget) {







    }
}
