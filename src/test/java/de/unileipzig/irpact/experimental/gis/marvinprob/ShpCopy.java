package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.util.gis.*;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
class ShpCopy {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ShpCopy.class);

    @SuppressWarnings("PointlessArithmeticExpression")
    private static final long GB1 = 1L * 1024 * 1024 * 1024;

    private static final Path gisDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");
    private static final Path outDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Gis_Group_1");
    private static final Path og2ogr = Paths.get("D:\\QGIS 3.18\\bin", "ogr2ogr");
    private static final Path outDir3 = Paths.get("E:\\MyTemp\\Marvin-Daten\\output3");

    @Test
    void peekShp() throws IOException {
        IRPLogging.initConsole();

        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(gisDir);
        List<ShapeFiles> files1gb = allFiles.partAfterSize(GB1);
        int i = 0;
        for(ShapeFiles shapeFiles: files1gb) {
            Path subDir = outDir.resolve("_" + i);
            for(ShapeFile sf: shapeFiles.getFiles()) {
                ShapeFile target = sf.copyTo(subDir);
                LOGGER.debug("copy '{}' to '{}'", sf.getFileWithoutExtension(), target.getFileWithoutExtension());
            }
            return;
        }
    }

    @Test
    void testOgr() throws IOException, InterruptedException {
        Path input = gisDir.resolve("3D_LoD2_33280_5592_2_sn.shp");
        Path output = outDir3.resolve("3D_LoD2_33280_5592_2_sn.shp");

        ProcessResult pr = Ogr2Ogr.extractPolygon(og2ogr, input, output);
        if(pr.isError()) {
            System.out.println(pr.printErr(Util.windows1252()));
        }
    }
}
