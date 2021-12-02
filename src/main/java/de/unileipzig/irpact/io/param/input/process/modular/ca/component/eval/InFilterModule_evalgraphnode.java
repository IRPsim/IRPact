package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.FilterModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
@ToRemove
public class InFilterModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_EVAL_FILTER);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "inputModule_graphedge");
        addEntry(res, thisClass(), "taskModule_graphedge");
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

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InFilterModule inputModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] inputModule_graphedge;
    public void setInputModule(InConsumerAgentEvaluationModule awarenessModule) {
        this.inputModule_graphedge = new InConsumerAgentEvaluationModule[]{awarenessModule};
    }
    public InConsumerAgentEvaluationModule getInputModule() throws ParsingException {
        return ParamUtil.getInstance(inputModule_graphedge, "inputModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InFilterModule taskModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] taskModule_graphedge;
    public void setTaskModule(InConsumerAgentEvaluationModule feasibilityModule) {
        this.taskModule_graphedge = new InConsumerAgentEvaluationModule[]{feasibilityModule};
    }
    public InConsumerAgentEvaluationModule getTaskModule() throws ParsingException {
        return ParamUtil.getInstance(taskModule_graphedge, "taskModule");
    }

    public InFilterModule_evalgraphnode() {
    }

    @Override
    public InFilterModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFilterModule_evalgraphnode newCopy(CopyCache cache) {
        InFilterModule_evalgraphnode copy = new InFilterModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public FilterModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), FilterModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}'", thisName(), getName());

        FilterModule module = new FilterModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setInputModule(parser.parseEntityTo(getInputModule()));
        module.setTaskModule(parser.parseEntityTo(getTaskModule()));

        return module;
    }
}
