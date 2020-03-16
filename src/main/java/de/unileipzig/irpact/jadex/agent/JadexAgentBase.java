package de.unileipzig.irpact.jadex.agent;

import de.unileipzig.irpact.commons.exception.MissingArgumentException;
import org.slf4j.Logger;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class JadexAgentBase implements JadexAgent {

    public JadexAgentBase() {
    }

    //=========================
    //init
    //=========================

    protected static <T> T get(Map<String, Object> args, String name) throws MissingArgumentException {
        return get(args, name, true);
    }

    @SuppressWarnings("unchecked")
    protected static <T> T get(Map<String, Object> args, String name, boolean throwExceptionIfNull) throws MissingArgumentException {
        Object arg = args.get(name);
        if(throwExceptionIfNull && arg == null) {
            throw new MissingArgumentException(name);
        }
        return (T) arg;
    }

    protected abstract void initArgs(Map<String, Object> args);

    //=========================
    //lifecycle
    //=========================

    protected abstract void onInit();

    protected abstract void onStart();

    protected abstract void onEnd();
}
