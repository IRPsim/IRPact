package de.unileipzig.irpact.core.agent.company.advertisement;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.company.message.CompanyConsumerPerceptionManipulationMessage;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.message.MessageSystem;
import de.unileipzig.irpact.core.message.communication.BasicCommunicationEvent;
import de.unileipzig.irpact.core.message.communication.CommunicationEvent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.simulation.EventManager;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class UniformAdvertisementScheme implements AdvertisementScheme {

    public static final String NAME = UniformAdvertisementScheme.class.getSimpleName();
    public static final UniformAdvertisementScheme INSTANCE = new UniformAdvertisementScheme();

    @Override
    public void advertise(CompanyAgent agent) {
        Collection<? extends ConsumerAgent> consumerAgents = agent.getEnvironment()
                .getConfiguration()
                .getConsumerAgents();
        MessageSystem system = agent.getEnvironment()
                .getMessageSystem();
        EventManager eventManager = agent.getEnvironment()
                .getEventManager();
        for(Product product: agent.getProductPortfolio()) {
            for(ProductAttribute attribute: product.getAttributes()) {
                CompanyConsumerPerceptionManipulationMessage msg = new CompanyConsumerPerceptionManipulationMessage(
                        product,
                        attribute,
                        attribute.getValue()
                );
                CommunicationEvent event = new BasicCommunicationEvent(
                        system,
                        agent,
                        consumerAgents,
                        msg
                );
                eventManager.schedule(event);
            }
        }
    }
}
