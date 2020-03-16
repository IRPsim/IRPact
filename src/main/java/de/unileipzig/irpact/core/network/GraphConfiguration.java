package de.unileipzig.irpact.core.network;

/**
 * @author Daniel Abitz
 */
public interface GraphConfiguration {

    GraphTopologyScheme getGraphTopologyScheme();

    TopologyManipulationScheme getTopologyManipulationScheme();

    EdgeWeightManipulationScheme getEdgeWeightManipulationScheme();
}
