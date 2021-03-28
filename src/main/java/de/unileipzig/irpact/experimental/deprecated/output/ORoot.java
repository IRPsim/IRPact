package de.unileipzig.irpact.experimental.deprecated.output;

import de.unileipzig.irpact.experimental.deprecated.output.agent.consumer.OConsumerAgentGroup;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class ORoot implements RootClass {

    public static final List<ParserInput> CLASSES = CollectionUtil.arrayListOf(
            ParserInput.newInstance(Type.OUTPUT, OConsumerAgentGroup.class),
            ParserInput.newInstance(Type.OUTPUT, ORoot.class)
    );

    @FieldDefinition
    public OConsumerAgentGroup[] outGrps;

    public ORoot() {
    }

    public void set(List<OConsumerAgentGroup> grpList) {
        outGrps = grpList.toArray(new OConsumerAgentGroup[0]);
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }
}
