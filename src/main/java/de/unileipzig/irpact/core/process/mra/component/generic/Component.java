package de.unileipzig.irpact.core.process.mra.component.generic;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.Product;

import java.util.Collection;
import java.util.stream.Stream;

/**
 *
 * @author Daniel Abitz
 */
public interface Component extends Nameable, InitalizablePart {

    boolean isCyclic();

    Collection<Component> getAllComponents();

    Stream<? extends Component> streamAllComponents();

    Stream<? extends Component> streamComponents();

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

    //=========================
    //events (InitalizablePart)
    //=========================

    default void handleNewPlan(ProcessPlan plan) {
    }

    default void handleNewProduct(Product product) {
    }
}
