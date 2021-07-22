package de.unileipzig.irpact.core.process.mra.component.base;

import de.unileipzig.irpact.commons.exception.IRPactIllegalArgumentException;
import de.unileipzig.irpact.commons.util.ListSupplier;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process.mra.component.generic.AbstractComponent;
import de.unileipzig.irpact.core.process.mra.component.generic.Component;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAttributeComponent extends AbstractComponent implements ValueComponent {

    protected ListSupplier supplier;
    protected List<String> attributeNames;
    protected double weight = 1.0;
    protected double ifEmpty = 0.0;

    public AbstractAttributeComponent() {
        this(ListSupplier.ARRAY);
    }

    public AbstractAttributeComponent(ListSupplier supplier) {
        super(ComponentType.INPUT);
        this.supplier = supplier;
        this.attributeNames = supplier.newList();
    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public boolean addAttributeName(String name) {
        return attributeNames.add(name);
    }

    public int numberOfAttributeNames() {
        return attributeNames.size();
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setIfEmpty(double ifEmpty) {
        this.ifEmpty = ifEmpty;
    }

    public double getIfEmpty() {
        return ifEmpty;
    }

    protected static double findDoubleValue(Agent agent, String attributeName) {
        if(agent instanceof ConsumerAgent) {
            return AttributeHelper.findDoubleValue2((ConsumerAgent) agent, attributeName);
        } else {
            throw new IRPactIllegalArgumentException("agent '{}' is no consumer", agent.getName());
        }
    }

    @Override
    public Iterable<? extends Component> iterateComponents() {
        return Collections.emptyList();
    }

    @Override
    public final boolean isSupported(Component component) {
        return false;
    }

    @Override
    public final boolean add(Component component) {
        return false;
    }

    @Override
    public final boolean remove(Component component) {
        return false;
    }

    @Override
    public final boolean has(Component component) {
        return false;
    }
}
