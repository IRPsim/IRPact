package de.unileipzig.irpact.io.param.input.color;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InColorPalette implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }


    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.SETT_COLOR_PALETTE);
        addEntry(res, thisClass(), "colors");
    }

    public String _name;

    @FieldDefinition
    public InColor[] colors = new InColor[0];

    public InColorPalette() {
    }

    public InColorPalette(String name) {
        setName(name);
    }

    public InColorPalette(String name, InColor... colors) {
        setName(name);
        setColors(colors);
    }

    @Override
    public InColorPalette copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InColorPalette newCopy(CopyCache cache) {
        throw new UnsupportedOperationException();
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void addColor(InColor color) {
        colors = add(colors, color);
    }

    public void addAllColors(InColor... colors) {
        this.colors = addAll(this.colors, colors);
    }

    public void setColors(InColor... colors) {
        this.colors = colors;
    }

    public InColor[] getColors() throws ParsingException {
        return getNonEmptyArray(colors, "colors");
    }

    public ColorPalette toPalette() throws ParsingException {
        List<InColor> colorList = new ArrayList<>();
        Collections.addAll(colorList, getColors());
        colorList.sort(InColor.COMPARE_PRIORITY);

        ColorPalette palette = new ColorPalette();
        for(InColor color: colorList) {
            palette.add(color.toColor());
        }
        return palette;
    }

    @Override
    public ColorPalette parse(IRPactInputParser parser) throws ParsingException {
        return toPalette();
    }
}
