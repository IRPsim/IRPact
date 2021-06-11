package de.unileipzig.irpact.util.R.builder.ggplot2;

import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Interaction implements Element {

    protected String first;
    protected String second;
    protected boolean ignoreQuote;
    protected boolean forceQuote;

    public Interaction(String first, String second) {
        this(first, second, false, false);
    }

    public Interaction(String first, String second, boolean ignoreQuote, boolean forceQuote) {
        this.first = first;
        this.second = second;
        this.ignoreQuote = ignoreQuote;
        this.forceQuote = forceQuote;
    }

    protected void print(String text, StringSettings settings, Appendable target) throws IOException {
        if(ignoreQuote) {
            target.append(text);
        }
        else if(forceQuote) {
            target.append(settings.quote(text));
        }
        else {
            target.append(settings.tryQuote(text));
        }
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append("interaction(");
        print(first, settings, target);
        target.append(settings.getElementDelimiter());
        print(second, settings, target);
        target.append(")");
        return true;
    }
}
