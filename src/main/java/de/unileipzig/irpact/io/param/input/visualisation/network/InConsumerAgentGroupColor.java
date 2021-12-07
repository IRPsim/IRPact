package de.unileipzig.irpact.io.param.input.visualisation.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISUNETWORK_AGENTCOLOR;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_VISUNETWORK_AGENTCOLOR)
public class InConsumerAgentGroupColor implements InIRPactEntity {

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

    public static final InConsumerAgentGroupColor BLACK = new InConsumerAgentGroupColor("BLACK", Color.BLACK);
    public static final InConsumerAgentGroupColor BLUE = new InConsumerAgentGroupColor("BLUE", Color.BLUE);
    public static final InConsumerAgentGroupColor CYAN = new InConsumerAgentGroupColor("CYAN", Color.CYAN);
    public static final InConsumerAgentGroupColor DARK_GRAY = new InConsumerAgentGroupColor("DARK_GRAY", Color.DARK_GRAY);
    public static final InConsumerAgentGroupColor GRAY = new InConsumerAgentGroupColor("GRAY", Color.GRAY);
    public static final InConsumerAgentGroupColor GREEN = new InConsumerAgentGroupColor("GREEN", Color.GREEN);
    public static final InConsumerAgentGroupColor LIGHT_GRAY = new InConsumerAgentGroupColor("LIGHT_GRAY", Color.LIGHT_GRAY);
    public static final InConsumerAgentGroupColor MAGENTA = new InConsumerAgentGroupColor("MAGENTA", Color.MAGENTA);
    public static final InConsumerAgentGroupColor ORANGE = new InConsumerAgentGroupColor("ORANGE", Color.ORANGE);
    public static final InConsumerAgentGroupColor PINK = new InConsumerAgentGroupColor("PINK", Color.PINK);
    public static final InConsumerAgentGroupColor RED = new InConsumerAgentGroupColor("RED", Color.RED);
    public static final InConsumerAgentGroupColor YELLOW = new InConsumerAgentGroupColor("YELLOW", Color.YELLOW);
    public static final InConsumerAgentGroupColor WHITE = new InConsumerAgentGroupColor("WHITE", Color.WHITE);

    public static final InConsumerAgentGroupColor[] ALL = {BLACK, BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, YELLOW, WHITE};
    public static final InConsumerAgentGroupColor[] COLORS = {BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED, YELLOW};
    public static InConsumerAgentGroupColor[] copyColors() {
        return Arrays.stream(COLORS)
                .map(InConsumerAgentGroupColor::derive)
                .toArray(InConsumerAgentGroupColor[]::new);
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public long rgba = Integer.toUnsignedLong(Color.BLACK.getRGB());

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InConsumerAgentGroup[] groups = new InConsumerAgentGroup[0];

    public InConsumerAgentGroupColor() {
    }

    public InConsumerAgentGroupColor(String name, Color color, InConsumerAgentGroup... groups) {
        this(name, Integer.toUnsignedLong(color.getRGB()), groups);
    }

    public InConsumerAgentGroupColor(String name, long rgba, InConsumerAgentGroup... groups) {
        this.name = name;
        this.groups = groups;
        this.rgba = rgba;
    }

    @Override
    public InConsumerAgentGroupColor copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InConsumerAgentGroupColor newCopy(CopyCache cache) {
        InConsumerAgentGroupColor copy = new InConsumerAgentGroupColor();
        copy.name = name;
        copy.rgba = rgba;
        copy.groups = cache.copyArray(groups);
        return copy;
    }

    public InConsumerAgentGroupColor derive() {
        return derive(getName());
    }

    public InConsumerAgentGroupColor derive(InConsumerAgentGroup... cags) {
        return derive(getName(), cags);
    }

    public InConsumerAgentGroupColor derive(String newName, InConsumerAgentGroup... cags) {
        InConsumerAgentGroupColor copy = new InConsumerAgentGroupColor();
        copy.setName(newName);
        copy.setRGBA(getRGBA());
        if(cags != null && cags.length > 0) {
            copy.setGroups(cags);
        }
        return copy;
    }

    public static void roundRobin(InConsumerAgentGroupColor[] colors, InConsumerAgentGroup[] cags) {
        for(int i = 0; i < cags.length; i++) {
            InConsumerAgentGroup cag = cags[i];
            InConsumerAgentGroupColor color = colors[i % colors.length];
            color.addGroup(cag);
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setGroups(InConsumerAgentGroup... groups) {
        this.groups = groups;
    }
    public InConsumerAgentGroup[] getGroups() throws ParsingException {
        return getNonNullArray(groups, "groups");
    }
    public void addGroup(InConsumerAgentGroup cag) {
        addGroups(cag);
    }
    public void addGroups(InConsumerAgentGroup... cags) {
        this.groups = addAll(groups, cags);
    }
    public boolean hasConsumerAgentGroups() {
        return isNotNullAndNotEmpty(groups);
    }
    public boolean hasConsumerAgentGroup(InConsumerAgentGroup cag) {
        if(hasConsumerAgentGroups()) {
            for(InConsumerAgentGroup arrCag: groups) {
                if(arrCag == cag) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setRGBA(long rgba) {
        this.rgba = rgba;
    }
    public void setColor(Color color) {
        setRGBA(Integer.toUnsignedLong(color.getRGB()));
    }
    public long getRGBA() {
        return rgba;
    }
    public int getIntRGBA() {
        return (int) getRGBA();
    }
    public Color toColor() {
        return new Color(getIntRGBA(), true);
    }
    public guru.nidi.graphviz.attribute.Color toGraphvizColor() {
        return guru.nidi.graphviz.attribute.Color.rgba(getIntRGBA());
    }
}
