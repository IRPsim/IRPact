package de.unileipzig.irpact.commons.util.data;

import java.awt.geom.Rectangle2D;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

/**
 * @author Daniel Abitz
 */
public class Quadtree<T> {

    protected ToDoubleFunction<T> xFunc;
    protected ToDoubleFunction<T> yFunc;
    protected Node root;

    protected double minX = Double.MAX_VALUE;
    protected double minY = Double.MAX_VALUE;
    protected double maxX = Double.MIN_VALUE;
    protected double maxY = Double.MIN_VALUE;

    public Quadtree() {
    }

    public void setXFunc(ToDoubleFunction<T> xFunc) {
        this.xFunc = xFunc;
    }

    public void setYFunc(ToDoubleFunction<T> yFunc) {
        this.yFunc = yFunc;
    }

    public void insert(T value) {
        root = insert(root, value);
    }

    protected Node insert(Node n, T value) {
        if(n == null) {
            return new Node(value);
        }

        double x = getX(value);
        double y = getY(value);

        if(x < minX) minX = x;
        if(x > maxX) maxX = x;
        if(y < minY) minY = y;
        if(y > maxY) maxY = y;

        if(x < n.getX()) {
            if(y < n.getY()) {
                n.sw = insert(n.sw, value);
            } else {
                n.nw = insert(n.nw, value);
            }
        } else {
            if(y < n.getY()) {
                n.se = insert(n.se, value);
            } else {
                n.ne = insert(n.ne, value);
            }
        }
        return n;
    }

    protected double getX(T value) {
        return xFunc.applyAsDouble(value);
    }

    protected double getY(T value) {
        return yFunc.applyAsDouble(value);
    }

    public int get(
            Rectangle2D.Double rec,
            double maxDifference,
            Consumer<? super T> consumer) {
        double area = Math.abs((maxX - minX) * (maxY - minY));
        double recArea = rec.getWidth() * rec.getHeight();
        double diff = recArea / area;
        if(diff < maxDifference) {
            return get(rec, consumer);
        } else {
            return getIterative(rec, consumer);
        }
    }

    public int get(Rectangle2D.Double rec, Consumer<? super T> consumer) {
        return get(root, rec, consumer);
    }

    protected int get(Node n, Rectangle2D.Double rec, Consumer<? super T> consumer) {
        if(n == null) {
            return 0;
        }

        int c = 0;
        if(rec.contains(n.getX(), n.getY())) {
            c++;
            consumer.accept(n.value);
        }

        if(rec.getMinX() < n.getX()) {
            if(rec.getMinY() < n.getY()) {
                c += get(n.sw, rec, consumer);
            }
            if(rec.getMaxY() >= n.getY()) {
                c += get(n.nw, rec, consumer);
            }
        }

        if(rec.getMaxX() >= n.getX()) {
            if(rec.getMinY() < n.getY()) {
                c += get(n.se, rec, consumer);
            }
            if(rec.getMaxY() >= n.getY()) {
                c += get(n.ne, rec, consumer);
            }
        }

        return c;
    }

    public int getIterative(Rectangle2D.Double rec, Consumer<? super T> consumer) {
        return getIterative(root, rec, consumer);
    }

    protected int getIterative(Node n, Rectangle2D.Double rec, Consumer<? super T> consumer) {
        if(n == null) {
            return 0;
        }

        int c = 0;

        if(rec.contains(n.getX(), n.getY())) {
            c++;
            consumer.accept(n.value);
        }

        if(n.sw != null) c += get(n.sw, rec, consumer);
        if(n.nw != null) c += get(n.nw, rec, consumer);
        if(n.se != null) c += get(n.se, rec, consumer);
        if(n.ne != null) c += get(n.ne, rec, consumer);

        return c;
    }

    public int get2(Rectangle2D.Double rec, Consumer<? super T> consumer) {
        return get2(root, rec, consumer);
    }

    protected int get2(Node n, Rectangle2D.Double rec, Consumer<? super T> consumer) {
        if(n == null) {
            return 0;
        }

        int c = 0;
        Deque<Node> queue = new LinkedList<>();
        queue.add(n);

        Node next;
        while(!queue.isEmpty()) {
            next = queue.remove();

            if(rec.contains(next.getX(), next.getY())) {
                c++;
                consumer.accept(next.value);
            }

            if(rec.getMinX() < next.getX()) {
                if(rec.getMinY() < next.getY()) {
                    if(next.sw != null) {
                        queue.add(next.sw);
                    }
                }
                if(rec.getMaxY() >= next.getY()) {
                    if(next.nw != null) {
                        queue.add(next.nw);
                    }
                }
            }

            if(rec.getMaxX() >= next.getX()) {
                if(rec.getMinY() < next.getY()) {
                    if(next.se != null) {
                        queue.add(next.se);
                    }
                }
                if(rec.getMaxY() >= next.getY()) {
                    if(next.ne != null) {
                        queue.add(next.ne);
                    }
                }
            }
        }

        return c;
    }

    /**
     * @author Daniel Abitz
     */
    protected class Node {

        protected Node nw;
        protected Node ne;
        protected Node sw;
        protected Node se;

        protected T value;

        protected Node(T value) {
            this.value = value;
        }

        protected double getX() {
            return Quadtree.this.getX(value);
        }

        protected double getY() {
            return Quadtree.this.getY(value);
        }

        protected int countRecursivly() {
            int c = 1;
            if(nw != null) {
                c += nw.countRecursivly();
            }
            if(ne != null) {
                c += ne.countRecursivly();
            }
            if(sw != null) {
                c += sw.countRecursivly();
            }
            if(se != null) {
                c += se.countRecursivly();
            }
            return c;
        }
    }
}
