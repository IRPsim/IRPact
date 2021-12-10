package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.DecisionMakingDeciderModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InConsumerAgentBoolModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DECISIONDECIDER;

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
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.EVALRA_NODE
        ),
        graphNode3 = @GraphNode(
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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DECISIONDECIDER)
public class InDecisionMakingDeciderModule3 implements InConsumerAgentEvalRAModule2 {

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

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean forceEvaluation = false;
    public boolean isForceEvaluation() {
        return forceEvaluation;
    }
    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = BOOL_EDGE_LABEL,
//                    color = BOOL_EDGE_COLOR,
//                    tags = {"InDecisionMakingDeciderModule2 finCheck"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.BOOL_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentBoolModule2[] finCheckModule;
    public InConsumerAgentBoolModule2 getFinCheck() throws ParsingException {
        return ParamUtil.getInstance(finCheckModule, "finCheck");
    }
    public void setFinCheck(InConsumerAgentBoolModule2 finCheck) {
        this.finCheckModule = new InConsumerAgentBoolModule2[]{finCheck};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InDecisionMakingDeciderModule2 threshold"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] thresholdModule;
    public InConsumerAgentCalculationModule2 getThreshold() throws ParsingException {
        return ParamUtil.getInstance(thresholdModule, "threshold");
    }
    public void setThreshold(InConsumerAgentCalculationModule2 threshold) {
        this.thresholdModule = new InConsumerAgentCalculationModule2[]{threshold};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InDecisionMakingDeciderModule2 utility"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] utilityModule;
    public InConsumerAgentCalculationModule2 getUtility() throws ParsingException {
        return ParamUtil.getInstance(utilityModule, "utility");
    }
    public void setUtility(InConsumerAgentCalculationModule2 utility) {
        this.utilityModule = new InConsumerAgentCalculationModule2[]{utility};
    }

    public InDecisionMakingDeciderModule3() {
    }

    public InDecisionMakingDeciderModule3(String name) {
        setName(name);
    }

    @Override
    public InDecisionMakingDeciderModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDecisionMakingDeciderModule3 newCopy(CopyCache cache) {
        InDecisionMakingDeciderModule3 copy = new InDecisionMakingDeciderModule3();
        return Dev.throwException();
    }

    @Override
    public DecisionMakingDeciderModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        DecisionMakingDeciderModule2 module = new DecisionMakingDeciderModule2();
        module.setName(getName());
        module.setForceEvaluation(isForceEvaluation());
        module.setFinancialCheckModule(parser.parseEntityTo(getFinCheck()));
        module.setThresholdModule(parser.parseEntityTo(getThreshold()));
        module.setDecisionMakingModule(parser.parseEntityTo(getUtility()));

        return module;
    }
}
