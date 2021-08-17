package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class StringElement implements Element {

    protected String value;
    protected boolean ignoreQuote;
    protected boolean forceQuote;

    public StringElement(String value) {
        this(value, false, false);
    }

    public StringElement(String value, boolean ignoreQuote, boolean forceQuote) {
        this.value = value;
        this.ignoreQuote = ignoreQuote;
        this.forceQuote = forceQuote;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean isPrintable() {
        return true;
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        if(ignoreQuote) {
            target.append(value);
        }
        else if(forceQuote) {
            target.append(settings.quote(value));
        }
        else {
            target.append(settings.tryQuote(value));
        }

        return true;
    }
}
