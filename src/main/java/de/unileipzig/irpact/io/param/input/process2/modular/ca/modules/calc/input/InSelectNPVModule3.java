package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SELECTNPV;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgAgentAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalAvgAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalAvgNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.NPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCACalculationModule2;
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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SELECTNPV)
public class InSelectNPVModule3 implements InConsumerAgentInputModule2 {

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

    public static final int CASE_NPVModule2 = 1;
    public static final int CASE_AssetNPVModule2 = 2;
    public static final int CASE_AnnualAvgNPVModule2 = 3;
    public static final int CASE_AnnualAvgAgentNPVModule2 = 4;
    public static final int CASE_AnnualAvgAgentAssetNPVModule2 = 5;
    public static final int CASE_GlobalAvgNPVModule2 = 6;
    public static final int CASE_GlobalAvgAssetNPVModule2 = 7;

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
        intDefault = 1,
        domain = "[1,7]"
    )
    public long npvId = 1;
    public void setNpvId(int npvId) {
        this.npvId = npvId;
    }
    public int getNpvId() {
        return (int) npvId;
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

    public InSelectNPVModule3() {
    }

    public InSelectNPVModule3(String name) {
        setName(name);
    }

    public InSelectNPVModule3(String name, InPVFile pvFile) {
        setName(name);
        setPvFile(pvFile);
    }

    @Override
    public InSelectNPVModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSelectNPVModule3 newCopy(CopyCache cache) {
        InSelectNPVModule3 copy = new InSelectNPVModule3();
        return Dev.throwException();
    }

    @Override
    public AbstractCACalculationModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}' id={}", thisName(), getName(), getNpvId());

        AbstractCACalculationModule2 module;
        switch (getNpvId()) {

            case CASE_NPVModule2:
                module = new NPVModule2();
                module.setName(getName());
                ((NPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AssetNPVModule2:
                module = new AssetNPVModule2();
                module.setName(getName());
                ((AssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualAvgNPVModule2:
                module = new AnnualAvgNPVModule2();
                module.setName(getName());
                ((AnnualAvgNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualAvgAgentNPVModule2:
                module = new AnnualAvgAgentNPVModule2();
                module.setName(getName());
                ((AnnualAvgAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualAvgAgentAssetNPVModule2:
                module = new AnnualAvgAgentAssetNPVModule2();
                module.setName(getName());
                ((AnnualAvgAgentAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalAvgNPVModule2:
                module = new GlobalAvgNPVModule2();
                module.setName(getName());
                ((GlobalAvgNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalAvgAssetNPVModule2:
                module = new GlobalAvgAssetNPVModule2();
                module.setName(getName());
                ((GlobalAvgAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            default:
                throw new ParsingException("illegal NPV-ID: " + getNpvId());
        }

        return module;
    }

    public NPVXlsxData getNPVData(InputParser parser) throws ParsingException {
        return parser.parseEntityTo(getPvFile());
    }

    private NPVXlsxData getPvFile(IRPactInputParser parser) throws ParsingException {
        if (hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            return getNPVData(parser);
        } else {
            throw new ParsingException("missing pv file");
        }
    }
}
