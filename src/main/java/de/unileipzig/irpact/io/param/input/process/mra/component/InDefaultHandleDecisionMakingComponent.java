package de.unileipzig.irpact.io.param.input.process.mra.component;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.filter.DisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessModel;
import de.unileipzig.irpact.core.process.mra.component.special.DefaultHandleDecisionMakingComponent;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanNodeFilterScheme;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InDefaultHandleDecisionMakingComponent implements InEvaluableComponent {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MRA_COMPONENTS_DEFDEC);

        addEntry(res, thisClass(), "a");
        addEntry(res, thisClass(), "b");
        addEntry(res, thisClass(), "c");
        addEntry(res, thisClass(), "d");
        addEntry(res, thisClass(), "weightFT");
        addEntry(res, thisClass(), "weightNPV");
        addEntry(res, thisClass(), "weightSocial");
        addEntry(res, thisClass(), "weightLocal");
        addEntry(res, thisClass(), "pvFile");
        addEntry(res, thisClass(), "nodeFilterScheme");


        setDefault(res, thisClass(), "a", VALUE_0_25);
        setDefault(res, thisClass(), "b", VALUE_0_25);
        setDefault(res, thisClass(), "c", VALUE_0_25);
        setDefault(res, thisClass(), "d", VALUE_0_25);
        setDefault(res, thisClass(), "weightFT", VALUE_0_5);
        setDefault(res, thisClass(), "weightNPV", VALUE_0_5);
        setDefault(res, thisClass(), "weightSocial", VALUE_0_5);
        setDefault(res, thisClass(), "weightLocal", VALUE_0_5);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition
    public double a;
    public double getA() {
        return a;
    }
    public void setA(double a) {
        this.a = a;
    }

    @FieldDefinition
    public double b;
    public double getB() {
        return b;
    }
    public void setB(double b) {
        this.b = b;
    }

    @FieldDefinition
    public double c;
    public double getC() {
        return c;
    }
    public void setC(double c) {
        this.c = c;
    }

    @FieldDefinition
    public double d;
    public double getD() {
        return d;
    }
    public void setD(double d) {
        this.d = d;
    }

    @FieldDefinition
    protected double weightFT;
    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }
    public double getWeightFT() {
        return weightFT;
    }

    @FieldDefinition
    protected double weightNPV;
    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }
    public double getWeightNPV() {
        return weightNPV;
    }

    @FieldDefinition
    protected double weightSocial;
    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }
    public double getWeightSocial() {
        return weightSocial;
    }

    @FieldDefinition
    protected double weightLocal;
    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }
    public double getWeightLocal() {
        return weightLocal;
    }

    @FieldDefinition
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
    public InRAProcessPlanNodeFilterScheme[] nodeFilterScheme;
    public boolean hasNodeFilterScheme() {
        return ParamUtil.len(nodeFilterScheme) > 0;
    }
    public InRAProcessPlanNodeFilterScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }
    public void setNodeFilterScheme(InRAProcessPlanNodeFilterScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InRAProcessPlanNodeFilterScheme[0];
        } else {
            this.nodeFilterScheme = new InRAProcessPlanNodeFilterScheme[]{nodeFilterScheme};
        }
    }

    public InDefaultHandleDecisionMakingComponent() {
    }

    @Override
    public InDefaultHandleDecisionMakingComponent copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDefaultHandleDecisionMakingComponent newCopy(CopyCache cache) {
        InDefaultHandleDecisionMakingComponent copy = new InDefaultHandleDecisionMakingComponent();
        return Dev.throwException();
    }

    @Override
    public DefaultHandleDecisionMakingComponent parse(IRPactInputParser parser, Object input) throws ParsingException {
        ModularRAProcessModel model = getAs(input);

        DefaultHandleDecisionMakingComponent component = new DefaultHandleDecisionMakingComponent();

        component.setA(getA());
        component.setB(getB());
        component.setC(getC());
        component.setD(getD());
        component.setWeightFT(getWeightFT());
        component.setWeightNPV(getWeightNPV());
        component.setWeightSocial(getWeightSocial());
        component.setWeightLocal(getWeightLocal());
        component.setModel(model);

        if(hasNodeFilterScheme()) {
            InRAProcessPlanNodeFilterScheme inFilterScheme = getNodeFilterScheme();
            ProcessPlanNodeFilterScheme filterScheme = parser.parseEntityTo(inFilterScheme);
            component.setNodeFilterScheme(filterScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set node filter scheme '{}'", filterScheme.getName());
        } else {
            component.setNodeFilterScheme(DisabledProcessPlanNodeFilterScheme.INSTANCE);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "no node filter scheme specified");
        }

        applyPvFile(parser, component);

        return component;
    }

    private void applyPvFile(IRPactInputParser parser, DefaultHandleDecisionMakingComponent component) throws ParsingException {
        if(hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            NPVXlsxData xlsxData = parser.parseEntityTo(getPvFile());
            component.setNpvData(xlsxData);
        } else {
            LOGGER.trace("no pv file found");
        }
    }
}
