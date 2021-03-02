package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface InputParser {

    void cache(InEntity key, Object value);

    boolean isCached(InEntity key);

    Object getCached(InEntity key);

    ResourceLoader getResourceLoader();

    SimulationEnvironment getEnvironment();

    Rnd deriveRnd();

    InRoot getRoot();

    //mal aendern, dass der parser den typ vorgibt
    <T> T parseRoot(InRoot root) throws ParsingException;

    Object parseEntity(InEntity input) throws ParsingException;

    @SuppressWarnings("unchecked")
    default <R> R parseEntityTo(InEntity input) throws ParsingException {
        return (R) parseEntity(input);
    }

    void dispose();
}
