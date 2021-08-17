package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public interface PositionMapper {

    void reset();

    void setBoundingBox(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY);

    void computeBoundingBoxHeight(double topLeftX, double topLeftY, double bottomRightX);

    void computeBoundingBoxWidth(double topLeftX, double topLeftY, double bottomRightY);

    void computeBoundingBox(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY, boolean preferSmallerArea);

    double getBoundingBoxWidth();

    double getBoundingBoxHeight();

    void update(double x, double y);

    double mapX(double x);

    double mapY(double y);
}
