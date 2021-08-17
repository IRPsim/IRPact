package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irpact.commons.util.table.SimpleTable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class CsvVarCollectionWriter implements VarCollectionWriter {

    protected Function<? super Object[], ? extends String[]> entry2str;
    protected Path target;
    protected SimpleTable<String> table;
    protected List<String> infos = new ArrayList<>();
    protected String delimiter = ";";
    protected Charset charset = StandardCharsets.UTF_8;
    protected String commentPrefix = "#";

    public CsvVarCollectionWriter() {
    }

    public void setTarget(Path target) {
        this.target = target;
    }

    public Path getTarget() {
        return target;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public void addInfo(String info) {
        infos.add(info);
    }

    public void setInfos(String... infos) {
        this.infos.clear();
        Collections.addAll(this.infos, infos);
    }

    public void setColumns(String... columns) {
        table = new SimpleTable<>();
        table.addColumns(columns);
    }

    public void setEntry2Str(Function<? super Object[], ? extends String[]> entry2str) {
        this.entry2str = entry2str;
    }

    public Function<? super Object[], ? extends String[]> getEntry2Str() {
        return entry2str;
    }

    public void setCommentPrefix(String comment) {
        this.commentPrefix = comment;
    }

    public String getCommentPrefix() {
        return commentPrefix;
    }

    @Override
    public void write(VarCollection vcoll) throws IOException {
        if(entry2str == null) {
            throw new NullPointerException("missing mapping function");
        }

        for(Object[] entry: vcoll.iterable()) {
            String[] attributes = entry2str.apply(entry);
            table.addRow(attributes);
        }

        write();
    }

    protected List<String> getInfosWithPrefix() {
        if(infos == null || infos.isEmpty()) {
            return infos;
        } else {
            return infos.stream()
                    .map(i -> getCommentPrefix() + i)
                    .collect(Collectors.toList());
        }
    }

    public void write() throws IOException {
        if(table == null) {
            throw new IllegalStateException("no table created");
        }

        CsvPrinter<String> printer = new CsvPrinter<>();
        printer.setValuePrinter(CsvPrinter.STRING_IDENTITY);
        printer.setDelimiter(delimiter);

        printer.write(
                target,
                charset,
                getInfosWithPrefix(),
                table.getHeaderAsArray(),
                table.listTable()
        );
    }
}
