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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class ModularRAProcessModel extends RAProcessModelBase {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ModularRAProcessModel.class);

    protected final Set<Component> allComponents = new HashSet<>();
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
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                rnd,
                uncertaintyHandler.getManager(),
                interestComponent,
                feasibilityComponent,
                decisionMakingComponent,
                actionComponent
        );
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        if(interestComponent == null) throw new MissingDataException("interestComponent");
        if(feasibilityComponent == null) throw new MissingDataException("feasibilityComponent");
        if(decisionMakingComponent == null) throw new MissingDataException("decisionMakingComponent");
        if(actionComponent == null) throw new MissingDataException("actionComponent");

        allComponents.clear();
        for(Component component: iterateAllCompontens()) {
            allComponents.add(component);
        }

        super.preAgentCreation();
        for(Component component: allComponents) {
            component.preAgentCreation();
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        super.preAgentCreationValidation();
        for(Component component: allComponents) {
            component.preAgentCreationValidation();
        }
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        super.postAgentCreation();
        for(Component component: allComponents) {
            component.postAgentCreation();
        }
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        super.postAgentCreationValidation();
        for(Component component: allComponents) {
            component.postAgentCreationValidation();
        }
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        super.preSimulationStart();
        for(Component component: allComponents) {
            component.preSimulationStart();
        }
    }

    @Override
    public void postSimulation() {
        super.postSimulation();
        for(Component component: allComponents) {
            component.postSimulation();
        }
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        for(Component component: allComponents) {
            component.handleNewProduct(newProduct);
        }
    }

    @Override
    public ModularRAProcessPlan newPlan(Agent agent, Need need, Product product) {
        ConsumerAgent cAgent = (ConsumerAgent) agent;
        Rnd rnd = getEnvironment().getSimulationRandom().deriveInstance();
        ModularRAProcessPlan plan = new ModularRAProcessPlan(this, cAgent, need, product, rnd);
        for(Component component: allComponents) {
            component.handleNewPlan(plan);
        }
        return plan;
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
