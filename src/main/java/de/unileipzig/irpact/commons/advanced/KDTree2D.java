package de.unileipzig.irpact.commons.advanced;

import de.unileipzig.irpact.core.spatial.twodim.Metric2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KDTree2D<T> extends KDTree<T> {

  public static final int DIMENSIONS_2D = 2;
  public static final int DIM_X = 0;
  public static final int DIM_Y = 1;

  public void build(Collection<? extends T> elements) {
    build(DIMENSIONS_2D, elements);
  }

  public List<T> circularQuery(T point, double radius, Metric2D metric) {
    List<T> container = new ArrayList<>();
    circularQuery(point, radius, container, metric);
    return container;
  }

  public void circularQuery(T point, double radius, Collection<? super T> container, Metric2D metric) {
    circularQuery(
        root,
        value(point, DIM_X), value(point, DIM_Y), radius,
        DIM_X,
        min[DIM_X], min[DIM_Y], max[DIM_X], max[DIM_Y],
        container,
        metric);
  }

  // https://stackoverflow.com/questions/59426224/how-to-implement-range-search-in-kd-tree
  protected void circularQuery(Node<T> node,
                               double px, double py, double radius,
                               int dimension,
                               double rx1, double ry1, double rx2, double ry2,
                               Collection<? super T> container,
                               Metric2D metric) {
    if (rectangleInCircle(px, py, radius, rx1, ry1, rx2, ry2, metric)) {
      addAll(node, container);
    } else {
      int nextDimension = nextDimension(dimension);
      double value = value(node.reference, dimension);

      if (node.left != null) {
        double _rx2 = dimension == DIM_X ? value : rx2;
        double _ry2 = dimension == DIM_Y ? value : ry2;
        if (rectangleIntersectsCircle2(px, py, radius, rx1, ry1, _rx2, _ry2, metric)) {
          circularQuery(node.left, px, py, radius, nextDimension,
              rx1, ry1, _rx2, _ry2, container, metric);
        }
      }

      if (node.right != null) {
        double _rx1 = dimension == DIM_X ? value : rx1;
        double _ry1 = dimension == DIM_Y ? value : ry1;
        if (rectangleIntersectsCircle2(px, py, radius, _rx1, _ry1, rx2, ry2, metric)) {
          circularQuery(node.right, px, py, radius, nextDimension,
              _rx1, _ry1, rx2, ry2, container, metric);
        }
      }

      if (pointInCircle(px, py, radius,
          value(node.reference, DIM_X), value(node.reference, DIM_Y), metric)) {
        add(node, container);
      }
    }
  }

  public static boolean pointInCircle(double cx, double cy, double r,
                                      double px, double py,
                                      Metric2D metric) {
    //return (px - cx)*(px - cx) + (py - cy)*(py - cy) <= r2;
    return metric.distance(cx, cy, px, py) < r;
  }

  public static boolean rectangleInCircle(double cx, double cy, double r,
                                          double x1, double y1, double x2, double y2,
                                          Metric2D metric) {
    return pointInCircle(cx, cy, r, x1, y1, metric)
        && pointInCircle(cx, cy, r, x1, y2, metric)
        && pointInCircle(cx, cy, r, x2, y1, metric)
        && pointInCircle(cx, cy, r, x2, y2, metric);
  }

  // https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/
  public static boolean rectangleIntersectsCircle2(double cx, double cy, double r,
                                                   double x1, double y1, double x2, double y2,
                                                   Metric2D metric) {
    double nx = Math.max(x1, Math.min(cx, x2));
    double ny = Math.max(y1, Math.min(cy, y2));
//    double dx = nx - cx;
//    double dy = ny - cy;
//    return dx*dx + dy*dy <= r2;
    return metric.distance(cx, cy, nx, ny) <= r;
  }
}
