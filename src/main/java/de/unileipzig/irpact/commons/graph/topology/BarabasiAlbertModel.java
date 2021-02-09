package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.commons.graph.Graph;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
//https://www.frontiersin.org/articles/10.3389/fncom.2011.00011/full
public class BarabasiAlbertModel<V, E, T> extends AbstractMultiGraphTopology<V, E, T> {

    private static final IRPLogger logger = IRPLogging.getLogger(BarabasiAlbertModel.class);

    protected int m;
    protected int m0;
    protected Random rnd;
    protected boolean shuffleAtStart;

    public BarabasiAlbertModel() {
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setM0(int m0) {
        this.m0 = m0;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    protected boolean isSameOrAdjacent(List<V> vList, Graph<V, E> graph, int i, int j) {
        if(i == j) {
            return true;
        }
        V vi = vList.get(i);
        V vj = vList.get(j);
        return hasEdge(graph, vi, vj);
    }

    protected int draw(List<V> vList, Graph<V, E> graph, int i) {
        int j;
        do {
            j = rnd.nextInt(vList.size());
        } while(isSameOrAdjacent(vList, graph, i, j));
        return j;
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
                int j = draw(vList, graph, i);
                V vj = vList.get(j);
                final double b = (double) getOutDegree(graph, vj) / (double) E;
                if(rnd.nextDouble() < b) {
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
                        boolean noConnection = true;
                        while(noConnection) {
                            int h = draw(vList, graph, i);
                            V vh = vList.get(h);
                            final double b2 = (double) getOutDegree(graph, vh) / (double) E;
                            if(rnd.nextDouble() < b2) {
                                addEdge(graph, vh, vi);
                                logger.trace("add edge {}->{}", h, i);
                                E += 1;
                                noConnection = false;
                            }
                        }
                    }
                }//if(rnd.nextDouble() < b)
            }//while(currentDegree < m)
        }
    }
}
