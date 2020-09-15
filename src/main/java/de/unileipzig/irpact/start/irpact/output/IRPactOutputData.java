package de.unileipzig.irpact.start.irpact.output;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irptools.defstructure.GamsType;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.annotation.Definition;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class IRPactOutputData {

    public static final List<ParserInput> INPUT_LIST = CollectionUtil.arrayListOf(
            ParserInput.newInstance(GamsType.OUTPUT, AgentGroup.class)
    );
}
