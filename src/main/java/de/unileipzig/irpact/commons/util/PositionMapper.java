package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public interface PositionMapper {

    void reset();

    void setBoundingBox(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY);

    void update(double x, double y);

    double mapX(double x);

    double mapY(double y);
}
