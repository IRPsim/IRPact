package de.unileipzig.irpact.core.process.modular.ca.components;

import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentModule extends Module, InitalizablePart {

    default void handleMissingParametersRecursively(ConsumerAgentMPM model) {
    }

    default void handleNewProductRecursively(Product newProduct) {
    }

    default ConsumerAgentModule searchModule(String name) {
        if(Objects.equals(getName(), name)) {
            return this;
        } else {
            return null;
        }
    }
}
