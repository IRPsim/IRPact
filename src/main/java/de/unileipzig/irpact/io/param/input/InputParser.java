package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.res.ResourceLoader;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.param.ParamUtil;

/**
 * @author Daniel Abitz
 */
public interface InputParser {

    void cache(InEntity key, Object value);

    boolean isCached(InEntity key);

    Object getCached(InEntity key);

    int getSimulationYear();

    ResourceLoader getResourceLoader();

    SimulationEnvironment getEnvironment();

    Rnd deriveRnd();

    InRoot getRoot();

    //mal aendern, dass der parser den typ vorgibt
    <T> T parseRoot(InRoot root) throws ParsingException;

    <T> void parseRootAndUpdate(InRoot root, T instance) throws ParsingException;

    Object parseEntity(InEntity input) throws ParsingException;

    @SuppressWarnings("unchecked")
    default <R> R parseEntityTo(InEntity input) throws ParsingException {
        return (R) parseEntity(input);
    }

    default <R> R parseEntityTo(InEntity input, Class<R> outClass) throws ParsingException {
        Object obj = parseEntity(input);
        if(outClass.isInstance(obj)) {
            return outClass.cast(obj);
        } else {
            throw new ParsingException("class mismatch: " + ParamUtil.printClass(obj) + " != " + ParamUtil.printClass(outClass));
        }
    }

    void dispose();
}
