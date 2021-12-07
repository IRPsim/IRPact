package de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.MultiReevaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval.InConsumerAgentReevaluationModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.InReevaluator2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODULAR3_REEVAL_LINKER;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODULAR3_REEVAL_LINKER)
public class InMultiReevaluator implements InReevaluator2 {

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
    public InConsumerAgentReevaluationModule2[] modules;
    public InConsumerAgentReevaluationModule2[] getModules() throws ParsingException {
        return ParamUtil.getNonEmptyArray(modules, "modules");
    }
    public void setModules(InConsumerAgentReevaluationModule2... modules) {
        this.modules = modules;
    }

    public InMultiReevaluator() {
    }

    @Override
    public InMultiReevaluator copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMultiReevaluator newCopy(CopyCache cache) {
        InMultiReevaluator copy = new InMultiReevaluator();
        return Dev.throwException();
    }

    @Override
    public MultiReevaluator<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse {} '{}'", thisName(), getName());

        MultiReevaluator<ConsumerAgentData2> wrapper = new MultiReevaluator<>();
        wrapper.setName(getName());
        for(InConsumerAgentReevaluationModule2 module: getModules()) {
            wrapper.addReevaluator(parser.parseEntityTo(module));
        }

        return wrapper;
    }
}
