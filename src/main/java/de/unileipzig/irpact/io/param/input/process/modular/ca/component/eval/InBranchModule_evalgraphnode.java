package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.BranchModule;
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
public class InBranchModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_BRANCH);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "inputModule_graphedge");
        addEntry(res, thisClass(), "onAdoptModule_graphedge");
        addEntry(res, thisClass(), "onImpededModule_graphedge");
        addEntry(res, thisClass(), "onInProcessModule_graphedge");
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
                    tags = {"InBranchModule inputModule"}
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
                    tags = {"InBranchModule onAdoptModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] onAdoptModule_graphedge;
    public void setOnAdoptModule(InConsumerAgentEvaluationModule feasibilityModule) {
        this.onAdoptModule_graphedge = new InConsumerAgentEvaluationModule[]{feasibilityModule};
    }
    public InConsumerAgentEvaluationModule getOnAdoptModule() throws ParsingException {
        return ParamUtil.getInstance(onAdoptModule_graphedge, "onAdoptModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InBranchModule onImpededModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] onImpededModule_graphedge;
    public void setImpededModule(InConsumerAgentEvaluationModule decisionMakingModule) {
        this.onImpededModule_graphedge = new InConsumerAgentEvaluationModule[]{decisionMakingModule};
    }
    public InConsumerAgentEvaluationModule getImpededModule() throws ParsingException {
        return ParamUtil.getInstance(onImpededModule_graphedge, "onImpededModule");
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InBranchModule onInProcessModule"}
            )
    )
    public InConsumerAgentEvaluationModule[] onInProcessModule_graphedge;
    public void setOnInProcessModule(InConsumerAgentEvaluationModule adoptedModule) {
        this.onInProcessModule_graphedge = new InConsumerAgentEvaluationModule[]{adoptedModule};
    }
    public InConsumerAgentEvaluationModule getOnInProcessModule() throws ParsingException {
        return ParamUtil.getInstance(onInProcessModule_graphedge, "onInProcessModule");
    }

    public InBranchModule_evalgraphnode() {
    }

    @Override
    public InBranchModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBranchModule_evalgraphnode newCopy(CopyCache cache) {
        InBranchModule_evalgraphnode copy = new InBranchModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public BranchModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), BranchModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}'", thisName(), getName());

        BranchModule module = new BranchModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setInputModule(parser.parseEntityTo(getInputModule()));
        module.setOnAdoptModule(parser.parseEntityTo(getOnAdoptModule()));
        module.setOnImpededModule(parser.parseEntityTo(getImpededModule()));
        module.setOnInProcessModule(parser.parseEntityTo(getOnInProcessModule()));

        return module;
    }
}
