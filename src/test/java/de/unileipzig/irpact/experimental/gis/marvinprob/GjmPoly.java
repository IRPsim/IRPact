package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.util.gis.*;
import org.citygml4j.builder.jaxb.CityGMLBuilderException;
import org.citygml4j.xml.io.reader.CityGMLReadException;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.linemerge.LineMerger;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Disabled
class GjmPoly {

    private static final Path gjmDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_CityGML\\data");
    private static final Path gisDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");
    private static final Path outDir2 = Paths.get("E:\\MyTemp\\Marvin-Daten\\output2");

    @Test
    void testIt() throws CityGMLBuilderException, CityGMLReadException {
        Path file = gjmDir.resolve("3D_LoD2_33278_5602_2_sn.gml");
        Map<String, List<Point3DList>> data = CityGjm2Gis.getAllLod2Terrains(file);
        List<Point3DList> list3D = data.get("DESNATPU1000Eg5v");
        List<Point2DList> list2D = list3D.stream().map(Point3DList::to2D).collect(Collectors.toList());

        GeometryFactory fac = new GeometryFactory();

        LineMerger merger = new LineMerger();
        for(Point2DList list: list2D) {
            LineString ls = Gis.toLineString(fac, list);
            merger.add(ls);
        }
        Collection<?> result = merger.getMergedLineStrings();
        for(Object obj: result) {
            System.out.println(obj);
        }
    }

    @Test
    void testSingleFile() throws Exception {
        Path gjm = gjmDir.resolve("3D_LoD2_33278_5602_2_sn.gml");
        Path dbf = gisDir.resolve("3D_LoD2_33278_5602_2_sn.dbf");
        Path out = outDir2.resolve("3D_LoD2_33278_5602_2_sn__out.dbf");

        CityGjm2GisMerger g2g = new CityGjm2GisMerger();
        g2g.setGjmCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        g2g.setGisCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        g2g.mergeToGis(gjm, dbf, out);
    }

    @Test
    void peekShp() throws IOException {
        //1745345
        Path dir = Paths.get("C:\\Users\\daniel\\Desktop\\gis");
        //Gis.peekAllDbf(dir.resolve("erstes1gb.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(dir.resolve("erstes1gb.shp"), true);
    }

    @Test
    void peekShp2() throws IOException {
        //1745345
        Path dir = Paths.get("E:\\MyTemp\\Marvin-Daten\\output3");
        Gis.peekAllDbf(dir.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(dir.resolve("3D_LoD2_33278_5602_2_sn.shp"), true);
    }
}
