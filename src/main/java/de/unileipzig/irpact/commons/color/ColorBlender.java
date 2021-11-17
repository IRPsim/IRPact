package de.unileipzig.irpact.commons.color;

import java.awt.*;

/**
 * @author Daniel Abitz
 */
public class ColorBlender {

    protected ColorBlender() {
    }

    public static Color blendRGB(Color c1, Color c2) {
        return blendRGB(c1, 1, c2, 1);
    }

    public static Color blendRGB(Color c1, double r1, Color c2, double r2) {
        //norm
        double sum = r1 + r2;
        double n1 = r1 / sum;
        double n2 = r2 / sum;

        return blendRGBNormed(c1, n1, c2, n2);
    }

    protected static Color blendRGBNormed(Color c1, double n1, Color c2, double n2) {
        //combine
        double r = c1.getRed() * n1 + c2.getRed() * n2;
        double g = c1.getGreen() * n1 + c2.getGreen() * n2;
        double b = c1.getBlue() * n1 + c2.getBlue() * n2;

        return new Color((int) r, (int) g, (int) b);
    }

    public static Color blendRGB(Color c1, Color c2, Color c3) {
        return blendRGB(c1, 1, c2, 1, c3, 1);
    }

    public static Color blendRGB(Color c1, double r1, Color c2, double r2, Color c3, double r3) {
        //norm
        double sum = r1 + r2 + r3;
        double n1 = r1 / sum;
        double n2 = r2 / sum;
        double n3 = r3 / sum;

        return blendRGBNormed(c1, n1, c2, n2, c3, n3);
    }

    protected static Color blendRGBNormed(Color c1, double n1, Color c2, double n2, Color c3, double n3) {
        //combine
        double r = c1.getRed() * n1 + c2.getRed() * n2 + c3.getRed() * n3;
        double g = c1.getGreen() * n1 + c2.getGreen() * n2 + c3.getGreen() * n3;
        double b = c1.getBlue() * n1 + c2.getBlue() * n2 + c3.getBlue() * n3;

        return new Color((int) r, (int) g, (int) b);
    }
}
