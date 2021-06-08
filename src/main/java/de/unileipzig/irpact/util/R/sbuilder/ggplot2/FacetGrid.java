package de.unileipzig.irpact.util.R.sbuilder.ggplot2;

import de.unileipzig.irpact.util.R.sbuilder.Component;
import de.unileipzig.irpact.util.R.sbuilder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class FacetGrid extends Component {

    public FacetGrid() {
    }

    @Override
    protected void init() {
        init(null);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        target.append("facet_grid(");
        target.append(settings.getWaveSign());
        target.append(get(null).print(settings));
        target.append(")");
    }

    public void setData(String data) {
        setIgnoreQuote(null, data);
    }
    public String getX() {
        return getString(null);
    }
}
