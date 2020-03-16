package de.unileipzig.irpact.draft;

/**
 * @author Daniel Abitz
 * @since 0.0
 */
public class Settings {

    //description: Anzahl der Agenten angeben.
    //type: Integer
    //identifier:
    //unit:
    //domain: [0,)
    //validation:
    //hidden:
    //processing:
    private int numberOfAgents;

    public Settings() {
    }

    public void setNumberOfAgents(int numberOfAgents) {
        this.numberOfAgents = numberOfAgents;
    }

    public int getNumberOfAgents() {
        return numberOfAgents;
    }
}
