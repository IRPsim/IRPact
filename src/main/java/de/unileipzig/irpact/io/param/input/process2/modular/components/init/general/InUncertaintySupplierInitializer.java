package de.unileipzig.irpact.io.param.input.process2.modular.components.init.general;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.handler.UncertaintySupplierInitializer;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.InInitializationHandler;
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
public class InUncertaintySupplierInitializer implements InInitializationHandler {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR3_HANDLER_INIT_UNCERT);

        addEntryWithDefault(res, thisClass(), "priority", asValue(InitializationHandler.NORM_PRIORITY));
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
    public int priority = InitializationHandler.NORM_PRIORITY;
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
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
    public InUncertaintySupplierInitializer copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUncertaintySupplierInitializer newCopy(CopyCache cache) {
        InUncertaintySupplierInitializer copy = new InUncertaintySupplierInitializer();
        return Dev.throwException();
    }

    @Override
    public UncertaintySupplierInitializer parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}", thisName(), getName());

        UncertaintySupplierInitializer initializer = new UncertaintySupplierInitializer();
        initializer.setName(getName());
        initializer.setPriority(getPriority());

        for(InUncertaintySupplier supplier: getUncertaintySuppliers()) {
            initializer.add(parser.parseEntityTo(supplier));
        }

        return initializer;
    }
}
