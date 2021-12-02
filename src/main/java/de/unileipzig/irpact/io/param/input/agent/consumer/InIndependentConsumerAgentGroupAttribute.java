package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InIndependentConsumerAgentGroupAttribute extends InConsumerAgentGroupAttribute {

    @TreeAnnotationResource.Init
    static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(LocalizedUiResource res) {
    }

    InConsumerAgentGroup getConsumerAgentGroup(InputParser parser) throws ParsingException;

    String getConsumerAgentGroupName() throws ParsingException;

    InUnivariateDoubleDistribution getDistribution() throws ParsingException;
}
