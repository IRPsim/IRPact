package de.unileipzig.irpact.commons.util.data;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

/**
 * @author Daniel Abitz
 */
public class Quadtree2<T> {

    protected ToDoubleFunction<T> xFunc;
    protected ToDoubleFunction<T> yFunc;
    protected double x1, y1, x2, y2;
    protected Node root;

    public Quadtree2(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setXFunc(ToDoubleFunction<T> xFunc) {
        this.xFunc = xFunc;
    }

    public void setYFunc(ToDoubleFunction<T> yFunc) {
        this.yFunc = yFunc;
    }

    public boolean insert(T point) {
        if(root == null) {
            root = new Node(x1, y1, x2, y2);
        }
        return root.insert(point);
    }

    public int query(Rectangle2D.Double area, Consumer<? super T> consumer) {
        return root == null
                ? 0
                : root.query(area, consumer);
    }

    public int countPoints() {
        return root == null
                ? 0
                : root.countPoints();
    }

    protected double getX(T value) {
        return xFunc.applyAsDouble(value);
    }

    protected double getY(T value) {
        return yFunc.applyAsDouble(value);
    }

    public static int interTests = 0;
    /**
     * @author Daniel Abitz
     */
    protected class Node {

        protected double x1, y1, x2, y2, px, py;
        protected Node nw, ne, sw, se;
        protected List<T> points;

        protected Node(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        protected boolean insert(T point) {
            double px = getX(point);
            double py = getY(point);

            if(notInArea(px, py)) {
                return false;
            }

            if(isSame(px, py)) {
                points.add(point);
                return true;
            }

            if(isEmpty()) {
                points = new ArrayList<>();
                points.add(point);
                this.px = px;
                this.py = py;
                return true;
            }

            return divideAndInsert(point);
        }

        protected int query(Rectangle2D.Double area, Consumer<? super T> consumer) {
            if(isEmpty()) {
                return 0;
            }

            if(hasPoints()) {
                if(area.contains(px, py)) {
                    for(T point: points) {
                        consumer.accept(point);
                    }
                    return points.size();
                } else {
                    return 0;
                }
            }

            if(!intersects(area)) {
                return 0;
            }

            if(isDivided()) {
                return sw.query(area, consumer)
                        + nw.query(area, consumer)
                        + se.query(area, consumer)
                        + ne.query(area, consumer);
            }

            throw new IllegalStateException();
        }

        protected boolean inArea(double px, double py) {
            return px >= x1 &&
                    px < x2 &&
                    py >= y1 &&
                    py < y2;
        }

        protected boolean notInArea(double px, double py) {
            return !inArea(px, py);
        }

        protected boolean intersects(Rectangle2D.Double area) {
            return area.intersects(x1, y1, x2 - x1, y2 - y1);
        }

        protected boolean hasPoints() {
            return points != null && !points.isEmpty();
        }

        protected boolean isSame(double px, double py) {
            return hasPoints()
                    && this.px == px
                    && this.py == py;
        }

        protected boolean isEmpty() {
            return points == null && isNotDivided();
        }

        protected boolean isDivided() {
            return sw != null;
        }

        protected boolean isNotDivided() {
            return sw == null;
        }

        protected boolean divideAndInsert(T point) {
            if(isNotDivided()) {
                subdivide();
            }
            return insertSub(point);
        }

        protected boolean insertSub(T point) {
            if(sw.insert(point)) return true;
            if(nw.insert(point)) return true;
            if(se.insert(point)) return true;
            if(ne.insert(point)) return true;
            throw new IllegalStateException();
        }

        protected void subdivide() {
            double w = x2 - x1;
            double h = y2 - y1;
            double x1w = x1 + w*0.5;
            double y1h = y1 + h*0.5;
            sw = new Node(x1, y1, x1w, y1h);
            nw = new Node(x1, y1h, x1w, y2);
            se = new Node(x1w, y1, x2, y1h);
            ne = new Node(x1w, y1h, x2, y2);

            if(hasPoints()) {
                for(T point: points) {
                    insertSub(point);
                }
                points.clear();
                points = null;
                px = 0;
                py = 0;
            }
        }

        protected int countPoints() {
            if(hasPoints()) {
                return points.size();
            }
            if(isDivided()) {
                return sw.countPoints()
                        + nw.countPoints()
                        + se.countPoints()
                        + ne.countPoints();
            }
            return 0;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "x1=" + x1 +
                    ", y1=" + y1 +
                    ", x2=" + x2 +
                    ", y2=" + y2 +
                    '}';
        }
    }
}
