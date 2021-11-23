package de.unileipzig.irpact.io.param.input.color;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.awt.*;
import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InColorARGB implements InColor {

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
        putClassPath(res, thisClass(), InRootUI.SETT_COLOR_ARGB);
        addEntryWithDefault(res, thisClass(), "priority", asValue(5));
        addEntryWithDefaultAndDomain(res, thisClass(), "alpha", asValue(255), DOMAIN_CLOSED_0_255);
        addEntryWithDefaultAndDomain(res, thisClass(), "red", VALUE_0, DOMAIN_CLOSED_0_255);
        addEntryWithDefaultAndDomain(res, thisClass(), "green", VALUE_0, DOMAIN_CLOSED_0_255);
        addEntryWithDefaultAndDomain(res, thisClass(), "blue", VALUE_0, DOMAIN_CLOSED_0_255);
    }

    public String _name;

    @FieldDefinition
    public int priority = 0;

    @FieldDefinition
    public int alpha = 255;

    @FieldDefinition
    public int red = 0;

    @FieldDefinition
    public int green = 0;

    @FieldDefinition
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

    @Override
    public InColorARGB copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InColorARGB newCopy(CopyCache cache) {
        throw new UnsupportedOperationException();
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
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
}
