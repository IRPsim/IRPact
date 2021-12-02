package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate.ConstructionRenovationUpdater;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
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
public class InConstructionRenovationUpdater implements InReevaluator2 {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR3_REEVAL_CONSTRENO);

        addEntry(res, thisClass(), "placeholder");
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

    @Override
    public InConstructionRenovationUpdater copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InConstructionRenovationUpdater newCopy(CopyCache cache) {
        InConstructionRenovationUpdater copy = new InConstructionRenovationUpdater();
        return Dev.throwException();
    }

    @Override
    public ConstructionRenovationUpdater parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}", thisName(), getName());

        ConstructionRenovationUpdater wrapper = new ConstructionRenovationUpdater();
        wrapper.setName(getName());

        return wrapper;
    }
}
