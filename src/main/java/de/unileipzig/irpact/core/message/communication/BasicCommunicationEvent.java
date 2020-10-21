package de.unileipzig.irpact.core.message.communication;

import de.unileipzig.irpact.v2.commons.Check;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.message.MessageSystem;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class BasicCommunicationEvent implements CommunicationEvent {

    protected MessageSystem messageSystem;
    protected Agent sender;
    protected Agent receiver;
    protected Agent[] receivers;
    protected Collection<? extends Agent> iterableReceivers;
    protected Message message;

    public BasicCommunicationEvent(
            MessageSystem messageSystem,
            Agent sender,
            Agent receiver,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.sender = Check.requireNonNull(sender, "sender");
        this.receiver = Check.requireNonNull(receiver, "receiver");
        this.message = Check.requireNonNull(message, "message");
    }

    public BasicCommunicationEvent(
            MessageSystem messageSystem,
            Agent sender,
            Agent[] receivers,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.sender = Check.requireNonNull(sender, "sender");
        this.receivers = Check.requireNonNull(receivers, "receivers");
        this.message = Check.requireNonNull(message, "message");
    }

    public BasicCommunicationEvent(
            MessageSystem messageSystem,
            Agent sender,
            Collection<? extends Agent> iterableReceivers,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.sender = Check.requireNonNull(sender, "sender");
        this.iterableReceivers = Check.requireNonNull(iterableReceivers, "iterableReceivers");
        this.message = Check.requireNonNull(message, "message");
    }

    @Override
    public void process() {
        if(receivers != null) {
            messageSystem.send(
                    sender,
                    message,
                    receivers
            );
        }
        else if(iterableReceivers != null) {
            messageSystem.send(
                    sender,
                    message,
                    iterableReceivers
            );
        }
        else {
            messageSystem.send(
                    sender,
                    message,
                    receiver
            );
        }
    }
}
