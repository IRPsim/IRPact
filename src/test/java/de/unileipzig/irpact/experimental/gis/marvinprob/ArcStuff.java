package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.util.gis.CityGjm2Gis;
import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.cityjson.CityJSONBuilder;
import org.citygml4j.builder.cityjson.json.io.writer.CityJSONOutputFactory;
import org.citygml4j.builder.cityjson.json.io.writer.CityJSONWriter;
import org.citygml4j.builder.cityjson.util.TextureFileHandler;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.builder.jaxb.CityGMLBuilderException;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurve;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurveProperty;
import org.citygml4j.model.gml.geometry.primitives.CurveProperty;
import org.citygml4j.model.gml.geometry.primitives.LineString;
import org.citygml4j.xml.io.CityGMLInputFactory;
import org.citygml4j.xml.io.reader.CityGMLReadException;
import org.citygml4j.xml.io.reader.CityGMLReader;
import org.geotools.data.FeatureReader;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Daniel Abitz
 */
@Disabled
class ArcStuff {

    @SuppressWarnings("FieldMayBeFinal")
    public static class SimpleTextureFileHandler implements TextureFileHandler {
        private Path baseDir;
        private Path targetDir;

        public SimpleTextureFileHandler(Path baseDir, Path targetDir) {
            this.baseDir = baseDir;
            this.targetDir = targetDir;
        }

        @Override
        public String getImageURI(String imageURI) {
		/*
		  This is a very simple implementation of the TextureFileHandler interface.
		  We simply copy the incoming texture image to the target directory. A real
		  implementation should consider the following:

		  Firstly, the same texture image might be reused several times in the input
		  file. So we should make sure not to copy the same image file multiple times.

		  Secondly, an imageURI may be any URI reference and not just a pointer into the
		  local file system. For instance, the imageURI might also reference an HTTP resource.
		  So we should parse the imageURI more carefully.

		  Finally, copying the texture images could be done in multiple threads to
		  speed up the process.
		 */

            if (imageURI != null) {
                try {
                    imageURI = imageURI.replace('\\', '/');
                    Path imagePath = Paths.get(imageURI);

                    // simply return imageURI in case it is an absolute path
                    if (imagePath.isAbsolute())
                        return imageURI;

                    Path source = baseDir.resolve(imagePath).normalize().toAbsolutePath();
                    Path target = targetDir.resolve(imagePath).normalize().toAbsolutePath();

                    if (!Files.exists(target.getParent()))
                        Files.createDirectories(target.getParent());

                    copy(source, target);
                    return imagePath.toString().replace('\\', '/');
                } catch (Exception e) {
                    //
                }
            }

            // return null if we cannot handle the texture image
            return null;
        }

        private void copy(Path source, Path target) throws IOException {
            FileInputStream fromStream = new FileInputStream(source.toFile());
            FileChannel fromChannel = fromStream.getChannel();

            FileOutputStream toStream = new FileOutputStream(target.toFile());
            FileChannel toChannel = toStream.getChannel();

            fromChannel.transferTo(0, fromChannel.size(), toChannel);

            fromChannel.close();
            toChannel.close();

            fromStream.close();
            toStream.close();
        }

    }

    private static final Path dir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\3D_LoD2_Shape_33280_5588_2_sn");

    @Test
    void testIt() throws CityGMLBuilderException, CityGMLReadException {
        Path path = Paths.get("E:\\MyTemp\\Marvin-Daten\\3D_LoD2_CityGML_33282_5590_2_sn", "3D_LoD2_33282_5590_2_sn.gml");
        Assertions.assertTrue(Files.exists(path));

        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(path.toFile());
        while(reader.hasNext()) {
            CityModel cityModel = (CityModel) reader.nextFeature();
            for(CityObjectMember member: cityModel.getCityObjectMember()) {
                Building building = (Building) member.getCityObject();
                MultiCurveProperty prop = building.getLod2TerrainIntersection();
                MultiCurve curve = prop.getMultiCurve();
                for(CurveProperty curveProp: curve.getCurveMember()) {
                    LineString ls = (LineString) curveProp.getCurve();
                    System.out.println(ls.toList3d() + " " + ls.toList3d().size());
                }
            }
        }
        reader.close();
    }

    @Test
    void toJson() throws Exception {
        Path inPath = Paths.get("E:\\MyTemp\\Marvin-Daten\\3D_LoD2_CityGML_33282_5590_2_sn", "3D_LoD2_33282_5590_2_sn.gml");
        Path outPath = Paths.get("E:\\MyTemp\\Marvin-Daten\\3D_LoD2_CityGML_33282_5590_2_sn", "3D_LoD2_33282_5590_2_sn.json");

        final SimpleDateFormat df = new SimpleDateFormat("[HH:mm:ss] ");

        System.out.println(df.format(new Date()) + "setting up citygml4j context and CityGML builder");
        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();

        // reading CityGML dataset
        System.out.println(df.format(new Date()) + "reading LOD3_Building_v200.gml into main memory");
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(inPath.toFile());

        // we know that the root element is a <core:CityModel> so we use a shortcut here
        CityModel cityModel = (CityModel)reader.nextFeature();
        reader.close();

        // create a CityJSON builder
        System.out.println(df.format(new Date()) + "creating CityJSON builder");
        CityJSONBuilder jsonBuilder = ctx.createCityJSONBuilder();

        // create a CityJSON output factory
        System.out.println(df.format(new Date()) + "writing citygml4j object tree as CityJSON file");
        CityJSONOutputFactory out = jsonBuilder.createCityJSONOutputFactory();

        // create a simple CityJSON writer
        CityJSONWriter writer = out.createCityJSONWriter(outPath.toFile());

        /*
         * we can use different helpers on the CityJSON writer such as builders
         * for the "vertices" and "vertices-texture" arrays. Especially when converting
         * an existing CityGML dataset, it is recommended that you provide your own
         * texture file handler.
         * A texture file handler is invoked for every texture image found in the CityGML
         * dataset. Its task is to generate a value for the "image" property of a CityJSON
         * texture object and to possibly copy the image file to the target appearance folder.
         * If you do not provide your own texture file handler, a default one will be used
         * that simply takes the <imageURI> value from the CityGML input file as value for the
         * "image" property. Note that you should create the target CityJSON file in the same
         * folder as the input CityGML file when using the default handler. Otherwise
         * "image" property values being relative paths cannot be correctly resolved.
         */
        //writer.setTextureFileHandler(new SimpleTextureFileHandler(Paths.get("datasets"), Paths.get("output")));

        /*
         * citygml4j also supports the transformation of the coordinates of the vertices
         * to integer values. This transformation might substantially reduce the size
         * of the CityJSON file and thus is a simple but efficient compression method.
         * The transformation parameters are stored as "transform" in the file to be able
         * to retrieve the original coordinate values.
         * In order to apply compression, you can simple register a vertices transformer
         * with the output factory. citygml4j provides a default transformer which lets
         * you define the number of significant digits to keep (3 per default).
         */

        //writer.setVerticesTransformer(new DefaultVerticesTransformer().withSignificantDigits(3));

        // send the city model to the CityJSON writer
        writer.write(cityModel);
        writer.close();

        System.out.println(df.format(new Date()) + "CityJSON file LOD3_Building.json written");
        System.out.println(df.format(new Date()) + "sample citygml4j application successfully finished");
    }

    @Test
    void testXX() throws ParseException {
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        org.locationtech.jts.geom.LineString ls = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282931.67 5591036.03 , 282931.825 5591036.825 , 282931.859 5591037.0 , 282932.0 5591037.724 , 282932.054 5591038.0 , 282932.067 5591038.067 , 282932.248 5591039.0 , 282932.309 5591039.309 , 282932.443 5591040.0 , 282932.55 5591040.55 , 282932.638 5591041.0 , 282932.792 5591041.792 , 282932.833 5591042.0 , 282933.0 5591042.859 , 282933.01 5591042.91)");

    }

    @Test
    void testXX2() throws ParseException {
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        org.locationtech.jts.geom.LineString ls0 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(0 0, 0 1)");
        org.locationtech.jts.geom.LineString ls1 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(0 1, 1 1)");
        org.locationtech.jts.geom.LineString ls2 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(1 1, 1 0)");
        org.locationtech.jts.geom.LineString ls3 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(1 0, 0 0)");
        MultiLineString mls = fac.createMultiLineString(new org.locationtech.jts.geom.LineString[]{ls0, ls1, ls2, ls3});
        System.out.println(mls.getBoundary());
    }

    @Test
    void testXX3() throws ParseException {
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        org.locationtech.jts.geom.LineString ls0 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(0 0, 0 1, 1 1, 1 0, 0 0)");
        Polygon polygon = fac.createPolygon(ls0.getCoordinates());
        System.out.println(polygon);
    }

    //https://docs.geotools.org/stable/userguide/library/referencing/crs.html
    @Test
    void codeStuff() throws FactoryException, TransformException, ParseException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833");
        CoordinateReferenceSystem targetCRS  = DefaultGeographicCRS.WGS84;
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        org.locationtech.jts.geom.LineString ls = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282931.67 5591036.03 , 282931.825 5591036.825 , 282931.859 5591037.0 , 282932.0 5591037.724 , 282932.054 5591038.0 , 282932.067 5591038.067 , 282932.248 5591039.0 , 282932.309 5591039.309 , 282932.443 5591040.0 , 282932.55 5591040.55 , 282932.638 5591041.0 , 282932.792 5591041.792 , 282932.833 5591042.0 , 282933.0 5591042.859 , 282933.01 5591042.91)");
        //org.locationtech.jts.geom.LineString ls = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282931.67 5591036.03 582.763, 282931.825 5591036.825 582.652, 282931.859 5591037.0 582.624, 282932.0 5591037.724 582.482, 282932.054 5591038.0 582.428, 282932.067 5591038.067 582.417, 282932.248 5591039.0 582.263, 282932.309 5591039.309 582.208, 282932.443 5591040.0 582.081, 282932.55 5591040.55 581.98, 282932.638 5591041.0 581.897, 282932.792 5591041.792 581.791, 282932.833 5591042.0 581.763, 282933.0 5591042.859 581.614, 282933.01 5591042.91 581.605)");
        Geometry tarLs = JTS.transform(ls, transform);

        System.out.println(ls);
        System.out.println(tarLs);
    }

    //https://docs.geotools.org/stable/userguide/library/referencing/crs.html
    @Test
    void codeStuff3() throws FactoryException, TransformException, ParseException {
        CoordinateReferenceSystem sourceCRS = CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833");
        CoordinateReferenceSystem targetCRS  = DefaultGeographicCRS.WGS84;
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();

        org.locationtech.jts.geom.LineString ls0 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282931.67 5591036.03 582.763, 282931.67 5591036.03 582.763)");
        Geometry tarLs0 = JTS.transform(ls0, transform);
        System.out.println(ls0);
        System.out.println(tarLs0);

        org.locationtech.jts.geom.LineString ls1 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282933.01 5591042.91 581.605, 282933.01 5591042.91 581.605)");
        Geometry tarLs1 = JTS.transform(ls1, transform);
        System.out.println(ls1);
        System.out.println(tarLs1);

        org.locationtech.jts.geom.LineString ls2 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282936.57 5591035.08 582.075, 282936.57 5591035.08 582.075)");
        Geometry tarLs2 = JTS.transform(ls2, transform);
        System.out.println(ls2);
        System.out.println(tarLs2);

        org.locationtech.jts.geom.LineString ls3 = (org.locationtech.jts.geom.LineString) new WKTReader2(fac).read("LINESTRING(282937.92 5591041.95 581.346, 282937.92 5591041.95 581.346)");
        Geometry tarLs3 = JTS.transform(ls3, transform);
        System.out.println(ls3);
        System.out.println(tarLs3);

        //BEACHTE: Umkerhung von y,x wegen lat
    }

    @Test
    void testMyMerge() throws Exception {
        Path gjmIn = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_CityGML\\data", "3D_LoD2_33280_5590_2_sn.gml");
        Path gisDbfIn = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data", "3D_LoD2_33280_5590_2_sn.dbf");
        Path output = Paths.get("E:\\MyTemp\\Marvin-Daten\\output", "xxx");

        CityGjm2Gis conv = new CityGjm2Gis();
        conv.setGjmCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        conv.mergeToGis(gjmIn, gisDbfIn, output);
    }

    @Test
    void testMiniShp() throws IOException {
        Path path = Paths.get("E:\\MyTemp\\Marvin-Daten\\output", "xxx.shp");
        assertTrue(Files.exists(path), "> " + path);

        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(path.toFile());
        System.out.println(Arrays.toString(store.getTypeNames()));

        SimpleFeatureType type = store.getSchema("xxx");
        Query query = new Query("xxx");

//        for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
//            String name = descriptor.getName().toString();
//            System.out.println(name);
//        }

        try(FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            while(reader.hasNext()) {
                SimpleFeature feature = reader.next();
                for(AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
                    String name = descriptor.getName().toString();
                    Object obj = feature.getAttribute(descriptor.getName());
                    System.out.println(name + " " + obj);
                }
            }
        } finally {
            store.dispose(); //!!! sonst lock error
            //https://stackoverflow.com/questions/19882341/why-this-code-how-read-a-shapefile-using-geotools-throws-this-exception
        }
    }
}
