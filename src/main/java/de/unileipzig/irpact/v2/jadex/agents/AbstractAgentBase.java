package de.unileipzig.irpact.v2.jadex.agents;

import de.unileipzig.irpact.v2.core.misc.InitializationData;
import de.unileipzig.irpact.v2.core.time.TimeModel;
import de.unileipzig.irpact.v2.jadex.JadexConstants;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAgentBase {

    protected InitializationData data;

    public AbstractAgentBase() {
    }

    protected void initData(Map<String, Object> input) {
        data = (InitializationData) input.get(JadexConstants.DATA);
    }

    protected InitializationData getData() {
        return data;
    }

    public String getName() {
        return data.getName();
    }

    protected abstract Logger log();

    public abstract JadexSimulationEnvironment getEnvironment();

    protected JadexTimeModel getTimeModel() {
        return getEnvironment().getTimeModel();
    }

    @OnInit
    protected abstract void onInit();

    @OnStart
    protected abstract void onStart();

    @OnEnd
    protected abstract void onEnd();
}
