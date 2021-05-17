package de.unileipzig.irpact.commons.log;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractLoggingMessageCollection implements LoggingMessage {

    protected Collection<LoggingMessage> parts;

    public AbstractLoggingMessageCollection() {
        this(new ArrayList<>());
    }

    public AbstractLoggingMessageCollection(Collection<LoggingMessage> parts) {
        this.parts = parts;
    }

    public void reset() {
        parts.clear();
    }

    public abstract AbstractLoggingMessageCollection append(LoggingMessage msg);

    public abstract AbstractLoggingMessageCollection append(String msg);

    public abstract AbstractLoggingMessageCollection append(String pattern, Object arg);

    public abstract AbstractLoggingMessageCollection append(String pattern, Object arg1, Object arg2);

    public abstract AbstractLoggingMessageCollection append(String pattern, Object... args);

    public String buildMessage(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for(LoggingMessage message: parts) {
            if(sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(message.format());
        }
        return sb.toString();
    }

    public LazyToString buildLazyMessage(String delimiter) {
        return new LazyToString() {
            @Override
            public String toString() {
                return buildMessage(delimiter);
            }
        };
    }
}
