package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.MainBranchingModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InConsumerAgentActionModule2;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = EVALRA_LABEL,
                shape = EVALRA_SHAPE,
                color = EVALRA_COLOR,
                border = EVALRA_BORDER,
                tags = {EVALRA_GRAPHNODE}
//                tags = {"colorPlaceholder"},
//                colorMode = NodeMode.PARAMETER,
//                borderMode = NodeMode.PARAMETER
        )
)
public class InMainBranchingModule_evalragraphnode2 implements InConsumerAgentEvalRAModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_EVALRA_MAINBRANCH);
        setShapeColorFillBorder(res, thisClass(), EVALRA_SHAPE, EVALRA_COLOR, EVALRA_FILL, EVALRA_BORDER);

        addEntry(res, thisClass(), "init_graphedge2");
        addEntry(res, thisClass(), "awareness_graphedge2");
        addEntry(res, thisClass(), "feasibility_graphedge2");
        addEntry(res, thisClass(), "decision_graphedge2");
        addEntry(res, thisClass(), "adopted_graphedge2");
        addEntry(res, thisClass(), "impeded_graphedge2");
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
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule init"}
            )
    )
    public InConsumerAgentEvalRAModule2[] init_graphedge2;
    public InConsumerAgentEvalRAModule2 getInit() throws ParsingException {
        return ParamUtil.getInstance(init_graphedge2, "init");
    }
    public void setInit(InConsumerAgentEvalRAModule2 first) {
        this.init_graphedge2 = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule awareness"}
            )
    )
    public InConsumerAgentEvalRAModule2[] awareness_graphedge2;
    public InConsumerAgentEvalRAModule2 getAwareness() throws ParsingException {
        return ParamUtil.getInstance(awareness_graphedge2, "awareness");
    }
    public void setAwareness(InConsumerAgentEvalRAModule2 first) {
        this.awareness_graphedge2 = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule feasibility"}
            )
    )
    public InConsumerAgentEvalRAModule2[] feasibility_graphedge2;
    public InConsumerAgentEvalRAModule2 getFeasibility() throws ParsingException {
        return ParamUtil.getInstance(feasibility_graphedge2, "feasibility");
    }
    public void setFeasibility(InConsumerAgentEvalRAModule2 first) {
        this.feasibility_graphedge2 = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule decision"}
            )
    )
    public InConsumerAgentEvalRAModule2[] decision_graphedge2;
    public InConsumerAgentEvalRAModule2 getDecision() throws ParsingException {
        return ParamUtil.getInstance(decision_graphedge2, "decision");
    }
    public void setDecision(InConsumerAgentEvalRAModule2 first) {
        this.decision_graphedge2 = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule adopted"}
            )
    )
    public InConsumerAgentActionModule2[] adopted_graphedge2;
    public InConsumerAgentActionModule2 getAdopted() throws ParsingException {
        return ParamUtil.getInstance(adopted_graphedge2, "adopted");
    }
    public void setAdopted(InConsumerAgentActionModule2 first) {
        this.adopted_graphedge2 = new InConsumerAgentActionModule2[]{first};
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MODULAR_GRAPH,
                    label = EVALRA_EDGE_LABEL,
                    color = EVALRA_EDGE_COLOR,
                    tags = {"InMainBranchingModule impeded"}
            )
    )
    public InConsumerAgentActionModule2[] impeded_graphedge2;
    public InConsumerAgentActionModule2 getImpeded() throws ParsingException {
        return ParamUtil.getInstance(impeded_graphedge2, "impeded");
    }
    public void setImpeded(InConsumerAgentActionModule2 first) {
        this.impeded_graphedge2 = new InConsumerAgentActionModule2[]{first};
    }

    public InMainBranchingModule_evalragraphnode2() {
    }

    @Override
    public InMainBranchingModule_evalragraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMainBranchingModule_evalragraphnode2 newCopy(CopyCache cache) {
        InMainBranchingModule_evalragraphnode2 copy = new InMainBranchingModule_evalragraphnode2();
        return Dev.throwException();
    }

    @Override
    public MainBranchingModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        MainBranchingModule2 module = new MainBranchingModule2();
        module.setName(getName());
        module.setInitModule(parser.parseEntityTo(getInit()));
        module.setAwarenessModule(parser.parseEntityTo(getAwareness()));
        module.setFeasibilityModule(parser.parseEntityTo(getFeasibility()));
        module.setDecisionModule(parser.parseEntityTo(getDecision()));
        module.setAdopterModule(parser.parseEntityTo(getAdopted()));
        module.setImpededModule(parser.parseEntityTo(getImpeded()));

        return module;
    }
}
