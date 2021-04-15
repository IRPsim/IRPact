package de.unileipzig.irpact.util.gis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class Point3DList {

    private List<Point3D> list;

    public Point3DList(Point3D... points) {
        list = new ArrayList<>();
        Collections.addAll(list, points);
    }

    public Point3DList(List<Point3D> list) {
        this.list = list;
    }

    public Point3DList reverse() {
        List<Point3D> reversed = new ArrayList<>(list);
        Collections.reverse(reversed);
        return new Point3DList(reversed);
    }

    public void close() {
        if(!isCircle()) {
            add(getFirst());
        }
    }

    public int size() {
        return list.size();
    }

    public void add(Point3D p) {
        list.add(p);
    }

    public void add(int index, Point3D p) {
        list.add(index, p);
    }

    public List<Point3D> getList() {
        return list;
    }

    public Point3D get(int i) {
        return list.get(i);
    }

    public boolean has(Point3D point) {
        for(Point3D p: list) {
            if(p.isEquals(point)) {
                return true;
            }
        }
        return false;
    }

    public int count(Point3D point) {
        int count = 0;
        for(Point3D p: list) {
            if(p.isEquals(point)) {
                count++;
            }
        }
        return count;
    }

    public Point3D getFirst() {
        return get(0);
    }

    public Point3D getLast() {
        return get(size() - 1);
    }

    public boolean isCircle() {
        return getFirst().isEquals(getLast());
    }

    public boolean isEquals(Point3DList other) {
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
            Point3D p = get(i);
            sb.append(p.getX());
            sb.append(' ');
            sb.append(p.getY());
            sb.append(' ');
            sb.append(p.getZ());
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Point3DList" + list;
    }
}
