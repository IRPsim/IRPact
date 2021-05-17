package de.unileipzig.irpact.io.param.output.agent;

import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition(copy = InPVactConsumerAgentGroup.class)
public class OutGeneralConsumerAgentGroup implements OutConsumerAgentGroup {

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
    }

    public String _name;

    @FieldDefinition
    public double placeholder;

    public OutGeneralConsumerAgentGroup() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @Override
    public OutGeneralConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutGeneralConsumerAgentGroup newCopy(CopyCache cache) {
        OutGeneralConsumerAgentGroup copy = new OutGeneralConsumerAgentGroup();
        copy._name = _name;

        return copy;
    }
}
