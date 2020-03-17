package de.unileipzig.irpact.jadex.message;

import de.unileipzig.irpact.core.agent.Agent;
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

    public void sendBasic(Agent from, MessageContent content, Agent to) {
        super.send(from, content, to);
    }

    public void sendBasic(Agent from, MessageContent content, Agent... to) {
        for(Agent toAgent: to) {
            sendBasic(from, content, toAgent);
        }
    }

    public IFuture<Void> sendJadex(Agent from, MessageContent content, Agent to) {
        IExternalAccess fromExternal = env().getCache()
                .getAccess(from.getName());
        IComponentIdentifier toCompentIdentifier = env().getCache()
                .getIdentifier(to.getName());
        return fromExternal.scheduleStep(fromInternal -> {
            IMessageFeature msgFeature = fromInternal.getFeature(IMessageFeature.class);
            return msgFeature.sendMessage(content, toCompentIdentifier);
        });
    }

    public IFuture<Void> sendJadex(Agent from, MessageContent content, Agent... to) {
        IExternalAccess fromExternal = env().getCache()
                .getAccess(from.getName());
        IComponentIdentifier[] toCompentIdentifiers = new IComponentIdentifier[to.length];
        for(int i = 0; i < to.length; i++) {
            toCompentIdentifiers[i] = env().getCache()
                    .getIdentifier(to[i].getName());
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
