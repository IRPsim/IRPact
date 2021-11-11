package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPLoggingMessageCollection;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModuleN_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.BooleanModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DecisionMakingModule2
        extends AbstractUniformCAMultiModuleN_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DecisionMakingModule2.class);

    protected BooleanModule2<ConsumerAgentData2> finCheck;

    protected boolean forceEvaluation = false;
    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }
    public boolean isForceEvaluation() {
        return forceEvaluation;
    }
    public boolean isNotForceEvaluation() {
        return !forceEvaluation;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
        finCheck.validate();
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
        finCheck.initialize(environment);
        sortPriority();
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    public void setFinancialCheckComponent(BooleanModule2<ConsumerAgentData2> module) {
        this.finCheck = module;
    }
    public BooleanModule2<ConsumerAgentData2> getFinancialCheckComponent() {
        return finCheck;
    }
    protected boolean testFinancialCheckComponent(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return finCheck.test(input, actions);
    }

    protected double apply0(
            ConsumerAgentData2 input,
            List<PostAction2> actions) throws Throwable {

        IRPLoggingMessageCollection lmc = new IRPLoggingMessageCollection()
                .setLazy(true)
                .setAutoDispose(true);

        lmc.append("[{}] calculate U", input.getAgentName());

        //threshold check
        boolean yesFinancial = testFinancialCheckComponent(input, actions);
        boolean noFinancial = !yesFinancial;
        if(noFinancial && isNotForceEvaluation()) {
            lmc.append("purchase power < financial threshold");
            traceSimulationProcess(lmc);
            return Double.NaN;
        }

        double B = 0.0;
        for(int i = 0; i < getSubmoduleCount(); i++) {
            CalculationModule2<ConsumerAgentData2> submodule = getSubmodule(i);
            lmc.append("call submodule '{}'", submodule.getName());
            B += submodule.apply(input, actions);
        }

        double adoptionThreshold = getAdoptionThreshold(input);
        boolean noAdoption = B < adoptionThreshold;

        lmc.append("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);
        lmc.append("noAdoption={}, noFinancial={}", noAdoption, noFinancial);
        traceSimulationProcess(lmc);

        return B;
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return apply0(input, actions);
    }

    @Override
    protected void setSharedDataThis(SharedModuleData sharedData) {
        super.setSharedDataThis(sharedData);
        finCheck.setSharedData(sharedData);
    }
}
