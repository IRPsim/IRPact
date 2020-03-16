package de.unileipzig.irpact.core.spatial.dim2;

import de.unileipzig.irpact.core.spatial.Metric;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class PredefinedModel2D extends Point2DModel {

    private List<Point2D> pointList;

    public PredefinedModel2D(String name, Metric metric, List<Point2D> pointList) {
        super(name, metric);
        this.pointList = pointList;
    }

    public List<Point2D> getPointList() {
        return pointList;
    }
}
