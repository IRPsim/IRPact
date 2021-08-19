package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.StageEvaluationModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
public class InStageEvaluationModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_STAGEEVAL);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "awarenessModule_graphedge");
        addEntry(res, thisClass(), "feasibilityModule_graphedge");
        addEntry(res, thisClass(), "decisionMakingModule_graphedge");
        addEntry(res, thisClass(), "adoptedModule_graphedge");
        addEntry(res, thisClass(), "impededModule_graphedge");
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
                    tags = {"InStageEvaluationModule awarenessModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] awarenessModule_graphedge;
    public void setAwarenessModule(InConsumerAgentEvaluationModule awarenessModule) {
        this.awarenessModule_graphedge = new InConsumerAgentEvaluationModule[]{awarenessModule};
    }
    public InConsumerAgentEvaluationModule getAwarenessModule() throws ParsingException {
        return ParamUtil.getInstance(awarenessModule_graphedge, "awarenessModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InStageEvaluationModule feasibilityModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] feasibilityModule_graphedge;
    public void setFeasibilityModule(InConsumerAgentEvaluationModule feasibilityModule) {
        this.feasibilityModule_graphedge = new InConsumerAgentEvaluationModule[]{feasibilityModule};
    }
    public InConsumerAgentEvaluationModule getFeasibilityModule() throws ParsingException {
        return ParamUtil.getInstance(feasibilityModule_graphedge, "feasibilityModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InStageEvaluationModule decisionMakingModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] decisionMakingModule_graphedge;
    public void setDecisionMakingModule(InConsumerAgentEvaluationModule decisionMakingModule) {
        this.decisionMakingModule_graphedge = new InConsumerAgentEvaluationModule[]{decisionMakingModule};
    }
    public InConsumerAgentEvaluationModule getDecisionMakingModule() throws ParsingException {
        return ParamUtil.getInstance(decisionMakingModule_graphedge, "decisionMakingModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InStageEvaluationModule adoptedModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] adoptedModule_graphedge;
    public void setAdoptedModule(InConsumerAgentEvaluationModule adoptedModule) {
        this.adoptedModule_graphedge = new InConsumerAgentEvaluationModule[]{adoptedModule};
    }
    public InConsumerAgentEvaluationModule getAdoptedModule() throws ParsingException {
        return ParamUtil.getInstance(adoptedModule_graphedge, "adoptedModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InStageEvaluationModule impededModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] impededModule_graphedge;
    public void setImpededModule(InConsumerAgentEvaluationModule impededModule) {
        this.impededModule_graphedge = new InConsumerAgentEvaluationModule[]{impededModule};
    }
    public InConsumerAgentEvaluationModule getImpededModule() throws ParsingException {
        return ParamUtil.getInstance(impededModule_graphedge, "impededModule");
    }

    public InStageEvaluationModule_evalgraphnode() {
    }

    @Override
    public InStageEvaluationModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InStageEvaluationModule_evalgraphnode newCopy(CopyCache cache) {
        InStageEvaluationModule_evalgraphnode copy = new InStageEvaluationModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public StageEvaluationModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), StageEvaluationModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}'", thisName(), getName());

        StageEvaluationModule module = new StageEvaluationModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setAwarenessModule(parser.parseEntityTo(getAwarenessModule()));
        module.setFeasibilityModule(parser.parseEntityTo(getFeasibilityModule()));
        module.setDecisionMakingModule(parser.parseEntityTo(getDecisionMakingModule()));
        module.setAdoptedModule(parser.parseEntityTo(getAdoptedModule()));
        module.setImpededModule(parser.parseEntityTo(getImpededModule()));

        return module;
    }
}
