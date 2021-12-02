package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.SumThresholdEvaluationModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
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
import java.util.Collection;

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
@ToRemove
public class InSumThresholdEvaluationModule_evalgraphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_EVAL_SUMTHRESH);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntryWithDefault(res, thisClass(), "threshold", VALUE_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "acceptIfBelowThreshold", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "adoptIfAccepted", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "impededIfFailed", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntry(res, thisClass(), "input_graphedge");
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
    public boolean acceptIfBelowThreshold = true;
    public void setAcceptIfBelowThreshold(boolean acceptIfBelowThreshold) {
        this.acceptIfBelowThreshold = acceptIfBelowThreshold;
    }
    public boolean isAcceptIfBelowThreshold() {
        return acceptIfBelowThreshold;
    }

    @FieldDefinition
    public boolean adoptIfAccepted = true;
    public void setAdoptIfAccepted(boolean adoptIfAccepted) {
        this.adoptIfAccepted = adoptIfAccepted;
    }
    public boolean isAdoptIfAccepted() {
        return adoptIfAccepted;
    }

    @FieldDefinition
    public boolean impededIfFailed = true;
    public void setImpededIfFailed(boolean impededIfFailed) {
        this.impededIfFailed = impededIfFailed;
    }
    public boolean isImpededIfFailed() {
        return impededIfFailed;
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
    public void setInputs(InConsumerAgentCalculationModule[] awarenessModule) {
        this.input_graphedge = awarenessModule;
    }
    public void setInputs(Collection<? extends InConsumerAgentCalculationModule> awarenessModule) {
        setInputs(awarenessModule.toArray(new InConsumerAgentCalculationModule[0]));
    }
    public InConsumerAgentCalculationModule[] getInputs() throws ParsingException {
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

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        SumThresholdEvaluationModule module = new SumThresholdEvaluationModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setThreshold(getThreshold());
        module.setAcceptIfBelowThreshold(isAcceptIfBelowThreshold());
        module.setAdoptIfAccepted(isAdoptIfAccepted());
        module.setImpededIfFailed(isImpededIfFailed());

        for(InConsumerAgentCalculationModule in: getInputs()) {
            ConsumerAgentCalculationModule inCalc = parser.parseEntityTo(in);
            module.addSubModule(inCalc);
        }

        return module;
    }
}
