package de.unileipzig.irpact.experimental;

import org.geotools.data.*;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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

        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(p), false, StandardCharsets.ISO_8859_1)) {
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
}
