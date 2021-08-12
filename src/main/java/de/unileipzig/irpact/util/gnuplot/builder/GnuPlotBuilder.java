package de.unileipzig.irpact.util.gnuplot.builder;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.util.gnuplot.GnuPlotFileScript;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class GnuPlotBuilder {

    public static final String WILDCARD = "*";
    public static final String GPVAL_X_MIN = "GPVAL_X_MIN";
    public static final String GPVAL_X_MAX = "GPVAL_X_MAX";
    public static final String GPVAL_Y_MIN = "GPVAL_Y_MIN";
    public static final String GPVAL_Y_MAX = "GPVAL_Y_MAX";

    protected final List<Command> commands = new ArrayList<>();
    protected StringSettings settings;
    protected boolean autoNewLine = true;

    public GnuPlotBuilder() {
        this(null);
    }

    public GnuPlotBuilder(StringSettings settings) {
        setSettings(settings);
    }

    public static String arg(int i) {
        return "ARG" + i;
    }

    public static String quote(String input) {
        return "\"" + input + "\"";
    }

    public static String print(Path path) {
        return path.toString().replace('\\', '/');
    }

    public static String quote(Path path) {
        return quote(print(path));
    }

    public static String escapeUnderscore(String input) {
        return input.replace("_", "\\\\\\_");
    }

    public static String min(Object a, Object b, boolean parentheses) {
        String text = a + " < " + b + " ? " + a + " : " + b;
        if(parentheses) {
            return "(" + text + ")";
        } else {
            return text;
        }
    }

    public static String max(Object a, Object b, boolean parentheses) {
        String text = a + " > " + b + " ? " + a + " : " + b;
        if(parentheses) {
            return "(" + text + ")";
        } else {
            return text;
        }
    }

    public static String add(Object a, Object b, boolean parentheses) {
        String text = a + " + " + b;
        if(parentheses) {
            return "(" + text + ")";
        } else {
            return text;
        }
    }

    public static String sub(Object a, Object b, boolean parentheses) {
        String text = a + " - " + b;
        if(parentheses) {
            return "(" + text + ")";
        } else {
            return text;
        }
    }

    public void setSettings(StringSettings settings) {
        this.settings = settings;
    }

    public void add(Command command) {
        commands.add(command);
    }

    public void addComment(String text) {
        add(new Comment(text));
    }

    public void addSet(String text) {
        add(new SetCommand(text));
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String print() {
        try {
            StringWriter sw = new StringWriter();
            print(sw);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void print(Appendable target) throws IOException {
        for(Command command: commands) {
            command.print(settings, target);
            if(autoNewLine && command != NewLine.INSTANCE) {
                NewLine.INSTANCE.print(settings, target);
            }
        }
    }

    public GnuPlotFileScript build() {
        try {
            return build(null, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public GnuPlotFileScript build(Path scriptPath, Charset charset) throws IOException {
        GnuPlotFileScript script = new GnuPlotFileScript();
        script.setText(print());
        script.setPath(scriptPath);
        script.setCharset(charset);
        if(scriptPath != null) {
            script.store();
        }
        return script;
    }

    @Override
    public String toString() {
        return print();
    }

    //=========================
    //custom
    //=========================

    protected void buildSet(Object... parts) {
        addSet(StringUtil.concat(" " , parts));
    }

    public void setStyle(Object... parts) {
        buildSet("style", StringUtil.concat(" " , parts));
    }

    public void setStyleDataHistograms() {
        setStyle("data histograms");
    }

    public void setStyleDataLines() {
        setStyle("data lines");
    }

    public void setStyleDataLinesPoints() {
        setStyle("data linespoints");
    }

    public void setStyleHistrogramColumnStacked() {
        setStyle("histogram columnstacked");
    }

    public void setStyleHistrogramRowStacked() {
        setStyle("histogram rowstacked");
    }

    public void setFillSolid() {
        setStyle("fill solid 1.0 border -1");
    }

    public void setBoxWidthAbsolute(String width) {
        buildSet("boxwidth", width, "absolute");
    }

    public void setBoxWidthAbsolute(double width) {
        setBoxWidthAbsolute(Double.toString(width));
    }

    public void setTitle(String title) {
        buildSet("title", quote(title));
    }

    public void setXLabel(String label) {
        buildSet("xlabel", quote(label));
    }

    public void setYLabel(String label) {
        buildSet("ylabel", quote(label));
    }

    public void setLegendOutsideRightTop() {
        //buildSet("key outside right top vertical Left reverse noenhanced autotitle columnhead box lt black linewidth 1.0 dashtype solid");
        buildSet("key outside right top vertical Left");
    }

    public void setLegendOutsideRightTop(String label) {
        //buildSet("key outside right top vertical Left reverse noenhanced autotitle columnhead box lt black linewidth 1.0 dashtype solid title ", quote(label));
        buildSet("key outside right top vertical Left title ", quote(label));
    }

    public void printUnknown() {
        buildSet("terminal", "unknown");
    }

    public void printPng() {
        buildSet("terminal", "png");
    }

    public void printPng(int width, int height) {
        buildSet("terminal", "png", "size", width + "," + height);
    }

    public void printPngCairo() {
        buildSet("terminal", "pngcairo");
    }

    public void printPngCairo(int width, int height) {
        buildSet("terminal", "pngcairo", "size", width + "," + height);
    }

    public void setRawOutput(String target) {
        buildSet("output", target);
    }

    public void setOutput(String target) {
        setRawOutput(quote(target));
    }

    public void setArgOutput(int i) {
        setRawOutput(arg(i));
    }

    public void setDataFileSeparator(String separator) {
        buildSet("datafile", "separator", quote(separator));
    }

    public void plotSpecialLinePlot(String data) {
        plotRawSpecialLinePlot(quote(data));
    }

    public void plotArgSpecialLinePlot(int i) {
        plotRawSpecialLinePlot(arg(i));
    }

    public void plotRawSpecialLinePlot(String data) {
        plotRawSpecialLinePlot(data, 1);
    }

    public void plotRawSpecialLinePlot(String data, int linewidth) {
        add(new SpecialLinePlotCommand(data, linewidth));
    }

    public void plotRawSpecialLinePlotWithKey(String phase0, String phase1, String phase2, String data, int linewidth) {
        add(new SpecialPlotCommandWith3DataColumnsAndCustomKey(phase0, phase1, phase2, data, linewidth));
    }

    public void setXRange(Object min, Object max) {
        String range = Objects.equals(min, max)
                ? "[*:*]"
                : "[" + min + ":" + max + "]";
        buildSet("xrange", range);
    }

    public void setYRange(Object min, Object max) {
        String range = Objects.equals(min, max)
                ? "[*:*]"
                : "[" + min + ":" + max + "]";
        buildSet("yrange", range);
    }

    public void replot() {
        add(new SimpleCommand("replot"));
    }
}
