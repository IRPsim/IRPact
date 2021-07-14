package de.unileipzig.irpact.commons.geo;

import de.unileipzig.irpact.commons.util.PositionMapper;

/**
 * @author Daniel Abitz
 */
public class LatLng2XY implements PositionMapper {

    protected double radius;
    protected boolean empty = true;
    protected double topLeftX;
    protected double topLeftY;
    protected double topLeftLat;
    protected double topLeftLng;
    protected Double topLeftGlobalX;
    protected Double topLeftGlobalY;
    protected double bottomRightX;
    protected double bottomRightY;
    protected double bottomRightLat;
    protected double bottomRightLng;
    protected Double bottomRightGlobalX;
    protected Double bottomRightGlobalY;
    protected Double cosPhi;

    public LatLng2XY(double radius) {
        this.radius = radius;
    }

    public LatLng2XY(boolean useKM) {
        this(useKM ? GeoMath.EARTH_RADIUS_KILOMETER : GeoMath.EARTH_RADIUS_METER);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public double getTopLeftX() {
        return topLeftX;
    }

    public double getTopLeftY() {
        return topLeftY;
    }

    public double getTopLeftLat() {
        return topLeftLat;
    }

    public double getTopLeftLng() {
        return topLeftLng;
    }

    public double getBottomRightX() {
        return bottomRightX;
    }

    public double getBottomRightY() {
        return bottomRightY;
    }

    public double getBottomRightLat() {
        return bottomRightLat;
    }

    public double getBottomRightLng() {
        return bottomRightLng;
    }

    @Override
    public void setBoundingBox(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    @Override
    public void update(double x, double y) {
        updateLatLng(y, x);
    }

    public void updateLatLng(double lat, double lng) {
        if(empty) {
            topLeftLat = lat;
            topLeftLng = lng;
            bottomRightLat = lat;
            bottomRightLng = lng;
            empty = false;
        } else {
            if(topLeftLat < lat) {
                topLeftLat = lat;
            }
            if(lat < bottomRightLat) {
                bottomRightLat = lat;
            }
            if(lng < topLeftLng) {
                topLeftLng = lng;
            }
            if(bottomRightLng < lng) {
                bottomRightLng = lng;
            }
        }
        resetParams();
    }

    @Override
    public void reset() {
        empty = true;
        topLeftLat = 0;
        topLeftLng = 0;
        topLeftX = 0;
        topLeftY = 0;
        bottomRightLat = 0;
        bottomRightLng = 0;
        bottomRightX = 0;
        bottomRightY = 0;
        resetParams();
    }

    protected void resetParams() {
        cosPhi = null;
        topLeftGlobalX = null;
        topLeftGlobalY = null;
        bottomRightGlobalX = null;
        bottomRightGlobalY = null;
    }

    public double getCosPhi() {
        if(cosPhi == null) {
            cosPhi = GeoMath.cosPhi(topLeftLat, bottomRightLat);
        }
        return cosPhi;
    }

    public double getTopLeftGlobalX() {
        if(topLeftGlobalX == null) {
            topLeftGlobalX = latlng2globalX(topLeftLng);
        }
        return topLeftGlobalX;
    }

    public double getTopLeftGlobalY() {
        if(topLeftGlobalY == null) {
            topLeftGlobalY = latlng2globalY(topLeftLat);
        }
        return topLeftGlobalY;
    }

    public double getBottomRightGlobalX() {
        if(bottomRightGlobalX == null) {
            bottomRightGlobalX = latlng2globalX(bottomRightLng);
        }
        return bottomRightGlobalX;
    }

    public double getBottomRightGlobalY() {
        if(bottomRightGlobalY == null) {
            bottomRightGlobalY = latlng2globalY(bottomRightLat);
        }
        return bottomRightGlobalY;
    }

    public double latlng2globalX(double lng) {
        return GeoMath.latlng2globalX(lng, radius, getCosPhi());
    }

    public double latlng2globalY(double lat) {
        return GeoMath.latlng2globalY(lat, radius);
    }

    public double latlng2screenX(double lng) {
        double globalX = latlng2globalX(lng);
        return GeoMath.latlng2screenX(globalX, topLeftX, getTopLeftGlobalX(), bottomRightX, getBottomRightGlobalX());
    }

    public double latlng2screenY(double lat) {
        double globalY = latlng2globalY(lat);
        return GeoMath.latlng2screenY(globalY, topLeftY, getTopLeftGlobalY(), bottomRightY, getBottomRightGlobalY());
    }

    @Override
    public double mapX(double x) {
        return latlng2screenX(x);
    }

    @Override
    public double mapY(double y) {
        return latlng2screenY(y);
    }
}
