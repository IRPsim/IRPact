package de.unileipzig.irpact.io.param.input.process2.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.process2.modular.InModularProcessModel2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InDummyColor implements InModularProcessModel2 {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;
    @Override
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition
    public double placeholder = 0;

    public InDummyColor() {
    }

    @Override
    public InDummyColor copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDummyColor newCopy(CopyCache cache) {
        InDummyColor copy = new InDummyColor();
        return Dev.throwException();
    }

    @Override
    public Void parse(IRPactInputParser parser) throws ParsingException {
        return null;
    }
}
