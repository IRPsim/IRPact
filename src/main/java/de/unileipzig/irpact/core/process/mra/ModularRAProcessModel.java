package de.unileipzig.irpact.core.process.mra;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.mra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;
import de.unileipzig.irpact.core.process.ra.RAProcessModelBase;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessModel extends RAProcessModelBase {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessModel.class);

    protected EvaluableComponent interestComponent;
    protected EvaluableComponent feasibilityComponent;
    protected EvaluableComponent decisionMakingComponent;
    protected EvaluableComponent actionComponent;

    public ModularRAProcessModel() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        super.preAgentCreation();
        for(Component component: iterateAllCompontens()) {
            component.preAgentCreation();
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        super.preAgentCreationValidation();
        for(Component component: iterateAllCompontens()) {
            component.preAgentCreationValidation();
        }
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        super.postAgentCreation();
        for(Component component: iterateAllCompontens()) {
            component.postAgentCreation();
        }
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        super.postAgentCreationValidation();
        for(Component component: iterateAllCompontens()) {
            component.postAgentCreationValidation();
        }
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        super.preSimulationStart();
        for(Component component: iterateAllCompontens()) {
            component.preSimulationStart();
        }
    }

    @Override
    public void postSimulation() {
        super.postSimulation();
        for(Component component: iterateAllCompontens()) {
            component.postSimulation();
        }
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        streamAllComponents().forEach(c -> c.handleNewProduct(newProduct));
    }

    @Override
    public ModularRAProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = (ConsumerAgent) agent;
        Rnd rnd = getEnvironment().getSimulationRandom().deriveInstance();
        return new ModularRAProcessPlan(this, cAgent, need, product, rnd);
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName()
        );
    }

    public void setInterestComponent(EvaluableComponent interestComponent) {
        this.interestComponent = interestComponent;
    }

    public EvaluableComponent getInterestComponent() {
        return interestComponent;
    }

    public void setFeasibilityComponent(EvaluableComponent feasibilityComponent) {
        this.feasibilityComponent = feasibilityComponent;
    }

    public EvaluableComponent getFeasibilityComponent() {
        return feasibilityComponent;
    }

    public void setDecisionMakingComponent(EvaluableComponent decisionMakingComponent) {
        this.decisionMakingComponent = decisionMakingComponent;
    }

    public EvaluableComponent getDecisionMakingComponent() {
        return decisionMakingComponent;
    }

    public void setActionComponent(EvaluableComponent actionComponent) {
        this.actionComponent = actionComponent;
    }

    public EvaluableComponent getActionComponent() {
        return actionComponent;
    }

    protected Stream<Component> streamAllComponents() {
        Stream<Component> s0 = Stream.concat(interestComponent.streamAllComponents(), feasibilityComponent.streamAllComponents());
        Stream<Component> s1 = Stream.concat(decisionMakingComponent.streamAllComponents(), actionComponent.streamAllComponents());
        return Stream.concat(s0, s1);
    }

    protected Iterable<Component> iterateAllCompontens() {
        return () -> streamAllComponents().iterator();
    }
}
