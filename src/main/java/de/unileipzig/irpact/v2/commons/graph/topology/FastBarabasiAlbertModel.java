package de.unileipzig.irpact.v2.commons.graph.topology;

import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.v2.commons.CollectionUtil;
import de.unileipzig.irpact.v2.commons.graph.Graph;
import de.unileipzig.irptools.util.IRPLogger;

import java.util.*;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public class FastBarabasiAlbertModel<V, E, T> extends BarabasiAlbertModel<V, E, T> {

    private static final IRPLogger logger = IRPact.getUtilLogger(FastBarabasiAlbertModel.class);

    public FastBarabasiAlbertModel() {
    }

    protected int drawWeighted(List<V> vList, Graph<V, E> graph, int i, int E) {
        int drawnJ = CollectionUtil.getWeightedRandomIndex(
                vList,
                (vj, j) -> {
                    if(isSameOrAdjacent(vList, graph, i, j)) {
                        return 0.0;
                    } else {
                        return (double) getDegree(graph, vj) / (double) E;
                    }
                },
                rnd
        );
        if(drawnJ == -1) {
            throw new IllegalStateException("no valid edge found");
        }
        return drawnJ;
    }

    @Override
    public void initalizeEdges(Graph<V, E> graph) {
        List<V> vList = new ArrayList<>(graph.getVertices());
        if(shuffleAtStart) {
            Collections.shuffle(vList, rnd);
        }
        final int N = vList.size();
        int E = 0;
        //initial
        for(int i = 0; i < m0; i++) {
            for(int j = i + 1; j < m0; j++) {
                V fromNode = vList.get(i);
                V toNode = vList.get(j);
                addEdge(graph, fromNode, toNode);
                addEdge(graph, toNode, fromNode);
                logger.trace("add edge {}->{}", i, j);
                logger.trace("add edge {}->{}", j, i);
                E += 2;
            }
        }
        //rest
        for(int i = m0; i < N; i++) {
            V vi = vList.get(i);
            while(getDegree(graph, vi) < m) {
                int j = drawWeighted(vList, graph, i, E);
                V vj = vList.get(j);
                final double b = (double) getDegree(graph, vj) / (double) E;
                if(rnd.nextDouble() < b) {
                    addEdge(graph, vi, vj);
                    addEdge(graph, vj, vi);
                    logger.trace("add edge {}->{}", i, j);
                    logger.trace("add edge {}->{}", j, i);
                    E += 2;
                } else {
                    addEdge(graph, vi, vj);
                    logger.trace("add edge {}->{}", i, j);
                    E += 1;
                    int h = drawWeighted(vList, graph, i, E);
                    V vh = vList.get(h);
                    addEdge(graph, vh, vi);
                    logger.trace("add edge {}->{}", h, i);
                    E += 1;
                }
            }//while(currentDegree < m)
        }
    }
}
