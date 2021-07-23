package de.unileipzig.irpact.core.start;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.InRoot;

/**
 * @author Daniel Abitz
 */
public interface InputParser {

    //=========================
    //caching
    //=========================

    void cache(InIRPactEntity key, Object value);

    boolean isCached(InIRPactEntity key);

    Object getCached(InIRPactEntity key);

    //=========================
    //parse
    //=========================

    //mal aendern, dass der parser den typ vorgibt
    Object parseRoot(InRoot root) throws ParsingException;

    Object parseEntity(InIRPactEntity input) throws ParsingException;

    Object parseEntity(InIRPactEntity input, Object param) throws ParsingException;

    @SuppressWarnings("unchecked")
    default <R> R parseEntityTo(InIRPactEntity input) throws ParsingException {
        return (R) parseEntity(input);
    }

    @SuppressWarnings("unchecked")
    default <R> R parseEntityTo(InIRPactEntity input, Object param) throws ParsingException {
        return (R) parseEntity(input, param);
    }

    void update(InIRPactEntity input, Object original, Object param) throws ParsingException;

    default <R> R parseEntityTo(InIRPactEntity input, Class<R> outClass) throws ParsingException {
        Object obj = parseEntity(input);
        if(outClass.isInstance(obj)) {
            return outClass.cast(obj);
        } else {
            throw new ParsingException("class mismatch: " + ParamUtil.printClass(obj) + " != " + ParamUtil.printClass(outClass));
        }
    }

    //=========================
    //cleanup
    //=========================

    void dispose();
}
