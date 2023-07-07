package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate.DecisionMakingReevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.InModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.InConsumerAgentModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR3_REEVAL_DEC;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODULAR3_REEVAL_DEC)
public class InDecisionMakingReevaluator implements InReevaluator2 {

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
    public InConsumerAgentModule2[] modules = new InConsumerAgentModule2[0];
    public InConsumerAgentModule2[] getModules() throws ParsingException {
        return ParamUtil.getNonEmptyArray(modules, "modules");
    }
    public void addModule(InConsumerAgentModule2 module) {
        this.modules = add(modules, module);
    }
    public void setModules(InConsumerAgentModule2... modules) {
        this.modules = modules;
    }

    public InDecisionMakingReevaluator() {
    }

    @Override
    public InDecisionMakingReevaluator copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDecisionMakingReevaluator newCopy(CopyCache cache) {
        InDecisionMakingReevaluator copy = new InDecisionMakingReevaluator();
        return Dev.throwException();
    }

    @Override
    public DecisionMakingReevaluator parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse wrapper {} '{}", thisName(), getName());

        DecisionMakingReevaluator wrapper = new DecisionMakingReevaluator();
        wrapper.setName(getName());
        for(InModule2 submodule: getModules()) {
            wrapper.addModule(parser.parseEntityTo(submodule));
        }

        return wrapper;
    }
}
