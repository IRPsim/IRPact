package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.util.gis.CityGjm2Gis;
import de.unileipzig.irpact.util.gis.Gis;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Daniel Abitz
 */
class RealX {


    private static final Path gjmDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_CityGML\\data");
    private static final Path gisDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");
    private static final Path outDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\output");

    private static Set<String> collectNames(Path dir) throws IOException {
        Set<String> set = new TreeSet<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path p: stream) {
                String fileName = p.getFileName().toString();
                if(fileName.endsWith(".csv")) {
                    continue;
                }
                String name = CityGjm2Gis.removeExtension(fileName);
                set.add(name);
            }
        }
        return set;
    }

    @Test
    void asd() throws Exception {
        Set<String> files = collectNames(gisDir);
        int i = 0;
        for(String name: files) {
            Path gjmFile = gjmDir.resolve(name + ".gml");
            Path gisFile = gisDir.resolve(name + ".dbf");
            Path outFile = outDir.resolve(name);

            System.out.println("convert '" + name + "' (" + (i+1) + "/" + files.size() + ")");

            CityGjm2Gis conv = new CityGjm2Gis();
            conv.setGjmCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
            conv.mergeToGis(gjmFile, gisFile, outFile);

            i++;
            if(i == 4) {
                break;
            }
        }
    }

    @Test
    void asd2() throws Exception {
        String name = "3D_LoD2_33278_5602_2_sn";
        Path gjmFile = gjmDir.resolve(name + ".gml");
        Path gisFile = gisDir.resolve(name + ".dbf");
        Path outFile = outDir.resolve(name);

        CityGjm2Gis conv = new CityGjm2Gis();
        conv.setGjmCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        conv.setUseLines(true);
        conv.mergeToGis(gjmFile, gisFile, outFile);
    }

    @Test
    void peek() throws IOException {
        Gis.peekDbf(outDir.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8);
        Gis.peekShp(outDir.resolve("3D_LoD2_33278_5602_2_sn.shp"));
    }
}
