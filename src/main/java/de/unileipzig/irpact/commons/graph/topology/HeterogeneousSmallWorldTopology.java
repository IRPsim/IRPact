package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.graph.Graph;

import java.util.*;
import java.util.function.Function;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @param <VG>
 * @author Daniel Abitz
 */
public class HeterogeneousSmallWorldTopology<V, E, T, VG> extends HeterogeneousRegularMultiGraphTopology<V, E, T, VG> {

    protected Function<? super E, ? extends V> sourceFunction;
    protected Function<? super E, ? extends V> targetFunction;
    protected Map<VG, Double> grpBetaMapping;

    public HeterogeneousSmallWorldTopology() {
    }

    public void setSourceFunction(Function<? super E, ? extends V> sourceFunction) {
        this.sourceFunction = sourceFunction;
    }

    public void setTargetFunction(Function<? super E, ? extends V> targetFunction) {
        this.targetFunction = targetFunction;
    }

    public void setGrpBetaMapping(Map<VG, Double> grpBetaMapping) {
        this.grpBetaMapping = grpBetaMapping;
    }

    protected Map<VG, Set<V>> groupNodes(Graph<V, E> graph) {
        Map<VG, Set<V>> grpedNodes = new HashMap<>();
        for(V node: graph.getVertices()) {
            VG grp = getGroupFunction.apply(node);
            Set<V> nodeSet = grpedNodes.computeIfAbsent(grp, _grp -> new HashSet<>());
            nodeSet.add(node);
        }
        return grpedNodes;
    }

    @Override
    public void initalizeEdges(Graph<V, E> graph) {
        super.initalizeEdges(graph);
        Map<VG, Set<V>> grpedNodes = groupNodes(graph);
        Set<E> edgesCopy = new LinkedHashSet<>(getEdges(graph));
        for(E edge: edgesCopy) {
            V sourceNode = sourceFunction.apply(edge);
            VG sourceGrp = getGroupFunction.apply(sourceNode);
            final double beta = grpBetaMapping.get(sourceGrp);
            if(rnd.nextDouble() < beta) {
                V targetNode = targetFunction.apply(edge);
                VG targetGrp = getGroupFunction.apply(targetNode);
                while(true) {
                    Set<? extends V> sourceTargets = getTargets(graph, sourceNode);
                    Set<V> potentialTargets = new HashSet<>(grpedNodes.get(targetGrp));
                    potentialTargets.removeAll(sourceTargets);
                    if(!isSelfReferential) {
                        potentialTargets.remove(sourceNode);
                    }
                    if(potentialTargets.isEmpty()) {
                        targetGrp = drawWeightedGroupFunction.apply(sourceGrp, rnd);
                        continue;
                    }
                    V newTarget = CollectionUtil.getRandom(potentialTargets, rnd);
                    graph.removeEdge(edge);
                    addEdge(graph, sourceNode, newTarget);
                    break;
                }
            }
        }
    }
}
