package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.commons.graph.Graph;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @author Daniel Abitz
 */
public class FreeMultiGraphTopology<V, E, T> extends AbstractMultiGraphTopology<V, E, T> {

    private static final IRPLogger logger = IRPLogging.getLogger(FreeMultiGraphTopology.class);

    protected boolean selfReferential;
    protected int edgeCount;
    protected Random rnd;

    public FreeMultiGraphTopology() {
    }

    public void setSelfReferential(boolean selfReferential) {
        this.selfReferential = selfReferential;
    }

    public void setEdgeCount(int edgeCount) {
        this.edgeCount = edgeCount;
    }

    public void setRandom(Random rnd) {
        this.rnd = rnd;
    }

    protected boolean isSelfReferential(int i, int j) {
        return !selfReferential && i == j;
    }

    @Override
    public void initalizeEdges(Graph<V, E> graph) {
        List<V> vList = new ArrayList<>(graph.getVertices());
        for(int i = 0; i < vList.size(); i++) {
            final V vi = vList.get(i);
            int c = 0;
            while(c < edgeCount) {
                int j;
                V vj;
                do {
                    j = rnd.nextInt(vList.size());
                    vj = vList.get(j);
                } while(isSelfReferential(i, j) || hasEdge(graph, vi, vj));
                addEdge(graph, vi, vj);
                logger.trace("add edge {}->{}", i, j);
                c++;
            }
        }
    }
}
