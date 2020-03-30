package de.unileipzig.irpact.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class Input {

    private Map<String, String> paramMap = new LinkedHashMap<>();
    private String type;
    private String name;
    private List<String> interfaceList = new ArrayList<>();
    private List<InputField> fields = new ArrayList<>();

    public Input() {
    }

    public void putParam(String key, String value) {
        paramMap.put(key, value);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addInterfaces(Collection<? extends String> coll) {
        interfaceList.addAll(coll);
    }

    public List<String> getInterfaceList() {
        return interfaceList;
    }

    public void addFields(Collection<? extends InputField> coll) {
        fields.addAll(coll);
    }

    public void addField(InputField field) {
        fields.add(field);
    }

    public List<InputField> getFields() {
        return fields;
    }

    //=========================
    //Parsing
    //=========================

    private static final String PUBLIC = "public";

    private static String getName(String line) {
        int index = line.indexOf(' ');
        int index2 = line.indexOf(' ', index + 1);
        return line.substring(index + 1, index2);
    }

    private static String[] getInterface(String line) {
        int index = line.indexOf(Constants.IMPLEMENTS) + Constants.IMPLEMENTS.length() + 1;
        int lineLen = line.length();
        String interfaceString = line.substring(index, lineLen - 2);
        String[] interfaces = interfaceString.split(",");
        for(int i = 0; i < interfaces.length; i++) {
            interfaces[i] = interfaces[i].trim();
        }
        return interfaces;
    }

    private static void parseField(BufferedReader reader, String line, Input input) throws IOException {
        InputField inputField = new InputField();
        while(!line.isEmpty() && !"}".equals(line)) {
            if(line.startsWith(Constants.COMMENT)) {
                continue;
            }
            if(line.startsWith(Constants.PARAM)) {
                int index = line.indexOf(':');
                String key = line.substring(3, index).trim();
                String value = index == line.length() - 1
                        ? ""
                        : line.substring(index + 2).trim();
                inputField.putParam(key, value);
            } else {
                int index = line.lastIndexOf(' ');
                String type = line.substring(0, index).trim();
                String name = line.substring(index + 1, line.length() - 1).trim();
                if(type.endsWith("[]")) {
                    inputField.setArray(true);
                    inputField.setType(type.substring(0, type.length() - 2));
                } else {
                    inputField.setArray(false);
                    inputField.setType(type);
                }
                inputField.setName(name);
            }
            line = reader.readLine();
            line = line.trim();
            if(line.startsWith(PUBLIC)) {
                line = line.substring(PUBLIC.length()).trim();
            }
        }
        if(!inputField.getName().endsWith(Constants.NAME)) {
            input.addField(inputField);
        }
    }

    private static void parseClass(BufferedReader reader, String line, Input input) throws IOException {
        String type = null;
        String name = null;
        List<String> interfaces = new ArrayList<>();
        while(!line.isEmpty()) {
            if(line.startsWith(Constants.COMMENT)) {
                continue;
            }
            if(line.startsWith(Constants.PARAM)) {
                int index = line.indexOf(':');
                String key = line.substring(3, index).trim();
                String value = index == line.length() - 1
                        ? ""
                        : line.substring(index + 2).trim();
                input.putParam(key, value);
            } else {
                if(line.startsWith(Constants.CLASS)) {
                    type = Constants.CLASS;
                    name = getName(line);
                }
                if(line.startsWith(Constants.INTERFACE)) {
                    type = Constants.INTERFACE;
                    name = getName(line);
                }
                if(line.contains(Constants.IMPLEMENTS)) {
                    String[] interfaceNamesArray = getInterface(line);
                    Collections.addAll(interfaces, interfaceNamesArray);
                }
                break;
            }
            line = reader.readLine();
            line = line.trim();
            if(line.startsWith(PUBLIC)) {
                line = line.substring(PUBLIC.length()).trim();
            }
        }
        input.setType(type);
        input.setName(name);
        input.addInterfaces(interfaces);
    }

    public static Input parse(BufferedReader reader) throws IOException {
        boolean classParsed = false;
        Input input = new Input();
        String line;
        while((line = reader.readLine()) != null) {
            line = line.trim();
            if(line.startsWith(Constants.COMMENT)) {
                continue;
            }
            if(line.startsWith(Constants.END_OF_DEFINITION)) {
                break;
            }
            if(line.startsWith(PUBLIC)) {
                line = line.substring(PUBLIC.length()).trim();
            }
            if(line.startsWith(Constants.PARAM)) {
                if(classParsed) {
                    parseField(reader, line, input);
                } else {
                    parseClass(reader, line, input);
                    classParsed = true;
                }
            }
        }
        return input;
    }
}
