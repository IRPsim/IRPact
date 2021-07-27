package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.StageEvaluationModule;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.ModuleHelper;
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

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = ModuleHelper.MODULAR_GRAPH,
                label = "Eval-Modul",
                color = ModuleHelper.COLOR_DARK_CYAN,
                shape = ModuleHelper.SHAPE_OCTAGON,
                tags = {"InStageEvaluationModule"}
        )
)
public class InStageEvaluationModule implements InConsumerAgentEvaluationModule {

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

        addEntry(res, thisClass(), "awarenessModule");
        addEntry(res, thisClass(), "feasibilityModule");
        addEntry(res, thisClass(), "decisionMakingModule");
        addEntry(res, thisClass(), "adoptedModule");
        addEntry(res, thisClass(), "impededModule");
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
                    id = ModuleHelper.MODULAR_GRAPH,
                    label = "Eval-Kante",
                    color = ModuleHelper.COLOR_DARK_CYAN,
                    tags = "awarenessModule"
            )
    )
    public InConsumerAgentEvaluationModule[] graphedge_awarenessModule;
    public void setAwarenessModule(InConsumerAgentEvaluationModule awarenessModule) {
        this.graphedge_awarenessModule = new InConsumerAgentEvaluationModule[]{awarenessModule};
    }
    public InConsumerAgentEvaluationModule getAwarenessModule() throws ParsingException {
        return ParamUtil.getInstance(graphedge_awarenessModule, "awarenessModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = ModuleHelper.MODULAR_GRAPH,
                    label = "Eval-Kante",
                    color = ModuleHelper.COLOR_DARK_CYAN,
                    tags = "feasibilityModule"
            )
    )
    public InConsumerAgentEvaluationModule[] graphedge_feasibilityModule;
    public void setFeasibilityModule(InConsumerAgentEvaluationModule feasibilityModule) {
        this.graphedge_feasibilityModule = new InConsumerAgentEvaluationModule[]{feasibilityModule};
    }
    public InConsumerAgentEvaluationModule getFeasibilityModule() throws ParsingException {
        return ParamUtil.getInstance(graphedge_feasibilityModule, "feasibilityModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = ModuleHelper.MODULAR_GRAPH,
                    label = "Eval-Kante",
                    color = ModuleHelper.COLOR_DARK_CYAN,
                    tags = "decisionMakingModule"
            )
    )
    public InConsumerAgentEvaluationModule[] graphedge_decisionMakingModule;
    public void setDecisionMakingModule(InConsumerAgentEvaluationModule decisionMakingModule) {
        this.graphedge_decisionMakingModule = new InConsumerAgentEvaluationModule[]{decisionMakingModule};
    }
    public InConsumerAgentEvaluationModule getDecisionMakingModule() throws ParsingException {
        return ParamUtil.getInstance(graphedge_decisionMakingModule, "decisionMakingModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = ModuleHelper.MODULAR_GRAPH,
                    label = "Eval-Kante",
                    color = ModuleHelper.COLOR_DARK_CYAN,
                    tags = "adoptedModule"
            )
    )
    public InConsumerAgentEvaluationModule[] graphedge_adoptedModule;
    public void setAdoptedModule(InConsumerAgentEvaluationModule adoptedModule) {
        this.graphedge_adoptedModule = new InConsumerAgentEvaluationModule[]{adoptedModule};
    }
    public InConsumerAgentEvaluationModule getAdoptedModule() throws ParsingException {
        return ParamUtil.getInstance(graphedge_adoptedModule, "adoptedModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = ModuleHelper.MODULAR_GRAPH,
                    label = "Eval-Kante",
                    color = ModuleHelper.COLOR_DARK_CYAN,
                    tags = "impededModule"
            )
    )
    public InConsumerAgentEvaluationModule[] impededModule;
    public void setImpededModule(InConsumerAgentEvaluationModule impededModule) {
        this.impededModule = new InConsumerAgentEvaluationModule[]{impededModule};
    }
    public InConsumerAgentEvaluationModule getImpededModule() throws ParsingException {
        return ParamUtil.getInstance(impededModule, "impededModule");
    }

    public InStageEvaluationModule() {
    }

    @Override
    public InStageEvaluationModule copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InStageEvaluationModule newCopy(CopyCache cache) {
        InStageEvaluationModule copy = new InStageEvaluationModule();
        return Dev.throwException();
    }

    @Override
    public StageEvaluationModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            Module module = ModuleHelper.searchModule(parser, getName());
            if(module instanceof StageEvaluationModule) {
                return (StageEvaluationModule) module;
            } else {
                throw new ParsingException("class mismatch");
            }
        }

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
