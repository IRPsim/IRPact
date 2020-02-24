package de.unileipzig.irpact.agent;

import de.unileipzig.irpact.services.KillPlatformService;
import de.unileipzig.irpact.util.JadexUtil;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
@Agent
@Arguments({
        @Argument(name = "myName", clazz = String.class)
})
public class KillAgent implements IRPactAgent {

    public static final String FILE_NAME = "de.unileipzig.irpact.agent.KillAgent.class";

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
        System.out.println("[_KillAgent] created!");
    }

    @AgentBody
    public void body() {
        System.out.println("[" + getName() + "] body!");
        agent.getFeature(IRequiredServicesFeature.class)
                .searchServices(JadexUtil.searchNetwork(KillPlatformService.class))
                .addResultListener(new IntermediateDefaultResultListener<>() {
                    @Override
                    public void intermediateResultAvailable(KillPlatformService killService) {
                        System.out.println("[" + getName() + "] call killPlatform");
                        IFuture<Void> killResult = killService.killPlatform();
                        killResult.get();
                        IExternalAccess platform = agent.getExternalAccess(agent.getId().getRoot());
                        platform.killComponent();
                    }

                    @Override
                    public void exceptionOccurred(Exception exception) {
                        System.out.println("[" + getName() + "] ERROR");
                        exception.printStackTrace();
                    }
                });
    }

    @AgentKilled
    public void killed() {
        System.out.println("[" + getName() + "] killed!");
    }

    @Override
    public String getName() {
        return myName;
    }
}
