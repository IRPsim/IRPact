package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.modular.reevaluate.UncertaintyReevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InUncertaintyReevaluator implements InReevaluator2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_REEVAL_UNCERT);

        addEntryWithDefault(res, thisClass(), "priorty", asValue(InitializationHandler.NORM_PRIORITY));
        addEntry(res, thisClass(), "uncertaintySuppliers");
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
    public int priorty = InitializationHandler.NORM_PRIORITY;
    public int getPriorty() {
        return priorty;
    }
    public void setPriorty(int priorty) {
        this.priorty = priorty;
    }

    @FieldDefinition
    public InUncertaintySupplier[] uncertaintySuppliers;
    public void setUncertaintySuppliers(InUncertaintySupplier... uncertaintySuppliers) {
        this.uncertaintySuppliers = uncertaintySuppliers;
    }
    public InUncertaintySupplier[] getUncertaintySuppliers() throws ParsingException {
        return getNonEmptyArray(uncertaintySuppliers, "uncertaintySuppliers");
    }

    @Override
    public InUncertaintyReevaluator copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUncertaintyReevaluator newCopy(CopyCache cache) {
        InUncertaintyReevaluator copy = new InUncertaintyReevaluator();
        return Dev.throwException();
    }

    @Override
    public UncertaintyReevaluator<?> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}", thisName(), getName());

        UncertaintyReevaluator<?> reeval = new UncertaintyReevaluator<>();
        reeval.setName(getName());
        reeval.setPriority(getPriorty());

        for(InUncertaintySupplier supplier: getUncertaintySuppliers()) {
            reeval.addSupplier(parser.parseEntityTo(supplier));
        }

        return reeval;
    }
}
