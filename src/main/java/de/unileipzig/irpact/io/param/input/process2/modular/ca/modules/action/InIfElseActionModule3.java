package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.modules.action.IfElseActionModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InConsumerAgentBoolModule2;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = ACTION_LABEL,
//                shape = ACTION_SHAPE,
//                color = ACTION_COLOR,
//                border = ACTION_BORDER,
//                tags = {ACTION_GRAPHNODE}
//        )
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.ACTION_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.ACTION_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.ACTION_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE)
public class InIfElseActionModule3 implements InConsumerAgentActionModule2 {

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
        //setShapeColorFillBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_FILL, ACTION_BORDER);
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
//                    label = BOOL_EDGE_LABEL,
//                    color = BOOL_EDGE_COLOR,
//                    tags = {"InIfElseActionModule test"}
//            ),
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.BOOL_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentBoolModule2[] testModule;
    public InConsumerAgentBoolModule2 getTestModule() throws ParsingException {
        return ParamUtil.getInstance(testModule, "test");
    }
    public void setTestModule(InConsumerAgentBoolModule2 test) {
        this.testModule = new InConsumerAgentBoolModule2[]{test};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = ACTION_EDGE_LABEL,
//                    color = ACTION_EDGE_COLOR,
//                    tags = {"InIfElseActionModule onTrue"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.ACTION_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentActionModule2[] onTrueModule;
    public InConsumerAgentActionModule2 getOnTrueModule() throws ParsingException {
        return ParamUtil.getInstance(onTrueModule, "onTrue");
    }
    public void setOnTrueModule(InConsumerAgentActionModule2 onTrue) {
        this.onTrueModule = new InConsumerAgentActionModule2[]{onTrue};
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = ACTION_EDGE_LABEL,
//                    color = ACTION_EDGE_COLOR,
//                    tags = {"InIfElseActionModule onFalse"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.ACTION_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentActionModule2[] onFalseModule;
    public InConsumerAgentActionModule2 getOnFalseModule() throws ParsingException {
        return ParamUtil.getInstance(onFalseModule, "onFalse");
    }
    public void setOnFalseModule(InConsumerAgentActionModule2 onFalse) {
        this.onFalseModule = new InConsumerAgentActionModule2[]{onFalse};
    }

    public InIfElseActionModule3() {
    }

    public InIfElseActionModule3(String name) {
        setName(name);
    }

    @Override
    public InIfElseActionModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InIfElseActionModule3 newCopy(CopyCache cache) {
        InIfElseActionModule3 copy = new InIfElseActionModule3();
        return Dev.throwException();
    }

    @Override
    public IfElseActionModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        IfElseActionModule2<ConsumerAgentData2> module = new IfElseActionModule2<>();
        module.setName(getName());
        module.setTest(parser.parseEntityTo(getTestModule()));
        module.setOnTrue(parser.parseEntityTo(getOnTrueModule()));
        module.setOnFalse(parser.parseEntityTo(getOnFalseModule()));

        return module;
    }
}
