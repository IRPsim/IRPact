package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Options extends Component {

    public Options() {
    }

    @Override
    protected void init() {
        init(ENCODING);
    }

    public void setUtf8() {
        setEncoding(UTF8);
    }

    public void setEncoding(String encoding) {
        setForceQuote(ENCODING, encoding);
    }

    public String getEncoding() {
        return getString(ENCODING);
    }

    @Override
    protected void print0(StringSettings settings, Appendable target) throws IOException {
        print("options", settings, target);
    }
}
