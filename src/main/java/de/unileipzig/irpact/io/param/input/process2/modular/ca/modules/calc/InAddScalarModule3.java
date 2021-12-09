package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.AddScalarModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_ADDSCALAR;

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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_ADDSCALAR)
public class InAddScalarModule3 implements InConsumerAgentCalculationModule2 {

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
            decDefault = 0
    )
    public double scalar = 0;
    public void setScalar(double scalar) {
        this.scalar = scalar;
    }
    public double getScalar() {
        return scalar;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = CALC_EDGE_LABEL,
//                    color = CALC_EDGE_COLOR,
//                    tags = {"InAddScalarModule input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationModule2[] inputModule = new InConsumerAgentCalculationModule2[0];
    public InConsumerAgentCalculationModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(inputModule, "input");
    }
    public void setInput(InConsumerAgentCalculationModule2 first) {
        this.inputModule = new InConsumerAgentCalculationModule2[]{first};
    }

    public InAddScalarModule3() {
    }

    @Override
    public InAddScalarModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAddScalarModule3 newCopy(CopyCache cache) {
        InAddScalarModule3 copy = new InAddScalarModule3();
        return Dev.throwException();
    }

    @Override
    public AddScalarModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        AddScalarModule2<ConsumerAgentData2> module = new AddScalarModule2<>();
        module.setName(getName());
        module.setScalar(getScalar());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
