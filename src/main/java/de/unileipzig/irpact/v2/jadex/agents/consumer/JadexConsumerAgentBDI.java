package de.unileipzig.irpact.v2.jadex.agents.consumer;

import de.unileipzig.irpact.v2.jadex.agents.AbstractJadexAgentBDI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
public class JadexConsumerAgentBDI extends AbstractJadexAgentBDI {

    private static final Logger LOGGER = LoggerFactory.getLogger(JadexConsumerAgentBDI.class);

    public JadexConsumerAgentBDI() {
    }

    @Override
    public JadexConsumerAgentInitializationData getData() {
        return (JadexConsumerAgentInitializationData) data;
    }

    @Override
    protected Logger log() {
        return LOGGER;
    }

    @Override
    protected void onInit() {
        initData();
        log().trace("[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace("[{}] start", getName());
    }

    @Override
    protected void onEnd() {
        log().trace("[{}] end", getName());
    }
}
