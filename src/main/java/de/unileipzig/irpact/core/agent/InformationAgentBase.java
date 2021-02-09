package de.unileipzig.irpact.core.agent;

/**
 * @author Daniel Abitz
 */
public abstract class InformationAgentBase extends AgentBase implements InformationAgent {

    protected double informationAuthority;

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }
}
