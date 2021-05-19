package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.filter.DisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_FILTER;
import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InDisabledProcessPlanNodeFilterScheme implements InRAProcessPlanNodeFilterScheme {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, PROCESS_FILTER, thisName());
        addEntry(res, thisClass(), "placeholder");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double placeholder;

    public InDisabledProcessPlanNodeFilterScheme() {
    }

    public InDisabledProcessPlanNodeFilterScheme(String name) {
        setName(name);
    }

    @Override
    public InDisabledProcessPlanNodeFilterScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDisabledProcessPlanNodeFilterScheme newCopy(CopyCache cache) {
        InDisabledProcessPlanNodeFilterScheme copy = new InDisabledProcessPlanNodeFilterScheme();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        DisabledProcessPlanNodeFilterScheme scheme = new DisabledProcessPlanNodeFilterScheme();
        scheme.setName(getName());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created DisabledProcessPlanNodeFilterScheme '{}'", getName());

        return scheme;
    }
}
