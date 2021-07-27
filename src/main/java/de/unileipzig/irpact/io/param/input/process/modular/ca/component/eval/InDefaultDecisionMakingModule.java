package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.filter.DisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.DefaultDecisionMakingModule;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.modular.ca.ModuleHelper;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanNodeFilterScheme;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = ModuleHelper.MODULAR_GRAPH,
                label = "Eval-Modul",
                color = ModuleHelper.COLOR_DARK_CYAN,
                shape = ModuleHelper.SHAPE_OCTAGON,
                tags = {"InDefaultDecisionMakingModule"}
        )
)
public class InDefaultDecisionMakingModule implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTDECISION);

        addEntry(res, thisClass(), "a");
        addEntry(res, thisClass(), "b");
        addEntry(res, thisClass(), "c");
        addEntry(res, thisClass(), "d");
        addEntry(res, thisClass(), "weightFT");
        addEntry(res, thisClass(), "weightNPV");
        addEntry(res, thisClass(), "weightSocial");
        addEntry(res, thisClass(), "weightLocal");
        addEntry(res, thisClass(), "logisticFactor");
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
        setDefault(res, thisClass(), "logisticFactor", varargs(RAConstants.DEFAULT_LOGISTIC_FACTOR));
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
    public double weightFT;
    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }
    public double getWeightFT() {
        return weightFT;
    }

    @FieldDefinition
    public double weightNPV;
    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }
    public double getWeightNPV() {
        return weightNPV;
    }

    @FieldDefinition
    public double weightSocial;
    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }
    public double getWeightSocial() {
        return weightSocial;
    }

    @FieldDefinition
    public double weightLocal;
    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }
    public double getWeightLocal() {
        return weightLocal;
    }

    @FieldDefinition
    public double logisticFactor;
    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
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

    public InDefaultDecisionMakingModule() {
    }

    @Override
    public InDefaultDecisionMakingModule copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDefaultDecisionMakingModule newCopy(CopyCache cache) {
        InDefaultDecisionMakingModule copy = new InDefaultDecisionMakingModule();
        return Dev.throwException();
    }

    public void setDefaultValues() {
        setABCD(0.25);
        setWeightFT(0.5);
        setWeightNPV(0.5);
        setWeightLocal(0.5);
        setWeightSocial(0.5);
        setLogisticFactor(RAConstants.DEFAULT_LOGISTIC_FACTOR);
    }

    public void setABCD(double abcd) {
        setA(abcd);
        setB(abcd);
        setC(abcd);
        setD(abcd);
    }

    @Override
    public DefaultDecisionMakingModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            Module module = ModuleHelper.searchModule(parser, getName());
            if(module instanceof DefaultDecisionMakingModule) {
                DefaultDecisionMakingModule ori = (DefaultDecisionMakingModule) module;
                applyPvFile(parser, ori);
                return ori;
            } else {
                throw new ParsingException("class mismatch");
            }
        }

        DefaultDecisionMakingModule module = new DefaultDecisionMakingModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setA(getA());
        module.setB(getB());
        module.setC(getC());
        module.setD(getD());
        module.setWeightFT(getWeightFT());
        module.setWeightNPV(getWeightNPV());
        module.setWeightSocial(getWeightSocial());
        module.setWeightLocal(getWeightLocal());
        module.setLogisticFactor(getLogisticFactor());

        if(hasNodeFilterScheme()) {
            InRAProcessPlanNodeFilterScheme inFilterScheme = getNodeFilterScheme();
            ProcessPlanNodeFilterScheme filterScheme = parser.parseEntityTo(inFilterScheme);
            module.setNodeFilterScheme(filterScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set node filter scheme '{}'", filterScheme.getName());
        } else {
            module.setNodeFilterScheme(DisabledProcessPlanNodeFilterScheme.INSTANCE);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "no node filter scheme specified");
        }

        applyPvFile(parser, module);

        return module;
    }

    private void applyPvFile(IRPactInputParser parser, DefaultDecisionMakingModule module) throws ParsingException {
        if(hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            NPVXlsxData xlsxData = parser.parseEntityTo(getPvFile());
            module.setNPVData(xlsxData);
        } else {
            LOGGER.trace("no pv file found");
        }
    }
}
