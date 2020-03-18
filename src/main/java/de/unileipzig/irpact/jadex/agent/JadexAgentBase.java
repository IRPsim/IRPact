package de.unileipzig.irpact.jadex.agent;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.commons.exception.MissingArgumentException;
import de.unileipzig.irpact.core.message.Message;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.component.IMessageHandler;
import jadex.bridge.component.IMsgHeader;
import jadex.bridge.service.types.security.ISecurityInfo;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class JadexAgentBase implements JadexAgent {

    protected IMessageHandler messageHandler;

    public JadexAgentBase() {
    }

    //=========================
    //init
    //=========================

    protected static <T> T get(Map<String, Object> args, String name) throws MissingArgumentException {
        return get(args, name, true);
    }

    @SuppressWarnings("unchecked")
    protected static <T> T get(Map<String, Object> args, String name, boolean throwExceptionIfNull) throws MissingArgumentException {
        Object arg = args.get(name);
        if(throwExceptionIfNull && arg == null) {
            throw new MissingArgumentException(name);
        }
        return (T) arg;
    }

    protected abstract Logger logger();

    protected abstract IComponentIdentifier getCompnentIdentifier();

    protected abstract IMessageFeature getMessageFeature();

    protected abstract void initArgs(Map<String, Object> args);

    @ToDo("hmmm")
    protected void initMessageHandler() {
        if(messageHandler == null) {
            messageHandler = new IMessageHandler() {
                @Override
                public boolean isHandling(ISecurityInfo secinfos, IMsgHeader header, Object msg) {
                    logger().trace("[{}] TEST: '{}'", getName(), msg);
                    return false;
                    /*
                    if(getCompnentIdentifier() == header.getReceiver()
                            && msg instanceof MessageContent) {
                        IComponentIdentifier sender = header.getSender();
                        de.unileipzig.irpact.core.agent.Agent senderAgent = getEnvironment().getConfiguration()
                                .getEntity(sender);
                        if(senderAgent == null) {
                            return false;
                        }
                        MessageContent content = (MessageContent) msg;
                        return JadexAgentBase.this.isHandling(senderAgent, content);
                    } else {
                        return false;
                    }
                     */
                }

                @Override
                public boolean isRemove() {
                    return false;
                }

                @Override
                public void handleMessage(ISecurityInfo secinfos, IMsgHeader header, Object msgObj) {
                    IComponentIdentifier sender = header.getSender();
                    de.unileipzig.irpact.core.agent.Agent senderAgent = getEnvironment().getConfiguration()
                            .getEntity(sender);
                    Message content = (Message) msgObj;
                    logger().trace("[{}] handle JadexMessage from '{}'", getName(), sender.getName());
                    JadexAgentBase.this.handleMessage(senderAgent, content);
                }
            };
        }
        getMessageFeature().addMessageHandler(messageHandler);
    }

    @Override
    public boolean isHandling(de.unileipzig.irpact.core.agent.Agent sender, Message content) {
        return true;
    }

    @Override
    public void handleMessage(de.unileipzig.irpact.core.agent.Agent sender, Message content) {
        logger().trace("[{}] handle Message from '{}'", getName(), sender.getName());
        content.process(sender, this);
    }

    //=========================
    //lifecycle
    //=========================

    protected abstract void onInit();

    protected abstract void onStart();

    protected abstract void onEnd();
}
