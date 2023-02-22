package de.unileipzig.irpact.commons.advanced;

import java.util.*;

// https://en.wikipedia.org/wiki/K-d_tree
// https://stackoverflow.com/questions/50699240/query-points-in-circle-areas
// https://leetcode.com/problems/queries-on-number-of-points-inside-a-circle/solutions/1182639/K-D-tree-solution-+-Follow-up-with-solutions-overview-C++/
// https://rosettacode.org/wiki/K-d_tree#Java
public class KDTree<T> {

  protected static final long DEFAULT_SEED = 42;

  protected DimensionFunction<? super T> dimensionFunction;
  protected int dimensions;
  protected long seed = DEFAULT_SEED;
  protected Node<T> root;
  protected double[] min;
  protected double[] max;

  public void setDimensionFunction(DimensionFunction<? super T> dimensionFunction) {
    this.dimensionFunction = dimensionFunction;
  }

  public void build(int dimensions,
                    Collection<? extends T> elements) {
    this.dimensions = dimensions;
    Builder<T> builder = new Builder<>(dimensions, dimensionFunction, new Random(seed));
    this.root = builder.build(elements);
    this.min = builder.min;
    this.max = builder.max;
  }

  protected double value(T value, int dimension) {
    return dimensionFunction.get(value, dimension);
  }

  protected void add(Node<T> node, Collection<? super T> container) {
    if (node != null) {
      node.addTo(container);
    }
  }

  protected void addAll(Node<T> node, Collection<? super T> container) {
    if (node != null) {
      if (node.subElements == null) {
        addAll0(node, container);
      } else {
        container.addAll(node.subElements);
      }
    }
  }

  protected void addAll0(Node<T> node, Collection<? super T> container) {
    if (node != null) {
      node.addTo(container);
      addAll0(node.left, container);
      addAll0(node.right, container);
    }
  }

  public void buildSubElementLists() {
    if (root != null) {
      root.buildSubElementList();
    }
  }

  public int countElements() {
    return countElements(root);
  }

  protected int countElements(Node<?> node) {
    if (node == null) {
      return 0;
    } else {
      return node.elements() + countElements(node.left) + countElements(node.right);
    }
  }

  public int countNodes() {
    return countNodes(root);
  }

  protected int countNodes(Node<?> node) {
    if (node == null) {
      return 0;
    } else {
      return 1 + countNodes(node.left) + countNodes(node.right);
    }
  }

  protected int nextDimension(int dimension) {
    return (dimension + 1) % dimensions;
  }

  public Node<T> searchReference(T reference) {
    return searchReference(root, reference);
  }

  protected Node<T> searchReference(Node<T> node, T reference) {
    if (node == null) {
      return null;
    } else if (node.reference == reference) {
      return node;
    } else {
      Node<T> next = searchReference(node.left, reference);
      if (next != null) {
        return next;
      } else {
        return searchReference(node.right, reference);
      }
    }
  }

  public static class Node<T> {

    protected List<T> others;
    protected List<T> subElements;
    protected T reference;
    protected Node<T> left;
    protected Node<T> right;

    protected Node(T reference) {
      this.reference = reference;
    }

    public T getReference() {
      return reference;
    }

    public int elements() {
      return others == null ? 1 : others.size() + 1;
    }

    protected void buildSubElementList() {
      if (subElements == null) {
        subElements = new ArrayList<>();
        subElements.add(reference);
        if (others != null) {
          subElements.addAll(others);
        }
        if (left != null) {
          left.buildSubElementList();
          subElements.addAll(left.subElements);
        }
        if (right != null) {
          right.buildSubElementList();
          subElements.addAll(right.subElements);
        }
      }
    }

    protected void addTo(Collection<? super T> container) {
      container.add(reference);
      if (others != null) {
        container.addAll(others);
      }
    }

    protected void addOthers(List<? extends T> others) {
      if (others.size() > 1) {
        this.others = new ArrayList<>();
        for (int i = 1; i < others.size(); i++) {
          this.others.add(others.get(i));
        }
      }
    }

    public boolean collect(Collection<? super T> target) {
      boolean changed = target.add(reference);
      if (others != null) {
        changed |= target.addAll(others);
      }
      if (left != null) {
        changed |= left.collect(target);
      }
      if (right != null) {
        changed |= right.collect(target);
      }
      return changed;
    }

    @Override
    public String toString() {
      return "Node(" + reference + ")";
    }
  }

  protected static class Builder<T> {

    protected final List<Comparator<? super Node<T>>> comparators;
    protected final DimensionFunction<? super T> dimensionFunction;
    protected final Random random;
    protected final int dimensions;
    protected final double[] min;
    protected final double[] max;

    public Builder(int dimensions,
                   DimensionFunction<? super T> dimensionFunction,
                   Random random) {
      this.dimensions = dimensions;
      this.dimensionFunction = dimensionFunction;
      this.random = random;
      this.min = new double[dimensions];
      this.max = new double[dimensions];
      Arrays.fill(min, Double.NaN);
      Arrays.fill(max, Double.NaN);

      this.comparators = new ArrayList<>(dimensions);
      for (int i = 0; i < dimensions; i++) {
        final int dimension = i;
        comparators.add((o1, o2) -> {
          double key1 = dimensionFunction.get(o1.reference, dimension);
          double key2 = dimensionFunction.get(o2.reference, dimension);
          return Double.compare(key1, key2);
        });
      }
    }

    protected void updateMinMax(T element) {
      for (int i = 0; i < dimensions; i++) {
        setMin(dimensionFunction.get(element, i), min, i);
        setMax(dimensionFunction.get(element, i), max, i);
      }
    }

    protected void setMin(double value, double[] array, int dimension) {
      if (Double.isNaN(array[dimension])) {
        array[dimension] = value;
      } else {
        array[dimension] = Math.min(value, array[dimension]);
      }
    }

    protected void setMax(double value, double[] array, int dimension) {
      if (Double.isNaN(array[dimension])) {
        array[dimension] = value;
      } else {
        array[dimension] = Math.max(value, array[dimension]);
      }
    }

    protected Node<T> build(Collection<? extends T> elements) {
      List<Node<T>> nodes = toNodes(elements);
      return build(nodes, 0, nodes.size(), 0);
    }

    protected List<Node<T>> toNodes(Collection<? extends T> elements) {
      Map<double[], List<T>> grouped = new HashMap<>();
      for (T element : elements) {
        double[] position = position(element);
        grouped.computeIfAbsent(position, key -> new ArrayList<>()).add(element);
      }
      List<Node<T>> nodes = new ArrayList<>(grouped.size());
      for (List<T> group : grouped.values()) {
        Node<T> node = new Node<>(group.get(0));
        updateMinMax(node.reference);
        node.addOthers(group);
        nodes.add(node);
      }
      return nodes;
    }

    protected double[] position(T element) {
      double[] position = new double[dimensions];
      for (int i = 0; i < dimensions; i++) {
        position[i] = dimensionFunction.get(element, i);
      }
      return position;
    }

    protected Node<T> build(List<Node<T>> nodes, int begin, int end, int dimension) {
      if (end <= begin) {
        return null;
      }
      int n = begin + (end - begin) / 2;
      Node<T> node = QuickSelect.getInstance().select(nodes, begin, end - 1, n,
          comparators.get(dimension), random);
      dimension = (dimension + 1) % dimensions;
      node.left = build(nodes, begin, n, dimension);
      node.right = build(nodes, n + 1, end, dimension);
      return node;
    }
  }
}
