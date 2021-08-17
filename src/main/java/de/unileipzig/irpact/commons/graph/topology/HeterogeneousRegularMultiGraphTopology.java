package de.unileipzig.irpact.commons.graph.topology;

import de.unileipzig.irpact.commons.graph.Graph;
import de.unileipzig.irpact.commons.util.Rnd;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @param <V>
 * @param <E>
 * @param <T>
 * @param <VG>
 * @author Daniel Abitz
 */
public class HeterogeneousRegularMultiGraphTopology<V, E, T, VG> extends AbstractMultiGraphTopology<V, E, T> {

    protected Function<? super V, ? extends VG> getGroupFunction;
    protected BiFunction<? super VG, ? super Rnd, ? extends VG> drawWeightedGroupFunction;
    protected Map<VG, Integer> grpZMapping;
    protected boolean isSelfReferential;
    protected Rnd rnd;

    public HeterogeneousRegularMultiGraphTopology() {
    }

    public void setGetGroupFunction(Function<? super V, ? extends VG> getGroupFunction) {
        this.getGroupFunction = getGroupFunction;
    }

    public void setDrawWeightedGroupFunction(BiFunction<? super VG, ? super Rnd, ? extends VG> drawWeightedGroupFunction) {
        this.drawWeightedGroupFunction = drawWeightedGroupFunction;
    }

    public void setGrpZMapping(Map<VG, Integer> grpZMapping) {
        this.grpZMapping = grpZMapping;
    }

    public void setSelfReferential(boolean selfReferential) {
        isSelfReferential = selfReferential;
    }

    public void setRandom(Rnd rnd) {
        this.rnd = rnd;
    }

    //Anmerkung: Gefahr von Endlosloop, wenn z zu gross bzw zu wenige nodes
    @Override
    public void initalizeEdges(Graph<V, E> graph) {
        Map<VG, NodeHelper<V>> grpNodes = new HashMap<>();
        for(V node: graph.getVertices()) {
            VG grp = getGroupFunction.apply(node);
            NodeHelper<V> grpHelper = grpNodes.computeIfAbsent(grp, _grp -> new NodeHelper<>());
            grpHelper.add(node);
        }
        for(NodeHelper<V> grpHelper: grpNodes.values()) {
            grpHelper.init(rnd);
        }
        int x = 0;
        for(V sourceNode: graph.getVertices()) {
            VG sourceGrp = getGroupFunction.apply(sourceNode);
            int i = 0;
            final int zMapping = grpZMapping.get(sourceGrp);
            while(i < zMapping) {
                VG targetGrp = drawWeightedGroupFunction.apply(sourceGrp, rnd);
                NodeHelper<V> targetHelper = grpNodes.get(targetGrp);
                V peek = targetHelper.peek();
                if(!isSelfReferential && peek == sourceNode) {
                    targetHelper.discard();
                    continue;
                }
                if(hasEdge(graph, sourceNode, peek)) {
                    targetHelper.discard();
                    continue;
                }
                V targetNode = targetHelper.next(rnd);
                if(peek != targetNode) {
                    throw new IllegalStateException();
                }
                addEdge(graph, sourceNode, targetNode);
                i++;
            }
        }
    }

    /**
     * @param <V>
     * @author Daniel Abitz
     */
    protected static class NodeHelper<V> {

        //In der front-Liste sind alle Knoten, welche i eingehende Kanten haben.
        //In der back-Liste sind alle Knoten, welche i+1 eingehende Kanten haben.
        //Fuer eine neue Verknuepfung wird aus der front-Liste das erste Elemente entfernt
        //und in die back-Liste eingefuegt.
        //Sobald die front-Liste leer ist, wird sie mit der back-Liste getauscht und geshuffelt.
        //Damit besteht eine zufaellige Reihenefolge der Elemente.
        protected LinkedList<V> back = new LinkedList<>();
        protected LinkedList<V> front = new LinkedList<>();

        protected NodeHelper() {
        }

        protected void add(V node) {
            front.add(node);
        }

        protected void init(Rnd rnd) {
            shuffle(rnd);
        }

        protected void shuffle(Rnd rnd) {
            rnd.shuffle(front);
        }

        protected V peek() {
            return front.getFirst();
        }

        protected void discard() {
            V next = front.removeFirst();
            front.addLast(next);
        }

        protected V next(Rnd rnd) {
            V next = front.removeFirst();
            back.addLast(next);
            if(front.isEmpty()) {
                LinkedList<V> temp = front;
                front = back;
                back = temp;
                shuffle(rnd);
            }
            return next;
        }
    }
}
