package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.eval.RunUntilFailureModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.InConsumerAgentModule2;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = EVAL_LABEL,
//                shape = EVAL_SHAPE,
//                color = EVAL_COLOR,
//                border = EVAL_BORDER,
//                tags = {EVAL_GRAPHNODE}
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.EVAL_NODE
        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.EVAL_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.EVAL_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.EVAL_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL)
public class InRunUntilFailureModule3 implements InConsumerAgentEvalModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_FILL, EVAL_BORDER);
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
//                    label = EVAL_EDGE_LABEL,
//                    color = EVAL_EDGE_COLOR,
//                    tags = {"InRunUntilFailureModule input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVAL_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentModule2[] inputModule;
    public InConsumerAgentModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(inputModule, "input");
    }
    public void setInput(InConsumerAgentModule2 first) {
        this.inputModule = new InConsumerAgentModule2[]{first};
    }

    public InRunUntilFailureModule3() {
    }

    public InRunUntilFailureModule3(String name) {
        setName(name);
    }

    @Override
    public InRunUntilFailureModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRunUntilFailureModule3 newCopy(CopyCache cache) {
        InRunUntilFailureModule3 copy = new InRunUntilFailureModule3();
        return Dev.throwException();
    }

    @Override
    public RunUntilFailureModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        RunUntilFailureModule2 module = new RunUntilFailureModule2();
        module.setName(getName());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
