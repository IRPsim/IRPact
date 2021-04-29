package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.builder.jaxb.CityGMLBuilderException;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReadException;
import org.citygml4j.xml.io.reader.CityGMLReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class CityGjm2GisBase {

    protected static final String GIS_KEY = "BldgRoot";
    protected static final String GROUND = "Ground";
    protected static final String ELEMENT = "Element";

    protected CoordinateReferenceSystem gjmCRS;
    protected CoordinateReferenceSystem gisCRS = DefaultGeographicCRS.WGS84;
    protected MathTransform gjmToGis;
    protected Charset dbfCharset = StandardCharsets.UTF_8;

    public CityGjm2GisBase() {
    }

    public void setGjmCRS(CoordinateReferenceSystem gjmCRS) {
        this.gjmCRS = gjmCRS;
    }

    public void setGisCRS(CoordinateReferenceSystem gisCRS) {
        this.gisCRS = gisCRS;
    }

    public void mergeToGis(
            Path gjmInput,
            Path gisDbfInput,
            Path gisOutputWithoutExtension) throws Exception {
        if(Files.notExists(gjmInput)) throw new FileNotFoundException(gjmInput.toString());
        if(Files.notExists(gisDbfInput)) throw new FileNotFoundException(gisDbfInput.toString());

        buildTransform();

        Path outDbf = setExtension(gisOutputWithoutExtension, ".dbf");
        Path outShp = setExtension(gisOutputWithoutExtension, ".shp");

        Set<String> toSkip = new HashSet<>();
        Map<String, Geometry> gjmData = new LinkedHashMap<>();
        parseGjm(gjmInput, gjmData, toSkip);

        List<String> orderedGis = new ArrayList<>();
        parseGisOrder(gisDbfInput, orderedGis);
        List<String> both = getSameKeys(orderedGis, gjmData.keySet());
        writeShapeFile(both, gjmData, outShp);
        writeDbfFile(new HashSet<>(both), gisDbfInput, outDbf, both.size());
    }

    //=========================
    //abstract
    //=========================

    protected abstract IRPLogger logger();

    protected abstract Pair<String, ? extends Geometry> getLod2Terrain(CityObjectMember member);

    //=========================
    //static
    //=========================

    protected static Path setExtension(Path path, String extension) {
        Path parent = path.getParent();
        String fileName = path.getFileName().toString();
        return parent.resolve(fileName + extension);
    }

    protected static int findIndex(DbaseFileHeader header, String key) {
        for(int i = 0; i < header.getNumFields(); i++) {
            if(Objects.equals(key, header.getFieldName(i))) {
                return i;
            }
        }
        return -1;
    }


    protected static List<String> getSameKeys(List<String> orderedGis, Set<String> gjmData) {
        List<String> both = new ArrayList<>();
        for(String str: orderedGis) {
            if(gjmData.contains(str)) {
                both.add(str);
            }
        }
        return both;
    }

    //=========================
    //util
    //=========================

    protected void buildTransform() throws FactoryException {
        if(gisCRS == gjmCRS) {
            gjmToGis = null;
        } else {
            gjmToGis = CRS.findMathTransform(gjmCRS, gisCRS, true);
        }
    }

    protected void parseGjm(
            Path path,
            Map<String, ? super Geometry> out,
            Set<String> toSkip) throws CityGMLBuilderException, CityGMLReadException {

        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(path.toFile());

        while(reader.hasNext()) {
            CityModel cityModel = (CityModel) reader.nextFeature();
            for(CityObjectMember member: cityModel.getCityObjectMember()) {
                Pair<String, ? extends Geometry> data = getLod2Terrain(member);
                if(toSkip.contains(data.getKey())) {
                    logger().debug("[gjm] skip '{}'" , data.getKey());
                    continue;
                }
                if(out.containsKey(data.getKey())) {
                    throw ExceptionUtil.create(IllegalArgumentException::new, "key '{}' already exists", data.getKey());
                } else {
                    out.put(data.getKey(), data.getValue());
                }
            }
        }
    }

    protected void parseGisOrder(
            Path gisDbfInput,
            List<String> out) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(gisDbfInput), false, dbfCharset)) {
            DbaseFileHeader header = reader.getHeader();

            int elementIndex = findIndex(header, ELEMENT);
            if(elementIndex == -1) {
                throw new IllegalArgumentException("element index not found");
            }
            int keyIndex = findIndex(header, GIS_KEY);
            if(keyIndex == -1) {
                throw new IllegalArgumentException("header index not found");
            }

            while(reader.hasNext()) {
                Object[] entry = reader.readEntry();
                String element = entry[elementIndex].toString();
                if(!Objects.equals(GROUND, element)) {
                    continue;
                }
                Object value = entry[keyIndex];
                out.add(value.toString());
            }
        }
    }

    protected void writeShapeFile(List<String> keys, Map<String, ? extends Geometry> gjmData, Path outShp) throws IOException, SchemaException {
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", outShp.toUri().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newStore = (ShapefileDataStore) fac.createNewDataStore(params);
        String schema = Gis.removeExtension(outShp.getFileName().toString());
        SimpleFeatureTypeImpl type = (SimpleFeatureTypeImpl) DataUtilities.createType(schema, "the_geom:MultiPolygon");
        newStore.createSchema(type);
        newStore.forceSchemaCRS(gisCRS);

        DefaultFeatureCollection coll = new DefaultFeatureCollection();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        for(String key: keys) {
            Geometry geometry = gjmData.get(key);
            featureBuilder.add(geometry);
            SimpleFeature feature = featureBuilder.buildFeature(null);
            coll.add(feature);
        }

        Transaction transaction = new DefaultTransaction("create");
        SimpleFeatureStore featureStore = (SimpleFeatureStore) newStore.getFeatureSource(schema);
        featureStore.setTransaction(transaction);
        try {
            featureStore.addFeatures(coll);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        } finally {
            transaction.close();
            newStore.dispose();
        }
    }

    protected void writeDbfFile(
            Set<String> both,
            Path gisDbfInput,
            Path gisDbfOutput,
            int numberOfRecords) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(gisDbfInput), false, dbfCharset)) {
            DbaseFileHeader header = Gis.copyHeaderWithNewRecordCount(reader.getHeader(), dbfCharset, numberOfRecords);

            int elementIndex = findIndex(header, ELEMENT);
            if(elementIndex == -1) {
                throw new IllegalArgumentException("element index not found");
            }
            int keyIndex = findIndex(header, GIS_KEY);
            if(keyIndex == -1) {
                throw new IllegalArgumentException("header index not found");
            }

            Files.deleteIfExists(gisDbfOutput);
            FileChannel channel = FileChannel.open(gisDbfOutput, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            DbaseFileWriter writer = new DbaseFileWriter(header, channel);
            try {
                while(reader.hasNext()) {
                    Object[] entry = reader.readEntry();
                    String element = entry[elementIndex].toString();
                    if(!Objects.equals(GROUND, element)) {
                        continue;
                    }
                    String key = entry[keyIndex].toString();
                    if(!both.contains(key)) {
                        logger().trace("[dbf] skip '{}'", key);
                        continue;
                    }
                    writer.write(entry);
                }
            } finally {
                writer.close();
            }
        }
    }
}
