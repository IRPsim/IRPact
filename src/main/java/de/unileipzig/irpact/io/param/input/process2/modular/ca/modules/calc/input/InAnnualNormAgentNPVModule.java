package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_InAnnualNormAgentNPVModule;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualNormAgentNPVModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;
import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = INPUT_LABEL,
//                shape = INPUT_SHAPE,
//                color = INPUT_COLOR,
//                border = INPUT_BORDER,
//                tags = {INPUT_GRAPHNODE}
//        )
        edn = @Edn(
                additionalTags2 = CAMPMGraphSettings.INPUT_NODE
        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.INPUT_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.INPUT_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.INPUT_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_InAnnualNormAgentNPVModule)
public class InAnnualNormAgentNPVModule implements InConsumerAgentInputModule2 {

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
//        setShapeColorFillBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_COLOR, INPUT_BORDER);
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
    public InPVFile[] pvFile;
    public boolean hasPvFile() {
        return pvFile != null && pvFile.length > 0;
    }
    public InPVFile getPvFile() throws ParsingException {
        return ParamUtil.getInstance(pvFile, "PvFile");
    }
    public void setPvFile(InPVFile pvFile) {
        this.pvFile = new InPVFile[]{pvFile};
    }

    public InAnnualNormAgentNPVModule() {
    }

    public InAnnualNormAgentNPVModule(String name) {
        setName(name);
    }

    public InAnnualNormAgentNPVModule(String name, InPVFile pvFile) {
        setName(name);
        setPvFile(pvFile);
    }

    @Override
    public InAnnualNormAgentNPVModule copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAnnualNormAgentNPVModule newCopy(CopyCache cache) {
        InAnnualNormAgentNPVModule copy = new InAnnualNormAgentNPVModule();
        return Dev.throwException();
    }

    @Override
    public AnnualNormAgentNPVModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        AnnualNormAgentNPVModule module = new AnnualNormAgentNPVModule();
        module.setName(getName());
        applyPvFile(parser, module);

        return module;
    }

    public NPVXlsxData getNPVData(InputParser parser) throws ParsingException {
        return parser.parseEntityTo(getPvFile());
    }

    private void applyPvFile(IRPactInputParser parser, AnnualNormAgentNPVModule module) throws ParsingException {
        if(hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            NPVXlsxData xlsxData = getNPVData(parser);
            module.setData(xlsxData);
        } else {
            throw new ParsingException("missing pv file");
        }
    }
}
