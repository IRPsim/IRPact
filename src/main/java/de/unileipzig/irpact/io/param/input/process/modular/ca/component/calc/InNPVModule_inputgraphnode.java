package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.NPVModule;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = INPUT_LABEL,
                shape = INPUT_SHAPE,
                color = INPUT_COLOR,
                border = INPUT_BORDER,
                tags = {INPUT_GRAPHNODE}
        )
)
@ToRemove
public class InNPVModule_inputgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_CALC_NPV);
        setShapeColorBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_BORDER);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "pvFile");

        setDefault(res, thisClass(), "weight", VALUE_1);
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
    public double weight;
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
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

    public InNPVModule_inputgraphnode() {
    }

    public InNPVModule_inputgraphnode(String name, InPVFile pvFile) {
        setName(name);
        setPvFile(pvFile);
    }

    @Override
    public InNPVModule_inputgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNPVModule_inputgraphnode newCopy(CopyCache cache) {
        InNPVModule_inputgraphnode copy = new InNPVModule_inputgraphnode();
        return Dev.throwException();
    }

    @Override
    public NPVModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), NPVModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        NPVModule module = new NPVModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setWeight(getWeight());
        applyPvFile(parser, module);

        return module;
    }

    private void applyPvFile(IRPactInputParser parser, NPVModule module) throws ParsingException {
        if(hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            NPVXlsxData xlsxData = parser.parseEntityTo(getPvFile());
            module.setNPVData(xlsxData);
        } else {
            LOGGER.trace("no pv file found");
        }
    }
}
