package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public class BasicMessageEvent implements MessageEvent {

    protected MessageSystem messageSystem;
    protected Agent sender;
    protected Agent receiver;
    protected Agent[] receivers;
    protected Message message;

    public BasicMessageEvent(
            MessageSystem messageSystem,
            Agent sender,
            Agent receiver,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.sender = Check.requireNonNull(sender, "sender");
        this.receiver = Check.requireNonNull(receiver, "receiver");
        this.message = Check.requireNonNull(message, "message");
    }

    public BasicMessageEvent(
            MessageSystem messageSystem,
            Agent sender,
            Agent[] receivers,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.sender = Check.requireNonNull(sender, "sender");
        this.receivers = Check.requireNonNull(receivers, "receivers");
        this.message = Check.requireNonNull(message, "message");
    }

    @Override
    public void process() {
        if(receiver == null) {
            messageSystem.send(
                    sender,
                    message,
                    receivers
            );
        } else {
            messageSystem.send(
                    sender,
                    message,
                    receiver
            );
        }
    }
}
