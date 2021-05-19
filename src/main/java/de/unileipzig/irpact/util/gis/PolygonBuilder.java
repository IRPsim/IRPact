package de.unileipzig.irpact.util.gis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class PolygonBuilder {

    private VertexCache vertexCache = new VertexCache();
    private EdgeCache edgeCache = new EdgeCache();

    public PolygonBuilder() {
    }

    public static List<Point3DList> getPolygons(List<Point3DList> lines) {
        PolygonBuilder builder = new PolygonBuilder();
        return builder.run(lines);
    }

    private List<Point3DList> run(List<Point3DList> lines) {
        vertexCache.addPoints(lines);
        edgeCache.addAllEdges(lines);

        List<PotentialPolygon> cycles = new ArrayList<>();
        for(UndirectedEdge edge: edgeCache.getEdges()) {
            Point3D p0 = edge.getList().getFirst();
            appendNext(
                    p0,
                    p0,
                    null,
                    new ArrayList<>(),
                    cycles
            );

            Point3D p1 = edge.getList().getLast();
            appendNext(
                    p0,
                    p0,
                    null,
                    new ArrayList<>(),
                    cycles
            );
        }

        List<PotentialPolygon> distinct = distinct(cycles);
        return distinct.stream()
                .map(PotentialPolygon::toList)
                .collect(Collectors.toList());
    }

    private List<UndirectedEdge> getTargets(
            Point3D currentPosition,
            UndirectedEdge lastEdge) {
        List<UndirectedEdge> targets = new ArrayList<>();
        for(UndirectedEdge e: edgeCache.getEdges()) {
            if(e != lastEdge && e.startsWith(currentPosition)) {
                targets.add(e);
            }
        }
        return targets;
    }

    private void appendNext(
            Point3D startPosition,
            Point3D currentPosition,
            Point3D secondToLastPosition,
            List<UndirectedEdge> currentRoute,
            List<PotentialPolygon> cycles) {
        if(currentRoute.size() > 1) {
            if(startPosition.isEquals(currentPosition)) {
                PotentialPolygon pp = new PotentialPolygon(startPosition, new ArrayList<>(currentRoute), vertexCache);
                cycles.add(pp);
                return;
            }
            if(count(currentRoute, currentPosition) > 1) {
                return; //cancel
            }
        }

        List<UndirectedEdge> targets = getTargets(currentPosition, getLast(currentRoute));
        for(UndirectedEdge next: targets) {
            Point3D nextPoint = next.getTo(currentPosition);
            currentRoute.add(next);
            appendNext(
                    startPosition,
                    next.getTo(currentPosition),
                    currentPosition,
                    currentRoute,
                    cycles
            );
            currentRoute.remove(currentRoute.size() - 1);
        }
    }

    private static List<PotentialPolygon> distinct(List<PotentialPolygon> input) {
        List<PotentialPolygon> output = new ArrayList<>();
        for(PotentialPolygon in: input) {
            boolean exists = false;
            for(PotentialPolygon out: output) {
                if(in.isEquals(out)) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                output.add(in);
                System.out.println("ADDED: " + in); //TODO
            }
        }
        return output;
    }

//    private static List<PotentialPolygon> checkSubPolygon(List<PotentialPolygon> input) {
//        List<PotentialPolygon> sortedAfterSize = new ArrayList<>(input);
//        sortedAfterSize.sort(Comparator.comparingInt(o -> o.getList().size()));
//
//        List<PotentialPolygon> output = new ArrayList<>();
//        for(PotentialPolygon in: sortedAfterSize) {
//            boolean exists = false;
//            for(PotentialPolygon smaller: output) {
//                if(smaller.isSub(in)) {
//                    exists = true;
//                    break;
//                }
//            }
//            if(!exists) {
//                output.add(in);
//            }
//        }
//        return output;
//    }

    private static int count(List<UndirectedEdge> list, Point3D p) {
        return list.stream()
                .mapToInt(e -> e.count(p))
                .sum();
    }

    private static UndirectedEdge getLast(List<UndirectedEdge> list) {
        return list.size() > 0
                ? list.get(list.size() - 1)
                : null;
    }

    /**
     * @author Daniel Abitz
     */
    private static class UndirectedEdge {

        private Point3DList list;

        private UndirectedEdge(Point3DList list) {
            this.list = list;
        }

        private boolean startsWith(Point3D point) {
            return list.getFirst() == point || list.getLast() == point;
        }

        private Point3DList getList() {
            return list;
        }

        private Point3D getTo(Point3D from) {
            Point3D p0 = list.getFirst();
            Point3D p1 = list.getLast();
            if(p0 == from) {
                return p1;
            }
            if(p1 == from) {
                return p0;
            }
            throw new IllegalArgumentException(from.toString());
        }

        private void getPoints(Point3D from, Collection<Point3D> target) {
            if(!startsWith(from)) {
                throw new IllegalArgumentException(from.toString() + ": " + list.toString());
            }
            Point3DList l = list.getFirst() == from
                    ? list
                    : list.reverse();
            for(int i = 1; i < l.size(); i++) {
                target.add(l.get(i));
            }
        }

        private boolean isEquals(Point3DList l) {
            return list.isEquals(l);
        }

        private int count(Point3D p) {
            return list.count(p);
        }

        @Override
        public String toString() {
            return "Edge{" + list.getList() + '}';
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class EdgeCache {

        private Set<UndirectedEdge> edges = new HashSet<>();

        private EdgeCache() {
        }

        private Set<UndirectedEdge> getEdges() {
            return edges;
        }

        private void addIfNotExists(Point3DList list) {
            for(UndirectedEdge e: edges) {
                if(e.isEquals(list)) {
                    return;
                }
            }
            edges.add(new UndirectedEdge(list));
        }

        private void addAllEdges(List<Point3DList> list) {
            for(Point3DList l: list) {
                addEdge(l);
            }
        }

        private void addEdge(Point3DList list) {
            addIfNotExists(list);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class Vertex {

        private Point3D p;
        private int id;

        private Vertex(Point3D p, int id) {
            this.p = p;
            this.id = id;
        }

        private Point3D getPoint() {
            return p;
        }

        private int getId() {
            return id;
        }

        private boolean isEquals(Vertex other) {
            return p.isEquals(other.p);
        }

        private boolean isPoint(Point3D other) {
            return p.isEquals(other);
        }
    }

    /**
     * @author Daniel Abitz
     */
    private static class VertexCache {

        private Set<Vertex> vertices = new HashSet<>();
        private int id = 0;

        private VertexCache() {
        }

        private int nextId() {
            return id++;
        }

        private Set<Vertex> getVertices() {
            return vertices;
        }

        private void addPoints(List<Point3DList> list) {
            for(Point3DList l: list) {
                addPoints(l);
            }
        }

        private void addPoints(Point3DList list) {
            for(Point3D p: list.getList()) {
                get(p);
            }
        }

        private Vertex get(Point3D p) {
            for(Vertex v: vertices) {
                if(v.isPoint(p)) {
                    return v;
                }
            }

            Vertex v = new Vertex(p, nextId());
            vertices.add(v);
            return v;
        }
    }

    /**
     * @author Daniel Abitz
     */
    @SuppressWarnings("FieldCanBeLocal")
    private static class PotentialPolygon {

        private List<UndirectedEdge> edgeList;
        private List<Point3D> pointList;
        private int[] sortedIds;

        private PotentialPolygon(Point3D start, List<UndirectedEdge> edgeList, VertexCache cache) {
            this.edgeList = edgeList;
            this.pointList = traverse(start, edgeList);
            pointList.remove(pointList.size() - 1);
            sortedIds = pointList.stream()
                    .map(cache::get)
                    .mapToInt(Vertex::getId)
                    .sorted()
                    .toArray();
        }

        private static List<Point3D> traverse(Point3D start, List<UndirectedEdge> list) {
            List<Point3D> points = new ArrayList<>();
            points.add(start);
            for(UndirectedEdge edge: list) {
                Point3D from = points.get(points.size() - 1);
                edge.getPoints(from, points);
            }
            return points;
        }

        private List<Point3D> getList() {
            return pointList;
        }

        private boolean isEquals(PotentialPolygon other) {
            return Arrays.equals(sortedIds, other.sortedIds);
        }

//        private boolean isSub(PotentialPolygon bigger) {
//            int[] biggerIds = bigger.sortedIds;
//            for(int i = 0; i < biggerIds.length; i++) {
//                if(biggerIds[i] == sortedIds[0]) {
//                    for(int j = 0; j < sortedIds.length; j++) {
//                        if(biggerIds.length <= i + j || sortedIds[j] != biggerIds[i + j]) {
//                            return false;
//                        }
//                    }
//                    return true;
//                }
//            }
//            return false;
//        }

        private Point3DList toList() {
            Point3DList pl = new Point3DList(new ArrayList<>(pointList));
            pl.close();
            return pl;
        }

        @Override
        public String toString() {
            return "PotentialPolygon{" + pointList + '}';
        }
    }
}
