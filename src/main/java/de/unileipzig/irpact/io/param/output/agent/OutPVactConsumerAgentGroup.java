package de.unileipzig.irpact.io.param.output.agent;

import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Deprecated
@Definition(copy = InPVactConsumerAgentGroup.class)
public class OutPVactConsumerAgentGroup implements OutConsumerAgentGroup {

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
        putClassPath(res, thisClass(), thisName());
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public double placeholder;

    public OutPVactConsumerAgentGroup() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @Override
    public OutPVactConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutPVactConsumerAgentGroup newCopy(CopyCache cache) {
        OutPVactConsumerAgentGroup copy = new OutPVactConsumerAgentGroup();
        copy._name = _name;

        return copy;
    }
}
