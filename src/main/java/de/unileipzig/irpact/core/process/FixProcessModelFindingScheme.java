package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.product.Product;

import java.util.Objects;

/**
 * Returns the same ProcessModel instance for every product.
 *
 * @author Daniel Abitz
 */
public class FixProcessModelFindingScheme extends NameableBase implements ProcessFindingScheme {

    protected ProcessModel model;

    public FixProcessModelFindingScheme() {
    }

    public ProcessModel getModel() {
        return model;
    }

    public void setModel(ProcessModel model) {
        this.model = model;
    }

    @Override
    public ProcessModel findModel(Product product) {
        if(model == null) {
            throw new NullPointerException("model not set");
        }
        return model;
    }

    @Override
    public int getHashCode() {
        return Objects.hash(getName(), getModel().getName());
    }
}
