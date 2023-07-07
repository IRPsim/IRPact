package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_LOGISTICNPV;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMaxExistingAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.AnnualMinExistingAssetNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMaxExistingAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinAgentNPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.GlobalMinExistingAssetNPVModule3;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.NPVModule2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input.NormalizedNPVModule2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.LogisticModule2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.MulScalarModule2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.SumModule2;
import de.unileipzig.irpact.core.process2.modular.modules.calc.input.ValueModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
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
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_LOGISTICNPV)
public class InNPVLogisticModule3 implements InConsumerAgentCalculationModule2 {

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

    public static final int CASE_AnnualExistingAssetNPVModule2 = 1;
    public static final int CASE_AnnualAssetNPVModule2 = 2;
    public static final int CASE_AnnualAgentNPVModule2 = 3;
    public static final int CASE_GlobalExistingAssetNPVModule3 = 4;
    public static final int CASE_GlobalAssetNPVModule3 = 5;
    public static final int CASE_GlobalAgentNPVModule2 = 6;
    public static final int CASE_test = 100;

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
            decDefault = 1
    )
    public double valueL = 1;
    public void setValueL(double valueL) {
        this.valueL = valueL;
    }
    public double getValueL() {
        return valueL;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 1
    )
    public double valueK = 1;
    public void setValueK(double valueK) {
        this.valueK = valueK;
    }
    public double getValueK() {
        return valueK;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
        intDefault = 1,
        domain = "[1,6]"
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

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = -1
    )
    public int specialId = -1;

    public InNPVLogisticModule3() {
    }

    public InNPVLogisticModule3(String name) {
        setName(name);
    }

    @Override
    public InNPVLogisticModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNPVLogisticModule3 newCopy(CopyCache cache) {
        InNPVLogisticModule3 copy = new InNPVLogisticModule3();
        return Dev.throwException();
    }

    protected CalculationModule2<ConsumerAgentData2> getXInput(IRPactInputParser parser)
        throws ParsingException {
        if (getNpvId() == CASE_test) {
            NormalizedNPVModule2 module = new NormalizedNPVModule2();
            module.setName(getName() + "_normalizedNpv");
            module.setData(getNPVData(parser));
            return module;
        } else {
            NPVModule2 module = new NPVModule2();
            module.setName(getName() + "_npv");
            module.setData(getNPVData(parser));
            return module;
        }
    }

    protected CalculationModule2<ConsumerAgentData2> getX0Input(IRPactInputParser parser)
        throws ParsingException {
        SumModule2<ConsumerAgentData2> sum = new SumModule2<>();
        sum.setName(getName() + "_sumMinMax");
        CalculationModule2<ConsumerAgentData2> min;
        CalculationModule2<ConsumerAgentData2> max;
        switch (getNpvId()) {
            case CASE_AnnualExistingAssetNPVModule2:
                min = new AnnualMinExistingAssetNPVModule2();
                ((AnnualMinExistingAssetNPVModule2) min).setData(getNPVData(parser));
                max = new AnnualMaxExistingAssetNPVModule2();
                ((AnnualMaxExistingAssetNPVModule2) max).setData(getNPVData(parser));
               break;
            case CASE_AnnualAssetNPVModule2:
                min = new AnnualMinAssetNPVModule2();
                ((AnnualMinAssetNPVModule2) min).setData(getNPVData(parser));
                max = new AnnualMaxAssetNPVModule2();
                ((AnnualMaxAssetNPVModule2) max).setData(getNPVData(parser));
                break;
            case CASE_AnnualAgentNPVModule2:
                min = new AnnualMinAgentNPVModule2();
                ((AnnualMinAgentNPVModule2) min).setData(getNPVData(parser));
                max = new AnnualMaxAgentNPVModule2();
                ((AnnualMaxAgentNPVModule2) max).setData(getNPVData(parser));
                break;
            case CASE_GlobalExistingAssetNPVModule3:
                min = new GlobalMinExistingAssetNPVModule3();
                ((GlobalMinExistingAssetNPVModule3) min).setData(getNPVData(parser));
                max = new GlobalMaxExistingAssetNPVModule3();
                ((GlobalMaxExistingAssetNPVModule3) max).setData(getNPVData(parser));
                break;
            case CASE_GlobalAssetNPVModule3:
                min = new GlobalMinAssetNPVModule3();
                ((GlobalMinAssetNPVModule3) min).setData(getNPVData(parser));
                max = new GlobalMaxAssetNPVModule3();
                ((GlobalMaxAssetNPVModule3) max).setData(getNPVData(parser));
                break;
            case CASE_GlobalAgentNPVModule2:
                min = new GlobalMinAgentNPVModule2();
                ((GlobalMinAgentNPVModule2) min).setData(getNPVData(parser));
                max = new GlobalMaxAgentNPVModule2();
                ((GlobalMaxAgentNPVModule2) max).setData(getNPVData(parser));
                break;
            case CASE_test:
                min = new ValueModule2<>();
                ((ValueModule2<?>) min).setValue(0);
                max = new ValueModule2<>();
                ((ValueModule2<?>) max).setValue(1);
                break;
            default:
                throw new IllegalStateException("unsupported npvId: " + getNpvId());
        }
        ((NameableBase) min).setName(getName() + "_min");
        ((NameableBase) max).setName(getName() + "_max");
        sum.add(min);
        sum.add(max);

        MulScalarModule2<ConsumerAgentData2> mul = new MulScalarModule2<>();
        mul.setName(getName() + "_div2");
        mul.setSubmodule(sum);
        mul.setScalar(0.5);
        return mul;
    }

    @Override
    public LogisticModule2<ConsumerAgentData2> parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        LogisticModule2<ConsumerAgentData2> module = new LogisticModule2<>();
        module.setName(getName());
        module.setL(getValueL());
        module.setK(getValueK());
        module.setX(getXInput(parser));
        module.setX0(getX0Input(parser));
        module.setSpecialId(specialId);

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
