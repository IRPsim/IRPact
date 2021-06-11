package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class ArrayElement implements Element {

    protected String[] elements;
    protected boolean ignoreQuote;
    protected boolean forceQuote;

    public ArrayElement(String[] elements) {
        this(elements, false, false);
    }

    public ArrayElement(String[] elements, boolean ignoreQuote, boolean forceQuote) {
        this.elements = elements;
        this.ignoreQuote = ignoreQuote;
        this.forceQuote = forceQuote;
    }

    protected boolean print0(String value, StringSettings settings, Appendable target) throws IOException {
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

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append("c(");
        for(int i = 0; i < elements.length; i++) {
            if(i > 0) target.append(settings.getElementDelimiter());
            print0(elements[i], settings, target);
        }
        target.append(")");
        return true;
    }
}
