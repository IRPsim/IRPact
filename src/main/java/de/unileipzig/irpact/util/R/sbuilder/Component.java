package de.unileipzig.irpact.util.R.sbuilder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class Component implements Element {

    protected Map<String, Element> data;

    public Component() {
        this(new LinkedHashMap<>());
    }

    public Component(Map<String, Element> data) {
        this.data = data;
        init();
    }

    protected abstract void init();

    protected boolean hasData() {
        for(Element value: data.values()) {
            if(value != null) {
                return true;
            }
        }
        return false;
    }

    protected boolean appendData(StringSettings settings, Appendable target) throws IOException {
        boolean added = false;
        boolean first = true;

        //value without key
        if(data.containsKey(null) && data.get(null) != null) {
            data.get(null).print(settings, target);
            added = true;
            first = false;
        }

        //values with key
        for(Map.Entry<String, Element> entry: data.entrySet()) {
            if(entry.getKey() == null || entry.getValue() == null) {
                continue;
            }

            if(first) {
                first = false;
            } else {
                target.append(settings.getElementDelimiter());
            }

            target.append(entry.getKey());
            target.append(settings.getEqualSign());
            entry.getValue().print(settings, target);
            added = true;
        }

        return added;
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        if(hasData()) {
            print0(settings, target);
            return true;
        } else {
            return false;
        }
    }

    protected abstract void print0(StringSettings settings, Appendable target) throws IOException;

    protected void print(String name, StringSettings settings, Appendable target) throws IOException {
        target.append(name);
        target.append("(");
        appendData(settings, target);
        target.append(")");
    }

    protected void init(String key) {
        set(key, (Element) null);
    }

    public void set(String key, String value) {
        data.put(key, new StringElement(value));
    }

    protected void setForceQuote(String key, String value) {
        data.put(key, new StringElement(value, false, true));
    }

    protected void setIgnoreQuote(String key, String value) {
        data.put(key, new StringElement(value, true, false));
    }

    protected void set(String key, Element value) {
        data.put(key, value);
    }

    public Element get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        Element element = data.get(key);
        if(element == null) {
            return null;
        }
        if(element instanceof StringElement) {
            return ((StringElement) element).getValue();
        }
        throw new IllegalArgumentException();
    }
}
