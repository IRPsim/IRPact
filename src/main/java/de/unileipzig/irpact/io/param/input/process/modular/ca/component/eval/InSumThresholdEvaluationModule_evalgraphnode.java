package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.SumThresholdEvaluationModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentCalculationModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
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
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
public class InSumThresholdEvaluationModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_SUMTHRESH);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "threshold");
        addEntryWithDefaultAndDomain(res, thisClass(), "adoptIfBelowThreshold", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "input_graphedge");

        setDefault(res, thisClass(), "threshold", VALUE_ONE);
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
    public double threshold;
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @FieldDefinition
    public boolean adoptIfBelowThreshold;
    public boolean isAdoptIfBelowThreshold() {
        return adoptIfBelowThreshold;
    }
    public void setAdoptIfBelowThreshold(boolean adoptIfBelowThreshold) {
        this.adoptIfBelowThreshold = adoptIfBelowThreshold;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = CALC_EDGE_LABEL,
                    color = CALC_EDGE_COLOR,
                    tags = {"InSumThresholdEvaluationModule input"}
            )
    )
    public InConsumerAgentCalculationModule[] input_graphedge;
    public void setInput(InConsumerAgentCalculationModule[] awarenessModule) {
        this.input_graphedge = awarenessModule;
    }
    public InConsumerAgentCalculationModule[] getInput() throws ParsingException {
        return ParamUtil.getNonEmptyArray(input_graphedge, "input");
    }

    public InSumThresholdEvaluationModule_evalgraphnode() {
    }

    @Override
    public InSumThresholdEvaluationModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSumThresholdEvaluationModule_evalgraphnode newCopy(CopyCache cache) {
        InSumThresholdEvaluationModule_evalgraphnode copy = new InSumThresholdEvaluationModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public SumThresholdEvaluationModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), SumThresholdEvaluationModule.class);
        }

        SumThresholdEvaluationModule module = new SumThresholdEvaluationModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setThreshold(getThreshold());
        module.setAdoptIfBelowThreshold(isAdoptIfBelowThreshold());

        for(InConsumerAgentCalculationModule in: getInput()) {
            ConsumerAgentCalculationModule inCalc = parser.parseEntityTo(in);
            module.add(inCalc);
        }

        return module;
    }
}
