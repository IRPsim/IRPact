package de.unileipzig.irpact.jadex.examples.deprecated.tests.dummy;

import de.unileipzig.irpact.jadex.examples.deprecated.tests.dummy.x.KillPlatformServiceX;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Dieser Agent ermoeglicht das Beenden der Simulation.
 * @author Daniel Abitz
 * @since 0.0
 */
@Agent
@Arguments({
        @Argument(name = "myName", clazz = String.class)
})
@Service
@ProvidedServices({
        @ProvidedService(type = KillPlatformServiceX.class)
})
public class SimulationAgent implements IRPactAgent, KillPlatformServiceX {

    public static final String FILE_NAME = "de.unileipzig.irpact.jadex.examples.tests.dummy.SimulationAgent.class";

    public static CreationInfo createInfo(String agentName, String myName) {
        Map<String, Object> map = new HashMap<>();
        map.put("myName", myName);
        CreationInfo ci = new CreationInfo(map);
        ci.setName(agentName);
        ci.setFilename(FILE_NAME);
        return ci;
    }

    @Agent
    protected IInternalAccess agent;
    @AgentArgument
    protected String myName;

    @AgentCreated
    public void created() {
        System.out.println("[_SimulationAgent] created!");
    }

    @AgentBody
    public void body() {
        System.out.println("[" + getName() + "] body!");
    }

    @AgentKilled
    public void killed() {
        System.out.println("[" + getName() + "] killed!");
    }

    @Override
    public String getName() {
        return myName;
    }

    @Override
    public IFuture<Void> killPlatform() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[" + getName() + "] Kill Platform...");
            IExternalAccess platform = agent.getExternalAccess(agent.getId().getRoot());
            platform.killComponent();
            System.out.println("[" + getName() + "] ...Platform killed!");
        }).start();
        return Future.getEmptyFuture();
    }
}
