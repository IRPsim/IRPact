package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Component;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class GeomLine extends Component {

    protected final Aes aes = new Aes();

    public GeomLine() {
    }

    public Aes aes() {
        return aes;
    }

    public void setData(String data) {
        setIgnoreQuote(DATA, data);
    }

    public String getData() {
        return getString(DATA);
    }

    public void setSize(String size) {
        setIgnoreQuote(SIZE, size);
    }
    public void setSize(int size) {
        setSize(Integer.toString(size));
    }
    public void setSize(double size) {
        setSize(Double.toString(size));
    }

    public void setLineType(String type) {
        setForceQuote(LINETYPE, type);
    }
    public String getLineType() {
        return getString(LINETYPE);
    }

    @Override
    protected boolean hasData() {
        return true;
    }

    @Override
    protected void init() {
        init(DATA);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        target.append("geom_line(");
        boolean dataAdded = appendData(settings, target);
        if(dataAdded && aes.hasData()) {
            target.append(", ");
        }
        aes.print(settings, target);
        target.append(")");
    }
}
