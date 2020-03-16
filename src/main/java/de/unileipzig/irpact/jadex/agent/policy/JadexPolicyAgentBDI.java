package de.unileipzig.irpact.jadex.agent.policy;

import de.unileipzig.irpact.core.agent.policy.PolicyAgent;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentBase;
import de.unileipzig.irpact.core.agent.policy.PolicyAgentIdentifier;
import de.unileipzig.irpact.core.agent.policy.TaxesScheme;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices({
        @ProvidedService(type = PolicyAgentService.class),
        @ProvidedService(type = JadexAgentService.class)
})
@Agent(type = BDIAgentFactory.TYPE)
public class JadexPolicyAgentBDI extends JadexAgentBase
        implements PolicyAgent, PolicyAgentService, JadexAgentService {

    //Argument names
    public static final String AGENT_BASE = StartSimulation.AGENT_BASE;

    //general
    private static final Logger logger = LoggerFactory.getLogger(JadexPolicyAgentBDI.class);
    private final JadexPolicyAgentIdentifier IDENTIFIER = new JadexPolicyAgentIdentifier();

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    //ConsumerAgent parameter
    protected PolicyAgentBase agentBase;

    //Beliefs


    //=========================
    //Constructer
    //=========================

    public JadexPolicyAgentBDI() {
    }

    //=========================
    //ConsumerAgent
    //=========================

    @Override
    public double getInformationAuthority() {
        return agentBase.getInformationAuthority();
    }

    @Override
    public String getName() {
        return agentBase.getName();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) agentBase.getEnvironment();
    }

    @Override
    public PolicyAgentIdentifier getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public TaxesScheme getTaxesScheme() {
        return agentBase.getTaxesScheme();
    }

    //=========================
    //JadexAgentBase
    //=========================

    @Override
    protected void initArgs(Map<String, Object> args) {
        try {
            agentBase = get(args, AGENT_BASE);
        } catch (Throwable t) {
            String _name = agentBase == null
                    ? getClass().getSimpleName()
                    : getName();
            logger.error("[" + _name + "] initArgs error", t);
            throw t;
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        logger.trace("[{}] onInit", getName());
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.trace("[{}] onStart", getName());
    }

    @OnEnd
    @Override
    protected void onEnd() {
        logger.trace("[{}] onEnd", getName());
    }

    //=========================
    //CompanyAgentService
    //=========================

    @Override
    public JadexPolicyAgentBDI getPolicyAgentSyn() {
        return this;
    }

    @Override
    public IFuture<JadexPolicyAgentBDI> getPolicyAgentAsyn() {
        return new Future<>(this);
    }

    //=========================
    //JadexAgentService
    //=========================

    @Override
    public JadexPolicyAgentBDI getAgentSyn() {
        return getPolicyAgentSyn();
    }

    @Override
    public IFuture<JadexPolicyAgentBDI> getAgentAsyn() {
        return getPolicyAgentAsyn();
    }
}
