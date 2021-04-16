package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Gis {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Gis.class);

    public static DbaseFileHeader getHeader(Path path, Charset charset) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(path), false, charset)) {
            return reader.getHeader();
        }
    }

    public static String getNamePart(String fullName, int part) {
        String[] parts = fullName.split("__");
        return parts.length == 1 ? fullName : parts[part];
    }

    public static DbaseFileHeader copyHeaderWithNewRecordCount(DbaseFileHeader header, Charset charset, int numberOfRecords) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try(WritableByteChannel outChannel = Channels.newChannel(out)) {
            header.writeHeader(outChannel);
        }
        DbaseFileHeader copy = new DbaseFileHeader(charset);
        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        try(ReadableByteChannel inChannel = Channels.newChannel(in)) {
            copy.readHeader(inChannel);
        }
        copy.setNumRecords(numberOfRecords);
        return copy;
    }

    public static String removeExtension(String name) {
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(0, dot);
    }

    private static String getSchema(Path target) {
        return removeExtension(target.getFileName().toString());
    }

    public static void peekDbf(Path target, Charset charset) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(target), false, charset)) {
            DbaseFileHeader header = reader.getHeader();

            int count = 0;
            while(reader.hasNext()) {
                System.out.println(count);
                Object[] entry = reader.readEntry();
                if(count == 0) {
                    for(int i = 0; i < header.getNumFields(); i++) {
                        System.out.println(header.getFieldName(i) + ": " + entry[i]);
                    }
                }
                count++;
            }
            System.out.println("entries: " + count);
        }
    }

    public static void peekAllDbf(Path target, Charset charset) throws IOException {
        peekAllDbf(target, charset, false);
    }

    public static void peekAllDbf(Path target, Charset charset, boolean silent) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(target), false, charset)) {
            int count = 0;
            while(reader.hasNext()) {
                Object[] entry = reader.readEntry();
                if(!silent) {
                    System.out.println(count + " " + Arrays.toString(entry));
                }
                count++;
            }
            System.out.println("entries: " + count);
        }
    }

    public static void peekShp(Path target) throws IOException {
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(target.toFile());
        String schema = getSchema(target);

        SimpleFeatureType type = store.getSchema(schema);
        Query query = new Query(schema);

        int count = 0;
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                count++;
                SimpleFeature feature = reader.next();
                if(count == 0) {
                    for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                        String name = descriptor.getName().toString();
                        Object obj = feature.getAttribute(descriptor.getName());
                        System.out.println(name + ": " + obj);
                    }
                }
            }
        } finally {
            store.dispose();
        }
        System.out.println("total: " + count);
    }

    public static void peekAllShp(Path target) throws IOException {
        peekAllShp(target, false);
    }

    public static void peekAllShp(Path target, boolean silent) throws IOException {
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(target.toFile());
        String schema = getSchema(target);

        SimpleFeatureType type = store.getSchema(schema);
        Query query = new Query(schema);

        int count = 0;
        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                count++;
                SimpleFeature feature = reader.next();
                if(!silent) {
                    for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                        String name = descriptor.getName().toString();
                        Object obj = feature.getAttribute(descriptor.getName());
                        System.out.println(count + ": " + name + ": " + obj);
                    }
                }
            }
        } finally {
            store.dispose();
        }
        System.out.println("total: " + count);
    }

    private static boolean collectAllDbfEntries(
            Collection<? extends Path> dbfFiles,
            Charset charset,
            Collection<? super Object[]> target) throws IOException {
        boolean changed = false;
        int i = 1;
        for(Path dbfFile: dbfFiles) {
            LOGGER.trace("[dbf] collect '{}' ({} / {})", dbfFile.getFileName(), i, dbfFiles.size());
            changed |= collectAllDbfEntries(dbfFile, charset, target);
            i++;
        }
        return changed;
    }

    private static boolean collectAllDbfEntries(
            Path dbfFile,
            Charset charset,
            Collection<? super Object[]> target) throws IOException {
        boolean changed = false;
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(dbfFile), false, charset)) {
            while(reader.hasNext()) {
                Object[] entry = reader.readEntry();
                changed |= target.add(entry);
            }
        }
        return changed;
    }

//    private static boolean collectAllGeomEntries(
//            Collection<? extends Path> shpFiles,
//            Collection<? super Object> target) throws IOException {
//        boolean changed = false;
//        int i = 1;
//        for(Path shpFile: shpFiles) {
//            LOGGER.trace("[shp] collect '{}' ({} / {})", shpFile, i, shpFiles.size());
//            changed |= collectAllGeomEntries(shpFile, target);
//            i++;
//        }
//        return changed;
//    }

//    private static boolean collectAllGeomEntries(
//            Path shpFile,
//            Collection<? super Object> target) throws IOException {
//        boolean changed = false;
//
//        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(shpFile.toFile());
//        String schema = getSchema(shpFile);
//        Query query = new Query(schema);
//
//        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
//            while(reader.hasNext()) {
//                SimpleFeature feature = reader.next();
//                Object obj = feature.getAttribute("the_geom");
//                if(obj == null) {
//                    throw new NullPointerException("no geom: " + shpFile);
//                }
//                changed |= target.add(obj);
//            }
//        } finally {
//            store.dispose();
//        }
//
//        return changed;
//    }

    public static void mergeShapeFiles(
            Collection<? extends Path> shpFiles,
            CoordinateReferenceSystem crs,
            Path outputShp) throws IOException, SchemaException {
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", outputShp.toUri().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newStore = (ShapefileDataStore) fac.createNewDataStore(params);
        String outSchema = Gis.removeExtension(outputShp.getFileName().toString());
        SimpleFeatureTypeImpl type = (SimpleFeatureTypeImpl) DataUtilities.createType(outSchema, "the_geom:MultiPolygon");
        newStore.createSchema(type);
        newStore.forceSchemaCRS(crs);
        try {
            int i = 1;
            int count = 0;
            for(Path inShp: shpFiles) {
                LOGGER.trace("[shp] append {} to {} ({} / {})", inShp.getFileName(), outputShp.getFileName(), i++, shpFiles.size());
                ShapefileDataStore inStore = (ShapefileDataStore) FileDataStoreFinder.getDataStore(inShp.toFile());
                String inSchema = getSchema(inShp);
                Query inQuery = new Query(inSchema);

                Transaction transaction = new DefaultTransaction("create");
                SimpleFeatureStore featureStore = (SimpleFeatureStore) newStore.getFeatureSource(outSchema);
                featureStore.setTransaction(transaction);
                DefaultFeatureCollection coll = new DefaultFeatureCollection();
                FeatureReader<SimpleFeatureType, SimpleFeature> reader = inStore.getFeatureReader(inQuery, Transaction.AUTO_COMMIT);

                try {
                    while(reader.hasNext()) {
                        SimpleFeature feature = reader.next();
                        Object obj = feature.getAttribute("the_geom");
                        if(obj == null) {
                            throw new NullPointerException("no geom: " + inShp);
                        }
                        count++;
                        coll.add(feature);
                    }
                    featureStore.addFeatures(coll);
                    transaction.commit();

                    reader.close();
                    transaction.close();
                } catch (IOException e) {
                    transaction.rollback();
                    throw e;
                } finally {
                    inStore.dispose();
                }
            }
            LOGGER.trace("[shp] {} entries added to {}", count, outputShp.getFileName());
        } finally {
            newStore.dispose();
        }
    }

    public static void mergeShapeFiles3(
            Collection<? extends Path> shpFiles,
            CoordinateReferenceSystem crs,
            Path outputShp) throws IOException, SchemaException {
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", outputShp.toUri().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newStore = (ShapefileDataStore) fac.createNewDataStore(params);
        String outSchema = Gis.removeExtension(outputShp.getFileName().toString());
        SimpleFeatureTypeImpl type = (SimpleFeatureTypeImpl) DataUtilities.createType(outSchema, "the_geom:MultiPolygon");
        newStore.createSchema(type);
        newStore.forceSchemaCRS(crs);

        try {
            int i = 1;
            int count = 0;
            for(Path inShp: shpFiles) {
                LOGGER.trace("[shp] append {} to {} ({} / {})", inShp.getFileName(), outputShp.getFileName(), i++, shpFiles.size());
                ShapefileDataStore inStore = (ShapefileDataStore) FileDataStoreFinder.getDataStore(inShp.toFile());

                Transaction transaction = new DefaultTransaction("create");
                SimpleFeatureStore featureStore = (SimpleFeatureStore) newStore.getFeatureSource(outSchema);
                featureStore.setTransaction(transaction);
                FeatureSource<SimpleFeatureType, SimpleFeature> source = inStore.getFeatureSource(inStore.getTypeNames()[0]);

                try {
                    FeatureCollection<SimpleFeatureType, SimpleFeature> coll = source.getFeatures();
                    featureStore.addFeatures(coll);
                    transaction.commit();
                    count += coll.size();
                    transaction.close();
                } catch (IOException e) {
                    transaction.rollback();
                    throw e;
                } finally {
                    inStore.dispose();
                }
            }
            LOGGER.trace("[shp] {} entries added to {}", count, outputShp.getFileName());
        } finally {
            newStore.dispose();
        }
    }

    public static void mergeShapeFiles2(
            Collection<? extends Path> shpFiles,
            CoordinateReferenceSystem crs,
            Path outputShp) throws IOException, SchemaException {

        Map<String, Serializable> outParams = new HashMap<>();
        outParams.put("url", outputShp.toUri().toURL());
        outParams.put("create spatial index", Boolean.TRUE);

        FileDataStoreFactorySpi outFac = FileDataStoreFinder.getDataStoreFactory("shp");
        ShapefileDataStore outStore = (ShapefileDataStore) outFac.createNewDataStore(outParams);
        String outSchema = Gis.removeExtension(outputShp.getFileName().toString());
        SimpleFeatureTypeImpl outType = (SimpleFeatureTypeImpl) DataUtilities.createType(outSchema, "the_geom:MultiPolygon");
        outStore.createSchema(outType);
        outStore.forceSchemaCRS(crs);

        try {
            int i = 1;
            for(Path inShp: shpFiles) {
                LOGGER.trace("[shp] append {} to {} ({} / {})", inShp.getFileName(), outputShp.getFileName(), i++, shpFiles.size());

                ShapefileDataStore inStore = (ShapefileDataStore) FileDataStoreFinder.getDataStore(inShp.toFile());
                FeatureSource<SimpleFeatureType, SimpleFeature> inSource = inStore.getFeatureSource(inStore.getTypeNames()[0]);
                FeatureCollection<SimpleFeatureType, SimpleFeature> inColl = inSource.getFeatures();

                try(Transaction outTrans = new DefaultTransaction("write")) {
                    try {
                        FeatureIterator<SimpleFeature> inFeatures = inColl.features();
                        FeatureWriter<SimpleFeatureType, SimpleFeature> outWriter = outStore.getFeatureWriter(outTrans);

                        while(inFeatures.hasNext()) {
                            SimpleFeature nextInFeature = inFeatures.next();
                            SimpleFeature nextOutFeature = outWriter.next();
                            nextOutFeature.setAttribute("the_geom", nextInFeature.getAttribute("the_geom"));
                            outWriter.write();
                        }

                        outWriter.close();
                        inFeatures.close();
                        outTrans.commit();
                    } catch (IOException e) {
                        outTrans.rollback();
                        throw e;
                    }
                } finally {
                    inStore.dispose();
                }
            }
        } finally {
            outStore.dispose();
        }
    }

    public static void mergeDbfFiles(
            List<Path> dbfFiles,
            Charset charset,
            Path outDbf) throws IOException {
        LOGGER.trace("handle {}", outDbf.getFileName());
        DbaseFileHeader tempHeader = getHeader(dbfFiles.get(0), charset);
        List<Object[]> entries = new ArrayList<>();
        collectAllDbfEntries(dbfFiles, charset, entries);
        writeDbfFile(tempHeader, outDbf, charset, entries);
    }

    public static List<Path> getAllFiles(Path dir, String extension) throws IOException {
        List<Path> out = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path path: stream) {
                if(path.getFileName().toString().endsWith(extension)) {
                    out.add(path);
                }
            }
        }
        return out;
    }

    private static void writeDbfFile(
            DbaseFileHeader tempHeader,
            Path gisDbfOutput,
            Charset charset,
            Collection<? extends Object[]> records) throws IOException {
        LOGGER.trace("[dbf] write {} entries", records.size());
        DbaseFileHeader header = copyHeaderWithNewRecordCount(tempHeader, charset, records.size());
        Files.deleteIfExists(gisDbfOutput);
        FileChannel channel = FileChannel.open(gisDbfOutput, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        DbaseFileWriter writer = new DbaseFileWriter(header, channel);
        try {
            for(Object[] entry: records) {
                writer.write(entry);
            }
        } finally {
            writer.close();
        }
    }

    public static void mergeAll(
            ShapeFiles inputFiles,
            CoordinateReferenceSystem crs,
            Charset charset,
            ShapeFile outputFile) throws SchemaException, IOException {
        LOGGER.trace("merge {} files, {} -> {}, out: {}", inputFiles.count(), inputFiles.getFirst().getName(), inputFiles.getLast().getName(), outputFile.getName());

        mergeShapeFiles3(
                inputFiles.getShpFiles(),
                crs,
                outputFile.shp()
        );

        mergeDbfFiles(
                inputFiles.getDbfFiles(),
                charset,
                outputFile.dbf()
        );
    }
}
