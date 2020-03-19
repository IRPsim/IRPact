package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.graph.SimpleEdge;
import de.unileipzig.irpact.commons.graph.SimpleNode;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class LaticeGraph {

    /*
    private static int index(int current, int delta, int len) {
        int pos = current + delta;
        if(pos < 0) {
            return len + pos; // - -
        }
        if(pos >= len) {
            return pos - len;
        }
        return pos;
    }

    private static void random(SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs, int k, String type, Random rnd) {
        List<SimpleNode> nodeList = new ArrayList<>(gs.getNodes());
        for(int i = 0; i < nodeList.size(); i++) {
            SimpleNode n0 = nodeList.get(i);
            int j = 0;
            while(j < k) {
                int index = rnd.nextInt(nodeList.size());
                if(index == i) continue;
                SimpleNode n1 = nodeList.get(index);
                if(!gs.hasEdge(n0, n1, type)) {
                    SimpleEdge<SimpleNode> e = new SimpleEdge<>(
                            n0,
                            n1,
                            n0.getLabel() + "|" + n1.getLabel()
                    );
                    gs.addEdge(e, type);
                    j++;
                }
            }
        }
    }

    private static void latice(SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs, int k, String type) {
        List<SimpleNode> nodeList = new ArrayList<>(gs.getNodes());
        //5 -> -2 < 3 -> -2 -1  1 2 3
        //6 -> -3 < 3 -> -3 -2   1 2 3
        int from = -(k / 2);
        int to = k / 2;
        if(k % 2 != 0) {
            to += 1;
        }
        System.out.println(from + " " + to);
        for(int i = 0; i < nodeList.size(); i++) {
            SimpleNode n0 = nodeList.get(i);
            for(int j = from; j <= to; j++) {
                if(j == 0) continue;
                int index = index(i, j, nodeList.size());
                SimpleNode n1 = nodeList.get(index);
                SimpleEdge<SimpleNode> e = new SimpleEdge<>(
                        n0,
                        n1,
                        n0.getLabel() + "|" + n1.getLabel()
                );
                gs.addEdge(e, type);
            }
        }
    }
    */

    public static void main(String[] args) {
        /*
        SimpleDirectedMultiGraphStructure<SimpleNode, SimpleEdge<SimpleNode>, String> gs = new SimpleDirectedMultiGraphStructure<>(
                LinkedHashMap::new,
                LinkedHashMap::new,
                LinkedHashSet::new,
                new LinkedHashMap<>()
        );
        for(int i = 0; i < 10; i++) {
            gs.addNode(new SimpleNode(String.valueOf(i)));
        }
        //latice(gs, 9, "x");
        random(gs, 5, "x", new Random(42));
        Collection<? extends SimpleEdge<SimpleNode>> edges = gs.getEdges("x");
        edges.forEach(System.out::println);
        */
    }
}
