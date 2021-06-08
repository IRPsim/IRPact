package de.unileipzig.irpact.util.R.sbuilder;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class RScriptBuilder {

    public static final Element DEV_OFF = new StringElement("dev.off()", true, false);

    protected StringSettings settings;
    protected List<Element> elements = new ArrayList<>();
    protected boolean autoNewLine = false;

    public RScriptBuilder() {
        this(null);
    }

    public RScriptBuilder(StringSettings settings) {
        setSettings(settings);
    }

    public void setSettings(StringSettings settings) {
        this.settings = settings;
    }

    public StringSettings getSettings() {
        return settings;
    }

    public void setAutoNewLine(boolean autoNewLine) {
        this.autoNewLine = autoNewLine;
    }

    public boolean isAutoNewLine() {
        return autoNewLine;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void add(Element element) {
        elements.add(element);
    }

    public void addLinked(Element... elements){
        add(new MultiElement(elements));
    }

    public void newLine() {
        add(NewLine.INSTANCE);
    }

    public void devoff() {
        add(DEV_OFF);
    }

    public void addComment(String comment) {
        add(new Comment(comment));
    }

    public void addCommand(String str) {
        elements.add(new StringElement(str, true, false));
    }

    public void print(Appendable target) throws IOException {
        for(Element element: elements) {
            element.print(settings, target);
            if(autoNewLine && element != NewLine.INSTANCE) {
                NewLine.INSTANCE.print(settings, target);
            }
        }
    }

    public String print() {
        try {
            StringBuilder sb = new StringBuilder();
            print(sb);
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
