package de.unileipzig.irpact.experimental.tests.extendedagent;

import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;

/**
 * @author Daniel Abitz
 */
public interface AgentInterface {

    @OnInit
    void onInit();

    @OnStart
    void onStart();

    @OnEnd
    void onEnd();
}
