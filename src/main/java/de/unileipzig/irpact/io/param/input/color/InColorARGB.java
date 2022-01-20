package de.unileipzig.irpact.io.param.input.color;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.awt.*;
import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_COLOR_ARGB;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_COLOR_ARGB)
public class InColorARGB implements InColor {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 5
    )
    public int priority = 5;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed0255Domain = true,
            intDefault = 255
    )
    public int alpha = 255;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed0255Domain = true,
            intDefault = 0
    )
    public int red = 0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed0255Domain = true,
            intDefault = 0
    )
    public int green = 0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed0255Domain = true,
            intDefault = 0
    )
    public int blue = 0;

    public InColorARGB() {
    }

    public InColorARGB(String name, Color c, int priority) {
        this(name, c.getAlpha(), c.getRed(), c.getGreen(), c.getBlue(), priority);
    }

    public InColorARGB(String name, int a, int r, int g, int b, int priority) {
        setName(name);
        setPriority(priority);
        setAlpha(a);
        setRed(r);
        setGreen(g);
        setBlue(b);
    }

    public InColorARGB copy(String newName, int newPriority) {
        return new InColorARGB(newName, alpha, red, green, blue, newPriority);
    }

    @Override
    public InColorARGB copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InColorARGB newCopy(CopyCache cache) {
        throw new UnsupportedOperationException();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public int getAlpha() {
        return alpha;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getRed() {
        return red;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getGreen() {
        return green;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public Color toColor() {
        return new Color(getRed(), getGreen(), getBlue(), getAlpha());
    }

    @Override
    public Color parse(IRPactInputParser parser) throws ParsingException {
        return toColor();
    }

    @Override
    public String toString() {
        return "InColorARGB{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", alpha=" + alpha +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }
}
