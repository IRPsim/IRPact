package de.unileipzig.irpact.core.process2.uncert;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class IndividualUncertainty extends NameableBase implements Uncertainty {

    protected IndividualGlobalModerateExtremistUncertaintySupplier supplier;
    protected Map<String, Double> values;

    public IndividualUncertainty() {
        this(new HashMap<>());
    }

    public IndividualUncertainty(Map<String, Double> values) {
        this.values = values;
    }

    public void setSupplier(IndividualGlobalModerateExtremistUncertaintySupplier supplier) {
        this.supplier = supplier;
    }

    public IndividualGlobalModerateExtremistUncertaintySupplier getSupplier() {
        return supplier;
    }

    @Override
    public void updateOpinion(ConsumerAgentAttribute attribute, double oldValue, double newValue) {
        //not used
    }

    @Override
    public void setUncertainty(ConsumerAgentAttribute attribute, double value) {
        values.put(attribute.getName(), value);
    }

    @Override
    public double getUncertainty(ConsumerAgentAttribute attribute) {
        Double current = values.get(attribute.getName());
        if(current == null) {
            current = supplier.getUncertainty(attribute);
            values.put(attribute.getName(), current);
        }
        return current;
    }
}
