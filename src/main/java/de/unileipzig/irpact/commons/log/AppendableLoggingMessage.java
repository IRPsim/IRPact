package de.unileipzig.irpact.commons.log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AppendableLoggingMessage {

    private final List<LoggingMessage> parts = new ArrayList<>();

    public AppendableLoggingMessage() {
    }

    public void reset() {
        parts.clear();
    }

    public void append(String msg) {
        parts.add(new LoggingMessage(msg));
    }

    public void append(String pattern, Object arg) {
        parts.add(new LoggingMessage(pattern, arg));
    }

    public void append(String pattern, Object arg1, Object arg2) {
        parts.add(new LoggingMessage(pattern, arg1, arg2));
    }

    public void append(String pattern, Object... args) {
        parts.add(new LoggingMessage(pattern, args));
    }

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
