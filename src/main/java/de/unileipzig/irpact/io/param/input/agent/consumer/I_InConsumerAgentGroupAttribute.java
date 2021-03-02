package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public interface I_InConsumerAgentGroupAttribute extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getAttributeName() throws ParsingException;

    String getConsumerAgentGroupName() throws ParsingException;
}
