package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.bool.BernoulliModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_BOOL_BERNOULLI;

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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_BOOL_BERNOULLI)
public class InBernoulliModule3 implements InConsumerAgentBoolModule2 {

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
//                    tags = {"InBernoulliModule input"}
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

    public InBernoulliModule3() {
    }

    public InBernoulliModule3(String name) {
        setName(name);
    }

    @Override
    public InBernoulliModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBernoulliModule3 newCopy(CopyCache cache) {
        InBernoulliModule3 copy = new InBernoulliModule3();
        return Dev.throwException();
    }

    @Override
    public BernoulliModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        BernoulliModule2 module = new BernoulliModule2();
        module.setName(getName());
        module.setPriority(getPriority());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
