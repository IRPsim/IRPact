package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.core.process.mra.component.generic.Component;
import de.unileipzig.irpact.core.process.mra.component.generic.ComponentType;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSingleMRAComponent extends AbstractMRAComponent {

    protected AbstractSingleMRAComponent(ComponentType type) {
        super(type);
    }

    @Override
    public final boolean isCyclic() {
        return false;
    }

    @Override
    public final Collection<Component> getAllComponents() {
        return Collections.singleton(this);
    }

    @Override
    public final Iterable<? extends Component> iterateComponents() {
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
