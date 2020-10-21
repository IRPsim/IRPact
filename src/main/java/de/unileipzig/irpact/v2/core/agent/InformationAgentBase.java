package de.unileipzig.irpact.v2.core.agent;

/**
 * @author Daniel Abitz
 */
public class InformationAgentBase extends AgentBase implements InformationAgent {

    protected double informationAuthority;

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }
}
