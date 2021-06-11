package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Aes extends Component {

    public Aes() {
    }

    @Override
    protected void init() {
        init(X);
        init(Y);
        init(GROUP);
        init(COLOUR);
        init(LINETYPE);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("aes", settings, target);
    }

    @Override
    protected boolean hasData() {
        return super.hasData();
    }

    public void setX(String x) {
        setIgnoreQuote(X, x);
    }
    public String getX() {
        return getString(X);
    }

    public void setY(String y) {
        setIgnoreQuote(Y, y);
    }
    public String getY() {
        return getString(Y);
    }

    public void setGroup(String group) {
        setIgnoreQuote(GROUP, group);
    }
    public void setGroupInteraction(String first, String second) {
        set(GROUP, new Interaction(first, second, true, false));
    }
    public String getGroup() {
        return getString(GROUP);
    }

    public void setColour(String colour) {
        setIgnoreQuote(COLOUR, colour);
    }
    public String getColour() {
        return getString(COLOUR);
    }

    public void setFill(String fill) {
        setIgnoreQuote(FILL, fill);
    }
    public String getFill() {
        return getString(FILL);
    }

    public void setLinetype(String linetype) {
        setIgnoreQuote(LINETYPE, linetype);
    }
    public String getLinetype() {
        return getString(LINETYPE);
    }
}
