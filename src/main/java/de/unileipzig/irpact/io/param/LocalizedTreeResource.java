package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irptools.util.RuleBuilder;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static de.unileipzig.irpact.io.param.IOConstants.*;

/**
 * @author Daniel Abitz
 */
public abstract class LocalizedTreeResource extends TreeAnnotationResource {

    protected final MultiCounter COUNTER = new MultiCounter();
    protected String keyDelimiter = "_";
    protected Locale locale;
    protected boolean initalized = false;

    public LocalizedTreeResource() {
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isInitalized() {
        return initalized;
    }

    public boolean isNotInitalized() {
        return !initalized;
    }

    public void checkInitalized() throws IllegalStateException {
        if(isNotInitalized()) {
            throw new IllegalStateException("not initalized");
        }
    }

    //=========================
    //resource access
    //=========================

    protected abstract String getString(String key, String tag) throws NoSuchElementException, IllegalArgumentException;

    protected void setValidString(String str, Consumer<? super String> set) {
        if(str != null && !str.isEmpty()) {
            set.accept(str);
        }
    }

    protected void setGamsStrings(TreeAnnotationResource.EntryBuilder builder, String key0, String key1) {
        setGamsStrings(builder, key0 + keyDelimiter + key1);
    }

    protected void setGamsStrings(TreeAnnotationResource.EntryBuilder builder, String key) {
        setValidString(getString(key, GAMS_IDENTIFIER), builder::setGamsIdentifier);
        setValidString(getString(key, GAMS_DESCRIPTION), builder::setGamsDescription);
        setValidString(getString(key, GAMS_UNIT), builder::setGamsUnit);
        setValidString(getString(key, GAMS_DOMAIN), builder::setGamsDomain);
        setValidString(getString(key, GAMS_DEFAULT), builder::setGamsDefault);
    }

    protected void setEdnStrings(TreeAnnotationResource.PathElementBuilder builder, String key) {
        setValidString(getString(key, EDN_LABEL), builder::setEdnLabel);
        setValidString(getString(key, EDN_DESCRIPTION), builder::setEdnDescription);
    }

    //=========================
    //create
    //=========================

    protected TreeAnnotationResource.EntryBuilder getBuilder(Class<?> c) {
        TreeAnnotationResource.Entry entry = getEntry(c);
        if(entry == null) {
            throw new NoSuchElementException("missing builder for: " + c);
        }
        return wrapEntryBuilder(entry);
    }

    protected TreeAnnotationResource.EntryBuilder getBuilder(Class<?> c, String field) {
        TreeAnnotationResource.Entry entry = getEntry(c, field);
        if(entry == null) {
            throw new NoSuchElementException("missing builder for: " + c + " " + field);
        }
        return wrapEntryBuilder(entry);
    }

    @SuppressWarnings("UnusedReturnValue")
    protected TreeAnnotationResource.PathElementBuilder computePathBuilderIfAbsent(String dataKey, String priorityKey) {
        TreeAnnotationResource.PathElementBuilder builder = getCacheAs(dataKey);
        if(builder == null) {
            builder = newElementBuilder();
            setEdnStrings(builder, dataKey);
            if(priorityKey != null) {
                builder = builder.setEdnPriority(COUNTER.getAndInc(priorityKey));
            }
            builder.putCache(dataKey);
        }
        return builder;
    }

    @SuppressWarnings("UnusedReturnValue")
    protected TreeAnnotationResource.EntryBuilder computeEntryBuilderIfAbsent(Class<?> c) {
        TreeAnnotationResource.Entry entry = getEntry(c);
        if(entry == null) {
            TreeAnnotationResource.EntryBuilder builder = newEntryBuilder();
            setGamsStrings(builder, c.getSimpleName());
            builder.store(c);
            return builder;
        } else {
            return wrapEntryBuilder(entry);
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    protected TreeAnnotationResource.EntryBuilder computeEntryBuilderIfAbsent(Class<?> c, String field) {
        TreeAnnotationResource.Entry entry = getEntry(c, field);
        if(entry == null) {
            TreeAnnotationResource.EntryBuilder builder = newEntryBuilder();
            setGamsStrings(builder, c.getSimpleName(), field);
            builder.store(c, field);
            return builder;
        } else {
            return wrapEntryBuilder(entry);
        }
    }

    //=========================
    //set
    //=========================

    public void setDefault(
            Class<?> c,
            Object[] defaults) {
        if(defaults != null && defaults.length > 0) {
            getBuilder(c).setGamsDefault(StringUtil.concat(", ", defaults));
        }
    }

    public void setDefault(
            Class<?> c,
            String field,
            Object[] defaults) {
        if(defaults != null && defaults.length > 0) {
            getBuilder(c, field).setGamsDefault(StringUtil.concat(", ", defaults));
        }
    }

    public void setDefault(
            Class<?> c,
            String[] fields,
            Object[] defaults) {
        for(String field: fields) {
            setDefault(c, field, defaults);
        }
    }

    public void setRules(
            Class<?> c,
            String field,
            String[] rules) {
        if(rules != null && rules.length > 0) {
            getBuilder(c, field).setGamsRules(rules);
        }
    }

    public void setRules(
            Class<?> c,
            String[] fields,
            RuleBuilder builder) {
        for(String field: fields) {
            setRules(c, field, builder.buildFor(field));
        }
    }

    public void setUnit(
            Class<?> c,
            String field,
            String unit) {
        if(unit != null && !unit.isEmpty()) {
            getBuilder(c, field).setGamsUnit(unit);
        }
    }

    public void setDomain(
            Class<?> c,
            String field,
            String domain) {
        if(domain != null) {
            getBuilder(c, field).setGamsDomain(domain);
        }
    }

    public void setDomain(
            Class<?> c,
            String[] fields,
            String domain) {
        for(String field: fields) {
            setDomain(c, field, domain);
        }
    }

    public void setShape(
            Class<?> c,
            String shape) {
        if(shape != null) {
            getBuilder(c).setGamsShape(shape);
        }
    }

    public void setShape(
            Class<?> c,
            String field,
            String shape) {
        if(shape != null) {
            getBuilder(c, field).setGamsShape(shape);
        }
    }

    public void setIcon(
            Class<?> c,
            String icon) {
        if(icon != null) {
            getBuilder(c).setGamsIcon(icon);
        }
    }

    public void setIcon(
            Class<?> c,
            String field,
            String icon) {
        if(icon != null) {
            getBuilder(c, field).setGamsIcon(icon);
        }
    }

    public void setColor(
            Class<?> c,
            String color) {
        if(color != null) {
            getBuilder(c).setGamsColor(color);
        }
    }

    public void setFill(
            Class<?> c,
            String fill) {
        if(fill != null) {
            getBuilder(c).setGamsFill(fill);
        }
    }

    public void setColor(
            Class<?> c,
            String field,
            String color) {
        if(color != null) {
            getBuilder(c, field).setGamsColor(color);
        }
    }

    public void setBorder(
            Class<?> c,
            String border) {
        if(border != null) {
            getBuilder(c).setGamsBorder(border);
        }
    }

    public void setBorder(
            Class<?> c,
            String field,
            String border) {
        if(border != null) {
            getBuilder(c, field).setGamsBorder(border);
        }
    }

    public void setShapeColorBorder(
            Class<?> c,
            String shape,
            String color,
            String border) {
        setShape(c, shape);
        setColor(c, color);
        //setBorder(c, border); //NOT SUPPORTED
    }

    public void setShapeColorFillBorder(
            Class<?> c,
            String shape,
            String color,
            String fill,
            String border) {
        setShape(c, shape);
        setColor(c, color);
        setFill(c, fill);
        //setBorder(c, border); //NOT SUPPORTED
    }

    //=========================
    //add
    //=========================

    public void addEntry(Class<?> c) {
        computeEntryBuilderIfAbsent(c);
    }

    public void addEntryWithShapeColorBorder(
            Class<?> c,
            String shape,
            String color,
            String border) {
        addEntry(c);
        setShapeColorBorder(c, shape, color, border);
    }

    public void addEntryWithShapeColorFillBorder(
            Class<?> c,
            String shape,
            String color,
            String fill,
            String border) {
        addEntry(c);
        setShapeColorFillBorder(c, shape, color, fill, border);
    }

    public void addEntry(Class<?> c, String field) {
        computeEntryBuilderIfAbsent(c, field);
    }

    public void addEntries(Class<?> c, String... fields) {
        for(String field: fields) {
            addEntry(c, field);
        }
    }

    public void addEntryWithDefault(Class<?> c, String field, Object[] defaults) {
        addEntry(c, field);
        setDefault(c, field, defaults);
    }

    public void addEntryWithDefaultAndDomain(
            Class<?> c,
            String field,
            Object[] defaults,
            String domain) {
        addEntry(c, field);
        setDefault(c, field, defaults);
        setDomain(c, field, domain);
    }

    public void addEntriesWithDefaultAndDomain(
            Class<?> c,
            String[] fields,
            Object[] defaults,
            String domain) {
        for(String field: fields) {
            addEntryWithDefaultAndDomain(c, field, defaults, domain);
        }
    }

    public void addPathElement(EdnPath path) {
        addPathElement(path.getLast(), path.getSecondToLast());
    }

    public void addPathElement(String dataKey, String priorityKey) {
        computePathBuilderIfAbsent(dataKey, priorityKey);
    }

    public void putClassPath(Class<?> c, String... keys) {
        putPath(c, getCachedElements(keys));
    }

    public void putClassPath(Class<?> c, EdnPath path) {
        putClassPath(c, path.toArrayWithoutRoot());
    }

    public void putFieldPath(Class<?> c, String field, String... keys) {
        putPath(c, field, getCachedElements(keys));
    }

    public void putFieldPath(Class<?> c, String field, EdnPath path) {
        putFieldPath(c, field, path.toArrayWithoutRoot());
    }

    public void putFieldPathAndAddEntry(Class<?> c, String field, EdnPath path) {
        putFieldPath(c, field, path);
        addEntry(c, field);
    }

    public void putFieldPathAndAddEntries(Class<?> c, String[] fields, EdnPath path) {
        for(String field: fields) {
            putFieldPathAndAddEntry(c, field, path);
        }
    }

    public void putFieldPathAndAddEntryWithDefaultAndDomain(
            Class<?> c,
            String field,
            EdnPath path,
            Object[] defaults,
            String domain) {
        putFieldPathAndAddEntry(c, field, path);
        setDefault(c, field, defaults);
        setDomain(c, field, domain);
    }
}
