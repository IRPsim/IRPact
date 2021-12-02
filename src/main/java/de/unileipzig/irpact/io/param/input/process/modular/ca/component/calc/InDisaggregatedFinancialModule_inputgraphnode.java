package de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.calc.DisaggregatedFinancialModule;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
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
public class InDisaggregatedFinancialModule_inputgraphnode implements InConsumerAgentCalculationModule {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_CALC_DISFIN);
        setShapeColorBorder(res, thisClass(), INPUT_SHAPE, INPUT_COLOR, INPUT_BORDER);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "logisticFactor");

        setDefault(res, thisClass(), "weight", VALUE_1);
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
    public double weight;
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @FieldDefinition
    public double logisticFactor;
    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
    }

    public InDisaggregatedFinancialModule_inputgraphnode() {
    }

    public InDisaggregatedFinancialModule_inputgraphnode(String name, double logisticFactor) {
        setName(name);
        setLogisticFactor(logisticFactor);
    }

    @Override
    public InDisaggregatedFinancialModule_inputgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDisaggregatedFinancialModule_inputgraphnode newCopy(CopyCache cache) {
        InDisaggregatedFinancialModule_inputgraphnode copy = new InDisaggregatedFinancialModule_inputgraphnode();
        return Dev.throwException();
    }

    @Override
    public DisaggregatedFinancialModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return searchModule(parser, getName(), DisaggregatedFinancialModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        DisaggregatedFinancialModule module = new DisaggregatedFinancialModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setLogisticFactor(getLogisticFactor());
        module.setWeight(getWeight());

        return module;
    }
}
