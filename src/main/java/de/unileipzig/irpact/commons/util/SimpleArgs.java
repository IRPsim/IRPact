package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class SimpleArgs implements Args {

    protected MapSupplier mapSupplier;
    protected ListSupplier listSupplier;
    protected Map<String, List<String>> argsMapping;

    public SimpleArgs() {
        this(MapSupplier.LINKED, ListSupplier.ARRAY);
    }

    public SimpleArgs(String... args) {
        this(MapSupplier.LINKED, ListSupplier.ARRAY, args);
    }

    public SimpleArgs(MapSupplier mapSupplier, ListSupplier listSupplier) {
        this(mapSupplier, listSupplier, (String[]) null);
    }

    public SimpleArgs(MapSupplier mapSupplier, ListSupplier listSupplier, String... args) {
        this.mapSupplier = mapSupplier;
        this.listSupplier = listSupplier;
        argsMapping = mapSupplier.newMap();
        if(args != null) {
            parse(args);
        }
    }

    protected boolean isOption(String input) {
        return input.startsWith("--") || input.startsWith("-");
    }

    protected int countDashes(String input) {
        if(input.startsWith("--")) {
            return 2;
        }
        if(input.startsWith("-")) {
            return 1;
        }
        return 0;
    }

    protected boolean isValid(String option) {
        int dashes = countDashes(option);
        switch (dashes) {
            case 1:
            case 2:
                return true;

            default:
                return false;
        }
    }

    protected void validate(String option) throws IllegalArgumentException {
        if(!isValid(option)) {
            throw new IllegalArgumentException("missing dashes: " + option);
        }
    }

    public boolean has(String option) {
        int dashes = countDashes(option);
        switch (dashes) {
            case 1:
                return argsMapping.containsKey("-" + option);

            case 2:
                return argsMapping.containsKey(option);

            default:
                return false;
        }
    }

    protected String getExistingStringIfExists(String option) {
        if(argsMapping.containsKey(option)) {
            return option;
        }

        String temp = "-" + option;
        if(argsMapping.containsKey(temp)) {
            return temp;
        }

        return option;
    }

    protected static void requiresNonNull(String arg) {
        if(arg == null) {
            throw new NullPointerException("arg is null");
        }
    }

    protected static void requiresNonNull(String... args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i] == null) {
                throw new NullPointerException("arg is null at index " + i);
            }
        }
    }

    protected List<String> getArgs0(String o) {
        return argsMapping.computeIfAbsent(o, _o -> listSupplier.newList());
    }

    public List<String> getAll(String option) throws IllegalArgumentException {
        validate(option);
        String o = getExistingStringIfExists(option);
        return getArgs0(o);
    }

    public String get(String option) throws IllegalArgumentException {
        List<String> list = getAll(option);
        return list.size() < 1
                ? null
                : list.get(0);
    }

    public void add(String option) throws IllegalArgumentException {
        getAll(option);
    }

    public boolean remove(String option) throws IllegalArgumentException {
        String o = getExistingStringIfExists(option);
        return argsMapping.remove(o) != null;
    }

    public void clear(String option) throws IllegalArgumentException {
        List<String> list = getAll(option);
        list.clear();
    }

    public SimpleArgs add(String option, String arg) throws IllegalArgumentException {
        List<String> list = getAll(option);
        list.add(arg);
        return this;
    }

    public SimpleArgs set(String option) throws IllegalArgumentException {
        List<String> list = getAll(option);
        list.clear();
        return this;
    }

    public SimpleArgs set(String option, String arg) throws IllegalArgumentException {
        requiresNonNull(arg);
        List<String> list = getAll(option);
        list.clear();
        list.add(arg);
        return this;
    }

    public boolean remove(String option, String arg) throws IllegalArgumentException {
        requiresNonNull(arg);
        List<String> list = getAll(option);
        return list.remove(arg);
    }

    public SimpleArgs addAll(String option, String... args) throws IllegalArgumentException {
        requiresNonNull(args);
        List<String> list = getAll(option);
        Collections.addAll(list, args);
        return this;
    }

    public SimpleArgs setAll(String option, String... args) throws IllegalArgumentException {
        requiresNonNull(args);
        List<String> list = getAll(option);
        list.clear();
        Collections.addAll(list, args);
        return this;
    }

    public void parse(String... args) {
        requiresNonNull(args);
        List<String> list = null;
        for(String arg: args) {
            if(isOption(arg)) {
                list = getAll(arg);
            } else {
                if(list == null) {
                    throw new IllegalArgumentException("missing option before " + arg);
                }
                String a = removeQuotesIfRequired(arg);
                list.add(a);
            }
        }
    }

    protected String addQuotesIfRequired(String input) {
        if(input.contains(" ")) {
            return "\"" + input + "\"";
        } else {
            return input;
        }
    }

    protected String removeQuotesIfRequired(String input) {
        if(input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else {
            return input;
        }
    }

    public boolean collect(Collection<? super String> target) {
        boolean changed = false;
        for(Map.Entry<String, List<String>> entry: argsMapping.entrySet()) {
            String option = entry.getKey();
            changed |= target.add(option);
            for(String v: entry.getValue()) {
                String value = addQuotesIfRequired(v);
                changed |= target.add(value);
            }
        }
        return changed;
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();
        collect(list);
        return list;
    }

    @Override
    public String[] toArray() {
        List<String> list = toList();
        return list.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}
