package de.unileipzig.irpact.util.gis;

import de.unileipzig.irptools.util.ProcessResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class Ogr2Ogr {

    public static ProcessResult extractPolygon(Path ogr2ogr, Path input, Path output) throws IOException, InterruptedException {
        String[] args = {
                ogr2ogr.toString(),
                "-f", "\"ESRI Shapefile\"",
                "-nlt", "POLYGON",
                output.toString(),
                input.toString()
        };
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(args);
        Process process = builder.start();
        return ProcessResult.waitFor(process);
    }

    public static ProcessResult runSql(Path ogr2ogr, Path input, Path output) throws IOException, InterruptedException {
        return runSql(ogr2ogr, input, output, "\"Element = 'Ground'\"");
    }

    public static ProcessResult runSql(Path ogr2ogr, Path input, Path output, String whereQuery) throws IOException, InterruptedException {
        String[] args = {
                ogr2ogr.toString(),
                "-f", "\"ESRI Shapefile\"",
                "-where", whereQuery,
                //"-sql", "\"SELECT * from input WHERE Element = 'Ground'\"",
                output.toString(),
                input.toString()
        };
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(args);
        Process process = builder.start();
        return ProcessResult.waitFor(process);
    }

    public static ProcessResult runSqlWithDistinct(Path ogr2ogr, Path input, Path output) throws IOException, InterruptedException {
        String inputFileName = input.getFileName().toString();
        String inputName = Gis.removeExtension(inputFileName);
        String[] args = {
                ogr2ogr.toString(),
                "-f", "\"ESRI Shapefile\"",
                "-sql", "\"SELECT DISTINCT BldgPart, Element from " + inputName + " WHERE Element = 'Ground'\"",
                output.toString(),
                input.toString()
        };
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(args);
        Process process = builder.start();
        return ProcessResult.waitFor(process);
    }

    public static String[] initMergeCmd(Path ogr2ogr, Path first, Path target) {
        return new String[] {
                ogr2ogr.toString(),
                "-f", "\"ESRI Shapefile\"",
                target.toString(),
                first.toString()
        };
    }

    public static String[] updateMergeCmd(Path ogr2ogr, Path next, Path target) {
        return new String[]{
                ogr2ogr.toString(),
                "-f", "\"ESRI Shapefile\"",
                "-update",
                "-append",
                target.toString(),
                next.toString()
        };
    }

    public static List<String[]> buildMergeCmd(Path ogr2ogr, Collection<? extends Path> input, Path target) {
        List<String[]> cmds = new ArrayList<>();
        for(Path source: input) {
            if(cmds.isEmpty()) {
                cmds.add(initMergeCmd(ogr2ogr, source, target));
            } else {
                cmds.add(updateMergeCmd(ogr2ogr, source, target));
            }
        }
        return cmds;
    }

    public static List<String[]> buildMergeCmd2(Path ogr2ogr, Collection<? extends ShapeFile> input, Path target) {
        List<String[]> cmds = new ArrayList<>();
        for(ShapeFile sf: input) {
            Path source = sf.shp();
            if(cmds.isEmpty()) {
                cmds.add(initMergeCmd(ogr2ogr, source, target));
            } else {
                cmds.add(updateMergeCmd(ogr2ogr, source, target));
            }
        }
        return cmds;
    }

    public static List<String[]> buildMergeCmdToDir(Path ogr2ogr, List<? extends ShapeFile> input, Path outDir) {
        String mergeName = input.get(0).getName()
                + "__"
                + input.get(input.size() - 1).getName()
                + ".shp";
        Path target = outDir.resolve(mergeName);
        List<String[]> cmds = new ArrayList<>();
        for(ShapeFile sf: input) {
            Path source = sf.shp();
            if(cmds.isEmpty()) {
                cmds.add(initMergeCmd(ogr2ogr, source, target));
            } else {
                cmds.add(updateMergeCmd(ogr2ogr, source, target));
            }
        }
        return cmds;
    }
}
