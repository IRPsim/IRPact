package de.unileipzig.irpact.jadex.message;

import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.AgentIdentifier;
import de.unileipzig.irpact.core.message.BasicMessageSystem;
import de.unileipzig.irpact.core.message.MessageContent;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.component.IMessageFeature;
import jadex.commons.future.IFuture;

/**
 * @author Daniel Abitz
 */
@ToDo("boilerplate code reduzieren (falls sinnvoll)")
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

    public void sendBasic(AgentIdentifier from, MessageContent content, AgentIdentifier to) {
        super.send(from, content, to);
    }

    public void sendBasic(AgentIdentifier from, MessageContent content, AgentIdentifier... to) {
        for(AgentIdentifier toIdentifier: to) {
            sendBasic(from, content, toIdentifier);
        }
    }

    public IFuture<Void> sendJadex(AgentIdentifier from, MessageContent content, AgentIdentifier to) {
        String fromName = env().getName(from);
        IExternalAccess fromExternal = env().getExternalAccess(fromName);
        String toName = env().getName(to);
        IComponentIdentifier toCompentIdentifier = env().getComponentIdentifier(toName);
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            return msgFeature.sendMessage(content, toCompentIdentifier);
        });
    }

    public IFuture<Void> sendJadex(AgentIdentifier from, MessageContent content, AgentIdentifier... to) {
        String fromName = env().getName(from);
        IExternalAccess fromExternal = env().getExternalAccess(fromName);
        IComponentIdentifier[] toCompentIdentifiers = new IComponentIdentifier[to.length];
        for(int i = 0; i < to.length; i++) {
            String toName = env().getName(to[i]);
            toCompentIdentifiers[i] = env().getComponentIdentifier(toName);
        }
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            return msgFeature.sendMessage(content, toCompentIdentifiers);
        });
    }

    @Override
    public void send(AgentIdentifier from, MessageContent content, AgentIdentifier to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, content, to);
        } else {
            sendJadex(from, content, to).get();
        }
    }

    @Override
    public void send(AgentIdentifier from, MessageContent content, AgentIdentifier... to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, content, to);
        } else {
            sendJadex(from, content, to).get();
        }
    }

    public void sendBasic(Agent from, MessageContent content, Agent to) {
        super.send(from, content, to);
    }

    public void sendBasic(Agent from, MessageContent content, Agent... to) {
        for(Agent toAgent: to) {
            sendBasic(from, content, toAgent);
        }
    }

    public IFuture<Void> sendJadex(Agent from, MessageContent content, Agent to) {
        IExternalAccess fromExternal = env().getExternalAccess(from.getName());
        IComponentIdentifier toCompentIdentifier = env().getComponentIdentifier(to.getName());
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            return msgFeature.sendMessage(content, toCompentIdentifier);
        });
    }

    public IFuture<Void> sendJadex(Agent from, MessageContent content, Agent... to) {
        IExternalAccess fromExternal = env().getExternalAccess(from.getName());
        IComponentIdentifier[] toCompentIdentifiers = new IComponentIdentifier[to.length];
        for(int i = 0; i < to.length; i++) {
            toCompentIdentifiers[i] = env().getComponentIdentifier(to[i].getName());
        }
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            return msgFeature.sendMessage(content, toCompentIdentifiers);
        });
    }

    @Override
    public void send(Agent from, MessageContent content, Agent to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, content, to);
        } else {
            sendJadex(from, content, to).get();
        }
    }

    @Override
    public void send(Agent from, MessageContent content, Agent... to) {
        if(mode == Mode.BASIC) {
            sendBasic(from, content, to);
        } else {
            sendJadex(from, content, to).get();
        }
    }
}
