package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.commons.graph.Graph;
import de.unileipzig.irptools.util.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
//https://www.frontiersin.org/articles/10.3389/fncom.2011.00011/full
public class WattsStrogatzModel<V, E, T> extends AbstractMultiGraphTopology<V, E, T> {

    private static final IRPLogger logger = IRPact.getUtilLogger(WattsStrogatzModel.class);

    protected int k;
    protected double beta;
    protected boolean selfReferential;
    protected Random rnd;

    public WattsStrogatzModel() {
    }

    public void setK(int k) {
        this.k = k;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public void setSelfReferential(boolean selfReferential) {
        this.selfReferential = selfReferential;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    protected boolean isSelfReferential(int i, int j) {
        return !selfReferential && i == j;
    }

    @Override
    public void initalizeEdges(Graph<V, E> graph) {
        if(k % 2 != 0) {
            throw new IllegalArgumentException("odd k: " + k);
        }
        List<V> vList = new ArrayList<>(graph.getVertices());
        final int n = vList.size();
        final int kHalf = k / 2;
        //phase 1
        for(int i = 0; i < n; i++) {
            for(int _j = i + 1; _j < i + 1 + kHalf; _j++) {
                int j = _j >= n ? _j - n : _j;
                V fromNode = vList.get(i);
                V toNode = vList.get(j);
                addEdge(graph, fromNode, toNode);
                addEdge(graph, toNode, fromNode);
                logger.trace("add edge {}->{}", i, j);
                logger.trace("add edge {}->{}", j, i);
            }
        }
        //phase 2
        for(int i = 0; i < n; i++) {
            final V vi = vList.get(i);
            for(int _j = i + 1; _j < i + 1 + kHalf; _j++) {
                final int j = _j >= n ? _j - n : _j;
                final V vj = vList.get(j);
                if(rnd.nextDouble() < beta) {
                    int k;
                    V vk;
                    do {
                        k = rnd.nextInt(n);
                        vk = vList.get(k);
                    } while(isSelfReferential(i, j) || hasEdge(graph, vi, vk));
                    removeEdge(graph, vi, vj);
                    addEdge(graph, vi, vk);
                    logger.trace("rewire: {}->{} to {}->{}", i, j, i, k);
                }
            }
        }
    }
}
