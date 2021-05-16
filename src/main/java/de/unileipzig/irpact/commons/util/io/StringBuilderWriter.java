package de.unileipzig.irpact.commons.util.io;

import java.io.Writer;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("NullableProblems")
public class StringBuilderWriter extends Writer {

    protected StringBuilder builder;

    public StringBuilderWriter() {
        this(new StringBuilder());
    }

    public StringBuilderWriter(StringBuilder builder) {
        this.builder = builder;
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public void reset() {
        builder.setLength(0);;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    @Override
    public void write(int c) {
        append((char) c);
    }

    @Override
    public void write(char[] cbuf) {
        builder.append(cbuf);
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        builder.append(cbuf, off, len);
    }

    @Override
    public void write(String str) {
        builder.append(str);
    }

    @Override
    public void write(String str, int off, int len) {
        builder.append(str, off, off + len);
    }

    @Override
    public StringBuilderWriter append(char c) {
        builder.append(c);
        return this;
    }

    @Override
    public StringBuilderWriter append(CharSequence csq) {
        builder.append(csq);
        return this;
    }

    @Override
    public StringBuilderWriter append(CharSequence csq, int start, int end) {
        builder.append(csq, start, end);
        return this;
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }
}
