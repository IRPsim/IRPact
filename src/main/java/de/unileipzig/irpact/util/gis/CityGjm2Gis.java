package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
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
import org.opengis.referencing.FactoryException;
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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CityGjm2Gis.class);

    private static final String GIS_KEY = "BldgRoot";
    private static final String GROUND = "Ground";
    private static final String ELEMENT = "Element";

    private CoordinateReferenceSystem gjmCRS;
    private CoordinateReferenceSystem gisCRS = DefaultGeographicCRS.WGS84;
    private MathTransform gjmToGis;
    private Charset dbfCharset = StandardCharsets.UTF_8;
    private boolean useLines = false;
    private boolean reverse = false;

    public CityGjm2Gis() {
    }

    public void setUseLines(boolean useLines) {
        this.useLines = useLines;
    }

    public void setGjmCRS(CoordinateReferenceSystem gjmCRS) {
        this.gjmCRS = gjmCRS;
    }

    public void setGisCRS(CoordinateReferenceSystem gisCRS) {
        this.gisCRS = gisCRS;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
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
            Map<String, MultiPolygon> out,
            Set<String> toSkip) throws CityGMLBuilderException, CityGMLReadException, TransformException, ParseException {
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

    private LineString toGisLineString(GeometryFactory fac, LineString ls) throws TransformException {
        LineString ret;
        if(gjmToGis == null) {
            ret = ls;
        } else {
            ret = (LineString) JTS.transform(ls, gjmToGis);
        }
        return reverse
                ? reverse(fac, ret)
                : ret;
    }

    private MultiPolygon toMultiPolygon(List<Point3DList> llist) throws ParseException, TransformException {
        List<Polygon> polygons = new ArrayList<>();
        GeometryFactory fac = JTSFactoryFinder.getGeometryFactory();
        for(Point3DList list: llist) {
            String lsStr = list.toLineString();
            LineString ls = (LineString) new WKTReader2(fac).read(lsStr);
            LineString gisLs = toGisLineString(fac, ls);
            Polygon polygon = fac.createPolygon(gisLs.getCoordinates());
            polygons.add(polygon);
        }
        return fac.createMultiPolygon(polygons.toArray(new Polygon[0]));
    }

    private Map<String, MultiLineString> parseGjmWithLines(
            Path gjmInput,
            Map<String, MultiLineString> out,
            Set<String> toSkip) throws CityGMLBuilderException, CityGMLReadException, TransformException, ParseException {
        CityGMLContext ctx = CityGMLContext.getInstance();
        CityGMLBuilder builder = ctx.createCityGMLBuilder();
        CityGMLInputFactory in = builder.createCityGMLInputFactory();
        CityGMLReader reader = in.createCityGMLReader(gjmInput.toFile());
        while(reader.hasNext()) {
            CityModel cityModel = (CityModel) reader.nextFeature();
            for(CityObjectMember member: cityModel.getCityObjectMember()) {
                Pair<String, MultiLineString> data = getLod2TerrainAsLines(member);
                if(data.getValue() == null) {
                    LOGGER.warn("[SKIP] no curve: {} {}", gjmInput.getFileName(), data.getKey());
                    toSkip.add(data.getKey());
                    continue;
                }
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
        if(curveProperty == null) {
            return new Pair<>(building.getId(), null);
        }
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
            LineString gisLs = toGisLineString(fac, ls);
            lines.add(gisLs);
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
        return out;
    }

    private void writeDbfFile(
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
                        LOGGER.trace("[dbf] skip '{}'", key);
                        continue;
                    }
                    writer.write(entry);
                }
            } finally {
                writer.close();
            }
        }
    }

    private void writeShapeFile(List<String> keys, Map<String, ? extends Geometry> gjmData, Path outShp) throws IOException, SchemaException {
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

    private void buildTransform() throws FactoryException {
        if(gisCRS == gjmCRS) {
            gjmToGis = null;
        } else {
            gjmToGis = CRS.findMathTransform(gjmCRS, gisCRS, true);
        }
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
        Map<String, ? extends Geometry> gjmData = useLines
                ? parseGjmWithLines(gjmInput, new LinkedHashMap<>(), toSkip)
                : parseGjm(gjmInput, new LinkedHashMap<>(), toSkip);
        List<String> orderedGis = parseGisOrder(gisDbfInput, new ArrayList<>());
        List<String> both = getSameKeys(orderedGis, gjmData.keySet());
        writeShapeFile(both, gjmData, outShp);
        writeDbfFile(new HashSet<>(both), gisDbfInput, outDbf, both.size());
    }
}