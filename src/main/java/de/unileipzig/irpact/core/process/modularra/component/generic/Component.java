package de.unileipzig.irpact.core.process.modularra.component.generic;

import de.unileipzig.irpact.commons.Nameable;

import java.util.Collection;

/**
 *
 * @author Daniel Abitz
 */
public interface Component extends Nameable {

    boolean isCyclic();

    Collection<Component> getAllComponents();

    Iterable<? extends Component> iterateComponents();

    boolean isSupported(Component component);

    boolean add(Component component);

    boolean remove(Component component);

    boolean has(Component component);

    ComponentType getType();

    default <T extends Component> boolean is(Class<T> type) {
        return type.isInstance(this);
    }

    default boolean isInput() {
        return getType() == ComponentType.INPUT;
    }

    default boolean isOutput() {
        return getType() == ComponentType.OUTPUT;
    }

    default boolean isIntermediate() {
        return getType() == ComponentType.INTERMEDIATE;
    }
}
