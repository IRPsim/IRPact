package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Ggsave extends Component {

    public Ggsave() {
    }

    @Override
    protected void init() {
        init(null);
        init(HEIGHT);
        init(WIDTH);
        init(UNITS);
        init(DPI);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("ggsave", settings, target);
    }

    public void setFileName(String name) {
        set(null, name);
    }
    public String getFileName() {
        return getString(null);
    }

    public void setHeight(String height) {
        setIgnoreQuote(HEIGHT, height);
    }
    public void setHeight(int height) {
        setHeight(Integer.toString(height));
    }
    public String getHeight() {
        return getString(HEIGHT);
    }

    public void setWidth(String width) {
        setIgnoreQuote(WIDTH, width);
    }
    public void setWidth(int width) {
        setWidth(Integer.toString(width));
    }
    public String getWidth() {
        return getString(WIDTH);
    }

    public void setUnits(String units) {
        setForceQuote(UNITS, units);
    }
    public String getUnits() {
        return getString(UNITS);
    }

    public void setDpi(String dpi) {
        setIgnoreQuote(DPI, dpi);
    }
    public void setDpi(int dpi) {
        setDpi(Integer.toString(dpi));
    }
    public String getDpi() {
        return getString(DPI);
    }
}
