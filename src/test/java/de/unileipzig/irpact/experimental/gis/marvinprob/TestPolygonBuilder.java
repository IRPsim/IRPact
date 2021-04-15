package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.util.gis.Point3D;
import de.unileipzig.irpact.util.gis.Point3DList;
import de.unileipzig.irpact.util.gis.PolygonBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestPolygonBuilder {

    /*
    6 5 4
    7   3
    0 1 2
     */
    @SuppressWarnings("Convert2MethodRef")
    @Test
    void testIt() {
        Point3D p0 = new Point3D(0);
        Point3D p1 = new Point3D(1);
        Point3D p2 = new Point3D(2);

        Point3D p3 = new Point3D(3);
        Point3D p4 = new Point3D(4);
        Point3D p5 = new Point3D(5);

        Point3D p6 = new Point3D(6);
        Point3D p7 = new Point3D(7);

        Point3DList pl0_2 = new Point3DList(p0, p1, p2);
        Point3DList pl0_6 = new Point3DList(p0, p7, p6);
        Point3DList pl4_2 = new Point3DList(p4, p3, p2);
        Point3DList pl4_6 = new Point3DList(p4, p5, p6);

        List<Point3DList> list = CollectionUtil.arrayListOf(pl0_2, pl0_6, pl4_2, pl4_6);
        List<Point3DList> polys = PolygonBuilder.getPolygons(list);
        polys.forEach(pp -> System.out.println(pp));
    }

    /*
    6 5 4  15 14 13
    7   3  16    12
    0 1 2   4 10 11
     */
    @SuppressWarnings("Convert2MethodRef")
    @Test
    void testIt2() {
        Point3D p0 = new Point3D(0);
        Point3D p1 = new Point3D(1);
        Point3D p2 = new Point3D(2);
        Point3D p3 = new Point3D(3);
        Point3D p4 = new Point3D(4);
        Point3D p5 = new Point3D(5);
        Point3D p6 = new Point3D(6);
        Point3D p7 = new Point3D(7);

        Point3D p10 = new Point3D(10);
        Point3D p11 = new Point3D(11);
        Point3D p12 = new Point3D(12);
        Point3D p13 = new Point3D(13);
        Point3D p14 = new Point3D(14);
        Point3D p15 = new Point3D(15);
        Point3D p16 = new Point3D(16);

        Point3DList pl0_2 = new Point3DList(p0, p1, p2);
        Point3DList pl0_6 = new Point3DList(p0, p7, p6);
        Point3DList pl4_2 = new Point3DList(p4, p3, p2);
        Point3DList pl4_6 = new Point3DList(p4, p5, p6);

        Point3DList pl4_11 = new Point3DList(p4, p10, p11);
        Point3DList pl4_15 = new Point3DList(p4, p16, p15);
        Point3DList pl15_13 = new Point3DList(p15, p14, p13);
        Point3DList pl11_13 = new Point3DList(p11, p12, p13);

        List<Point3DList> list = CollectionUtil.arrayListOf(pl0_2, pl0_6, pl4_2, pl4_6, pl4_11, pl4_15, pl15_13, pl11_13);
        List<Point3DList> polys = PolygonBuilder.getPolygons(list);
        polys.forEach(pp -> System.out.println(pp));
    }

    /*
    6 5 4  4 14 13
    7   3  3    12
    0 1 2  2 10 11
     */
    @SuppressWarnings("Convert2MethodRef")
    @Test
    void testIt3() {
        Point3D p0 = new Point3D(0);
        Point3D p1 = new Point3D(1);
        Point3D p2 = new Point3D(2);
        Point3D p3 = new Point3D(3);
        Point3D p4 = new Point3D(4);
        Point3D p5 = new Point3D(5);
        Point3D p6 = new Point3D(6);
        Point3D p7 = new Point3D(7);

        Point3D p10 = new Point3D(10);
        Point3D p11 = new Point3D(11);
        Point3D p12 = new Point3D(12);
        Point3D p13 = new Point3D(13);
        Point3D p14 = new Point3D(14);

        Point3DList pl0_2 = new Point3DList(p0, p1, p2);
        Point3DList pl0_6 = new Point3DList(p0, p7, p6);
        Point3DList pl4_2 = new Point3DList(p4, p3, p2);
        Point3DList pl4_6 = new Point3DList(p4, p5, p6);

        Point3DList pl2_11 = new Point3DList(p2, p10, p11);
        Point3DList pl11_13 = new Point3DList(p11, p12, p13);
        Point3DList pl4_13 = new Point3DList(p4, p14, p13);

        List<Point3DList> list = CollectionUtil.arrayListOf(pl0_2, pl0_6, pl4_2, pl4_6, pl2_11, pl4_13, pl11_13);
        List<Point3DList> polys = PolygonBuilder.getPolygons(list);
        polys.forEach(pp -> System.out.println(pp));
    }

    /*
        100
    6 5 4  4 14 13
    7   3  3    12
    0 1 2  2 10 11
     */
    @SuppressWarnings("Convert2MethodRef")
    @Test
    void testIt4() {
        Point3D p0 = new Point3D(0);
        Point3D p1 = new Point3D(1);
        Point3D p2 = new Point3D(2);
        Point3D p3 = new Point3D(3);
        Point3D p4 = new Point3D(4);
        Point3D p5 = new Point3D(5);
        Point3D p6 = new Point3D(6);
        Point3D p7 = new Point3D(7);

        Point3D p10 = new Point3D(10);
        Point3D p11 = new Point3D(11);
        Point3D p12 = new Point3D(12);
        Point3D p13 = new Point3D(13);
        Point3D p14 = new Point3D(14);

        Point3D p100 = new Point3D(100);

        Point3DList pl0_2 = new Point3DList(p0, p1, p2);
        Point3DList pl0_6 = new Point3DList(p0, p7, p6);
        Point3DList pl4_2 = new Point3DList(p4, p3, p2);
        Point3DList pl4_6 = new Point3DList(p4, p5, p6);

        Point3DList pl2_11 = new Point3DList(p2, p10, p11);
        Point3DList pl11_13 = new Point3DList(p11, p12, p13);
        Point3DList pl4_13 = new Point3DList(p4, p14, p13);

        Point3DList pl6_100 = new Point3DList(p6, p100);
        Point3DList pl13_100 = new Point3DList(p13, p100);

        List<Point3DList> list = CollectionUtil.arrayListOf(pl0_2, pl0_6, pl4_2, pl4_6, pl2_11, pl4_13, pl11_13, pl6_100, pl13_100);
        List<Point3DList> polys = PolygonBuilder.getPolygons(list);
        polys.forEach(pp -> System.out.println(pp));
    }
}
