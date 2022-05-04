package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.CsvValueLoggingModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.SpecialUtilityCsvValueLoggingModule2;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_UTILITYLOGGING_CSV;

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
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.CALC_NODE
        ),
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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_UTILITYLOGGING_CSV)
public class InSpecialUtilityCsvValueLoggingModule3 implements InConsumerAgentCalculationModule2 {

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
    public InConsumerAgentCalculationModule2[] utilityModule;
    public InConsumerAgentCalculationModule2 getUtilityModule() throws ParsingException {
        return ParamUtil.getInstance(utilityModule, "utilityModule");
    }
    public void setUtilityModule(InConsumerAgentCalculationModule2 first) {
        this.utilityModule = new InConsumerAgentCalculationModule2[]{first};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] utilityLogger;
    public InCsvValueLoggingModule3 getUtilityLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(utilityLogger, null, "utilityLogger");
    }
    public void setUtilityLogger(InCsvValueLoggingModule3 utilityLogger) {
        this.utilityLogger = new InCsvValueLoggingModule3[]{utilityLogger};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] localShareLogger;
    public InCsvValueLoggingModule3 getLocalShareLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(localShareLogger, null, "localShareLogger");
    }
    public void setLocalShareLogger(InCsvValueLoggingModule3 localShareLogger) {
        this.localShareLogger = new InCsvValueLoggingModule3[]{localShareLogger};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] socialShareLogger;
    public InCsvValueLoggingModule3 getSocialShareLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(socialShareLogger, null, "socialShareLogger");
    }
    public void setSocialShareLogger(InCsvValueLoggingModule3 socialShareLogger) {
        this.socialShareLogger = new InCsvValueLoggingModule3[]{socialShareLogger};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] envLogger;
    public InCsvValueLoggingModule3 getEnvLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(envLogger, null, "envLogger");
    }
    public void setEnvLogger(InCsvValueLoggingModule3 envLogger) {
        this.envLogger = new InCsvValueLoggingModule3[]{envLogger};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] novLogger;
    public InCsvValueLoggingModule3 getNovLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(novLogger, null, "novLogger");
    }
    public void setNovLogger(InCsvValueLoggingModule3 novLogger) {
        this.novLogger = new InCsvValueLoggingModule3[]{novLogger};
    }

    @FieldDefinition(
            graphEdge3 = @GraphEdge(
                    graphId = CAMPMGraphSettings.GRAPH_ID,
                    color = CAMPMGraphSettings.CALC_EDGE_COLOR
            )
    )
    @LocalizedUiResource.AddEntry
    public InCsvValueLoggingModule3[] npvLogger;
    public InCsvValueLoggingModule3 getNpvLogger() throws ParsingException {
        return ParamUtil.getInstanceOr(npvLogger, null, "npvLogger");
    }
    public void setNpvLogger(InCsvValueLoggingModule3 npvLogger) {
        this.npvLogger = new InCsvValueLoggingModule3[]{npvLogger};
    }

    public InSpecialUtilityCsvValueLoggingModule3() {
    }

    public InSpecialUtilityCsvValueLoggingModule3(String name) {
        setName(name);
    }

    @Override
    public InSpecialUtilityCsvValueLoggingModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpecialUtilityCsvValueLoggingModule3 newCopy(CopyCache cache) {
        InSpecialUtilityCsvValueLoggingModule3 copy = new InSpecialUtilityCsvValueLoggingModule3();
        return Dev.throwException();
    }

    protected CsvValueLoggingModule2 parseLogger(IRPactInputParser parser, InCsvValueLoggingModule3 module) throws ParsingException {
        return module == null
                ? null
                : parser.parseEntityTo(module);
    }

    @Override
    public SpecialUtilityCsvValueLoggingModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        SpecialUtilityCsvValueLoggingModule2 module = new SpecialUtilityCsvValueLoggingModule2();
        module.setName(getName());
        module.setSubmodule(parser.parseEntityTo(getUtilityModule()));
        module.setUtilityLogger(parseLogger(parser, getUtilityLogger()));
        module.setLocalShareLogger(parseLogger(parser, getLocalShareLogger()));
        module.setSocialShareLogger(parseLogger(parser, getSocialShareLogger()));
        module.setEnvLogger(parseLogger(parser, getEnvLogger()));
        module.setNovLogger(parseLogger(parser, getNovLogger()));
        module.setNpvLogger(parseLogger(parser, getNpvLogger()));

        return module;
    }

    protected void unlink(InCsvValueLoggingModule3 module) {
        if(module != null) {
            module.removeInput();
        }
    }
}
