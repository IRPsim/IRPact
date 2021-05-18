package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;

/**
 * @author Daniel Abitz
 */
public interface InputParser {

    void cache(InIRPactEntity key, Object value);

    boolean isCached(InIRPactEntity key);

    Object getCached(InIRPactEntity key);

    //mal aendern, dass der parser den typ vorgibt
    Object parseRoot(InRoot root) throws ParsingException;

    Object parseEntity(InIRPactEntity input) throws ParsingException;

    @SuppressWarnings("unchecked")
    default <R> R parseEntityTo(InIRPactEntity input) throws ParsingException {
        return (R) parseEntity(input);
    }

    default <R> R parseEntityTo(InIRPactEntity input, Class<R> outClass) throws ParsingException {
        Object obj = parseEntity(input);
        if(outClass.isInstance(obj)) {
            return outClass.cast(obj);
        } else {
            throw new ParsingException("class mismatch: " + ParamUtil.printClass(obj) + " != " + ParamUtil.printClass(outClass));
        }
    }

    void dispose();
}
