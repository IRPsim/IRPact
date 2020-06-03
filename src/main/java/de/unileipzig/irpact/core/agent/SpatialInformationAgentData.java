package de.unileipzig.irpact.core.agent;

/**
 * @author Daniel Abitz
 */
public class SpatialInformationAgentData extends SpatialAgentData {

    public SpatialInformationAgentData() {
    }

    protected double informationAuthority;
    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }
    public double getInformationAuthority() {
        return informationAuthority;
    }
}
