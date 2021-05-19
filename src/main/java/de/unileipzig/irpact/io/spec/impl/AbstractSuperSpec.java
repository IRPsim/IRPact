package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;

import java.util.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_type;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSuperSpec<T> extends AbstractSpec<T> {

    protected static <T> List<AbstractSubSpec<? extends T>> createModels(AbstractSubSpec<? extends T>... entries) {
        List<AbstractSubSpec<? extends T>> list = CollectionUtil.arrayListOf(entries);
        validate(list);
        return list;
    }

    protected static void validate(List<? extends AbstractSubSpec<?>> list) {
        Set<String> set = new HashSet<>();
        for(AbstractSubSpec<?> spec: list) {
            if(!set.add(spec.getType())) {
                throw new IllegalArgumentException("type '" + spec.getType() + "' already exists");
            }
        }
    }

    protected abstract Collection<? extends AbstractSubSpec<? extends T>> getModels();

    @Override
    public T toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String type = rootSpec.getText(TAG_type);

        for(AbstractSubSpec<? extends T> helper: getModels()) {
            if(helper.isType(type)) {
                return helper.toParam(rootSpec, job);
            }
        }

        throw new ParsingException("unknown type: " + type);
    }

    @Override
    public void toSpec(T input, SpecificationJob job) throws ParsingException {

        for(AbstractSubSpec<? extends T> helper: getModels()) {
            if(helper.isInstance(input)) {
                helper.rawToSpec(input, job);
                return;
            }
        }

        throw new ParsingException("unknown class: " + input.getClass());
    }

    @Override
    public void create(T input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {

        for(AbstractSubSpec<? extends T> helper: getModels()) {
            if(helper.isInstance(input)) {
                helper.rawCreate(input, rootSpec, job);
                return;
            }
        }

        throw new ParsingException("unknown class: " + input.getClass());
    }
}
