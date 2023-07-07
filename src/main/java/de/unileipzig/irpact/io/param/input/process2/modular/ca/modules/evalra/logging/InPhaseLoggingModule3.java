package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.logging;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra.PhaseLoggingModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.InConsumerAgentEvalRAModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASELOGGER;

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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASELOGGER)
public class InPhaseLoggingModule3 implements InConsumerAgentEvalRALoggingModule2 {

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

    @Override
    public String getBaseName() {
        return getName();
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean enabled = true;
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @FieldDefinition(
//            graphEdge = @GraphEdge(
//                    id = MODULAR_GRAPH,
//                    label = EVALRA_EDGE_LABEL,
//                    color = EVALRA_EDGE_COLOR,
//                    tags = {"InPhaseLoggingModule input"}
//            )
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.EVALRA_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InConsumerAgentEvalRAModule2[] inputModule;
    public InConsumerAgentEvalRAModule2 getInput() throws ParsingException {
        return ParamUtil.getInstance(inputModule, "input");
    }
    public void setInput(InConsumerAgentEvalRAModule2 first) {
        this.inputModule = new InConsumerAgentEvalRAModule2[]{first};
    }

    public InPhaseLoggingModule3() {
    }

    public InPhaseLoggingModule3(String name) {
        setName(name);
    }

    @Override
    public InPhaseLoggingModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPhaseLoggingModule3 newCopy(CopyCache cache) {
        InPhaseLoggingModule3 copy = new InPhaseLoggingModule3();
        return Dev.throwException();
    }

    @Override
    public PhaseLoggingModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        PhaseLoggingModule2 module = new PhaseLoggingModule2();
        module.setName(getName());
        module.setEnabled(isEnabled());
        module.setSubmodule(parser.parseEntityTo(getInput()));

        return module;
    }
}
