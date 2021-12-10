package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.bool.ThresholdReachedModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = BOOL_LABEL,
//                shape = BOOL_SHAPE,
//                color = BOOL_COLOR,
//                border = BOOL_BORDER,
//                tags = {BOOL_GRAPHNODE}
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.BOOL_NODE
        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.BOOL_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.BOOL_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.BOOL_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD)
public class InThresholdReachedModule3 implements InConsumerAgentBoolModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), BOOL_SHAPE, BOOL_COLOR, BOOL_FILL, BOOL_BORDER);
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
            intDefault = Module2.NORM_PRIORITY
    )
    public int priority = Module2.NORM_PRIORITY;
    public void setPriority(int priority) {
        this.priority = priority;
    }
    public int getPriority() {
        return priority;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InThresholdReachedModule draw"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] drawModule = new InConsumerAgentCalculationModule2[0];
    public InConsumerAgentCalculationModule2 getDraw() throws ParsingException {
        return ParamUtil.getInstance(drawModule, "draw");
    }
    public void setDraw(InConsumerAgentCalculationModule2 value) {
        this.drawModule = new InConsumerAgentCalculationModule2[]{value};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InThresholdReachedModule threshold"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] thresholdModule = new InConsumerAgentCalculationModule2[0];
    public InConsumerAgentCalculationModule2 getThreshold() throws ParsingException {
        return ParamUtil.getInstance(thresholdModule, "threshold");
    }
    public void setThreshold(InConsumerAgentCalculationModule2 threshold) {
        this.thresholdModule = new InConsumerAgentCalculationModule2[]{threshold};
    }

    public InThresholdReachedModule3() {
    }

    public InThresholdReachedModule3(String name) {
        setName(name);
    }

    @Override
    public InThresholdReachedModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InThresholdReachedModule3 newCopy(CopyCache cache) {
        InThresholdReachedModule3 copy = new InThresholdReachedModule3();
        return Dev.throwException();
    }

    @Override
    public ThresholdReachedModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        ThresholdReachedModule2<ConsumerAgentData2> module = new ThresholdReachedModule2<>();
        module.setName(getName());
        module.setPriority(getPriority());
        module.setDrawModule(parser.parseEntityTo(getDraw()));
        module.setThresholdModule(parser.parseEntityTo(getThreshold()));

        return module;
    }
}
