package de.unileipzig.irpact.io.spec.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.distribution.UnivariateDoubleDistributionSpec;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.IntFunction;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_distribution;

/**
 * @author Daniel Abitz
 */
public final class SpecUtil {

    @SuppressWarnings("unchecked")
    public static <T> T find(Map<String, Object> cache, String key) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            throw new NoSuchElementException(key);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T putIfMissing(Map<String, Object> cache, String key, T value) {
        if(cache.containsKey(key)) {
            return (T) cache.get(key);
        } else {
            cache.put(key, value);
            return value;
        }
    }

    public static <T> void putAll(Map<String, Object> cache, T[] arr, Function<T, String> nameFunc) {
        for(T t: arr) {
            String key = nameFunc.apply(t);
            if(cache.containsKey(key)) {
                throw new IllegalArgumentException("key '" + key + "' already exists");
            } else {
                cache.put(key, t);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    public static <T> void inline(
            T input, String inputName,
            String tag,
            SpecificationManager manager,
            SpecificationHelper spec,
            SpecificationConverter converter,
            boolean inline) throws ParsingException {
        if(inline) {
            ObjectNode distRoot = spec.getObject(tag);
            ToSpecConverter toSpec = converter.getToSpecConverter(input.getClass());
            toSpec.create(input, distRoot, manager, converter, inline);
        } else {
            spec.set(tag, inputName);
            converter.callToSpec(input, manager, inline);
        }
    }

    public static <T> T parseInlined(
            String tag,
            SpecificationHelper root,
            Function<? super String, ? extends T> nameToObj,
            Function<? super SpecificationHelper, ? extends T> specToObj) {
        if(root.isInline(tag)) {
            SpecificationHelper subSpec = root.getObjectSpec(tag);
            return specToObj.apply(subSpec);
        } else {
            String name = root.getText(tag);
            return nameToObj.apply(name);
        }
    }

    public static <T extends InUnivariateDoubleDistribution> void inlineDistribution(
            T input,
            SpecificationHelper spec,
            SpecificationManager manager,
            SpecificationConverter converter,
            boolean inline) throws ParsingException {
        inlineDistribution(
                TAG_distribution,
                input,
                spec,
                manager,
                converter,
                inline
        );
    }

    public static <T extends InUnivariateDoubleDistribution> void inlineDistribution(
            String tag,
            T input,
            SpecificationHelper spec,
            SpecificationManager manager,
            SpecificationConverter converter,
            boolean inline) throws ParsingException {
        inline(
                input, input.getName(),
                tag,
                manager,
                spec,
                converter,
                inline
        );
    }

    @SuppressWarnings("unchecked")
    public static <T> T parseInlinedDistribution(
            String tag,
            SpecificationHelper root,
            SpecificationManager manager,
            SpecificationConverter converter,
            SpecificationCache cache) {
        return parseInlined(
                tag,
                root,
                name -> (T) UnivariateDoubleDistributionSpec.INSTANCE.toParamByName(name, manager, converter, cache),
                spec -> (T) UnivariateDoubleDistributionSpec.INSTANCE.toParam(spec.rootAsObject(), manager, converter, cache)
        );
    }

    public static <T> T parseInlinedDistribution(
            SpecificationHelper root,
            SpecificationManager manager,
            SpecificationConverter converter,
            SpecificationCache cache) {
        return parseInlinedDistribution(
                TAG_distribution,
                root,
                manager,
                converter,
                cache
        );
    }

    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    public static <T> void inlineArray(
            T[] inputs, Function<? super T, ? extends String> toString,
            SpecificationHelper listSpec,
            SpecificationManager manager,
            SpecificationConverter converter,
            boolean inline) throws ParsingException {
        for(T input: inputs) {
            if(inline) {
                ObjectNode root = listSpec.addObject();
                ToSpecConverter toSpec = converter.getToSpecConverter(input.getClass());
                toSpec.create(input, root, manager, converter, inline);
            } else {
                listSpec.add(toString.apply(input));
                converter.callToSpec(input, manager, inline);
            }
        }
    }

    public static <T> T[] parseInlinedArray(
            SpecificationHelper arrSpec,
            Function<? super String, ? extends T> nameToObj,
            Function<? super SpecificationHelper, ? extends T> specToObj,
            IntFunction<T[]> arrCreator) {
        T[] arr = arrCreator.apply(arrSpec.size());
        for(int i = 0; i < arrSpec.size(); i++) {
            System.out.println(arrSpec.root());
            SpecificationHelper child = arrSpec.get(i);
            System.out.println(child.root());
            if(child.isInline()) {
                arr[i] = specToObj.apply(child);
            } else {
                String text = child.getText();
                arr[i] = nameToObj.apply(text);
            }
        }
        return arr;
    }
}
