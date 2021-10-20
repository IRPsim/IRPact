package de.unileipzig.irpact.core.process2.modular.modules.calc;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.AbstractUniformMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ScalingWeightingModule2<I>
        extends AbstractUniformMultiModule1_2<I, Number, I, Number, CalculationModule2<I>>
        implements CalculationModule2<I>, HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ScalingWeightingModule2.class);

    protected MutableDouble weight = MutableDouble.empty();
    protected String attributeName;
    
    public void setInitialWeight(double value) {
        weight.set(value);
    }

    public MutableDouble getWeight() {
        return weight;
    }
    
    public double getWeightValue() {
        return weight.get();
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    @Override
    protected void validateSelf() throws Throwable {
        traceModuleValidation();
        
        if(attributeName == null) {
            throw new NullPointerException("attribute name");
        }
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();

        MutableDouble min = MutableDouble.empty();
        MutableDouble max = MutableDouble.empty();
        for(ConsumerAgent ca: environment.getAgents().iterableConsumerAgents()) {
            double value = getDoubleValue(environment, ca, attributeName, getFirstSimulationYear(environment));
            min.setMin(value);
            max.setMax(value);
        }

        double newWeight = (weight.get() - min.get()) / (max.get() - min.get());
        trace(
                "[{}] newWeight = (initialWeight - min) / (max - min) = {} = ({} - {}) / ({} - {})",
                getName(), newWeight, weight.get(), min.get(), max.get(), min.get()
        );
        weight.set(newWeight);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public double calculate(I input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();
        return getWeightValue() * getNonnullSubmodule().calculate(input, actions);
    }
}
