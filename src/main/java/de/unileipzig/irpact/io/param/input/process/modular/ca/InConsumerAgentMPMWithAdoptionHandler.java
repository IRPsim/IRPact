package de.unileipzig.irpact.io.param.input.process.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPMWithAdoptionHandler;
import de.unileipzig.irpact.core.product.handler.NewProductHandler;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
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
@ToRemove
public class InConsumerAgentMPMWithAdoptionHandler implements InConsumerAgentModularProcessModel {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_MODEL_SIMPLE);

        addEntry(res, thisClass(), "startModule");
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
    public InConsumerAgentEvaluationModule[] startModule = new InConsumerAgentEvaluationModule[0];
    public void setStartModule(InConsumerAgentEvaluationModule startModule) {
        this.startModule = new InConsumerAgentEvaluationModule[]{startModule};
    }
    public InConsumerAgentEvaluationModule getStartModule() throws ParsingException {
        return ParamUtil.getInstance(startModule, "startModule");
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

    public InConsumerAgentMPMWithAdoptionHandler() {
    }

    @Override
    public InConsumerAgentMPMWithAdoptionHandler copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InConsumerAgentMPMWithAdoptionHandler newCopy(CopyCache cache) {
        InConsumerAgentMPMWithAdoptionHandler copy = new InConsumerAgentMPMWithAdoptionHandler();
        return Dev.throwException();
    }

    @Override
    public ConsumerAgentMPMWithAdoptionHandler parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            ProcessModel model = MPMSettings.searchModel(parser, getName());
            if(model instanceof ConsumerAgentMPMWithAdoptionHandler) {
                return (ConsumerAgentMPMWithAdoptionHandler) model;
            } else {
                throw new ParsingException("class mismatch");
            }
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse modulares process model ({}) '{}'", thisName(), getName());

        ConsumerAgentMPMWithAdoptionHandler model = new ConsumerAgentMPMWithAdoptionHandler();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());

        Rnd rnd = parser.deriveRnd();
        model.setRnd(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set rnd-seed for '{}' ({}): {}", getName(), thisName(), rnd.getInitialSeed());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse start module '{}'", getStartModule().getName());
        ConsumerAgentEvaluationModule startModule = parser.parseEntityTo(getStartModule());
        model.setStartModule(startModule);

        if(hasNewProductHandlers()) {
            for(InNewProductHandler inHandler: getNewProductHandlers()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse NewProductHandler '{}'", inHandler.getName());
                NewProductHandler handler = parser.parseEntityTo(inHandler);
                model.addNewProductHandler(handler);
            }
        }

        return model;
    }
}
