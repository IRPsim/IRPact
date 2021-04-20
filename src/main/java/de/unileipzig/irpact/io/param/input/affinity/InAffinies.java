package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Todo("einbauen")
@Definition
public class InAffinies implements InEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_AFFINITY, thisName());
        addEntry(res, thisClass(), "entries");
    }

    public String _name;

    @FieldDefinition
    public InAffinityEntry[] entries;

    public InAffinies() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InAffinityEntry findEntry(String srcCag, String tarCag) throws NoSuchElementException, ParsingException {
        if(entries == null) {
            throw new NoSuchElementException();
        }
        for(InAffinityEntry entry: entries) {
            if(Objects.equals(srcCag, entry.getSrcCagName()) && Objects.equals(tarCag, entry.getTarCagName())) {
                return entry;
            }
        }
        throw new NoSuchElementException();
    }

    public void setEntries(Collection<? extends InAffinityEntry> entries) {
        this.entries = entries.toArray(new InAffinityEntry[0]);
    }

    public void setEntries(InAffinityEntry[] entries) {
        this.entries = entries;
    }

    public InAffinityEntry[] getEntries() throws ParsingException {
        return ParamUtil.getNonNullArray(entries, "entries");
    }
}
