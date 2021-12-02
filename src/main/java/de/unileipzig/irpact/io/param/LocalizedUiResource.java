package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructureEnum;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.util.RuleBuilder;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
public abstract class LocalizedUiResource extends TreeAnnotationResource {

    protected final MultiCounter COUNTER = new MultiCounter();
    protected String keyDelimiter = "_";
    protected Locale locale;
    protected boolean initalized = false;

    public LocalizedUiResource() {
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

    protected void setGraphString(TreeAnnotationResource.EntryBuilder builder, String key) {
        setValidString(getString(key, GRAPH_LABEL), value -> builder.setCustom(Constants.GRAPH_LABEL, value));
        setValidString(getString(key, GRAPH_DESCRIPTION), value -> builder.setCustom(Constants.GRAPH_DESCRIPTION, value));
        setValidString(getString(key, GRAPH_COLORHEADING), value -> builder.setCustom(Constants.GRAPH_COLOR_HEADING, value));
        setValidString(getString(key, GRAPH_BORDERHEADING), value -> builder.setCustom(Constants.GRAPH_BORDER_HEADING, value));
        setValidString(getString(key, GRAPH_SHAPEHEADING), value -> builder.setCustom(Constants.GRAPH_SHAPE_HEADING, value));
        setValidString(getString(key, GRAPH_ICONHEADING), value -> builder.setCustom(Constants.GRAPH_ICON_HEADING, value));
    }

    protected void setGraphNodeString(TreeAnnotationResource.EntryBuilder builder, String key) {
        setValidString(getString(key, GRAPHNODE_COLORLABEL), value -> builder.setCustom(Constants.GRAPHNODE_COLOR_LABEL, value));
        setValidString(getString(key, GRAPHNODE_SHAPELABEL), value -> builder.setCustom(Constants.GRAPHNODE_SHAPE_LABEL, value));
        setValidString(getString(key, GRAPHNODE_BORDERLABEL), value -> builder.setCustom(Constants.GRAPHNODE_BORDER_LABEL, value));
    }

    protected void setGraphEdgeString(TreeAnnotationResource.EntryBuilder builder, String key0, String key1) {
        setGraphEdgeString(builder, key0 + keyDelimiter + key1);
    }

    protected void setGraphEdgeString(TreeAnnotationResource.EntryBuilder builder, String key) {
        setValidString(getString(key, GRAPHEDGE_LABEL), value -> builder.setCustom(Constants.GRAPHEDGE_LABEL, value));
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
            setGraphString(builder, c.getSimpleName()); //graph is class only
            setGraphNodeString(builder, c.getSimpleName()); //node is class only
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
            setGraphEdgeString(builder, c.getSimpleName(), field); //edge is field only
            builder.store(c, field);
            return builder;
        } else {
            return wrapEntryBuilder(entry);
        }
    }

    //=========================
    //set
    //=========================

    public void setDelta(
            Class<?> c) {
        getBuilder(c).setEdnDelta(true);
    }

    public void setDelta(
            Class<?> c,
            String field) {
        getBuilder(c, field).setEdnDelta(true);
    }

    public void setHidden(
            Class<?> c) {
        getBuilder(c).setGamsHidden(true);
    }

    public void setHidden(
            Class<?> c,
            String field) {
        getBuilder(c, field).setGamsHidden(true);
    }

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
            String field,
            String defaultValue) {
        if(defaultValue != null && !defaultValue.isEmpty()) {
            getBuilder(c, field).setGamsDefault(defaultValue);
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
        if(domain != null && !domain.isEmpty()) {
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

    protected void putClassPath(Class<?> c, String... keys) {
        putPath(c, getCachedElements(keys));
    }

    public void putClassPath(Class<?> c, EdnPath path) {
        putClassPath(c, path.toArrayWithoutRoot());
    }

    protected void putFieldPath(Class<?> c, String field, String... keys) {
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

    //==================================================
    //meta
    //==================================================

    protected static boolean isTrueOrFalse(String str) {
        return Constants.TRUE1.equals(str) || Constants.FALSE0.equals(str);
    }

    protected static boolean isTrue(String str) {
        return Constants.TRUE1.equals(str);
    }

    protected static boolean isTrueOrFalse(int i) {
        return i == 1 || i == 0;
    }

    protected static boolean isTrue(int i) {
        return i == 1;
    }

    protected static <A extends Annotation> A getAnnotationOrNull(AnnotatedElement e, Class<A> a) {
        return e.isAnnotationPresent(a)
                ? e.getAnnotation(a)
                : null;
    }

    public <T> void parseAnnotations(
            Iterable<? extends T> c,
            Function<? super T, ? extends Class<?>> func) {
        for(T t: c) {
            parseAnnotations(func.apply(t));
        }
    }

    public void parseAnnotations(Iterable<? extends Class<?>> c) {
        for(Class<?> clazz: c) {
            parseAnnotations(clazz);
        }
    }

    public void parseAnnotations(Class<?> c) {
        if(c.isAnnotationPresent(Ignore.class)) {
            return;
        }

        parseClass(c);
        parseFields(c);
    }

    protected void parseClass(Class<?> c) {
        PutClassPath pcp = getAnnotationOrNull(c, PutClassPath.class);
        if(pcp != null) {
            apply(c, pcp);
        }
    }

    protected void parseFields(Class<?> c) {
        handleAddEntry(c);
        handleSimpleSet(c);
    }

    protected void handleAddEntry(Class<?> c) {
        Field[] fields = c.getDeclaredFields();

        //sort via map
        Map<Integer, List<Field>> validSortedFields = new TreeMap<>();
        for(Field f: fields) {
            AddEntry add = getAnnotationOrNull(f, AddEntry.class);
            if(add != null) {
                validSortedFields.computeIfAbsent(add.priority(), _priority -> new ArrayList<>()).add(f);
            }
        }

        for(List<Field> fList: validSortedFields.values()) {
            for(Field f: fList) {
                AddEntry add = f.getAnnotation(AddEntry.class);
                apply(c, f, add);
            }
        }
    }

    protected void handleSimpleSet(Class<?> c) {
        Field[] fields = c.getDeclaredFields();

        for(Field f: fields) {
            if(f.isAnnotationPresent(SimpleSet.class)) {
                apply(c, f, f.getAnnotation(SimpleSet.class));
            }
        }
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ignore {
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PutClassPath {

        TreeViewStructureEnum value();
    }

    protected void apply(Class<?> c, PutClassPath anno) {
        putClassPath(c, anno.value().getPath());
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AddEntry {

        TreeViewStructureEnum value() default TreeViewStructureEnum.NULL;

        int priority() default 0;
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SimpleSet {

        boolean boolDefault() default false; //only works with boolDomain==true
        long intDefault() default Long.MIN_VALUE; //only works if != MIN_VALUE
        double decDefault() default Double.NaN; //only works if != NaN
        String defaultValue() default "";
        String[] defaultValues() default {};
        boolean customImageDefault() default false;

        String unit() default "";
        boolean pixelUnit() default false;

        String domain() default "";
        boolean boolDomain() default false;
        boolean g0Domain() default false;
        boolean geq0Domain() default false;
        boolean customImageDomain() default false;
        boolean closed01Domain() default false;
        boolean closed0255Domain() default false;

        boolean hidden() default false;
    }

    protected void apply(Class<?> c, Field f, AddEntry add) {
        String fieldName = f.getName();
        if(add.value().isNotNull()) {
            putFieldPath(c, fieldName, add.value().getPath());
        }
        addEntry(c, fieldName);
    }

    protected void apply(Class<?> c, Field f, SimpleSet set) {
        String fieldName = f.getName();
        addEntry(c, fieldName);

        //defaults
        setDefault(c, fieldName, set.defaultValue());
        setDefault(c, fieldName, set.defaultValues());
        if(set.intDefault() != Long.MIN_VALUE) {
            setDefault(c, fieldName, Long.toString(set.intDefault()));
        }
        if(!Double.isNaN(set.decDefault())) {
            setDefault(c, fieldName, Double.toString(set.decDefault()));
        }
        if(set.boolDomain()) {
            setDefault(c, fieldName, set.boolDefault() ? TRUE1 : FALSE0);
        }
        if(set.customImageDefault()) {
            setDefault(c, fieldName, Long.toString(IRPact.INVALID_CUSTOM_IMAGE));
        }

        //unit
        setUnit(c, fieldName, set.unit());
        if(set.pixelUnit()) {
            setUnit(c, fieldName, UNIT_PIXEL);
        }

        //domain
        setDomain(c, fieldName, set.domain());
        if(set.boolDomain()) {
            setDomain(c, fieldName, DOMAIN_BOOLEAN);
        }
        if(set.g0Domain()) {
            setDefault(c, fieldName, DOMAIN_G0);
        }
        if(set.geq0Domain()) {
            setDefault(c, fieldName, DOMAIN_GEQ0);
        }
        if(set.customImageDomain()) {
            setDomain(c, fieldName, DOMAIN_CUSTOM_IMAGE);
        }
        if(set.closed01Domain()) {
            setDefault(c, fieldName, DOMAIN_CLOSED_0_1);
        }
        if(set.closed0255Domain()) {
            setDefault(c, fieldName, DOMAIN_CLOSED_0_255);
        }

        //hidden
        if(set.hidden()) {
            setHidden(c, fieldName);
        }
    }
}
