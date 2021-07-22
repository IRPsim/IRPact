package de.unileipzig.irpact.core.process.modularra.component.base;

import de.unileipzig.irpact.commons.util.ListSupplier;
import de.unileipzig.irpact.core.process.modularra.component.generic.AbstractComponent;
import de.unileipzig.irpact.core.process.modularra.component.generic.Component;
import de.unileipzig.irpact.core.process.modularra.component.generic.ComponentType;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractContainerComponent extends AbstractComponent implements ValueComponent {

    protected ListSupplier supplier;
    protected List<ValueComponent> components;
    protected double weight = 1.0;
    protected double ifEmpty = 0.0;

    public AbstractContainerComponent() {
        this(ListSupplier.ARRAY);
    }

    public AbstractContainerComponent(ListSupplier supplier) {
        super(ComponentType.INPUT);
        this.supplier = supplier;
        this.components = supplier.newList();
    }

    public List<ValueComponent> getComponents() {
        return components;
    }

    public int numberOfComponents() {
        return components.size();
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

    @Override
    public Iterable<? extends Component> iterateComponents() {
        return components;
    }

    @Override
    public final boolean isSupported(Component component) {
        return component instanceof ValueComponent;
    }

    @Override
    public final boolean add(Component component) {
        if(component instanceof ValueComponent) {
            return components.add((ValueComponent) component);
        } else {
            return false;
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public final boolean remove(Component component) {
        return components.remove(component);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public final boolean has(Component component) {
        return components.contains(component);
    }
}
