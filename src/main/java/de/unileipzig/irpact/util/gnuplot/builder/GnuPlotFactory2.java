package de.unileipzig.irpact.util.gnuplot.builder;

import de.unileipzig.irpact.util.gnuplot.builder.plot.PlotCommandBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.plot.PlotData;
import de.unileipzig.irpact.util.gnuplot.builder.plot.SimplifiedKeyEntry;
import de.unileipzig.irpact.util.gnuplot.builder.plot.SimplifiedNaN;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class GnuPlotFactory2 {

    //=========================
    //
    //=========================

    public static final StringSettings SETTINGS = new StringSettings();

    private GnuPlotFactory2() {
    }

    private static GnuPlotBuilder newBuilder() {
        GnuPlotBuilder builder = new GnuPlotBuilder();
        builder.setSettings(SETTINGS);
        return builder;
    }

    private static Comment getDate() {
        return new Comment("date: " + LocalDateTime.now());
    }

    private static Comment getAutoGenerated() {
        return new Comment("automatic generated gnuplot script");
    }

    private static Comment getUsageComment() {
        return new Comment("usage: gnuplot -c <script name> <input file> <output file>");
    }

    private static void setPngCairo(GnuPlotBuilder builder, int width, int height) {
        if(width > 0 && height > 0) {
            builder.printPngCairo(width, height);
        } else {
            builder.printPngCairo();
        }
    }

    private static List<Integer> createIndexListWithStartAt1(Collection<?> c) {
        return createIndexList(c, 1);
    }

    private static List<Integer> createIndexList(Collection<?> c, int startIndex) {
        return createIndexList(c.size(), startIndex);
    }

    private static List<Integer> createIndexList(int count, int startIndex) {
        List<Integer> list = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            list.add(startIndex++);
        }
        return list;
    }

    private static <R> List<R> createList(Collection<?> c, R value) {
        return createList(c.size(), value);
    }

    private static <R> List<R> createList(int count, R value) {
        List<R> list = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            list.add(value);
        }
        return list;
    }

    @SafeVarargs
    private static <R> List<R> createCycleList(Collection<?> c, R... values) {
        return createCycleList(c.size(), values);
    }

    @SafeVarargs
    private static <R> List<R> createCycleList(int count, R... values) {
        List<R> list = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            list.add(values[i % values.length]);
        }
        return list;
    }

    private static List<String> updateHexColor(Collection<? extends String> c) {
        return c.stream()
                .map(str -> {
                    if(str.startsWith("#")) {
                        return str;
                    } else {
                        return "#" + str;
                    }
                })
                .collect(Collectors.toList());
    }

    private static Integer calcIndex(Collection<?> c, int input) {
        return calcIndex(c.size(), input);
    }

    private static int calcIndex(int cycle, int input) {
        return (input % cycle) + 1;
    }

    private static String defaultCycleIndex(String indexChar, Object max) {
        return cycleIndex("(" + indexChar + "-2)", max, "+1");
    }

    private static String cycleIndex(Object input, Object max, Object modifier) {
        return "((" + input + "%" + max + ")" + modifier + ")";
    }

    private static Object or(Object input, Object ifNotNull, Object ifNull) {
        return input == null ? ifNull : ifNotNull;
    }

    private static Object ifNull(Object input, Object returnValue) {
        return input == null ? returnValue : null;
    }

    private static Object ifNotNull(Object input, Object returnValue) {
        return input == null ? null : returnValue;
    }

    private static <R> Object ifNotNull(R input, Function<? super R, ?> func) {
        return input == null ? null : func.apply(input);
    }

    public static GnuPlotBuilder simpleMultiLinePlot(
            String title,
            String xlab, String ylab,
            String sep,
            List<String> hexColors,
            int lineWidth,
            int width, int height,
            Number minY, Number maxY) {
        PlotCommandBuilder plot = new PlotCommandBuilder();
        plot.linebreak();
        plot.add(new PlotData()
                .setArgInput(1)
                .setColumn1(2)
                .setColumn2(PlotCommandBuilder.xtic(1))
                .setTi("col")
                .setLs(ifNotNull(hexColors, 1))
                .setLc(ifNull(hexColors, 1))
                .setLw(ifNull(hexColors, lineWidth))
        );
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setForLoop("i", 3, "*")
                .setSameInput()
                .setColumn1("i")
                .setTi("col")
                .setLs(ifNotNull(hexColors, _hexColors -> defaultCycleIndex("i", _hexColors.size())))
                .setLc(ifNull(hexColors, "(i-1)"))
                .setLw(ifNull(hexColors, lineWidth))
        );

        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: simple multi line plot");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataLinesPoints();
        builder.setLegendOutsideRightTop();
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, lineWidth), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.addComment("===plot===");
        builder.setDataFileSeparator(sep);
        builder.setArgOutput(2);
        builder.setYRange(minY, maxY);
        builder.printPngCairo(width, height);
        builder.add(plot);
        return builder;
    }

    //year;value0-A;value0-B;...
    public static GnuPlotBuilder multiLinePlotWithTwoDashtypes(
            //label
            String title,
            String xlab, String ylab,
            String keyTitle0, String keyTitle1,
            String dashType1Label, String dashType2Label,
            //style
            List<String> hexColors,
            int lineWidth,
            int dashtype1, int dashtype2,
            //util
            String sep,
            int width, int height,
            Number minY, Number maxY,
            int numberOfIndividualColumns) {

        PlotCommandBuilder plot = new PlotCommandBuilder();
        //key
        //title
        plot.linebreak();
        plot.add(new SimplifiedKeyEntry()
                .setTi(PlotCommandBuilder.quote(keyTitle0))
        );
        //a-Part
        plot.commaAndLinebreak();
        plot.add(new SimplifiedNaN()
                .setLines()
                .setDt(dashtype1)
                .setLc(PlotCommandBuilder.quote("black"))
                .setTi(PlotCommandBuilder.quote(dashType1Label))
        );
        //b-Part
        plot.commaAndLinebreak();
        plot.add(new SimplifiedNaN()
                .setLines()
                .setDt(dashtype2)
                .setLc(PlotCommandBuilder.quote("black"))
                .setTi(PlotCommandBuilder.quote(dashType2Label))
        );
        //data
        //title
        plot.commaAndLinebreak();
        plot.add(new SimplifiedKeyEntry()
                .setTi(PlotCommandBuilder.quote(keyTitle1))
        );
        for(int i = 0; i < numberOfIndividualColumns; i++) {
            //a-Part
            int aIndex = (i+1)*2;
            plot.commaAndLinebreak();
            if(i == 0) {
                plot.add(new PlotData()
                        .setArgInput(1)
                        .setColumn1(1)
                        .setColumn2(aIndex)
                        .setColumn3(PlotCommandBuilder.xtic(1))
                        .setTi("col")
                        .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                        .setLc(hexColors == null ? i+1 : null)
                        .setLw(hexColors == null ? lineWidth : null)
                        .setDt(dashtype1)
                );
            } else {
                plot.add(new PlotData()
                        .setSameInput()
                        .setColumn1(1)
                        .setColumn2(aIndex)
                        .setTi("col")
                        .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                        .setLc(hexColors == null ? i+1 : null)
                        .setLw(hexColors == null ? lineWidth : null)
                        .setDt(dashtype1)
                );
            }

            //b-Part
            int bIndex = aIndex + 1;
            plot.commaAndLinebreak();
            plot.add(new PlotData()
                    .setSameInput()
                    .setColumn1(1)
                    .setColumn2(bIndex)
                    .setNotitle()
                    .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                    .setLc(hexColors == null ? i+1 : null)
                    .setLw(hexColors == null ? lineWidth : null)
                    .setDt(dashtype2)
            );
        }

        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: special line chart v0");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataLinesPoints();
        builder.setLegendOutsideRightTop();
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, lineWidth), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.addComment("===plot===");
        builder.setDataFileSeparator(sep);
        builder.printPngCairo(width, height);
        builder.setArgOutput(2);
        builder.setYRange(minY, maxY);
        builder.add(plot);
        return builder;
    }

    //year;value0-A;value0-B;value0-C;...
    public static GnuPlotBuilder multiLinePlotWithThreeDashtypes(
            //label
            String title,
            String xlab, String ylab,
            String keyTitle0, String keyTitle1,
            String dashType1Label, String dashType2Label, String dashType3Label,
            //style
            List<String> hexColors,
            int lineWidth,
            int dashtype1, int dashtype2, int dashtype3,
            //util
            String sep,
            int width, int height,
            Number minY, Number maxY,
            int numberOfIndividualColumns) {

        PlotCommandBuilder plot = new PlotCommandBuilder();
        //key
        //title
        plot.linebreak();
        plot.add(new SimplifiedKeyEntry()
                .setTi(PlotCommandBuilder.quote(keyTitle0))
        );
        //a-Part
        plot.commaAndLinebreak();
        plot.add(new SimplifiedNaN()
                .setLines()
                .setDt(dashtype1)
                .setLc(PlotCommandBuilder.quote("black"))
                .setTi(PlotCommandBuilder.quote(dashType1Label))
        );
        //b-Part
        plot.commaAndLinebreak();
        plot.add(new SimplifiedNaN()
                .setLines()
                .setDt(dashtype2)
                .setLc(PlotCommandBuilder.quote("black"))
                .setTi(PlotCommandBuilder.quote(dashType2Label))
        );
        //c-Part
        plot.commaAndLinebreak();
        plot.add(new SimplifiedNaN()
                .setLines()
                .setDt(dashtype3)
                .setLc(PlotCommandBuilder.quote("black"))
                .setTi(PlotCommandBuilder.quote(dashType3Label))
        );
        //data
        //title
        plot.commaAndLinebreak();
        plot.add(new SimplifiedKeyEntry()
                .setTi(PlotCommandBuilder.quote(keyTitle1))
        );
        for(int i = 0; i < numberOfIndividualColumns; i++) {
            //a-Part
            int aIndex = (i+1)*3;
            plot.commaAndLinebreak();
            if(i == 0) {
                plot.add(new PlotData()
                        .setArgInput(1)
                        .setColumn1(1)
                        .setColumn2(aIndex)
                        .setColumn3(PlotCommandBuilder.xtic(1))
                        .setTi("col")
                        .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                        .setLc(hexColors == null ? i+1 : null)
                        .setLw(hexColors == null ? lineWidth : null)
                        .setDt(dashtype1)
                );
            } else {
                plot.add(new PlotData()
                        .setSameInput()
                        .setColumn1(1)
                        .setColumn2(aIndex)
                        .setTi("col")
                        .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                        .setLc(hexColors == null ? i+1 : null)
                        .setLw(hexColors == null ? lineWidth : null)
                        .setDt(dashtype1)
                );
            }

            //b-Part
            int bIndex = aIndex + 1;
            plot.commaAndLinebreak();
            plot.add(new PlotData()
                    .setSameInput()
                    .setColumn1(1)
                    .setColumn2(bIndex)
                    .setNotitle()
                    .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                    .setLc(hexColors == null ? i+1 : null)
                    .setLw(hexColors == null ? lineWidth : null)
                    .setDt(dashtype2)
            );

            //c-Part
            int cIndex = bIndex + 1;
            plot.commaAndLinebreak();
            plot.add(new PlotData()
                    .setSameInput()
                    .setColumn1(1)
                    .setColumn2(cIndex)
                    .setNotitle()
                    .setLs(hexColors == null ? null : calcIndex(hexColors, i))
                    .setLc(hexColors == null ? i+1 : null)
                    .setLw(hexColors == null ? lineWidth : null)
                    .setDt(dashtype2)
            );
        }

        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: special line chart v0");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataLinesPoints();
        builder.setLegendOutsideRightTop();
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, lineWidth), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.addComment("===plot===");
        builder.setDataFileSeparator(sep);
        builder.printPngCairo(width, height);
        builder.setArgOutput(2);
        builder.setYRange(minY, maxY);
        builder.add(plot);
        return builder;
    }

    public static GnuPlotBuilder clusteredBarChart(
            String title,
            String xlab, String ylab,
            String keylab,
            String sep,
            List<String> hexColors,
            double boxWidthAbsolute,
            Number yMin, Number yMax,
            int width, int height) {

        PlotCommandBuilder plot = new PlotCommandBuilder();
        plot.linebreak();
        plot.add(new PlotData()
                .setInput(PlotCommandBuilder.arg(1))
                .setColumn1(2)
                .setColumn2(PlotCommandBuilder.xtic(1))
                .setTi("col")
                .setLc(hexColors == null ? "1" : null)
                .setLs(hexColors == null ? null : "1")
        );
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setForLoop("i", 3, "*")
                .setInput("''")
                .setColumn1("i")
                .setTi("col")
                .setLc(hexColors == null ? "i" : null)
                .setLs(hexColors == null ? null : defaultCycleIndex("i", hexColors.size()))
        );

        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: clustered bar chart v0");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataHistograms();
        builder.setStyleHistrogramClustered();
        builder.setFillSolid();
        builder.setBoxWidthAbsolute(boxWidthAbsolute);
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, 1), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.setLegendOutsideRightTop(keylab);
        builder.addComment("===output===");
        setPngCairo(builder, width, height);
        builder.setArgOutput(2);
        builder.addComment("===plot===");
        builder.setYRange(yMin, yMax);
        builder.setDataFileSeparator(sep);
        builder.add(plot);
        return builder;
    }

    public static GnuPlotBuilder stackedBarChartForThreeValuesAndAHiddenValue(
            String title,
            String xlab, String ylab, String filllab,
            String sep,
            List<String> hexColors,
            double boxWidthAbsolute,
            Number yMin, Number yMax,
            int width, int height) {

        PlotCommandBuilder plot = new PlotCommandBuilder();
        //data
        //hidden phase
        plot.linebreak();
        plot.add(new PlotData()
                .setArgInput(1)
                .setColumn1(2)
                .setColumn2(PlotCommandBuilder.xtic(1))
                .setNotitle()
                .setLs(hexColors == null ? null : 1)
                .setLc(hexColors == null ? 1 : null)
                .setLw(hexColors == null ? 1 : null)
        );
        //phase0
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setSameInput()
                .setColumn1(3)
                .setTi("col")
                .setLs(hexColors == null ? null : 1)
                .setLc(hexColors == null ? 1 : null)
                .setLw(hexColors == null ? 1 : null)
        );
        //phase1
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setSameInput()
                .setColumn1(4)
                .setTi("col")
                .setLs(hexColors == null ? null : 2)
                .setLc(hexColors == null ? 2 : null)
                .setLw(hexColors == null ? 1 : null)
        );
        //phase2
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setSameInput()
                .setColumn1(5)
                .setTi("col")
                .setLs(hexColors == null ? null : 3)
                .setLc(hexColors == null ? 3 : null)
                .setLw(hexColors == null ? 1 : null)
        );


        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: stacked bar chart v1");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataHistograms();
        builder.setStyleHistrogramRowStacked();
        builder.setFillSolid();
        builder.setBoxWidthAbsolute(boxWidthAbsolute);
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, 1), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.formatAndSetLegendOutsideRightTop(filllab);
        builder.addComment("===output===");
        builder.printPngCairo(width, height);
        builder.setYRange(yMin, yMax);
        builder.setArgOutput(2);
        builder.addComment("===plot===");
        builder.setDataFileSeparator(sep);
        builder.add(plot);
        return builder;
    }

    public static GnuPlotBuilder simpleStackedBarChart(
            String title,
            String xlab, String ylab, String keylab,
            String sep,
            List<String> hexColors,
            double boxWidthAbsolute,
            Number yMin, Number yMax,
            int width, int height) {

        PlotCommandBuilder plot = new PlotCommandBuilder();
        plot.linebreak();
        plot.add(new PlotData()
                .setArgInput(1)
                .setColumn1(2)
                .setColumn2(PlotCommandBuilder.xtic(1))
                .setTi("col")
                .setLs(ifNotNull(hexColors, 1))
                .setLc(ifNull(hexColors, 1))
                .setLw(ifNull(hexColors, 1))
        );
        plot.commaAndLinebreak();
        plot.add(new PlotData()
                .setForLoop("i", 3, "*")
                .setSameInput()
                .setColumn1("i")
                .setTi("col")
                .setLs(ifNotNull(hexColors, _hexColors -> defaultCycleIndex("i", _hexColors.size())))
                .setLc(ifNull(hexColors, "(i-1)"))
                .setLw(ifNull(hexColors, 1))
        );


        GnuPlotBuilder builder = newBuilder();
        builder.add(getDate());
        builder.add(getAutoGenerated());
        builder.addComment("type: stacked bar chart v1");
        builder.add(getUsageComment());
        builder.addComment("===style===");
        builder.setStyleDataHistograms();
        builder.setStyleHistrogramRowStacked();
        builder.setFillSolid();
        builder.setBoxWidthAbsolute(boxWidthAbsolute);
        if(hexColors != null) {
            builder.setStyleLineWithWidthAndRGB(createIndexListWithStartAt1(hexColors), createList(hexColors, 1), updateHexColor(hexColors));
        }
        builder.addComment("===labels===");
        builder.formatAndSetTitle(title);
        builder.formatAndSetXLabel(xlab);
        builder.formatAndSetYLabel(ylab);
        builder.formatAndSetLegendOutsideRightTop(keylab);
        builder.addComment("===output===");
        builder.printPngCairo(width, height);
        builder.setYRange(yMin, yMax);
        builder.setArgOutput(2);
        builder.addComment("===plot===");
        builder.setDataFileSeparator(sep);
        builder.add(plot);
        return builder;
    }
}
