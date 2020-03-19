package de.unileipzig.irpact.core.agent.company.message;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;

/**
 * @author Daniel Abitz
 */
public class CompanyConsumerPerceptionManipulationMessage implements Message {

    private Product product;
    private ProductAttribute attribute;
    private double perceptionValue;

    public CompanyConsumerPerceptionManipulationMessage(
            Product product,
            ProductAttribute attribute,
            double perceptionValue) {
        this.product = product;
        this.attribute = attribute;
        this.perceptionValue = perceptionValue;
    }

    @Override
    public void process(Agent sender, Agent receiver) {
        CompanyAgent sendingCompany = (CompanyAgent) sender;
        ConsumerAgent receivingConsumer = (ConsumerAgent) receiver;
        if(!receivingConsumer.isAwareOf(product)) {
            receivingConsumer.makeAwareOf(product);
        }
        receivingConsumer.updateProductAttributePerception(
                attribute,
                perceptionValue,
                sendingCompany.getInformationAuthority()
        );
    }

    @Override
    public boolean isSerializable() {
        return false;
    }

    @Override
    public String serializeToString() {
        throw new UnsupportedOperationException();
    }
}
