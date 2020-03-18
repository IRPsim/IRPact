package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.commons.Check;

/**
 * @author Daniel Abitz
 */
public class BasicMessageEvent implements MessageEvent {

    protected MessageSystem messageSystem;
    protected Message message;

    public BasicMessageEvent(
            MessageSystem messageSystem,
            Message message) {
        this.messageSystem = Check.requireNonNull(messageSystem, "messageSystem");
        this.message = Check.requireNonNull(message, "message");
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public void process() {
        messageSystem.send(
                message.getSender(),
                message.getContent(),
                message.getReceiver()
        );
    }
}
