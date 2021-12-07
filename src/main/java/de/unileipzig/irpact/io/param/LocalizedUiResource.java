package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructureEnum;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.util.RuleBuilder;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithDefaultRuleBuilder;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    protected abstract IRPLogger getLogger();

    public abstract String printAll(String key);

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
        setValidString(getString(key, GRAPH_EDGEHEADING), value -> builder.setCustom(Constants.GRAPH_EDGE_HEADING, value));
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
        TreeAnnotationResource.PathElementBuilder builder = getCacheAsOr(dataKey, null);
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

    protected void setRules(
            Class<?> c,
            Collection<Field> fields,
            RuleBuilder builder) {
        for(Field field: fields) {
            setRules(c, field.getName(), builder.buildFor(field.getName()));
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
        try {
            putPath(c, getCachedElements(keys));
        } catch (NullPointerException e) {
            throw new NullPointerException(StringUtil.format(
                    "class={}, keys={} (msg: {})",
                    c.getName(),
                    Arrays.toString(keys),
                    e.getMessage()
            ));
        }
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

    protected static boolean isValid(String str) {
        return str != null && !str.isEmpty();
    }

    protected static String bool2str(boolean value) {
        return value ? Constants.TRUE1 : Constants.FALSE0;
    }

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

    public void add(Iterable<? extends ParserInput> c) {
        //1. parse annotations
        parseAnnotations(c, ParserInput::getClazz);
        //2. call init/apply
        try {
            callInitAndApply(c, ParserInput::getClazz);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
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
        //special
        handlePutGraph(c);
        handleXorWithoutUnselectRule(c);
        handleXorWithDefaultRule(c);
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
    public @interface PutGraph {

        TreeViewStructureEnum value();
    }

    protected void handlePutGraph(Class<?> c) {
        PutGraph anno = getAnnotationOrNull(c, PutGraph.class);
        if(anno != null) {
            if(anno.value().isNotNull()) {
                putClassPath(c, anno.value().getPath());
            }
            addEntry(c);
        }
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

        boolean addEntry() default false;
    }

    protected void apply(Class<?> c, PutClassPath anno) {
        putClassPath(c, anno.value().getPath());
        if(anno.addEntry()) {
            addEntry(c);
        }
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

    protected void apply(Class<?> c, Field f, AddEntry add) {
        String fieldName = f.getName();
        if(add.value().isNotNull()) {
            putFieldPath(c, fieldName, add.value().getPath());
        }
        if(add.value().isNull()) {
            addEntry(c);
        }
        addEntry(c, fieldName);
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
            setDomain(c, fieldName, DOMAIN_G0);
        }
        if(set.geq0Domain()) {
            setDomain(c, fieldName, DOMAIN_GEQ0);
        }
        if(set.customImageDomain()) {
            setDomain(c, fieldName, DOMAIN_CUSTOM_IMAGE);
        }
        if(set.closed01Domain()) {
            setDomain(c, fieldName, DOMAIN_CLOSED_0_1);
        }
        if(set.closed0255Domain()) {
            setDomain(c, fieldName, DOMAIN_CLOSED_0_255);
        }

        //hidden
        if(set.hidden()) {
            setHidden(c, fieldName);
        }
    }

    //=========================
    //SimpleRule
    //=========================

    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TodoAddSimpleRuler {
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface XorWithoutUnselectRules {

        XorWithoutUnselectRule[] value() default {};
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(XorWithoutUnselectRules.class)
    public @interface XorWithoutUnselectRule {

        String value() default DEFAULT_GROUP;

        String rawTrue() default "";
        boolean trueValue() default true;

        String rawFalse() default "";
        boolean falseValue() default false;
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface XorWithoutUnselectRuleEntry {

        String value() default DEFAULT_GROUP;
    }

    protected void handleXorWithoutUnselectRule(Class<?> c) {
        XorWithoutUnselectRules rules = getAnnotationOrNull(c, XorWithoutUnselectRules.class);
        if(rules == null || rules.value().length == 0) {
            return;
        }

        //init mappings
        Map<String, XorWithoutUnselectRule> groupedRules = new HashMap<>();
        Map<String, List<Field>> groupedFields = new HashMap<>();

        for(XorWithoutUnselectRule rule: rules.value()) {
            if(groupedRules.containsKey(rule.value())) {
                throw new IllegalArgumentException("XorWithoutUnselectRule '" + rule.value() + "' already exists.");
            }

            groupedRules.put(rule.value(), rule);
            groupedFields.put(rule.value(), new ArrayList<>());
        }

        //group fields
        Field[] fields = c.getDeclaredFields();
        for(Field f: fields) {
            XorWithoutUnselectRuleEntry entry = getAnnotationOrNull(f, XorWithoutUnselectRuleEntry.class);
            if(entry != null) {
                if(!groupedFields.containsKey(entry.value())) {
                    throw new IllegalArgumentException("'" + entry.value() + "' not found");
                }

                groupedFields.get(entry.value()).add(f);
            }
        }

        //build
        for(Map.Entry<String, List<Field>> entry: groupedFields.entrySet()) {
            List<Field> fieldList = entry.getValue();
            if(fieldList.size() > 0) {
                XorWithoutUnselectRule rule = groupedRules.get(entry.getKey());

                XorWithoutUnselectRuleBuilder builder = new XorWithoutUnselectRuleBuilder();
                builder.setKeyModifier(ParamUtil.buildDefaultParameterNameOperator(c));
                if(isValid(rule.rawTrue())) {
                    builder.setTrueValue(rule.rawTrue());
                } else {
                    builder.setTrueValue(bool2str(rule.trueValue()));
                }
                if(isValid(rule.rawFalse())) {
                    builder.setFalseValue(rule.rawFalse());
                } else {
                    builder.setFalseValue(bool2str(rule.trueValue()));
                }
                for(Field field: fieldList) {
                    builder.addKey(field.getName());
                }

                setRules(c, fieldList, builder);
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
    public @interface XorWithDefaultRules {

        XorWithDefaultRule[] value() default {};
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(XorWithDefaultRules.class)
    public @interface XorWithDefaultRule {

        String value() default DEFAULT_GROUP;

        String rawTrue() default "";
        boolean trueValue() default true;

        String rawFalse() default "";
        boolean falseValue() default false;
    }

    /**
     * @author Daniel Abitz
     */
    @Inherited
    @Documented
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface XorWithDefaultRuleEntry {

        String value() default DEFAULT_GROUP;

        boolean isDefault() default false;
    }

    protected void handleXorWithDefaultRule(Class<?> c) {
        XorWithDefaultRules rules = getAnnotationOrNull(c, XorWithDefaultRules.class);
        if(rules == null || rules.value().length == 0) {
            return;
        }

        //init mappings
        Map<String, XorWithDefaultRule> groupedRules = new HashMap<>();
        Map<String, List<Field>> groupedFields = new HashMap<>();

        for(XorWithDefaultRule rule: rules.value()) {
            if(groupedRules.containsKey(rule.value())) {
                throw new IllegalArgumentException("XorWithDefaultRule '" + rule.value() + "' already exists.");
            }

            groupedRules.put(rule.value(), rule);
            groupedFields.put(rule.value(), new ArrayList<>());
        }

        //group fields
        Field defaultField = null;

        Field[] fields = c.getDeclaredFields();
        for(Field f: fields) {
            XorWithDefaultRuleEntry entry = getAnnotationOrNull(f, XorWithDefaultRuleEntry.class);
            if(entry != null) {
                if(!groupedFields.containsKey(entry.value())) {
                    throw new IllegalArgumentException("'" + entry.value() + "' not found");
                }

                groupedFields.get(entry.value()).add(f);

                if(entry.isDefault()) {
                    if(defaultField == null) {
                        defaultField = f;
                    } else {
                        throw new IllegalArgumentException("multiple default fields: " + defaultField.getName() + " and " + f.getName());
                    }
                }
            }
        }

        if(defaultField == null) {
            throw new IllegalStateException("missing default field");
        }

        //build
        for(Map.Entry<String, List<Field>> entry: groupedFields.entrySet()) {
            List<Field> fieldList = entry.getValue();
            if(fieldList.size() > 0) {
                XorWithDefaultRule rule = groupedRules.get(entry.getKey());

                XorWithDefaultRuleBuilder builder = new XorWithDefaultRuleBuilder();
                builder.setKeyModifier(ParamUtil.buildDefaultParameterNameOperator(c));
                builder.setDefaultKey(defaultField.getName());
                if(isValid(rule.rawTrue())) {
                    builder.setTrueValue(rule.rawTrue());
                } else {
                    builder.setTrueValue(bool2str(rule.trueValue()));
                }
                if(isValid(rule.rawFalse())) {
                    builder.setFalseValue(rule.rawFalse());
                } else {
                    builder.setFalseValue(bool2str(rule.trueValue()));
                }
                for(Field field: fieldList) {
                    builder.addKey(field.getName());
                }

                setRules(c, fieldList, builder);
            }
        }
    }
}
