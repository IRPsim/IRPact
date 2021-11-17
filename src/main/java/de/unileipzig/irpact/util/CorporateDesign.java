package de.unileipzig.irpact.util;

import de.unileipzig.irpact.commons.color.ColorBlender;
import de.unileipzig.irpact.commons.color.ColorPalette;

import java.awt.*;

/**
 * @author Daniel Abitz
 */
public class CorporateDesign {

    public static final Color ACCENT0 = new Color(176, 47, 44);
    public static final Color ACCENT1 = new Color(214, 66, 66);
    public static final Color ACCENT2 = new Color(138, 194, 209);
    public static final Color ACCENT3 = new Color(38, 42, 49);
    public static final Color ACCENT4 = new Color(234, 158, 158);
    public static final Color ACCENT5 = new Color(201, 201, 201);

    //blend2
    public static final Color BLEND_0_2 = ColorBlender.blendRGB(ACCENT0, ACCENT2);
    public static final Color BLEND_0_3 = ColorBlender.blendRGB(ACCENT0, ACCENT3);
    public static final Color BLEND_0_5 = ColorBlender.blendRGB(ACCENT0, ACCENT5);
    public static final Color BLEND_2_3 = ColorBlender.blendRGB(ACCENT2, ACCENT3);
    public static final Color BLEND_2_4 = ColorBlender.blendRGB(ACCENT2, ACCENT4);
    public static final Color BLEND_2_5 = ColorBlender.blendRGB(ACCENT2, ACCENT5);

    //blend3
    public static final Color BLEND_0_2_3 = ColorBlender.blendRGB(ACCENT0, ACCENT2, ACCENT3);
    public static final Color BLEND_1_3_5 = ColorBlender.blendRGB(ACCENT1, ACCENT3, ACCENT5);
    public static final Color BLEND_2_3_4 = ColorBlender.blendRGB(ACCENT2, ACCENT3, ACCENT4);
    public static final Color BLEND_3_4_5 = ColorBlender.blendRGB(ACCENT3, ACCENT4, ACCENT5);

    public static ColorPalette DEFAULT_PALETTE = new ColorPalette(
            ACCENT0,
            ACCENT1,
            ACCENT2,
            ACCENT3,
            ACCENT4,
            ACCENT5,
            BLEND_0_2,
            BLEND_0_3,
            BLEND_0_5,
            BLEND_2_3,
            BLEND_2_4,
            BLEND_2_5,
            BLEND_0_2_3,
            BLEND_1_3_5,
            BLEND_2_3_4,
            BLEND_3_4_5
    );

    protected CorporateDesign() {
    }
}
