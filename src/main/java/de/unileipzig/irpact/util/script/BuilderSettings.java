package de.unileipzig.irpact.util.script;

import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public final class BuilderSettings {

    public static final int NO_USAGE = -1;
    public static final int USAGE_ARG0 = 0;
    public static final int USAGE_ARG2 = 2;

    protected final Map<String, Object> data = new HashMap<>();
    protected boolean nonnull = false;

    public BuilderSettings() {
    }

    //=========================
    //util
    //=========================

    public BuilderSettings copy() {
        return new BuilderSettings().putAll(this);
    }

    public BuilderSettings reset() {
        data.clear();
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public BuilderSettings putAll(BuilderSettings other) {
        return putAll(other.getData());
    }

    public BuilderSettings putAll(Map<? extends String, ?> other) {
        data.putAll(other);
        return this;
    }

    public BuilderSettings nonNull() {
        nonnull = true;
        return this;
    }

    public BuilderSettings allowNull() {
        nonnull = false;
        return this;
    }

    public int getIntOr(String key, int other) {
        Integer value = getAs(key);
        return value == null ? other : value;
    }

    public double getDoubleOr(String key, double other) {
        Double value = getAs(key);
        return value == null ? other : value;
    }

    public BuilderSettings set(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public BuilderSettings remove(String key) {
        data.remove(key);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <R> R getAs(String key) {
        Object value = data.get(key);
        if(nonnull && value == null) {
            throw new NullPointerException(key);
        }
        return (R) value;
    }

    public <R> R getAsOr(String key, R other) {
        R value = getAs(key);
        return value == null ? other : value;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    //=========================
    //flags
    //=========================

    public BuilderSettings setUseArgsFlag(boolean use) {
        if(use) {
            return set("useargsflag", null);
        } else {
            return remove("useargsflag");
        }
    }
    public boolean useArgsFlag() {
        return has("useargsflag");
    }

    public BuilderSettings setUsageFlag(int flag) {
        return set("usageflag", flag);
    }
    public int getUsageFlag() {
        return getIntOr("usageflag", NO_USAGE);
    }

    public BuilderSettings setEscapeSpecialCharacters(boolean escape) {
        if(escape) {
            return set("escapespecialcharacters", null);
        } else {
            return remove("escapespecialcharacters");
        }
    }
    public boolean escapeSpecialCharacters() {
        return has("escapespecialcharacters");
    }

    public BuilderSettings setCenterTitle(boolean center) {
        if(center) {
            return set("centertitle", null);
        } else {
            return remove("centertitle");
        }
    }
    public boolean centerTitle() {
        return has("centertitle");
    }

    //=========================
    //intern
    //=========================

    public BuilderSettings setInputFile(Path path) {
        return set("inputfile", path);
    }
    public Path getInputFile() {
        return getAs("inputfile");
    }

    public BuilderSettings setOutputFile(Path path) {
        return set("outputfile", path);
    }
    public Path getOutputFile() {
        return getAs("outputfile");
    }

    public BuilderSettings setXArg(String text) {
        return set("xarg", text);
    }
    public String getXArg() {
        return getAs("xarg");
    }

    public BuilderSettings setYArg(String text) {
        return set("yarg", text);
    }
    public String getYArg() {
        return getAs("yarg");
    }

    public BuilderSettings setFillArg(String text) {
        return set("fillarg", text);
    }
    public String getFillArg() {
        return getAs("fillarg");
    }

    public BuilderSettings setGrpArg(String text) {
        return set("grparg", text);
    }
    public String getGrpArg() {
        return getAs("grparg");
    }

    public BuilderSettings setDistinctArg(String text) {
        return set("distinctarg", text);
    }
    public String getDistinctArg() {
        return getAs("distinctarg");
    }

    public BuilderSettings setSep(String sep) {
        return set("sep", sep);
    }
    public String getSep() {
        return getAs("sep");
    }

    public BuilderSettings setHeight(int value) {
        return set("height", value);
    }
    public int getHeight() {
        return getIntOr("height", -1);
    }
    public boolean hasHeight() {
        return has("height");
    }

    public BuilderSettings setWidht(int value) {
        return set("width", value);
    }
    public int getWidht() {
        return getIntOr("width", -1);
    }
    public boolean hasWidht() {
        return has("width");
    }

    public BuilderSettings setUnits(String text) {
        return set("units", text);
    }
    public String getUnits() {
        return getAs("units");
    }
    public boolean hasUnits() {
        return has("units");
    }

    public BuilderSettings setDpi(int value) {
        return set("dpi", value);
    }
    public int getDpi() {
        return getIntOr("dpi", -1);
    }
    public boolean hasDpi() {
        return has("dpi");
    }

    public BuilderSettings setLineWidth(int value) {
        return set("linewidth", Integer.toString(value));
    }
    public BuilderSettings setLineWidth(double value) {
        return set("linewidth", Double.toString(value));
    }
    public BuilderSettings setLineWidth(String value) {
        return set("linewidth", value);
    }
    public String getLineWidth() {
        return getAs("linewidth");
    }
    public double getLineWidthDouble() {
        return Double.parseDouble(getAsOr("linewidth", "1.0"));
    }
    public boolean hasLineWidth() {
        return has("linewidth");
    }

    public BuilderSettings setLineType(String text) {
        return set("linetype", text);
    }
    public String getLineType() {
        return getAs("linetype");
    }
    public boolean hasLineType() {
        return has("linetype");
    }

    public BuilderSettings setFirstLinetype(String text) {
        return set("firstlinetype", text);
    }
    public String getFirstLinetype() {
        return getAs("firstlinetype");
    }
    public boolean hasFirstLinetype() {
        return has("firstlinetype");
    }

    public BuilderSettings setSecondLinetype(String text) {
        return set("secondlinetype", text);
    }
    public String getSecondLinetype() {
        return getAs("secondlinetype");
    }
    public boolean hasSecondLinetype() {
        return has("secondlinetype");
    }

    //=========================
    //label
    //=========================

    public BuilderSettings setTitle(String text) {
        return set("title", text);
    }
    public String getTitle() {
        return getAs("title");
    }
    public boolean hasTitle() {
        return has("title");
    }

    public BuilderSettings setXLab(String text) {
        return set("xlab", text);
    }
    public String getXLab() {
        return getAs("xlab");
    }

    public BuilderSettings setYLab(String text) {
        return set("ylab", text);
    }
    public String getYLab() {
        return getAs("ylab");
    }

    public BuilderSettings setFillLab(String text) {
        return set("filllab", text);
    }
    public String getFillLab() {
        return getAs("filllab");
    }
    public boolean hasFillLab() {
        return has("filllab");
    }

    public BuilderSettings setGrpLab(String text) {
        return set("grplab", text);
    }
    public String getGrpLab() {
        return getAs("grplab");
    }
    public boolean hasGrpLab() {
        return has("grplab");
    }

    public BuilderSettings setDistinctLab(String text) {
        return set("distinctlab", text);
    }
    public String getDistinctLab() {
        return getAs("distinctlab");
    }

    public BuilderSettings setBoxWidthAbsolute(double value) {
        return set("boxwidthabsolute", value);
    }
    public double getBoxWidthAbsolute() {
        return getDoubleOr("boxwidthabsolute", 1.0);
    }

    public BuilderSettings setXMin(Object value) {
        return set("xmin", value);
    }
    public Object getXMin() {
        return getAs("xmin");
    }

    public BuilderSettings setXMax(Object value) {
        return set("xmax", value);
    }
    public Object getXMax() {
        return getAs("xmax");
    }

    public BuilderSettings setYMin(Object value) {
        return set("ymin", value);
    }
    public Object getYMin() {
        return getAs("ymin");
    }

    public BuilderSettings setYMax(Object value) {
        return set("ymax", value);
    }
    public Object getYMax() {
        return getAs("ymax");
    }

    public BuilderSettings setXYRangeWildCard() {
        return setXMin(GnuPlotBuilder.WILDCARD).setXMax(GnuPlotBuilder.WILDCARD).setYMin(GnuPlotBuilder.WILDCARD).setYMax(GnuPlotBuilder.WILDCARD);
    }

    public BuilderSettings setEncoding(String encoding) {
        return set("encoding", encoding);
    }
    public String getEncoding() {
        return getAs("encoding");
    }
    public boolean hasEncoding() {
        return has("encoding");
    }

    public BuilderSettings setColClasses(String... classes) {
        return set("colclasses", classes);
    }
    public String[] getColClasses() {
        return getAs("colclasses");
    }
    public boolean hasColClasses() {
        return has("colclasses");
    }

    public BuilderSettings setScaleXContinuousBreaks(String... breaks) {
        if(breaks == null || breaks.length == 0) {
            return remove("scalexcontinuousbreaks");
        } else {
            return set("scalexcontinuousbreaks", breaks);
        }
    }
    public String[] getScaleXContinuousBreaks() {
        return getAs("scalexcontinuousbreaks");
    }
    public boolean hasScaleXContinuousBreaks() {
        return has("scalexcontinuousbreaks");
    }

    public BuilderSettings setDistinct0Label(String label) {
        return set("dashtype0label", label);
    }
    public String getDistinct0Label() {
        return getAs("dashtype0label");
    }

    public BuilderSettings setDistinct1Label(String label) {
        return set("dashtype1label", label);
    }
    public String getDistinct1Label() {
        return getAs("dashtype1label");
    }
}
