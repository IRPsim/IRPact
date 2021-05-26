package de.unileipzig.irpact.util.gis;

import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.core.CityObjectMember;
import org.citygml4j.model.gml.base.CoordinateListProvider;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurve;
import org.citygml4j.model.gml.geometry.aggregates.MultiCurveProperty;
import org.citygml4j.model.gml.geometry.primitives.CurveProperty;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.linemerge.LineMerger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class CityGjm2GisMerger extends CityGjm2GisBase {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CityGjm2GisMerger.class);

    protected GeometryFactory fac = new GeometryFactory();

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Pair<String, ? extends Geometry> getLod2Terrain(CityObjectMember member) {
        Building building = (Building) member.getCityObject();
        List<Point2DList> lists2D = getLod2TerrainAs2DList(member);

        LineMerger merger = new LineMerger();
        for(Point2DList list2D: lists2D) {
            LineString ls = Gis.toLineString(fac, list2D);
            merger.add(ls);
        }

        List<Polygon> polygons = new ArrayList<>();
        Collection<LineString> merged = merger.getMergedLineStrings();
        for(LineString ls: merged) {
            LinearRing ring = fac.createLinearRing(ls.getCoordinateSequence());
            Polygon polygon = fac.createPolygon(ring);
            polygons.add(polygon);
        }

        MultiPolygon multiPolygon = fac.createMultiPolygon(polygons.toArray(new Polygon[0]));

        return new Pair<>(building.getId(), multiPolygon);
    }

    //=========================
    //statif
    //=========================

    protected static List<Point3D> toPoint3D(List<Double> doubleList) {
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

    protected static List<Point2DList> getLod2TerrainAs2DList(CityObjectMember member) {
        Building building = (Building) member.getCityObject();
        MultiCurveProperty curveProperty = building.getLod2TerrainIntersection();
        MultiCurve curve = curveProperty.getMultiCurve();
        return curve.getCurveMember()
                .stream()
                .map(CurveProperty::getCurve)
                .map(CoordinateListProvider::toList3d)
                .map(CityGjm2GisMerger::toPoint3D)
                .map(Point3DList::new)
                .map(Point3DList::to2D)
                .collect(Collectors.toList());
    }
}
