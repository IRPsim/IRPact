package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Labs extends Component {

    public Labs() {
    }

    @Override
    protected void init() {
        init(X);
        init(Y);
        init(FILL);
        init(COLOUR);
        init(LINETYPE);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("labs", settings, target);
    }

    public void setX(String x) {
        setForceQuote(X, x);
    }
    public String getX() {
        return getString(X);
    }

    public void setY(String y) {
        setForceQuote(Y, y);
    }
    public String getY() {
        return getString(Y);
    }

    public void setFill(String fill) {
        setForceQuote(FILL, fill);
    }
    public String getFill() {
        return getString(FILL);
    }

    public void setColour(String colour) {
        setForceQuote(COLOUR, colour);
    }
    public String getColour() {
        return getString(COLOUR);
    }

    public void setLinetype(String linetype) {
        setForceQuote(LINETYPE, linetype);
    }
    public String getLinetype() {
        return getString(LINETYPE);
    }
}
