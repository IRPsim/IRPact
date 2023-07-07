package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.modules.action.ReevaluatorModule;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = REEVAL_LABEL,
//                shape = REEVAL_SHAPE,
//                color = REEVAL_COLOR,
//                border = REEVAL_BORDER,
//                tags = {REEVAL_GRAPHNODE}
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.REEVAL_NODE
        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.REEVAL_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.REEVAL_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.REEVAL_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL)
public class InReevaluatorModule3 implements InConsumerAgentReevaluationModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), REEVAL_SHAPE, REEVAL_COLOR, REEVAL_FILL, REEVAL_BORDER);
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
//                    tags = {"InReevaluatorModule input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.REEVAL_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentModule2[] inputModule;
    public InConsumerAgentModule2[] getInput() throws ParsingException {
        return ParamUtil.getNonNullArray(inputModule, "input");
    }
    public void setInput(InConsumerAgentModule2... input) {
        this.inputModule = input;
    }

    public InReevaluatorModule3() {
    }

    public InReevaluatorModule3(String name) {
        setName(name);
    }

    @Override
    public InReevaluatorModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InReevaluatorModule3 newCopy(CopyCache cache) {
        InReevaluatorModule3 copy = new InReevaluatorModule3();
        return Dev.throwException();
    }

    @Override
    public ReevaluatorModule<?> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse reevaluator {} '{}", thisName(), getName());

        ReevaluatorModule<?> reevaluator = new ReevaluatorModule<>();
        reevaluator.setName(getName());
        for(InConsumerAgentModule2 module: getInput()) {
            reevaluator.addSubmodule(parser.parseEntityTo(module));
        }

        return reevaluator;
    }
}
