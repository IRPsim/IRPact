package de.unileipzig.irpact.jadex.message;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.message.BasicMessageSystem;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IMessageFeature;
import jadex.commons.future.IFuture;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class JadexMessageSystem extends BasicMessageSystem {

    public enum Mode {
        BASIC,
        JADEX
    }

    private Mode mode;

    public JadexMessageSystem(JadexSimulationEnvironment env, Mode mode) {
        super(env);
        this.mode = mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    protected JadexSimulationEnvironment env() {
        return (JadexSimulationEnvironment) super.env();
    }

    public void sendBasic(Agent from, Message content, Agent to) {
        super.send(from, content, to);
    }

    public void sendBasic(Agent from, Message content, Agent... to) {
        for(Agent toAgent: to) {
            sendBasic(from, content, toAgent);
        }
    }

    public IFuture<Void> sendJadex(Agent from, Message content, Agent to) {
        if(!content.isSerializable()) {
            throw new IllegalArgumentException("not printable");
        }
        IExternalAccess fromExternal = env().getConfiguration()
                .findAccess(from.getName());
        IComponentIdentifier toCompentIdentifier = env().getConfiguration()
                .findIdentifier(to.getName());
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            /*
            Map<String, Object> msg = new HashMap<>();
            msg.put(SFipa.CONTENT, "content");
            msg.put(SFipa.PERFORMATIVE, SFipa.INFORM);
            msg.put(SFipa.CONVERSATION_ID, SUtil.createUniqueId(from.getName()));
            msg.put(SFipa.RECEIVERS, new IComponentIdentifier[]{toCompentIdentifier});
            */
            return msgFeature.sendMessage(content.serializeToString(), toCompentIdentifier);
        });
    }

    public IFuture<Void> sendJadex(Agent from, Message content, Agent... to) {
        if(!content.isSerializable()) {
            throw new IllegalArgumentException("not printable");
        }
        IExternalAccess fromExternal = env().getConfiguration()
                .getAccess(from.getName());
        IComponentIdentifier[] toCompentIdentifiers = new IComponentIdentifier[to.length];
        for(int i = 0; i < to.length; i++) {
            toCompentIdentifiers[i] = env().getConfiguration()
                    .getIdentifier(to[i].getName());
        }
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            /*
            Map<String, Object> msg = new HashMap<>();
            msg.put(SFipa.CONTENT, content);
            msg.put(SFipa.PERFORMATIVE, SFipa.INFORM);
            msg.put(SFipa.CONVERSATION_ID, SUtil.createUniqueId(from.getName()));
            msg.put(SFipa.RECEIVERS, toCompentIdentifiers);
            */
            return msgFeature.sendMessage(content.serializeToString(), toCompentIdentifiers);
        });
    }

    @Override
    public void send(Agent from, Message msg, Agent to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, msg, to);
        } else {
            sendJadex(from, msg, to).get();
        }
    }

    @Override
    public void send(Agent from, Message msg, Agent... to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, msg, to);
        } else {
            sendJadex(from, msg, to).get();
        }
    }

    @Override
    public void send(Agent from, Message msg, Collection<? extends Agent> to) {
        if(mode == Mode.JADEX) {
            Agent[] toArray = to.toArray(new Agent[0]);
            sendJadex(from, msg, toArray);
        } else {
            for(Agent toAgent: to) {
                sendBasic(from, msg, toAgent);
            }
        }
    }
}
