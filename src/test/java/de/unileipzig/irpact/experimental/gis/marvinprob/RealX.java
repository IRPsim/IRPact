package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.util.gis.CityGjm2Gis;
import de.unileipzig.irpact.util.gis.Gis;
import de.unileipzig.irpact.util.gis.ShapeFile;
import de.unileipzig.irpact.util.gis.ShapeFiles;
import org.geotools.feature.SchemaException;
import org.geotools.referencing.CRS;
import org.junit.jupiter.api.Test;
import org.opengis.referencing.FactoryException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("SameParameterValue")
class RealX {

    private static final long MB100 = 100L * 1024 * 1024;
    private static final long GB2 = 2L * 1024 * 1024 * 1024;

    private static final Path gjmDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_CityGML\\data");
    private static final Path gisDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");
    private static final Path outDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\output");
    private static final Path outDir0 = outDir.resolve("part0");
    private static final Path outDir1 = outDir.resolve("part1");
    private static final Path outDir2 = outDir.resolve("part2");
    private static final Path outDir3 = outDir.resolve("part3");
    private static final Path outDir4 = outDir.resolve("part4");
    private static final Path outDir5 = outDir.resolve("part5");
    private static final Path outDir6 = outDir.resolve("part6");
    private static final Path outDir7 = outDir.resolve("part7");
    private static final Path outDir8 = outDir.resolve("part8");
    private static final Path outDir9 = outDir.resolve("part9");
    private static final Path mergedDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\output\\merged");
    private static final Path mergedDir1 = Paths.get("E:\\MyTemp\\Marvin-Daten\\output\\merged_p1");
    private static final Path mergedDir2 = Paths.get("E:\\MyTemp\\Marvin-Daten\\output\\merged_p2");
    private static final Path mergedDirFinal = Paths.get("E:\\MyTemp\\Marvin-Daten\\output\\merged_final");

    private static final List<Path> outDirParts = CollectionUtil.arrayListOf(
            outDir0,
            outDir1,
            outDir2,
            outDir3,
            outDir4,
            outDir5,
            outDir6,
            outDir7,
            outDir8,
            outDir9
    );

    @SuppressWarnings("SameParameterValue")
    private static Set<String> collectNames(Path dir) throws IOException {
        Set<String> set = new TreeSet<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for(Path p: stream) {
                String fileName = p.getFileName().toString();
                if(fileName.endsWith(".csv")) {
                    continue;
                }
                String name = Gis.removeExtension(fileName);
                set.add(name);
            }
        }
        return set;
    }

    private Path getOutDir(int index) {
        if(index < 500) return outDir0;
        if(index < 1000) return outDir1;
        if(index < 1500) return outDir2;
        if(index < 2000) return outDir3;
        if(index < 2500) return outDir4;
        if(index < 3000) return outDir5;
        if(index < 3500) return outDir6;
        if(index < 4000) return outDir7;
        if(index < 4500) return outDir8;
        return outDir9;
    }

    //TODO 3D_LoD2_33436_5638_2_sn > DESNATP11N0002LK
    @Test
    void testConvertSingle() throws Exception {
        convert(outDir, "3D_LoD2_33436_5638_2_sn", 0, 0, new MutableBoolean(false));
    }


    @Test
    void convertParallel() throws Exception {
        convertParallel(6);
    }

    void convertParallel(int cores) throws Exception {
        Set<String> files = collectNames(gisDir);
        List<Callable<Void>> tasks = new ArrayList<>();
        List<Exception> errors = new ArrayList<>();
        MutableBoolean error = new MutableBoolean(false);

        int i = 0;
        int min = 3787;
        int max = files.size();
        for(String name: files) {
            if(i >= min) {
                final int ii = i;
                Callable<Void> task = () -> {
                    try {
                        convert(getOutDir(ii), name, ii, max, error);
                    } catch (IllegalArgumentException e) {
                        errors.add(new RuntimeException("[IGNORED] name", e));
                    } catch (Exception e) {
                        errors.add(new RuntimeException(name, e));
                        error.set(true);
                    }

                    if(ii % 100 == 0) {
                        System.gc();
                    }

                    return null;
                };
                tasks.add(task);
            }

            i++;
            if(i == max) {
                break;
            }
        }

        System.out.println("run " + tasks.size() + " tasks");

        ExecutorService exec = Executors.newFixedThreadPool(cores);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }

        System.out.println("errors: " + errors.size());
        for(Exception e: errors) {
            e.printStackTrace();
        }
    }

    void convert(Path outDir, String name, int i, int max, MutableBoolean error) throws Exception {
        if(error.get()) {
            return;
        }

        Path gjmFile = gjmDir.resolve(name + ".gml");
        Path gisFile = gisDir.resolve(name + ".dbf");
        Path outFile = outDir.resolve(name);

        System.out.println("convert '" + name + "' (" + (i+1) + "/" + max + ")");

        CityGjm2Gis conv = new CityGjm2Gis();
        conv.setUseLines(true);
        conv.setReverse(false);
        conv.setGjmCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        conv.setGisCRS(CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"));
        conv.mergeToGis(gjmFile, gisFile, outFile);

        if(i % 100 == 0) {
            System.gc();
        }
    }

    @Test
    void asdSingle() throws Exception {
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

    @Test
    void peek2() throws IOException {
        Gis.peekAllDbf(outDir.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8);
        Gis.peekAllShp(outDir.resolve("3D_LoD2_33278_5602_2_sn.shp"));
    }

    @Test
    void peek3() throws IOException {
        Gis.peekAllDbf(outDir0.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(outDir0.resolve("3D_LoD2_33278_5602_2_sn.shp"), true);
    }

    @Test
    void peek333() throws IOException {
        Gis.peekAllDbf(mergedDir1.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33294_5588_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(mergedDir1.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33294_5588_2_sn.shp"), true);
    }

    @Test
    void hmm() {
        long x = Integer.MAX_VALUE;
        System.out.println(x - (2216757L * 1024));
    }

    @Test
    void peek4() throws IOException {
        List<Path> ps = Gis.getAllFiles(outDir7, ".shp");
        for(Path p: ps) {
            Gis.peekAllShp(p, true);
        }
    }

    @Test
    void peek5() throws IOException {
        List<Path> ps = new ArrayList<>();
        for(Path p: outDirParts) {
            ps.addAll(Gis.getAllFiles(p, ".shp"));
        }
        for(Path p: ps) {
            System.out.println("peek: " + p);
            Gis.peekAllShp(p, true);
        }
    }

    @Test
    void checkFilesPart() throws IOException {
        ShapeFiles shapeFiles = new ShapeFiles();
        shapeFiles.addAll(mergedDir1);
        List<ShapeFiles> parts = shapeFiles.partAfterSize(2 * 1024L * 1024 * 1024);
        for(ShapeFiles part: parts) {
            System.out.println(part.maxSize() + " " + part.count());
        }
    }

    @Test
    void testMerge123() throws IOException, ExecutionException, InterruptedException, SchemaException, FactoryException {
        testMerge123(7);
    }

    void testMerge123(int cores) throws IOException, ExecutionException, InterruptedException, SchemaException, FactoryException {
        mergePhase1Parallel(cores);
        mergePhase2Parallel(cores);
        mergePhaseFinal();
    }

//    @Test
//    void mergePhase1() throws IOException, FactoryException, SchemaException {
//        List<Path> shpFiles = new ArrayList<>();
//        for(Path p: outDirParts) {
//            shpFiles.addAll(Gis.getAllFiles(p, ".shp"));
//        }
//        int blockSize = 50;
//        for(int i = 0; i < shpFiles.size(); i+= blockSize) {
//            List<Path> part = shpFiles.stream()
//                    .skip(i)
//                    .limit(blockSize)
//                    .collect(Collectors.toList());
//
//            Gis.mergeShapeFiles(
//                    part,
//                    CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
//                    mergedDir1.resolve("merged_1_" + i + ".shp")
//            );
//            System.gc();
//        }
//    }

    @Test
    void mergePhase1Parallel() throws IOException, InterruptedException, ExecutionException {
        mergePhase1Parallel(1);
    }

    void mergePhase1Parallel(int cores) throws IOException, InterruptedException, ExecutionException {
        System.out.println("merge phase 1");

        List<Path> shpFiles = new ArrayList<>();
        List<Path> dbfFiles = new ArrayList<>();
        for(Path p: outDirParts) {
            shpFiles.addAll(Gis.getAllFiles(p, ".shp"));
            dbfFiles.addAll(Gis.getAllFiles(p, ".dbf"));
        }
        int blockSize = 50;
        List<Callable<Void>> tasks = new ArrayList<>();
        for(int i = 0; i < shpFiles.size(); i+= blockSize) {
            int ii = i;
            Callable<Void> task = () -> {
                List<Path> shpPart = shpFiles.stream()
                        .skip(ii)
                        .limit(blockSize)
                        .collect(Collectors.toList());

                List<Path> dbfPart = dbfFiles.stream()
                        .skip(ii)
                        .limit(blockSize)
                        .collect(Collectors.toList());

                Gis.mergeShapeFiles(
                        shpPart,
                        CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
                        mergedDir1.resolve("merged_1_" + ii + ".shp")
                );

                Gis.mergeDbfFiles(
                        dbfPart,
                        StandardCharsets.UTF_8,
                        mergedDir1.resolve("merged_1_" + ii + ".dbf")
                );

                System.gc();
                return null;
            };
            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(cores);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }

//    @Test
//    void mergePhase2() throws IOException, SchemaException, FactoryException {
//        List<Path> shpFiles = Gis.getAllFiles(mergedDir1, ".shp");
//        int blockSize = 20;
//        for(int i = 0; i < shpFiles.size(); i+= blockSize) {
//            List<Path> part = shpFiles.stream()
//                    .skip(i)
//                    .limit(blockSize)
//                    .collect(Collectors.toList());
//
//            Gis.mergeShapeFiles(
//                    part,
//                    CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
//                    mergedDir2.resolve("merged_2_" + i + ".shp")
//            );
//            System.gc();
//        }
//    }

    //575251

    @Test
    void mergePhase2Parallel() throws IOException, InterruptedException, ExecutionException {
        mergePhase2Parallel(1);
    }

    void mergePhase2Parallel(int cores) throws IOException, InterruptedException, ExecutionException {
        System.out.println("merge phase 2");

        List<Path> shpFiles = Gis.getAllFiles(mergedDir1, ".shp");
        List<Path> dbfFiles = Gis.getAllFiles(mergedDir1, ".dbf");
        int blockSize = 20;
        List<Callable<Void>> tasks = new ArrayList<>();
        for(int i = 0; i < shpFiles.size(); i+= blockSize) {
            int ii = i;
            Callable<Void> task = () -> {
                List<Path> shpPart = shpFiles.stream()
                        .skip(ii)
                        .limit(blockSize)
                        .collect(Collectors.toList());

                List<Path> dbfPart = dbfFiles.stream()
                        .skip(ii)
                        .limit(blockSize)
                        .collect(Collectors.toList());

                Gis.mergeShapeFiles3(
                        shpPart,
                        CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
                        mergedDir2.resolve("merged_2_" + ii + ".shp")
                );

                Gis.mergeDbfFiles(
                        dbfPart,
                        StandardCharsets.UTF_8,
                        mergedDir2.resolve("merged_2_" + ii + ".dbf")
                );

                System.gc();
                return null;
            };
            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(cores);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }

    @Test
    void mergePhaseFinal() throws IOException, SchemaException, FactoryException {
        System.out.println("final merge");

        List<Path> shpFiles = Gis.getAllFiles(mergedDir2, ".shp");
        List<Path> dbfFiles = Gis.getAllFiles(mergedDir2, ".dbf");

        Gis.mergeShapeFiles(
                shpFiles,
                CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
                mergedDirFinal.resolve("merged_final.shp")
        );

        Gis.mergeDbfFiles(
                dbfFiles,
                StandardCharsets.UTF_8,
                mergedDirFinal.resolve("merged_final.dbf")
        );
    }

    @Test
    void mergeDbf() throws IOException {
        List<Path> dbfFiles = new ArrayList<>();
        for(Path p: outDirParts) {
            dbfFiles.addAll(Gis.getAllFiles(p, ".dbf"));
        }
        Gis.mergeDbfFiles(
                dbfFiles,
                StandardCharsets.UTF_8,
                mergedDirFinal.resolve("merged_final.dbf")
        );
    }

    @Test
    void mergeDbfPhase1() throws IOException {
        List<Path> dbfFiles = new ArrayList<>();
        for(Path p: outDirParts) {
            dbfFiles.addAll(Gis.getAllFiles(p, ".dbf"));
        }
        Gis.mergeDbfFiles(
                dbfFiles,
                StandardCharsets.UTF_8,
                mergedDirFinal.resolve("merged_final.dbf")
        );
    }

    @Test
    void funWithSizes() throws IOException {
        ShapeFiles shapeFiles = new ShapeFiles();
        shapeFiles.addAll(outDirParts);
        System.out.println("total: " + shapeFiles.count());

        List<ShapeFiles> part0 = shapeFiles.partAfterSize(GB2);
        System.out.println(part0.size());

        List<ShapeFiles> part1 = shapeFiles.sortAfterMaxSize().partAfterSize(GB2);
        System.out.println(part1.size());
    }

    @Test
    void mergeAfterSize() throws IOException, ExecutionException, InterruptedException {
        mergeAfterSize(
                CollectionUtil.arrayListOf(outDir0),
                mergedDir1,
                MB100,
                6,
                1
        );
    }

    void mergeAfterSize(
            Collection<? extends Path> dirs,
            Path outDir,
            long size,
            int cores,
            int maxTasks) throws IOException, InterruptedException, ExecutionException {
        ShapeFiles shapeFiles = new ShapeFiles();
        shapeFiles.addAll(dirs);
        List<ShapeFiles> parts = shapeFiles.partAfterSize(size);
        List<Callable<Void>> tasks = new ArrayList<>();
        MutableBoolean err = new MutableBoolean(false);

        for(ShapeFiles part: parts) {
            Callable<Void> task = () -> {
                if(err.get()) {
                    return null;
                }

                String name = part.getFirst().getName() + "__" + part.getLast().getName();
                ShapeFile outFile = new ShapeFile(outDir, name);
                Gis.mergeAll(
                        part,
                        CRS.decode("urn:ogc:def:crs:EPSG:6.12:25833"),
                        StandardCharsets.UTF_8,
                        outFile
                );
                return null;
            };
            tasks.add(task);
            if(tasks.size() >= maxTasks) {
                break;
            }
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(cores);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }
}
