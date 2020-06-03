package de.unileipzig.irpact.core.agent;

/**
 * Enthaelt alle Daten, welche ein Agent benoetigt.
 *
 * @author Daniel Abitz
 * @implNote Um Daten einfacher an Jadex-Agenten uebergeben zu koennen, werden diese gebuendelt
 *           in einer AgentData-Klasse uebergeben. Ein Agent sollte, so weit es moeglich ist, nur
 *           die AgentData nutzen.
 */
public class AgentData {

    public AgentData() {
    }

    protected String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
