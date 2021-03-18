package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Todo
@AddToRoot
@Definition
public interface InConsumerAgentGroupAttribute extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getAttributeName() throws ParsingException;

    InConsumerAgentGroup getConsumerAgentGroup(InputParser parser) throws ParsingException;

    String getConsumerAgentGroupName() throws ParsingException;

    InUnivariateDoubleDistribution getDistribution() throws ParsingException;
}
