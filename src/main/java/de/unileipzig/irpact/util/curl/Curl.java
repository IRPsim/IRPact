package de.unileipzig.irpact.util.curl;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irptools.util.ProcessResult;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
//https://curl.se/docs/manpage.html
public class Curl {

    public static final String DEFAULT_CURL_COMMAND = "curl";

    protected final List<Option> options = new ArrayList<>();

    protected final String exec;

    protected String quote = "\"";

    public Curl() {
        this(DEFAULT_CURL_COMMAND);
    }

    public Curl(String exec) {
        this.exec = exec;
    }

    public Curl(Path curlPath) {
        this("\"" + curlPath + "\"");
    }

    //=========================
    //general
    //=========================

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void reset() {
        options.clear();
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOption(Option option) {
        options.add(option);
    }

    public void addOptions(Option... options) {
        Collections.addAll(this.options, options);
    }

    //=========================
    //special
    //=========================

    public Curl addOption(String option, String value) {
        addOption(new SimpleOption(option, value));
        return this;
    }

    public Curl addOption(String option) {
        addOption(new SimpleOption(option, null));
        return this;
    }

    public Curl addValue(String value) {
        addOption(new SimpleOption(null, value));
        return this;
    }

    public Curl silent() {
        return addOption("-s");
    }

    public Curl showError() {
        return addOption("-S");
    }

    public Curl hideProgress() {
        return addOption("--no-progress-meter");
    }

    public Curl target(String target) {
        return addValue(target);
    }

    public Curl request(String value) {
        return addOption("-X", value);
    }
    public Curl POST() {
        return request("POST");
    }
    public Curl GET() {
        return request("GET");
    }
    public Curl PUT() {
        return request("PUT");
    }
    public Curl DELETE() {
        return request("DELETE");
    }

    public Curl header(String value) {
        return addOption("-H", value);
    }
    public Curl acceptJson() {
        return header("Accept: application/json");
    }
    public Curl acceptOctetStream() {
        return header("Accept: application/octet-stream");
    }
    public Curl acceptPlainText() {
        return header("Accept: text/plain");
    }
    public Curl acceptPng() {
        return header("Accept: image/png");
    }
    public Curl contentTypeJson() {
        return header("Content-Type: application/json");
    }

    public Curl data(String value) {
        return addOption("-d", value);
    }
    public Curl fileContent(Path pathToData) {
        return data("@" + pathToData);
    }

    public Curl rawData(String value) {
        return addOption("--data-raw", value);
    }
    public Curl rawFileContent(Path pathToData) {
        return rawData("@" + pathToData);
    }
    public Curl rawText(String text) {
        text = StringUtil.escapeNewLine(text, "\\");
        text = StringUtil.escapeQuote(text, "\\");
        return rawData(text);
    }

    public Curl binaryData(String value) {
        return addOption("--data-binary", value);
    }
    public Curl binaryFileContent(Path pathToData) {
        return rawData("@" + pathToData);
    }

    public Curl user(String name) {
        return user(name, null);
    }
    public Curl user(String name, String password) {
        String pw = password == null ? "" : password;
        return addOption("-u", name + ":" + pw);
    }

    public Curl output(Path target) {
        return addOption("-o", target.toString());
    }

    //=========================
    //exec
    //=========================

    protected String quote(String input) {
        return quote + input + quote;
    }

    protected List<String> toList() {
        List<String> cmds = new ArrayList<>(options.size() * 2 + 1);
        cmds.add(exec);
        options.stream()
                .flatMap(option -> {
                    String o = option.getOption();
                    String v = option.getValue();
                    if(o == null && v == null) return Stream.empty();
                    if(o == null) return Stream.of(quote(v));
                    if(v == null) return Stream.of(o);
                    return Stream.of(o, quote(v));
                })
                .forEach(cmds::add);
        return cmds;
    }

    public ProcessBuilder build() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(toList());
        return builder;
    }

    public String printCommand() {
        return toList().toString();
    }

    public ProcessResult execute() throws IOException, InterruptedException {
        ProcessBuilder builder = build();
        Process process = builder.start();
        return ProcessResult.waitFor(process);
    }

    public int execute(Path outPath, Path errPath) throws IOException, InterruptedException {
        ProcessBuilder builder = build();
        builder.redirectOutput(outPath.toFile());
        builder.redirectError(errPath.toFile());
        Process process = builder.start();
        return process.waitFor();
    }
}
