package de.unileipzig.irpact.io.param.input.color;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_COLOR_PALETTE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_COLOR_PALETTE)
public class InColorPalette implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
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
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
