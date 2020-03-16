package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public class EgoistTopology implements GraphTopologyScheme {

    public static final String NAME = EgoistTopology.class.getSimpleName();
    public static final EgoistTopology INSTANCE = new EgoistTopology();

    @Override
    public void initalize(SocialGraph graph) {

    }
}
