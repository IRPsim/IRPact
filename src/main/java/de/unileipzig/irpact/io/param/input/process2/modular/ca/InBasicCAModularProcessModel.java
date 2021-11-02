package de.unileipzig.irpact.io.param.input.process2.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.BasicCAModularProcessModel2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.InModularProcessModel2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval.InConsumerAgentEvalModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.handler.InInitializationHandler;
import de.unileipzig.irpact.io.param.input.process2.modular.reevaluate.InReevaluator2;
import de.unileipzig.irpact.io.param.input.product.initial.InNewProductHandler;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InBasicCAModularProcessModel implements InModularProcessModel2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODEL_BASIC);

        addEntry(res, thisClass(), "startModule");
        addEntry(res, thisClass(), "initializationHandlers");
        addEntry(res, thisClass(), "newProductHandlers");
        addEntry(res, thisClass(), "startOfYearReevaluators");
        addEntry(res, thisClass(), "midOfYearReevaluators");
        addEntry(res, thisClass(), "endOfYearReevaluators");
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
    public InConsumerAgentEvalModule2[] startModule = new InConsumerAgentEvalModule2[0];
    public void setStartModule(InConsumerAgentEvalModule2 startModule) {
        this.startModule = new InConsumerAgentEvalModule2[]{startModule};
    }
    public InConsumerAgentEvalModule2 getStartModule() throws ParsingException {
        return ParamUtil.getInstance(startModule, "startModule");
    }

    @FieldDefinition
    public InInitializationHandler[] initializationHandlers = new InInitializationHandler[0];
    public void setInitializationHandlers(InInitializationHandler[] initializationHandlers) {
        this.initializationHandlers = initializationHandlers;
    }
    public void setInitializationHandlers(Collection<? extends InInitializationHandler> initializationHandlers) {
        setInitializationHandlers(initializationHandlers.toArray(new InInitializationHandler[0]));
    }
    public void addInitializationHandlers(InInitializationHandler... initializationHandlers) {
        this.initializationHandlers = addAll(this.initializationHandlers, initializationHandlers);
    }
    public InInitializationHandler[] getInitializationHandlers() {
        return initializationHandlers;
    }
    public boolean hasInitializationHandlers() {
        return len(initializationHandlers) > 0;
    }

    @FieldDefinition
    public InNewProductHandler[] newProductHandlers = new InNewProductHandler[0];
    public void setNewProductHandlers(InNewProductHandler[] newProductHandlers) {
        this.newProductHandlers = newProductHandlers;
    }
    public void setNewProductHandlers(Collection<? extends InNewProductHandler> initialAdoptionHandlers) {
        setNewProductHandlers(initialAdoptionHandlers.toArray(new InNewProductHandler[0]));
    }
    public void addNewProductHandlers(InNewProductHandler... newProductHandlers) {
        this.newProductHandlers = addAll(this.newProductHandlers, newProductHandlers);
    }
    public InNewProductHandler[] getNewProductHandlers() {
        return newProductHandlers;
    }
    public boolean hasNewProductHandlers() {
        return len(newProductHandlers) > 0;
    }

    @FieldDefinition
    public InReevaluator2[] startOfYearReevaluators = new InReevaluator2[0];
    public void setStartOfYearReevaluators(InReevaluator2[] startOfYearReevaluators) {
        this.startOfYearReevaluators = startOfYearReevaluators;
    }
    public void setStartOfYearReevaluators(Collection<? extends InReevaluator2> startOfYearReevaluators) {
        setEndOfYearReevaluators(startOfYearReevaluators.toArray(new InReevaluator2[0]));
    }
    public void addStartOfYearReevaluators(InReevaluator2... startOfYearReevaluators) {
        this.startOfYearReevaluators = addAll(this.startOfYearReevaluators, startOfYearReevaluators);
    }
    public InReevaluator2[] getStartOfYearReevaluators() {
        return startOfYearReevaluators;
    }
    public boolean hasStartOfYearReevaluators() {
        return len(startOfYearReevaluators) > 0;
    }

    @FieldDefinition
    public InReevaluator2[] midOfYearReevaluators = new InReevaluator2[0];
    public void setMidOfYearReevaluators(InReevaluator2[] midOfYearReevaluators) {
        this.midOfYearReevaluators = midOfYearReevaluators;
    }
    public void setMidOfYearReevaluators(Collection<? extends InReevaluator2> midOfYearReevaluators) {
        setEndOfYearReevaluators(midOfYearReevaluators.toArray(new InReevaluator2[0]));
    }
    public void addMidOfYearReevaluators(InReevaluator2... midOfYearReevaluators) {
        this.midOfYearReevaluators = addAll(this.midOfYearReevaluators, midOfYearReevaluators);
    }
    public InReevaluator2[] getMidOfYearReevaluators() {
        return midOfYearReevaluators;
    }
    public boolean hasMidOfYearReevaluators() {
        return len(midOfYearReevaluators) > 0;
    }

    @FieldDefinition
    public InReevaluator2[] endOfYearReevaluators = new InReevaluator2[0];
    public void setEndOfYearReevaluators(InReevaluator2[] newProductHandlers) {
        this.endOfYearReevaluators = newProductHandlers;
    }
    public void setEndOfYearReevaluators(Collection<? extends InReevaluator2> endOfYearReevaluators) {
        setEndOfYearReevaluators(endOfYearReevaluators.toArray(new InReevaluator2[0]));
    }
    public void addEndOfYearReevaluator(InReevaluator2... endOfYearReevaluators) {
        this.endOfYearReevaluators = addAll(this.endOfYearReevaluators, endOfYearReevaluators);
    }
    public InReevaluator2[] getEndOfYearReevaluators() {
        return endOfYearReevaluators;
    }
    public boolean hasEndOfYearReevaluators() {
        return len(endOfYearReevaluators) > 0;
    }

    public InBasicCAModularProcessModel() {
    }

    @Override
    public InBasicCAModularProcessModel copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBasicCAModularProcessModel newCopy(CopyCache cache) {
        InBasicCAModularProcessModel copy = new InBasicCAModularProcessModel();
        return Dev.throwException();
    }

    @Override
    public BasicCAModularProcessModel2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse modulares process model ({}) '{}'", thisName(), getName());

        BasicCAModularProcessModel2 model = new BasicCAModularProcessModel2();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse start module '{}'", getStartModule().getName());
        model.setStartModule(parser.parseEntityTo(getStartModule()));

        if(hasNewProductHandlers()) {
            for(InNewProductHandler inHandler: getNewProductHandlers()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse NewProductHandler '{}'", inHandler.getName());
                model.addNewProductHandler(parser.parseEntityTo(inHandler));
            }
        }

        if(hasInitializationHandlers()) {
            for(InInitializationHandler initHandler: getInitializationHandlers()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse InitializationHandler '{}'", initHandler.getName());
                model.addInitializationHandler(parser.parseEntityTo(initHandler));
            }
        }

        if(hasStartOfYearReevaluators()) {
            for(InReevaluator2 reevaluator: getStartOfYearReevaluators()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse StartOfYear-Reevaluator '{}'", reevaluator.getName());
                model.addStartOfYearTask(parser.parseEntityTo(reevaluator));
            }
        }

        if(hasMidOfYearReevaluators()) {
            for(InReevaluator2 reevaluator: getMidOfYearReevaluators()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse MidOfYear-Reevaluator '{}'", reevaluator.getName());
                model.addMidOfYearTask(parser.parseEntityTo(reevaluator));
            }
        }

        if(hasEndOfYearReevaluators()) {
            for(InReevaluator2 reevaluator: getEndOfYearReevaluators()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse EndOfYear-Reevaluator '{}'", reevaluator.getName());
                model.addEndOfYearTask(parser.parseEntityTo(reevaluator));
            }
        }

        LOGGER.trace("add default tasks");

        return model;
    }
}
