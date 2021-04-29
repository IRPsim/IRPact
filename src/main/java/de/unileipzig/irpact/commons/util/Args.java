package de.unileipzig.irpact.commons.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class Args {

    protected Supplier<? extends Map<String, List<String>>> mapSupplier;
    protected Map<String, List<String>> args;

    public Args() {
        this(LinkedHashMap::new);
    }

    public Args(Supplier<? extends Map<String, List<String>>> mapSupplier) {
        this.mapSupplier = mapSupplier;
        args = mapSupplier.get();
    }

    protected boolean isOption(String input) {
        return input.startsWith("--") || input.startsWith("-");
    }

    protected String removePrefix(String option) {
        if(option.startsWith("--")) {
            return option.substring(2);
        }
        if(option.startsWith("-")) {
            return option.substring(1);
        }
        return option;
    }

    protected String addPrefix(String option) {
        if(option.length() == 1) {
            return "--" + option;
        } else {
            return "-" + option;
        }
    }

    protected String addQuotes(String input) {
        if(input.contains(" ")) {
            return "\"" + input + "\"";
        } else {
            return input;
        }
    }

    protected String removeQuotes(String input) {
        if(input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        } else {
            return input;
        }
    }

    public Map<String, List<String>> getMapping() {
        return args;
    }

    public Args set(String option, String... values) {
        String o = removePrefix(option);
        List<String> vList = args.computeIfAbsent(o, _o -> new ArrayList<>());
        vList.clear();
        for(String value: values) {
            String v = removeQuotes(value);
            vList.add(v);
        }
        return this;
    }

    protected void addTo(String[] inputs, Map<String, List<String>> target) {
        List<String> lastList = null;
        for(String input: inputs) {
            if(isOption(input)) {
                String o = removePrefix(input);
                lastList = target.computeIfAbsent(o, _o -> new ArrayList<>());
            } else {
                if(lastList == null) {
                    throw new IllegalArgumentException("missing option before " + input);
                }
                String v = removeQuotes(input);
                lastList.add(v);
            }
        }
    }

    public Args setAll(String... inputs) {
        args.clear();
        addTo(inputs, args);
        return this;
    }

    public Args putAll(String... inputs) {
        Map<String, List<String>> newArgs = mapSupplier.get();
        addTo(inputs, newArgs);
        args.putAll(newArgs);
        return this;
    }

    public Args addAll(String... inputs) {
        Map<String, List<String>> newArgs = mapSupplier.get();
        addTo(inputs, newArgs);

        for(Map.Entry<String, List<String>> newEntry: newArgs.entrySet()) {
            List<String> list = args.computeIfAbsent(newEntry.getKey(), _key -> new ArrayList<>());
            list.addAll(newEntry.getValue());
        }

        return this;
    }

    public List<String> getAll(String option) {
        String o = removePrefix(option);
        return args.get(o);
    }

    public String get(String option) {
        List<String> list = getAll(option);
        return list.isEmpty() ? null : list.get(0);
    }

    public String[] toArray() {
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, List<String>> entry: args.entrySet()) {
            String o = entry.getKey();
            String option = addPrefix(o);
            list.add(option);
            for(String v: entry.getValue()) {
                String value = addQuotes(v);
                list.add(value);
            }
        }
        return list.toArray(new String[0]);
    }
}
