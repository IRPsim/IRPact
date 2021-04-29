package de.unileipzig.irpact.experimental.gis.marvinprob;

import de.unileipzig.irpact.commons.util.data.DataCounter;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.util.gis.*;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Daniel Abitz
 */
@Disabled
class Ogr2OgrTest {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Ogr2OgrTest.class);

    private static final Path gisDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\LoD2_Shape\\data");
    private static final Path outDir = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned");
    private static final Path outDirMerged = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Merges");
    private static final Path outDirMerged2 = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Merges_2GB");
    private static final Path outDirSql = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Sql");
    private static final Path outDirSqlMerged2 = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Sql_Merges_2GB");
    private static final Path outDirSqlSpecial = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Sql_Special");
    private static final Path outDirSqlSpecial2 = Paths.get("E:\\MyTemp\\Marvin-Daten\\Lod2_Shape_Cleaned_Sql_Special_2GB");
    private static final Path og2ogr = Paths.get("D:\\QGIS 3.18\\bin", "ogr2ogr");

    private static final long MB100 = 100L * 1024 * 1024;
    @SuppressWarnings("PointlessArithmeticExpression")
    private static final long GB1 = 1L * 1024 * 1024 * 1024;
    private static final long GB2 = 2L * 1024 * 1024 * 1024;
    private static final long GB2X = 2L * 1024 * 1024 * 1024 - MB100;

    private static class Counter {

        private int c = 0;

        private synchronized int inc() {
            return c++;
        }
    }

    @Test
    void extractPolygon() throws IOException, InterruptedException, ExecutionException {
        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(gisDir);
        MutableBoolean err = new MutableBoolean(false);

        final Counter c = new Counter();
        List<Callable<Void>> tasks = new ArrayList<>();
        for(ShapeFile sf: allFiles.getFiles()) {
            final ShapeFile source = sf;
            final ShapeFile target = sf.changeDir(outDir);
            Callable<Void> task = () -> {
                try {
                    if(err.get()) {
                        return null;
                    }

                    ProcessResult pr = Ogr2Ogr.extractPolygon(og2ogr, source.shp(), target.shp());
                    if(pr.isError()) {
                        LOGGER.error("error @ {}", source.getFileWithoutExtension());
                        err.set(true);
                    } else {
                        LOGGER.trace("finished {} / {}", (c.inc() + 1), tasks.size());
                    }
                } catch (Exception e) {
                    LOGGER.error("error @ " + source.getFileWithoutExtension(), e);
                    err.set(true);
                }
                return null;
            };

            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(6);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }

    protected static Callable<Void> buildTask(List<String[]> cmds, MutableBoolean err) {
        return () -> {
            int i = 0;
            for(String[] cmd: cmds) {
                if(err.get()) {
                    return null;
                }
                try {
                    LOGGER.debug("call '{}' ({} / {})", Arrays.toString(cmd), i++, cmds.size());
                    ProcessBuilder builder = new ProcessBuilder();
                    builder.command(cmd);
                    Process process = builder.start();
                    ProcessResult result = ProcessResult.waitFor(process);

                    if(result.isError()) {
                        LOGGER.error("error @ {}", Arrays.toString(cmd));
                        err.set(true);
                    }
                } catch (Exception e) {
                    LOGGER.error("error @ " + Arrays.toString(cmd), e);
                    err.set(true);
                }
            }
            return null;
        };
    }

    @Test
    void testSingleMerge() throws IOException, InterruptedException, ExecutionException {
        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(outDir);
        List<ShapeFile> files = allFiles.limit(10);
        List<String[]> cmds = Ogr2Ogr.buildMergeCmdToDir(og2ogr, files, outDirMerged);

        MutableBoolean err = new MutableBoolean(false);
        Callable<Void> task = buildTask(cmds, err);

        ExecutorService exec = Executors.newFixedThreadPool(6);
        List<Future<Void>> futures = exec.invokeAll(Collections.singleton(task));
        for(Future<Void> future: futures) {
            future.get();
        }
    }

    @Test
    void merge1GB() throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "multimerge.log"));
        runIt(outDir, outDirMerged, GB1);
    }

    @Test
    void merge2GBX() throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "multimerge2.log"));
        LOGGER.info("=== merge GB2X ===");
        runIt(outDir, outDirMerged2, GB2X);
    }

    @Test
    void mergeSql2GBX() throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "merge-sql-2gb.log"));
        LOGGER.info("=== merge GB2X ===");
        runIt(outDirSql, outDirSqlMerged2, GB2X);
    }

    @Test
    void merge1GBAnd2GB() throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "multimerge.log"));
        LOGGER.info("=== merge 1GB ===");
        runIt(outDir, outDirMerged, GB1);
        LOGGER.info("=== merge 2GB ===");
        runIt(outDir, outDirMerged2, GB2);
    }

    void runIt(Path inputDir, Path outDir, long size) throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "multimerge.log"));

        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(inputDir);

        List<Callable<Void>> tasks = new ArrayList<>();
        MutableBoolean err = new MutableBoolean(false);
        List<ShapeFiles> grouped2GB = allFiles.partAfterSize(size);
        for(ShapeFiles group: grouped2GB) {
            List<String[]> cmds = Ogr2Ogr.buildMergeCmdToDir(og2ogr, group.getFiles(), outDir);
            Callable<Void> task = buildTask(cmds, err);
            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(5);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }

    @Test
    void runSingleSql() throws IOException, InterruptedException {
        ProcessResult result = Ogr2Ogr.runSql(
                og2ogr,
                outDir.resolve("3D_LoD2_33278_5602_2_sn.shp"),
                outDirSql.resolve("3D_LoD2_33278_5602_2_sn.shp")
        );
        if(result.isError()) {
            System.out.println(result.printErr(Util.windows1252()));
        }
    }

    @Test
    void runSingleSqlDistrinct() throws IOException, InterruptedException {
        IRPLogging.initConsole();
        Path test = Paths.get("E:\\MyTemp\\Marvin-Daten\\Test");
        ProcessResult result = Ogr2Ogr.runSqlWithDistinct(
                og2ogr,
                test.resolve("X3D_LoD2_33278_5602_2_sn.shp"),
                test.resolve("Z3D_LoD2_33278_5602_2_sn.shp")
        );
        if(result.isError()) {
            System.out.println(result.printErr(Util.windows1252()));
        }
    }

    @Test
    void peekSqlDistinct() throws IOException {
        Path test = Paths.get("E:\\MyTemp\\Marvin-Daten\\Test");
        Gis.peekAllDbf(test.resolve("Z3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8);
    }

    @Test
    void runAllSql() throws IOException, InterruptedException, ExecutionException {
        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(outDir);
        MutableBoolean err = new MutableBoolean(false);

        final Counter c = new Counter();
        List<Callable<Void>> tasks = new ArrayList<>();
        for(ShapeFile sf: allFiles.getFiles()) {
            final ShapeFile source = sf;
            final ShapeFile target = sf.changeDir(outDirSql);
            Callable<Void> task = () -> {
                try {
                    if(err.get()) {
                        return null;
                    }

                    ProcessResult pr = Ogr2Ogr.runSql(og2ogr, source.shp(), target.shp());
                    if(pr.isError()) {
                        LOGGER.error("error @ {}", source.getFileWithoutExtension());
                        err.set(true);
                    } else {
                        LOGGER.trace("finished {} / {}", (c.inc() + 1), tasks.size());
                    }
                } catch (Exception e) {
                    LOGGER.error("error @ " + source.getFileWithoutExtension(), e);
                    err.set(true);
                }
                return null;
            };

            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(6);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }
    }

    @Test
    void peekShp() throws IOException {
        Gis.peekAllDbf(outDir.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(outDir.resolve("3D_LoD2_33278_5602_2_sn.shp"), true);
    }

    @Test
    void peekShpSql() throws IOException {
        Gis.peekAllDbf(outDirSql.resolve("3D_LoD2_33278_5602_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(outDirSql.resolve("3D_LoD2_33278_5602_2_sn.shp"), true);
    }

    @Test
    void peekShpSqlSpecial2() throws IOException {
        Gis.peekAllDbf(outDirSqlSpecial2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33502_5682_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(outDirSqlSpecial2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33502_5682_2_sn.shp"), true);
    }

    @Test
    void peekShp2GB() throws IOException {
        Gis.peekAllDbf(outDirMerged2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33352_5604_2_sn.dbf"), StandardCharsets.UTF_8, true);
        Gis.peekAllShp(outDirMerged2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33352_5604_2_sn.shp"), true);
    }

    @Test
    void checkAllKeys() throws IOException {
        Set<String> noSqlValues = new HashSet<>();
        Gis.selectAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33352_5604_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                noSqlValues
        );
        System.out.println("elements: " + noSqlValues.size());

        Gis.selectAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33352_5606_2_sn__3D_LoD2_33444_5654_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                noSqlValues
        );
        System.out.println("elements: " + noSqlValues.size());

        Gis.selectAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33444_5656_2_sn__3D_LoD2_33502_5682_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                noSqlValues
        );
        System.out.println("elements: " + noSqlValues.size());


        Set<String> sqlValues = new HashSet<>();
        Gis.selectAllFromDbf(
                outDirSqlSpecial2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33502_5682_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                sqlValues
        );
        System.out.println("elements: " + sqlValues.size());

        System.out.println("noSql: " + noSqlValues.size());
        System.out.println("sql: " + sqlValues.size());
        System.out.println("all in noSql? " + noSqlValues.containsAll(sqlValues));
        System.out.println("all in sql? " + sqlValues.containsAll(noSqlValues));
        System.out.println("equals? " + noSqlValues.equals(sqlValues));
    }

    /*
elements: 1145601
elements: 2286134
elements: 2682076
elements: 2681178
noSql: 2682076
sql: 2681178
all in noSql? true
all in sql? false
equals? false
     */

    @Test
    void countAllKeys() throws IOException {
        DataCounter<String> counter = new DataCounter<>();
        Gis.countAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33352_5604_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "Element",
                counter
        );
        System.out.println("elements: " + counter.total());

        Gis.countAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33352_5606_2_sn__3D_LoD2_33444_5654_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "Element",
                counter
        );
        System.out.println("elements: " + counter.total());

        Gis.countAllFromDbf(
                outDirMerged2.resolve("3D_LoD2_33444_5656_2_sn__3D_LoD2_33502_5682_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "Element",
                counter
        );
        System.out.println("elements: " + counter.total());
        counter.forEach((k, c) -> System.out.println(k + ": " + c));
    }

    @Test
    void extractAllKeys() throws IOException {
        Map<String, Collection<String>> data = new LinkedHashMap<>();
        Gis.selectAllFromDbfAsList(
                outDirMerged2.resolve("3D_LoD2_33278_5590_2_sn__3D_LoD2_33352_5604_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                "Element",
                data,
                LinkedHashSet::new
        );

        Gis.selectAllFromDbfAsList(
                outDirMerged2.resolve("3D_LoD2_33352_5606_2_sn__3D_LoD2_33444_5654_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                "Element",
                data,
                LinkedHashSet::new
        );

        Gis.selectAllFromDbfAsList(
                outDirMerged2.resolve("3D_LoD2_33444_5656_2_sn__3D_LoD2_33502_5682_2_sn.dbf"),
                StandardCharsets.UTF_8,
                "BldgPart",
                "Element",
                data,
                LinkedHashSet::new
        );

        List<Map.Entry<String, Collection<String>>> withoutRoof = new ArrayList<>();
        for(Map.Entry<String, Collection<String>> entry: data.entrySet()) {
            if(!entry.getValue().contains("Roof")) {
                withoutRoof.add(entry);
                System.out.println(entry);
            }
        }
    }

    @Test
    void extractAllKeys2() throws IOException {
        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(outDir);

        List<OgrHelper> helpers = new ArrayList<>();
        int i = 0;
        for(ShapeFile file: allFiles.getFiles()) {
            Map<String, Collection<String>> data = new LinkedHashMap<>();
            Gis.selectAllFromDbfAsList(
                    file.dbf(),
                    StandardCharsets.UTF_8,
                    "BldgPart",
                    "Element",
                    data,
                    LinkedHashSet::new
            );
            for(Map.Entry<String, Collection<String>> entry: data.entrySet()) {
                if(!entry.getValue().contains("Roof")) {
                    OgrHelper helper = new OgrHelper(file, entry.getKey(), entry.getValue());
                    helpers.add(helper);
                }
            }

            if(i % 100 == 0) {
                System.out.println(i + " / " + allFiles.count());
            }
            i++;
        }
        helpers.forEach(System.out::println);
    }

    @Test
    void runSingleSqlSpecial() throws IOException, InterruptedException {
        ProcessResult result = Ogr2Ogr.runSql(
                og2ogr,
                outDir.resolve("3D_LoD2_33278_5602_2_sn.shp"),
                outDirSqlSpecial.resolve("3D_LoD2_33278_5602_2_sn.shp"),
                "(Element = 'Ground') OR ((Element = 'Wall') AND (BldgPart = 'GUID_1565102960178_15033043'))"
        );
        if(result.isError()) {
            System.out.println(result.printErr(Util.windows1252()));
        }
    }

    @Test
    void extractQueries() throws IOException, InterruptedException, ExecutionException {
        IRPLogging.initConsoleAndFile(Paths.get("E:\\MyTemp\\Marvin-Daten", "extract-merge.log"));

        ShapeFiles allFiles = new ShapeFiles();
        allFiles.addAll(outDir);

        int i = 0;
        Map<ShapeFile, String> queries = new LinkedHashMap<>();
        for(ShapeFile file: allFiles.getFiles()) {

            Map<String, Collection<String>> data = new LinkedHashMap<>();
            Gis.selectAllFromDbfAsList(
                    file.dbf(),
                    StandardCharsets.UTF_8,
                    "BldgPart",
                    "Element",
                    data,
                    LinkedHashSet::new
            );


            List<OgrHelper> helpers = new ArrayList<>();
            for(Map.Entry<String, Collection<String>> entry: data.entrySet()) {
                if(!entry.getValue().contains("Roof")) {
                    OgrHelper helper = new OgrHelper(file, entry.getKey(), entry.getValue());
                    helpers.add(helper);
                }
            }

            String query;
            if(helpers.isEmpty()) {
                query = "\"Element = 'Roof'\"";
            } else {
                query = OgrHelper.buildSpecialQueryFor(helpers);
                LOGGER.trace("query: {}", query);
            }
            queries.put(file, query);

            if(i % 100 == 0) {
                LOGGER.trace("{} / {}", i, allFiles.count());
            }
            i++;
        }

        LOGGER.trace("total: {}", queries.size());


        MutableBoolean err = new MutableBoolean(false);
        final Counter c = new Counter();
        List<Callable<Void>> tasks = new ArrayList<>();
        for(Map.Entry<ShapeFile, String> query: queries.entrySet()) {
            final ShapeFile source = query.getKey();
            final ShapeFile target = source.changeDir(outDirSqlSpecial);
            final String q = query.getValue();

            Callable<Void> task = () -> {
                try {
                    if(err.get()) {
                        return null;
                    }

                    ProcessResult pr = Ogr2Ogr.runSql(og2ogr, source.shp(), target.shp(), q);
                    if(pr.isError()) {
                        LOGGER.error("error @ {}", source.getFileWithoutExtension());
                        err.set(true);
                    } else {
                        LOGGER.trace("finished {} {} ({} / {})", source.getName(), q, (c.inc() + 1), tasks.size());
                    }
                } catch (Exception e) {
                    LOGGER.error("error @ " + source.getFileWithoutExtension(), e);
                    err.set(true);
                }
                return null;
            };

            tasks.add(task);
        }

        System.out.println("run " + tasks.size() + " tasks");
        ExecutorService exec = Executors.newFixedThreadPool(7);
        List<Future<Void>> futures = exec.invokeAll(tasks);
        for(Future<Void> future: futures) {
            future.get();
        }

        if(err.get()) {
            return;
        }

        LOGGER.info("=== merge GB2X ===");
        runIt(outDirSqlSpecial, outDirSqlSpecial2, GB2X);
    }

    /*
elements: 3851453
elements: 7700286
elements: 9048921
Roof: 2682058
Wall: 2681626
Closure: 1004059
Ground: 2681178
     */
}
