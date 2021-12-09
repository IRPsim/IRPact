package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.LogisticModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = CALC_LABEL,
//                shape = CALC_SHAPE,
//                color = CALC_COLOR,
//                border = CALC_BORDER,
//                tags = {CALC_GRAPHNODE}
//        )
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.CALC_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.CALC_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.CALC_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC)
public class InLogisticModule3 implements InConsumerAgentCalculationModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), CALC_SHAPE, CALC_COLOR, CALC_FILL, CALC_BORDER);
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
            decDefault = 1
    )
    public double valueL = 1;
    public void setValueL(double valueL) {
        this.valueL = valueL;
    }
    public double getValueL() {
        return valueL;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 1
    )
    public double valueK = 1;
    public void setValueK(double valueK) {
        this.valueK = valueK;
    }
    public double getValueK() {
        return valueK;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InLogisticModule xinput"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] xinputModule;
    public InConsumerAgentCalculationModule2 getXInput() throws ParsingException {
        return ParamUtil.getInstance(xinputModule, "x");
    }
    public void setXInput(InConsumerAgentCalculationModule2 first) {
        this.xinputModule = new InConsumerAgentCalculationModule2[]{first};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InLogisticModule x0input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] x0inputModule;
    public InConsumerAgentCalculationModule2 getX0Input() throws ParsingException {
        return ParamUtil.getInstance(x0inputModule, "x0");
    }
    public void setX0Input(InConsumerAgentCalculationModule2 first) {
        this.x0inputModule = new InConsumerAgentCalculationModule2[]{first};
    }

    public InLogisticModule3() {
    }

    public InLogisticModule3(String name) {
        setName(name);
    }

    @Override
    public InLogisticModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InLogisticModule3 newCopy(CopyCache cache) {
        InLogisticModule3 copy = new InLogisticModule3();
        return Dev.throwException();
    }

    @Override
    public LogisticModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        LogisticModule2<ConsumerAgentData2> module = new LogisticModule2<>();
        module.setName(getName());
        module.setL(getValueL());
        module.setK(getValueK());
        module.setX(parser.parseEntityTo(getXInput()));
        module.setX0(parser.parseEntityTo(getX0Input()));

        return module;
    }
}
