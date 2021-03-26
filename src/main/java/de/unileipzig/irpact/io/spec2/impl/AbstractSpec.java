package de.unileipzig.irpact.io.spec2.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.spec2.BidirectionalConverter2;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.SpecificationData2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpec<T> implements BidirectionalConverter2<T> {

    protected abstract T[] newArray(int len);

    protected abstract IRPLogger logger();

    protected SpecificationHelper2 toHelper(JsonNode node) {
        return new SpecificationHelper2(node);
    }

    protected SpecificationHelper2 toHelper(SpecificationData2.FileEntry entry) {
        return toHelper(entry.get());
    }

    protected T[] toParamArray(SpecificationData2.DirEntry entry, SpecificationJob2 job) throws ParsingException {
        T[] arr = newArray(entry.getAll().size());
        int i = 0;
        for(ObjectNode node: entry.getAll().values()) {
            arr[i++] = toParam(node, job);
        }
        return arr;
    }

    protected T[] toParamArray(SpecificationData2.FileEntry entry, SpecificationJob2 job) throws ParsingException {
        T[] arr = newArray(1);
        arr[0] = toParam(entry, job);
        return arr;
    }

    protected T toParam(SpecificationData2.FileEntry entry, SpecificationJob2 job) throws ParsingException {
        return toParam(entry.get(), job);
    }

    @Override
    public T toParam(ObjectNode root, SpecificationJob2 job) throws ParsingException {
        return toParam(toHelper(root), job);
    }

    public abstract T toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException;

    protected void create(T input, SpecificationData2.FileEntry entry, SpecificationJob2 job) throws ParsingException {
        create(input, entry.get(), job);
    }

    protected void toSpecIfNotExists(String name, SpecificationData2.DirEntry entry, T input, SpecificationJob2 job) throws ParsingException {
        if(entry.hasNot(name)) {
            create(input, entry.get(name), job);
        }
    }

    @Override
    public void create(T input, ObjectNode root, SpecificationJob2 job) throws ParsingException {
        create(input, toHelper(root), job);
    }

    protected abstract void create(T input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException;
}
