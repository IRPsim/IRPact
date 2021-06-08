package de.unileipzig.irpact.util.R.sbuilder.ggplot2;

import de.unileipzig.irpact.util.R.sbuilder.Component;
import de.unileipzig.irpact.util.R.sbuilder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class GeomBar extends Component {

    protected final Aes aes = new Aes();

    public GeomBar() {
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

    public void setPosition(String position) {
        setForceQuote(POSITION, position);
    }
    public String getPosition() {
        return getString(POSITION);
    }

    public void setStat(String stat) {
        setForceQuote(STAT, stat);
    }
    public String getStat() {
        return getString(STAT);
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
        target.append("geom_bar(");
        boolean dataAdded = appendData(settings, target);
        if(dataAdded && aes.hasData()) {
            target.append(", ");
        }
        aes.print(settings, target);
        target.append(")");
    }
}
