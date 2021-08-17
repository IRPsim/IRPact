package de.unileipzig.irpact.util.gis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class Point2DList {

    private List<Point2D> list;

    public Point2DList(Point2D... points) {
        list = new ArrayList<>();
        Collections.addAll(list, points);
    }

    public Point2DList(List<Point2D> list) {
        this.list = list;
    }

    public Point2DList reverse() {
        List<Point2D> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return new Point2DList(reversed);
    }

    public void close() {
        if(!isCircle()) {
            add(getFirst());
        }
    }

    public int size() {
        return list.size();
    }

    public void add(Point2D p) {
        list.add(p);
    }

    public void add(int index, Point2D p) {
        list.add(index, p);
    }

    public List<Point2D> getList() {
        return list;
    }

    public Point2D get(int i) {
        return list.get(i);
    }

    public boolean has(Point2D point) {
        for(Point2D p: list) {
            if(p.isEquals(point)) {
                return true;
            }
        }
        return false;
    }

    public int count(Point2D point) {
        int count = 0;
        for(Point2D p: list) {
            if(p.isEquals(point)) {
                count++;
            }
        }
        return count;
    }

    public Point2D getFirst() {
        return get(0);
    }

    public Point2D getLast() {
        return get(size() - 1);
    }

    public boolean isCircle() {
        return getFirst().isEquals(getLast());
    }

    public boolean isEquals(Point2DList other) {
        if(size() != other.size()) {
            return false;
        }
        for(int i = 0; i < size(); i++) {
            if(get(i).isNotEquals(other.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String toLineString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LINESTRING(");
        for(int i = 0; i < size(); i++) {
            if(i > 0) {
                sb.append(", ");
            }
            Point2D p = get(i);
            sb.append(p.getX());
            sb.append(' ');
            sb.append(p.getY());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Point2DList" + list;
    }
}
