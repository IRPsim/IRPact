package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SELECTNPV;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualAvgExistingAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxExistingAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinExistingAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalAvgAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalAvgAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalAvgExistingAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxExistingAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinExistingAssetNPVModule3;
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
    public static final int CASE_AnnualAvgExistingAssetNPVModule2 = 2;
    public static final int CASE_AnnualAvgAssetNPVModule2 = 3;
    public static final int CASE_AnnualAvgAgentNPVModule2 = 4;
    public static final int CASE_GlobalAvgExistingAssetNPVModule3 = 5;
    public static final int CASE_GlobalAvgAssetNPVModule3 = 6;
    public static final int CASE_GlobalAvgAgentNPVModule2 = 7;
    public static final int CASE_AnnualMinExistingAssetNPVModule2 = 8;
    public static final int CASE_AnnualMaxExistingAssetNPVModule2 = 9;
    public static final int CASE_AnnualMinAssetNPVModule2 = 10;
    public static final int CASE_AnnualMaxAssetNPVModule2 = 11;
    public static final int CASE_AnnualMinAgentNPVModule2 = 12;
    public static final int CASE_AnnualMaxAgentNPVModule2 = 13;
    public static final int CASE_GlobalMinExistingAssetNPVModule3 = 14;
    public static final int CASE_GlobalMaxExistingAssetNPVModule3 = 15;
    public static final int CASE_GlobalMinAssetNPVModule3 = 16;
    public static final int CASE_GlobalMaxAssetNPVModule3 = 17;
    public static final int CASE_GlobalMinAgentNPVModule2 = 18;
    public static final int CASE_GlobalMaxAgentNPVModule2 = 19;

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
        domain = "[1,19]"
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

            case CASE_AnnualAvgExistingAssetNPVModule2:
                module = new AnnualAvgExistingAssetNPVModule2();
                module.setName(getName());
                ((AnnualAvgExistingAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualAvgAssetNPVModule2:
                module = new AnnualAvgAssetNPVModule2();
                module.setName(getName());
                ((AnnualAvgAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualAvgAgentNPVModule2:
                module = new AnnualAvgAgentNPVModule2();
                module.setName(getName());
                ((AnnualAvgAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalAvgExistingAssetNPVModule3:
                module = new GlobalAvgExistingAssetNPVModule3();
                module.setName(getName());
                ((GlobalAvgExistingAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalAvgAssetNPVModule3:
                module = new GlobalAvgAssetNPVModule3();
                module.setName(getName());
                ((GlobalAvgAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalAvgAgentNPVModule2:
                module = new GlobalAvgAgentNPVModule2();
                module.setName(getName());
                ((GlobalAvgAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMinExistingAssetNPVModule2:
                module = new AnnualMinExistingAssetNPVModule2();
                module.setName(getName());
                ((AnnualMinExistingAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMaxExistingAssetNPVModule2:
                module = new AnnualMaxExistingAssetNPVModule2();
                module.setName(getName());
                ((AnnualMaxExistingAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMinAssetNPVModule2:
                module = new AnnualMinAssetNPVModule2();
                module.setName(getName());
                ((AnnualMinAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMaxAssetNPVModule2:
                module = new AnnualMaxAssetNPVModule2();
                module.setName(getName());
                ((AnnualMaxAssetNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMinAgentNPVModule2:
                module = new AnnualMinAgentNPVModule2();
                module.setName(getName());
                ((AnnualMinAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_AnnualMaxAgentNPVModule2:
                module = new AnnualMaxAgentNPVModule2();
                module.setName(getName());
                ((AnnualMaxAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMinExistingAssetNPVModule3:
                module = new GlobalMinExistingAssetNPVModule3();
                module.setName(getName());
                ((GlobalMinExistingAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMaxExistingAssetNPVModule3:
                module = new GlobalMaxExistingAssetNPVModule3();
                module.setName(getName());
                ((GlobalMaxExistingAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMinAssetNPVModule3:
                module = new GlobalMinAssetNPVModule3();
                module.setName(getName());
                ((GlobalMinAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMaxAssetNPVModule3:
                module = new GlobalMaxAssetNPVModule3();
                module.setName(getName());
                ((GlobalMaxAssetNPVModule3) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMinAgentNPVModule2:
                module = new GlobalMinAgentNPVModule2();
                module.setName(getName());
                ((GlobalMinAgentNPVModule2) module).setData(getPvFile(parser));
                break;

            case CASE_GlobalMaxAgentNPVModule2:
                module = new GlobalMaxAgentNPVModule2();
                module.setName(getName());
                ((GlobalMaxAgentNPVModule2) module).setData(getPvFile(parser));
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
