package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.MainBranchingModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InConsumerAgentActionModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_PVGENERAL_MAINBRANCH;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = EVALRA_LABEL,
//                shape = EVALRA_SHAPE,
//                color = EVALRA_COLOR,
//                border = EVALRA_BORDER,
//                tags = {EVALRA_GRAPHNODE}
////                tags = {"colorPlaceholder"},
////                colorMode = NodeMode.PARAMETER,
////                borderMode = NodeMode.PARAMETER
//        )
        graphNode3 = @de.unileipzig.irptools.defstructure.annotation.graph.GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.EVALRA_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.EVALRA_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.EVALRA_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_PVGENERAL_MAINBRANCH)
public class InMainBranchingModule3 implements InConsumerAgentEvalRAModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), EVALRA_SHAPE, EVALRA_COLOR, EVALRA_FILL, EVALRA_BORDER);
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

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule init"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVALRA_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentEvalRAModule2[] initModule;
    public InConsumerAgentEvalRAModule2 getInit() throws ParsingException {
        return ParamUtil.getInstance(initModule, "init");
    }
    public void setInit(InConsumerAgentEvalRAModule2 first) {
        this.initModule = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule awareness"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVALRA_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentEvalRAModule2[] awarenessModule;
    public InConsumerAgentEvalRAModule2 getAwareness() throws ParsingException {
        return ParamUtil.getInstance(awarenessModule, "awareness");
    }
    public void setAwareness(InConsumerAgentEvalRAModule2 first) {
        this.awarenessModule = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule feasibility"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVALRA_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentEvalRAModule2[] feasibilityModule;
    public InConsumerAgentEvalRAModule2 getFeasibility() throws ParsingException {
        return ParamUtil.getInstance(feasibilityModule, "feasibility");
    }
    public void setFeasibility(InConsumerAgentEvalRAModule2 first) {
        this.feasibilityModule = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule decision"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVALRA_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentEvalRAModule2[] decisionModule;
    public InConsumerAgentEvalRAModule2 getDecision() throws ParsingException {
        return ParamUtil.getInstance(decisionModule, "decision");
    }
    public void setDecision(InConsumerAgentEvalRAModule2 first) {
        this.decisionModule = new InConsumerAgentEvalRAModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule adopted"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.ACTION_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentActionModule2[] adoptedModule;
    public InConsumerAgentActionModule2 getAdopted() throws ParsingException {
        return ParamUtil.getInstance(adoptedModule, "adopted");
    }
    public void setAdopted(InConsumerAgentActionModule2 first) {
        this.adoptedModule = new InConsumerAgentActionModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InMainBranchingModule impeded"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.ACTION_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentActionModule2[] impededModule;
    public InConsumerAgentActionModule2 getImpeded() throws ParsingException {
        return ParamUtil.getInstance(impededModule, "impeded");
    }
    public void setImpeded(InConsumerAgentActionModule2 first) {
        this.impededModule = new InConsumerAgentActionModule2[]{first};
    }

    public InMainBranchingModule3() {
    }

    public InMainBranchingModule3(String name) {
        setName(name);
    }

    @Override
    public InMainBranchingModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMainBranchingModule3 newCopy(CopyCache cache) {
        InMainBranchingModule3 copy = new InMainBranchingModule3();
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
