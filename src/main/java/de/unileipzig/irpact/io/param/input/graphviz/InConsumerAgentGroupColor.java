package de.unileipzig.irpact.io.param.input.graphviz;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConsumerAgentGroupColor {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Graphviz"),
                res.getCachedElement("Agentengruppe-Farben-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Konsumergruppe")
                .setGamsDescription("Legt die Gruppe fest")
                .store(thisClass(), "group");

        res.newEntryBuilder()
                .setGamsIdentifier("Farbe")
                .setGamsDescription("Legt die Farbe fest")
                .store(thisClass(), "color");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] group;

    @FieldDefinition
    public GraphvizColor[] color;

    public InConsumerAgentGroupColor() {
    }

    public InConsumerAgentGroupColor(String name, InConsumerAgentGroup group, GraphvizColor color) {
        this._name = name;
        this.group = new InConsumerAgentGroup[]{group};
        this.color = new GraphvizColor[]{color};
    }

    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup getGroup() throws ParsingException {
        return InUtil.getInstance(group, "Group");
    }

    public GraphvizColor getColor() throws ParsingException {
        return InUtil.getInstance(color, "Color");
    }
}
