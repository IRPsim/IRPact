package de.unileipzig.irpact.io.spec.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.spec.BidirectionalConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.SpecificationData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpec<T> implements BidirectionalConverter<T> {

    protected abstract T[] newArray(int len);

    protected abstract IRPLogger logger();

    protected SpecificationHelper toHelper(JsonNode node) {
        return new SpecificationHelper(node);
    }

    protected SpecificationHelper toHelper(SpecificationData.FileEntry entry) {
        return toHelper(entry.get());
    }

    protected T[] toParamArray(SpecificationData.DirEntry entry, SpecificationJob job) throws ParsingException {
        T[] arr = newArray(entry.getAll().size());
        int i = 0;
        for(ObjectNode node: entry.getAll().values()) {
            arr[i++] = toParam(node, job);
        }
        return arr;
    }

    protected T[] toParamArray(SpecificationData.FileEntry entry, SpecificationJob job) throws ParsingException {
        T[] arr = newArray(1);
        arr[0] = toParam(entry, job);
        return arr;
    }

    protected T toParam(SpecificationData.FileEntry entry, SpecificationJob job) throws ParsingException {
        return toParam(entry.get(), job);
    }

    @Override
    public T toParam(ObjectNode root, SpecificationJob job) throws ParsingException {
        return toParam(toHelper(root), job);
    }

    public abstract T toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException;

    protected void create(T input, SpecificationData.FileEntry entry, SpecificationJob job) throws ParsingException {
        create(input, entry.get(), job);
    }

    protected void toSpecIfNotExists(String name, SpecificationData.DirEntry entry, T input, SpecificationJob job) throws ParsingException {
        if(entry.hasNot(name)) {
            create(input, entry.get(name), job);
        }
    }

    @Override
    public void create(T input, ObjectNode root, SpecificationJob job) throws ParsingException {
        create(input, toHelper(root), job);
    }

    protected abstract void create(T input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException;
}
