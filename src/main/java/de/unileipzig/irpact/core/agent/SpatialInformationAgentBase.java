package de.unileipzig.irpact.core.agent;

/**
 * @author Daniel Abitz
 */
public class SpatialInformationAgentBase extends SpatialAgentBase implements InformationAgent {

    protected double informationAuthority;

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }
}
