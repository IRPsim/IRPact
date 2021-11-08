package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.filter.EntireNetworkNodeFilterScheme;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InEntireNetworkNodeFilterScheme implements InRAProcessPlanNodeFilterScheme {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_FILTER_ENTIRE);
        addEntry(res, thisClass(), "placeholder");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double placeholder;

    public InEntireNetworkNodeFilterScheme() {
    }

    public InEntireNetworkNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public InEntireNetworkNodeFilterScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InEntireNetworkNodeFilterScheme newCopy(CopyCache cache) {
        InEntireNetworkNodeFilterScheme copy = new InEntireNetworkNodeFilterScheme();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        EntireNetworkNodeFilterScheme scheme = new EntireNetworkNodeFilterScheme();
        scheme.setName(getName());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created EntireNetworkNodeFilterScheme '{}'", getName());

        return scheme;
    }
}
