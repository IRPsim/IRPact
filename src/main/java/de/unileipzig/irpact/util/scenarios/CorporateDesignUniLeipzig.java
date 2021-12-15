package de.unileipzig.irpact.util.scenarios;

import de.unileipzig.irpact.commons.color.ColorBlender;
import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.io.param.input.color.InColorARGB;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;

import java.awt.*;

/**
 * @author Daniel Abitz
 */
public final class CorporateDesignUniLeipzig {

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

    public static ColorPalette CD_UL = new ColorPalette(
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

    public static final InColorARGB IN_ACCENT0 = new InColorARGB("CD_UL_1", ACCENT0, 1);
    public static final InColorARGB IN_ACCENT1 = new InColorARGB("CD_UL_2", ACCENT1, 2);
    public static final InColorARGB IN_ACCENT2 = new InColorARGB("CD_UL_3", ACCENT2, 3);
    public static final InColorARGB IN_ACCENT3 = new InColorARGB("CD_UL_4", ACCENT3, 4);
    public static final InColorARGB IN_ACCENT4 = new InColorARGB("CD_UL_5", ACCENT4, 5);
    public static final InColorARGB IN_ACCENT5 = new InColorARGB("CD_UL_6", ACCENT5, 6);
    public static final InColorARGB IN_BLEND_0_2 = new InColorARGB("CD_UL_7", BLEND_0_2, 7);
    public static final InColorARGB IN_BLEND_0_3 = new InColorARGB("CD_UL_8", BLEND_0_3, 8);
    public static final InColorARGB IN_BLEND_0_5 = new InColorARGB("CD_UL_9", BLEND_0_5, 9);
    public static final InColorARGB IN_BLEND_2_3 = new InColorARGB("CD_UL_10", BLEND_2_3, 10);
    public static final InColorARGB IN_BLEND_2_4 = new InColorARGB("CD_UL_11", BLEND_2_4, 11);
    public static final InColorARGB IN_BLEND_2_5 = new InColorARGB("CD_UL_12", BLEND_2_5, 12);
    public static final InColorARGB IN_BLEND_0_2_3 = new InColorARGB("CD_UL_13", BLEND_0_2_3, 13);
    public static final InColorARGB IN_BLEND_1_3_5 = new InColorARGB("CD_UL_14", BLEND_1_3_5, 14);
    public static final InColorARGB IN_BLEND_2_3_4 = new InColorARGB("CD_UL_15", BLEND_2_3_4, 15);
    public static final InColorARGB IN_BLEND_3_4_5 = new InColorARGB("CD_UL_16", BLEND_3_4_5, 16);

    public static final InColorPalette IN_CD_UL = new InColorPalette("CD_UL_PALETTE",
            IN_ACCENT0,
            IN_ACCENT1,
            IN_ACCENT2,
            IN_ACCENT3,
            IN_ACCENT4,
            IN_ACCENT5,
            IN_BLEND_0_2,
            IN_BLEND_0_3,
            IN_BLEND_0_5,
            IN_BLEND_2_3,
            IN_BLEND_2_4,
            IN_BLEND_2_5,
            IN_BLEND_0_2_3,
            IN_BLEND_1_3_5,
            IN_BLEND_2_3_4,
            IN_BLEND_3_4_5
    );

    public static final InColorPalette IN_CD_UL_2 = new InColorPalette("CD_UL_PALETTE_2",
            IN_ACCENT0,
            IN_ACCENT2,
            IN_ACCENT1,
            IN_ACCENT5,
            IN_ACCENT3,
            IN_ACCENT4,
            IN_BLEND_0_2,
            IN_BLEND_0_3,
            IN_BLEND_0_5,
            IN_BLEND_2_3,
            IN_BLEND_2_4,
            IN_BLEND_2_5,
            IN_BLEND_0_2_3,
            IN_BLEND_1_3_5,
            IN_BLEND_2_3_4,
            IN_BLEND_3_4_5
    );

    public static InColorPalette getDefault() {
        return IN_CD_UL_2;
    }

    CorporateDesignUniLeipzig() {
    }
}
