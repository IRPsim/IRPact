package de.unileipzig.irpact.util.R.sbuilder.ggplot2;

import de.unileipzig.irpact.util.R.sbuilder.ArrayElement;
import de.unileipzig.irpact.util.R.sbuilder.Component;
import de.unileipzig.irpact.util.R.sbuilder.StringSettings;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Daniel Abitz
 */
public class ScaleColorManual extends Component {

    public ScaleColorManual() {
    }

    public static String rgb2hex(int rgb) {
        StringBuilder sb = new StringBuilder(Integer.toHexString(rgb));
        while(sb.length() < 6) {
            sb.insert(0, "0");
        }
        sb.setLength(6);
        sb.insert(0, "#");
        return sb.toString();
    }

    public static String rgba2hex(int rgba) {
        StringBuilder sb = new StringBuilder(Integer.toHexString(rgba));
        while(sb.length() < 8) {
            sb.insert(0, "0");
        }
        sb.insert(0, "#");
        return sb.toString();
    }

    @Override
    protected void init() {
        init(VALUES);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("scale_color_manual", settings, target);
    }

    public void setRgb(int... rgbs) {
        setValues(Arrays.stream(rgbs).mapToObj(ScaleColorManual::rgb2hex).toArray(String[]::new));
    }

    public void setRgba(int... rgbas) {
        setValues(Arrays.stream(rgbas).mapToObj(ScaleColorManual::rgba2hex).toArray(String[]::new));
    }

    public void setValues(String... hexColors) {
        set(VALUES, new ArrayElement(hexColors, false, true));
    }

    public ArrayElement getValues() {
        return (ArrayElement) get(VALUES);
    }
}
