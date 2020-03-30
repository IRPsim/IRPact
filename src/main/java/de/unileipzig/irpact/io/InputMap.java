package de.unileipzig.irpact.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class InputMap {

    private static final DirectoryStream.Filter<Path> JAVA_FILTER = entry -> {
        if(entry != null) {
            String fileName = entry.toString();
            return fileName.endsWith(Constants.JAVA) && !fileName.endsWith(Constants.ROOT);
        }
        return false;
    };

    private Map<String, Input> inputMap = new HashMap<>();

    public InputMap() {
    }

    public static InputMap parseDir(Path dir) throws IOException {
        InputMap inputMap = new InputMap();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, JAVA_FILTER)) {
            for(Path path: stream) {
                try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    Input input = Input.parse(reader);
                    inputMap.put(input.getName(), input);
                }
            }
        }
        return inputMap;
    }

    public void put(String key, Input input) {
        inputMap.put(key, input);
    }

    public Map<String, Input> getInputMap() {
        return inputMap;
    }

    public Map<String, MappedInput> createMappedInput() {
        Map<String, MappedInput> mappednInputMap = new LinkedHashMap<>();
        for(Input input: inputMap.values()) {
            MappedInput set = new MappedInput();
            set.setPrefix(Constants.PREFIX_SET);
            set.setName(Constants.SET + input.getName());
            set.putParams(input.getParamMap());
            mappednInputMap.put(set.getName(), set);
            for(InputField field: input.getFields()) {
                if(field.isArray()) {
                    MappedInput par = new MappedInput();
                    par.setPrefix(Constants.PREFIX_PARAMETER);
                    par.setName(Constants.PAR + input.getName() + Constants.DELIMITER + field.getType());
                    par.addDep(Constants.SET + input.getName());
                    par.addDep(Constants.SET + field.getType());
                    par.putParams(field.getParamMap());
                    mappednInputMap.put(par.getName(), par);
                } else {
                    MappedInput par = new MappedInput();
                    par.setPrefix(Constants.PREFIX_PARAMETER);
                    par.setName(Constants.PAR + input.getName() + Constants.DELIMITER + field.getName());
                    par.addDep(Constants.SET + input.getName());
                    par.putParams(field.getParamMap());
                    mappednInputMap.put(par.getName(), par);
                }
            }
        }
        return mappednInputMap;
    }
}
