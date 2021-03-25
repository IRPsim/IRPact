package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InAffinityEntry extends InEntity {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    InConsumerAgentGroup getSrcCag(InputParser parser) throws ParsingException;

    InConsumerAgentGroup getTarCag(InputParser parser) throws ParsingException;

    String getSrcCagName() throws ParsingException;

    String getTarCagName() throws ParsingException;

    double getAffinityValue();
}
