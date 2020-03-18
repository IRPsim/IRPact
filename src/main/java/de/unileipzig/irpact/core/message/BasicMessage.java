package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.Agent;

/**
 * @author Daniel Abitz
 */
public class BasicMessage implements Message {

    protected Agent sender;
    protected Agent receiver;
    protected MessageContent content;

    public BasicMessage(
            Agent sender,
            Agent receiver,
            MessageContent content) {
        this.sender = Check.requireNonNull(sender, "sender");
        this.receiver = Check.requireNonNull(receiver, "receiver");
        this.content = Check.requireNonNull(content, "content");
    }

    @Override
    public Agent getSender() {
        return sender;
    }

    @Override
    public Agent getReceiver() {
        return receiver;
    }

    @Override
    public MessageContent getContent() {
        return content;
    }
}
