package de.unileipzig.irpact.OLD.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class InputParser {

    public enum Token {
        COMMENT,
        PARAM,
        CLASS,
        INTERFACE,
        EMPTY,
        UNKNOWN,
        EOD
    }

    private BufferedReader reader;
    private String line;
    private String trimmedLine;
    private Token last;
    private Token token;

    public InputParser(BufferedReader reader) {
        this.reader = Objects.requireNonNull(reader, "reader");
    }

    private static boolean isPublic(String input) {
        return input.startsWith(Constants.PUBLIC);
    }

    private static boolean isParam(String input) {
        return input.startsWith(Constants.PARAM);
    }

    private static boolean isComment(String input) {
        return input.startsWith(Constants.COMMENT) && !input.startsWith(Constants.PARAM);
    }

    private static boolean isClass(String input) {
        return input.startsWith(Constants.CLASS_WITH_SPACE);
    }

    private static boolean isInterface(String input) {
        return input.startsWith(Constants.INTERFACE_WITH_SPACE);
    }

    public Token nextToken() throws IOException {
        last = token;

        line = reader.readLine();
        trimmedLine = line == null
                ? null
                : line.trim();

        if(line == null || trimmedLine.startsWith(Constants.END_OF_DEFINITION)) {
            token = Token.EOD;
            trimmedLine = null;
            return token;
        }
        if(isPublic(trimmedLine)) {
            trimmedLine = trimmedLine.substring(Constants.PUBLIC.length()).trim();
        }

        if(isComment(trimmedLine)) {
            token = Token.COMMENT;
        }
        else if(isParam(trimmedLine)) {
            token = Token.PARAM;
        }
        else if(isClass(trimmedLine)) {
            token = Token.CLASS;
        }
        else if(isInterface(trimmedLine)) {
            token = Token.INTERFACE;
        }
        else if(trimmedLine.isEmpty()) {
            token = Token.EMPTY;
        }
        else {
            token = Token.UNKNOWN;
        }

        return token;
    }

    private static boolean isFiller(Token token, Token last, boolean considerParam) {
        if(considerParam && last == Token.PARAM) {
            return false;
        }
        switch (token) {
            case EMPTY:
            case UNKNOWN:
            case COMMENT:
                return true;

            default:
                return false;
        }
    }

    public Token nextValidToken(boolean considerParam) throws IOException {
        do {
            nextToken();
        } while(isFiller(token, last, considerParam));
        return token;
    }

    public Token getToken() {
        return token;
    }

    public String getLine() {
        return line;
    }

    public String getTrimmedLine() {
        return trimmedLine;
    }

    public boolean isParam() {
        return token == Token.PARAM;
    }

    public boolean isUnknown() {
        return token == Token.UNKNOWN;
    }

    public boolean isDefinition() {
        return token == Token.INTERFACE || token == Token.CLASS;
    }

    private void parseParam(Map<String, String> map) {
        String text = getTrimmedLine();
        int index = text.indexOf(':');
        if(index != -1) {
            String key = text.substring(3, index).trim();
            if(key.isEmpty()) {
                throw new IllegalStateException("empty key");
            }
            String value = index == text.length() - 1
                    ? "" //kein value vorhanden
                    : text.substring(index + 2).trim(); //value vorhanden
            map.put(key, value);
        }
    }

    //static vars einbauen? diese waeren ebenfalls global... aber nervig zu implementieren
    private static boolean isGlobal(String name) {
        return Constants.GLOBAL.equals(name);
    }

    private void parseDefinition(Input input) {
        String text = getTrimmedLine();
        Token token = getToken();
        int index1 = text.indexOf(' ');
        int index2 = text.indexOf(' ' , index1 + 1);
        String name = text.substring(index1 + 1, index2).trim();
        input.setName(name);
        input.setGlobal(isGlobal(name));
        input.setType(
                token == Token.CLASS
                        ? Constants.CLASS
                        : Constants.INTERFACE
        );
        if(text.contains(Constants.IMPLEMENTS)) {
            int index = text.indexOf(Constants.IMPLEMENTS) + Constants.IMPLEMENTS.length();
            int lineLen = text.length();
            String interfaceString = text.substring(index, lineLen).trim();
            if(interfaceString.endsWith("{")) {
                interfaceString = interfaceString.substring(0, interfaceString.length() - 1).trim();
            }
            String[] interfaces = interfaceString.split(",");
            for(int i = 0; i < interfaces.length; i++) {
                interfaces[i] = interfaces[i].trim();
            }
            input.addInterfaces(interfaces);
        }
    }

    private void parseField(InputField field) {
        if(getToken() != Token.UNKNOWN) {
            throw new IllegalStateException("not unknown");
        }
        String text = getTrimmedLine();
        int index = text.lastIndexOf(' ');
        String type = text.substring(0, index).trim();
        String name = text.substring(index + 1, text.length() - 1).trim();
        if(type.endsWith("[]")) {
            field.setArray(true);
            field.setType(type.substring(0, type.length() - 2));
        } else {
            field.setArray(false);
            field.setType(type);
        }
        field.setName(name);
    }

    public void parse(Input input) throws IOException {
        boolean definitionParsed = false;
        InputField field = null;
        while(nextValidToken(true) != Token.EOD) {
            if(isParam()) {
                if(definitionParsed) {
                    parseParam(field.getParamMap());
                } else {
                    parseParam(input.getParamMap());
                }
            }
            else if(isDefinition()) {
                parseDefinition(input);
                definitionParsed = true;
                field = new InputField();
            }
            else if(isUnknown()) {
                if(!definitionParsed) {
                    throw new IllegalStateException("missing definition");
                }
                parseField(field);
                field.setGlobal(input.isGlobal());
                if(field.isNotName()) {
                    input.addField(field);
                }
                field = new InputField();
            }
            else {
                throw new IllegalStateException(getToken().name());
            }
        }
    }

    public Input parse() throws IOException {
        Input input = new Input();
        parse(input);
        return input;
    }

    public static Input parse(Path path, Charset charset) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
            return parse(reader);
        }
    }

    public static Input parse(BufferedReader reader) throws IOException {
        InputParser parser = new InputParser(reader);
        return parser.parse();
    }
}
