package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.data.Pair;
import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.CityGMLBuilder;
import org.citygml4j.builder.jaxb.CityGMLBuilderException;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.model.gml.base.CoordinateListProvider;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurve;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurveProperty;
import org.citygml4j.model.gml.geometry.primitives.CurveProperty;
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
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

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
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class CityGjm2Gis {

    private static final String GIS_KEY = "BldgRoot";
    private static final String GROUND = "Ground";
    private static final String ELEMENT = "Element";

    private CoordinateReferenceSystem gjmCRS;
    private CoordinateReferenceSystem gisCRS = DefaultGeographicCRS.WGS84;
    private MathTransform gjmToGis;
    private Charset dbfCharset = StandardCharsets.UTF_8;
    private boolean useLines = false;

    public CityGjm2Gis() {
    }

    public void setUseLines(boolean useLines) {
        this.useLines = useLines;
    }

    public void setGjmCRS(CoordinateReferenceSystem gjmCRS) {
        this.gjmCRS = gjmCRS;
    }

    public static String removeExtension(String name) {
        int dot = name.lastIndexOf('.');
        if(dot == -1) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(0, dot);
    }

    private static List<Point3D> toPoint3D(List<Double> doubleList) {
        if(doubleList.size() % 3 != 0) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "list size '{} mod 3 != 0'", doubleList.size());
        }
        List<Point3D> out = new ArrayList<>(doubleList.size() / 3);
        for(int i = 0; i < doubleList.size(); i += 3) {
            out.add(new Point3D(
                    doubleList.get(i),
                    doubleList.get(i + 1),
                    doubleList.get(i + 2)
            ));
        }
        return out;
    }

    private LineString reverse(GeometryFactory fac, LineString ls) {
        Coordinate[] reversed = Arrays.stream(ls.getCoordinates())
                .map(c -> {
                    Coordinate out = new Coordinate();
                    out.setX(c.getY());
                    out.setY(c.getX());
                    return out;
                })
                .toArray(Coordinate[]::new);
        return fac.createLineString(reversed);
    }

    private Map<String, MultiPolygon> parseGjm(
            Path gjmInput,
            Map<String, MultiPolygon> out) throws CityGMLBuilderException, CityGMLReadException, TransformException, ParseException {
        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(gjmInput.toFile());
        while(reader.hasNext()) {
            CityModel cityModel = (CityModel) reader.nextFeature();
            for(CityObjectMember member: cityModel.getCityObjectMember()) {
                Pair<String, MultiPolygon> data = getLod2Terrain(member);
                if(out.containsKey(data.getKey())) {
                    throw ExceptionUtil.create(IllegalArgumentException::new, "key '{}' already exists", data.getKey());
                } else {
                    out.put(data.getKey(), data.getValue());
                }
            }
        }
        return out;
    }

    private Pair<String, MultiPolygon> getLod2Terrain(CityObjectMember member) throws TransformException, ParseException {
        Building building = (Building) member.getCityObject();
        MultiCurveProperty curveProperty = building.getLod2TerrainIntersection();
        MultiCurve curve = curveProperty.getMultiCurve();
        List<Point3DList> list3d = curve.getCurveMember()
                .stream()
                .map(CurveProperty::getCurve)
                .map(CoordinateListProvider::toList3d)
                .map(CityGjm2Gis::toPoint3D)
                .map(Point3DList::new)
                .collect(Collectors.toList());
        List<Point3DList> merged = PolygonBuilder.getPolygons(list3d);
        MultiPolygon polygon = toMultiPolygon(merged);
        return new Pair<>(building.getId(), polygon);
    }

    private MultiPolygon toMultiPolygon(List<Point3DList> llist) throws ParseException, TransformException {
        List<Polygon> polygons = new ArrayList<>();
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        for(Point3DList list: llist) {
            String lsStr = list.toLineString();
            LineString ls = (LineString) new WKTReader2(fac).read(lsStr);
            LineString gisLs = (LineString) JTS.transform(ls, gjmToGis);
            LineString reversed = reverse(fac, gisLs);
            Polygon polygon = fac.createPolygon(reversed.getCoordinates());
            polygons.add(polygon);
        }
        return fac.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    private Map<String, MultiLineString> parseGjmWithLines(
            Path gjmInput,
            Map<String, MultiLineString> out) throws CityGMLBuilderException, CityGMLReadException, TransformException, ParseException {
        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(gjmInput.toFile());
        while(reader.hasNext()) {
            CityModel cityModel = (CityModel) reader.nextFeature();
            for(CityObjectMember member: cityModel.getCityObjectMember()) {
                Pair<String, MultiLineString> data = getLod2TerrainAsLines(member);
                if(out.containsKey(data.getKey())) {
                    throw ExceptionUtil.create(IllegalArgumentException::new, "key '{}' already exists", data.getKey());
                } else {
                    out.put(data.getKey(), data.getValue());
                }
            }
        }
        return out;
    }

    private Pair<String, MultiLineString> getLod2TerrainAsLines(CityObjectMember member) throws TransformException, ParseException {
        Building building = (Building) member.getCityObject();
        MultiCurveProperty curveProperty = building.getLod2TerrainIntersection();
        MultiCurve curve = curveProperty.getMultiCurve();
        List<Point3DList> list3d = curve.getCurveMember()
                .stream()
                .map(CurveProperty::getCurve)
                .map(CoordinateListProvider::toList3d)
                .map(CityGjm2Gis::toPoint3D)
                .map(Point3DList::new)
                .collect(Collectors.toList());
        MultiLineString polygon = toMultiLineString(list3d);
        return new Pair<>(building.getId(), polygon);
    }

    private MultiLineString toMultiLineString(List<Point3DList> llist) throws ParseException, TransformException {
        List<LineString> lines = new ArrayList<>();
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        for(Point3DList list: llist) {
            String lsStr = list.toLineString();
            LineString ls = (LineString) new WKTReader2(fac).read(lsStr);
            LineString gisLs = (LineString) JTS.transform(ls, gjmToGis);
            LineString reversed = reverse(fac, gisLs);
            lines.add(reversed);
        }
        return fac.createMultiLineString(lines.toArray(new LineString[0]));
    }

    private static int findIndex(DbaseFileHeader header, String key) {
        for(int i = 0; i < header.getNumFields(); i++) {
            if(Objects.equals(key, header.getFieldName(i))) {
                return i;
            }
        }
        return -1;
    }

    private List<String> parseGisOrder(
            Path gisDbfInput,
            Path gisDbfOutput,
            List<String> out) throws IOException {
        try(DbaseFileReader reader = new DbaseFileReader(FileChannel.open(gisDbfInput), false, dbfCharset)) {
            DbaseFileHeader header = reader.getHeader();
            Files.deleteIfExists(gisDbfOutput);
            DbaseFileWriter writer = new DbaseFileWriter(header, FileChannel.open(gisDbfOutput, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE_NEW));

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
                writer.write(entry);
            }

            writer.close();
        }
        return out;
    }

    private void writeShapeFile(List<String> keys, Map<String, ? extends Geometry> gjmData, Path outShp) throws IOException, SchemaException {
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<>();
        params.put("url", outShp.toUri().toURL());
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newStore = (ShapefileDataStore) fac.createNewDataStore(params);
        newStore.forceSchemaCRS(gisCRS);
        String schema = removeExtension(outShp.getFileName().toString());
        SimpleFeatureTypeImpl type = (SimpleFeatureTypeImpl) DataUtilities.createType(schema, "the_geom:MultiPolygon");
        newStore.createSchema(type);

        DefaultFeatureCollection coll = new DefaultFeatureCollection();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        for(String key: keys) {
            Geometry geometry = gjmData.get(key);
            featureBuilder.add(geometry);
            SimpleFeature feature = featureBuilder.buildFeature(null);
            coll.add(feature);

            System.out.println(geometry);
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
        }
    }

    private List<String> getSameKeys(List<String> orderedGis, Set<String> gjmData) {
        List<String> both = new ArrayList<>();
        for(String str: orderedGis) {
            if(gjmData.contains(str)) {
                both.add(str);
            }
        }
        return both;
    }

    private static Path setExtension(Path path, String extension) {
        Path parent = path.getParent();
        String fileName = path.getFileName().toString();
        return parent.resolve(fileName + extension);
    }

    public void mergeToGis(
            Path gjmInput,
            Path gisDbfInput,
            Path gisOutputWithoutExtension) throws Exception {
        if(Files.notExists(gjmInput)) throw new FileNotFoundException(gjmInput.toString());
        if(Files.notExists(gisDbfInput)) throw new FileNotFoundException(gisDbfInput.toString());

        gjmToGis = CRS.findMathTransform(gjmCRS, gisCRS, true);

        Path outDbf = setExtension(gisOutputWithoutExtension, ".dbf");
        Path outShp = setExtension(gisOutputWithoutExtension, ".shp");

        Map<String, ? extends Geometry> gjmData = useLines
                ? parseGjmWithLines(gjmInput, new LinkedHashMap<>())
                : parseGjm(gjmInput, new LinkedHashMap<>());
        List<String> orderedGis = parseGisOrder(gisDbfInput, outDbf, new ArrayList<>());
        List<String> both = getSameKeys(orderedGis, gjmData.keySet());

        writeShapeFile(both, gjmData, outShp);
    }
}


// Point3DList[
// Point3D{280565.47, 5592013.07, 628.883}, Point3D{280566.0, 5592013.511, 628.824}, Point3D{280566.587, 5592014.0, 628.793}, Point3D{280567.0, 5592014.344, 628.782}, Point3D{280567.788, 5592015.0, 628.714}, Point3D{280568.0, 5592015.177, 628.712}, Point3D{280568.988, 5592016.0, 628.641}, Point3D{280569.0, 5592016.01, 628.64}, Point3D{280569.058, 5592016.058, 628.639}, Point3D{280570.0, 5592016.843, 628.64}, Point3D{280570.189, 5592017.0, 628.626}, Point3D{280571.0, 5592017.675, 628.643}, Point3D{280571.39, 5592018.0, 628.628}, Point3D{280571.57, 5592018.15, 628.629}, Point3D{280571.695, 5592018.0, 628.619}, Point3D{280571.834, 5592017.834, 628.617}, Point3D{280572.0, 5592017.635, 628.61}, Point3D{280572.289, 5592017.289, 628.604}, Point3D{280572.531, 5592017.0, 628.621}, Point3D{280572.744, 5592016.744, 628.64}, Point3D{280573.0, 5592016.438, 628.652}, Point3D{280573.199, 5592016.199, 628.668}, Point3D{280573.366, 5592016.0, 628.674}, Point3D{280573.655, 5592015.655, 628.677}, Point3D{280574.0, 5592015.241, 628.688}, Point3D{280574.11, 5592015.11, 628.689}, Point3D{280574.202, 5592015.0, 628.688}, Point3D{280574.565, 5592014.565, 628.697}, Point3D{280575.0, 5592014.044, 628.728}, Point3D{280575.02, 5592014.02, 628.729}, Point3D{280575.037, 5592014.0, 628.73}, Point3D{280575.475, 5592013.475, 628.756}, Point3D{280575.872, 5592013.0, 628.763}, Point3D{280575.93, 5592012.93, 628.761}, Point3D{280576.0, 5592012.847, 628.755}, Point3D{280576.386, 5592012.386, 628.73}, Point3D{280576.708, 5592012.0, 628.737}, Point3D{280576.841, 5592011.841, 628.74}, Point3D{280577.0, 5592011.65, 628.743}, Point3D{280577.296, 5592011.296, 628.747}, Point3D{280577.543, 5592011.0, 628.75}, Point3D{280577.751, 5592010.751, 628.748}, Point3D{280578.0, 5592010.453, 628.755}, Point3D{280578.206, 5592010.206, 628.75}, Point3D{280578.379, 5592010.0, 628.752}, Point3D{280578.661, 5592009.661, 628.754}, Point3D{280579.0, 5592009.256, 628.747}, Point3D{280579.117, 5592009.117, 628.742}, Point3D{280579.214, 5592009.0, 628.748}, Point3D{280579.572, 5592008.572, 628.766}, Point3D{280580.0, 5592008.059, 628.768}, Point3D{280580.027, 5592008.027, 628.768}, Point3D{280580.049, 5592008.0, 628.768}, Point3D{280580.482, 5592007.482, 628.736}, Point3D{280580.885, 5592007.0, 628.706}, Point3D{280580.937, 5592006.937, 628.703}, Point3D{280581.0, 5592006.862, 628.699}, Point3D{280581.392, 5592006.392, 628.682}, Point3D{280581.72, 5592006.0, 628.697}, Point3D{280581.848, 5592005.848, 628.7}, Point3D{280582.0, 5592005.665, 628.697}, Point3D{280582.303, 5592005.303, 628.681}, Point3D{280582.556, 5592005.0, 628.668}, Point3D{280582.758, 5592004.758, 628.665}, Point3D{280582.99, 5592004.48, 628.656},
// Point3D{280582.414, 5592004.0, 628.689}, Point3D{280582.0, 5592003.656, 628.703}, Point3D{280581.213, 5592003.0, 628.682}, Point3D{280581.0, 5592002.823, 628.689}, Point3D{280580.012, 5592002.0, 628.7}, Point3D{280580.0, 5592001.99, 628.7}, Point3D{280579.94, 5592001.94, 628.699}, Point3D{280579.0, 5592001.157, 628.692}, Point3D{280578.811, 5592001.0, 628.694}, Point3D{280578.0, 5592000.324, 628.696}, Point3D{280577.61, 5592000.0, 628.682}, Point3D{280577.0, 5591999.492, 628.68}, Point3D{280576.89, 5591999.4, 628.681},
// Point3D{280576.667, 5591999.667, 628.673}, Point3D{280576.389, 5592000.0, 628.731}, Point3D{280576.212, 5592000.212, 628.762}, Point3D{280576.0, 5592000.465, 628.756}, Point3D{280575.757, 5592000.757, 628.745}, Point3D{280575.553, 5592001.0, 628.736}, Point3D{280575.301, 5592001.301, 628.727}, Point3D{280575.0, 5592001.662, 628.723}, Point3D{280574.846, 5592001.846, 628.722}, Point3D{280574.718, 5592002.0, 628.723}, Point3D{280574.391, 5592002.391, 628.734}, Point3D{280574.0, 5592002.859, 628.739}, Point3D{280573.936, 5592002.936, 628.739}, Point3D{280573.883, 5592003.0, 628.739}, Point3D{280573.481, 5592003.481, 628.744}, Point3D{280573.047, 5592004.0, 628.76}, Point3D{280573.026, 5592004.026, 628.76}, Point3D{280573.0, 5592004.056, 628.761}, Point3D{280572.571, 5592004.571, 628.761}, Point3D{280572.212, 5592005.0, 628.731}, Point3D{280572.115, 5592005.115, 628.726}, Point3D{280572.0, 5592005.253, 628.728}, Point3D{280571.66, 5592005.66, 628.764}, Point3D{280571.376, 5592006.0, 628.794}, Point3D{280571.205, 5592006.205, 628.812}, Point3D{280571.0, 5592006.45, 628.815}, Point3D{280570.75, 5592006.75, 628.818}, Point3D{280570.541, 5592007.0, 628.81}, Point3D{280570.295, 5592007.295, 628.81}, Point3D{280570.0, 5592007.647, 628.816}, Point3D{280569.84, 5592007.84, 628.822}, Point3D{280569.706, 5592008.0, 628.823}, Point3D{280569.384, 5592008.384, 628.822}, Point3D{280569.0, 5592008.845, 628.822}, Point3D{280568.929, 5592008.929, 628.823}, Point3D{280568.87, 5592009.0, 628.827}, Point3D{280568.474, 5592009.474, 628.851}, Point3D{280568.035, 5592010.0, 628.869}, Point3D{280568.019, 5592010.019, 628.869}, Point3D{280568.0, 5592010.042, 628.871}, Point3D{280567.564, 5592010.564, 628.9}, Point3D{280567.199, 5592011.0, 628.9}, Point3D{280567.109, 5592011.109, 628.897}, Point3D{280567.0, 5592011.239, 628.912}, Point3D{280566.653, 5592011.653, 628.943}, Point3D{280566.364, 5592012.0, 628.918}, Point3D{280566.198, 5592012.198, 628.896}, Point3D{280566.0, 5592012.436, 628.891}, Point3D{280565.743, 5592012.743, 628.88}, Point3D{280565.528, 5592013.0, 628.889}, Point3D{280565.47, 5592013.07, 628.883}]