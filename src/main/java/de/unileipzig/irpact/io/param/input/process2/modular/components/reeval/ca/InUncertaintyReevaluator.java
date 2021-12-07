package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.modular.reevaluate.UncertaintyReevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR3_REEVAL_UNCERT;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODULAR3_REEVAL_UNCERT)
public class InUncertaintyReevaluator implements InReevaluator2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = InitializationHandler.NORM_PRIORITY
    )
    public int priorty = InitializationHandler.NORM_PRIORITY;
    public int getPriorty() {
        return priorty;
    }
    public void setPriorty(int priorty) {
        this.priorty = priorty;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
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
