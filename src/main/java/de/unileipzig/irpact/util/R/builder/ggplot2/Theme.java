package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Theme extends Component {

    public Theme() {
    }

    @Override
    protected void init() {
        init(PLOT_TITLE);
        init(Y);
        init(FILL);
        init(COLOUR);
        init(LINETYPE);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("theme", settings, target);
    }

    public static String createPlotTitleHjust(double value) {
        return "element_text(hjust = " + value + ")";
    }
    public void setPlotTitleHjust(double value) {
        setPlotTitle(createPlotTitleHjust(value));
    }
    public void setPlotTitle(String title) {
        setIgnoreQuote(PLOT_TITLE, title);
    }
    public String getPlotTitle() {
        return getString(PLOT_TITLE);
    }
    public String hasPlotTitle() {
        return getString(PLOT_TITLE);
    }
}
