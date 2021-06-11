package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.Element;
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

    public void setTitle(String title, String encoding) {
        if(encoding == null) {
            setTitle(title);
        } else {
            setIgnoreQuote(TITLE, Element.iconv(title, encoding));
        }
    }
    public void setTitle(String title) {
        setForceQuote(TITLE, title);
    }
    public String getTitle() {
        return getString(TITLE);
    }

    public void setX(String x, String encoding) {
        if(encoding == null) {
            setX(x);
        } else {
            setIgnoreQuote(X, Element.iconv(x, encoding));
        }
    }
    public void setX(String x) {
        setForceQuote(X, x);
    }
    public String getX() {
        return getString(X);
    }

    public void setY(String y, String encoding) {
        if(encoding == null) {
            setY(y);
        } else {
            setIgnoreQuote(Y, Element.iconv(y, encoding));
        }
    }
    public void setY(String y) {
        setForceQuote(Y, y);
    }
    public String getY() {
        return getString(Y);
    }

    public void setFill(String fill, String encoding) {
        if(encoding == null) {
            setFill(fill);
        } else {
            setIgnoreQuote(FILL, Element.iconv(fill, encoding));
        }
    }
    public void setFill(String fill) {
        setForceQuote(FILL, fill);
    }
    public String getFill() {
        return getString(FILL);
    }

    public void setColour(String colour, String encoding) {
        if(encoding == null) {
            setColour(colour);
        } else {
            setIgnoreQuote(COLOUR, Element.iconv(colour, encoding));
        }
    }
    public void setColour(String colour) {
        setForceQuote(COLOUR, colour);
    }
    public String getColour() {
        return getString(COLOUR);
    }

    public void setLinetype(String linetype, String encoding) {
        if(encoding == null) {
            setLinetype(linetype);
        } else {
            setIgnoreQuote(LINETYPE, Element.iconv(linetype, encoding));
        }
    }
    public void setLinetype(String linetype) {
        setForceQuote(LINETYPE, linetype);
    }
    public String getLinetype() {
        return getString(LINETYPE);
    }
}
