package de.unileipzig.irpact.core.start;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;

/**
 * @author Daniel Abitz
 */
public interface IRPactInputParser extends InputParser {

    //=========================
    //util
    //=========================

    int getSimulationYear();

    ResourceLoader getResourceLoader();

    MainCommandLineOptions getOptions();

    Rnd deriveRnd();

    SimulationEnvironment getEnvironment();

    boolean isRestored();

    InRoot getRoot();

    void initLoggingOnly(InRoot root) throws ParsingException;

    //=========================
    //parse
    //=========================

    @Override
    SimulationEnvironment parseRoot(InRoot root) throws ParsingException;

    @Override
    default Object parseEntity(InIRPactEntity input, Object param) throws ParsingException {
        Object parsed = input.parse(this, param);
        if(parsed == null) {
            throw new ParsingException("parse result is null for entity '" + input.getName() + "' (" + input.getClass().getName() + ")");
        }
        return parsed;
    }

    @Override
    default void update(InIRPactEntity input, Object original, Object param) throws ParsingException {
        input.update(this, original, param);
    }
}
