package de.unileipzig.irpact.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Verwaltet eine Menge von Definitionsklassen und deren Zusammengehoerigkeiten.
 *
 * @author Daniel Abitz
 */
public class InputMapping {

    private static final DirectoryStream.Filter<Path> JAVA_FILTER = entry -> {
        if(entry != null) {
            String fileName = entry.toString();
            return fileName.endsWith(Constants.JAVA) && !fileName.endsWith(Constants.ROOT_JAVA);
        }
        return false;
    };

    private Map<String, Input> inputMap;

    public InputMapping() {
        this(new LinkedHashMap<>());
    }

    public InputMapping(Map<String, Input> inputMap) {
        this.inputMap = inputMap;
    }

    public static InputMapping parseDir(Path dir) throws IOException {
        InputMapping inputMap = new InputMapping();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, JAVA_FILTER)) {
            for(Path path: stream) {
                try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    Input input = InputParser.parse(reader);
                    inputMap.put(input);
                }
            }
        }
        return inputMap;
    }

    public void parseAndPut(Path path, Charset charset) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
            parseAndPut(reader);
        }
    }

    public void parseAndPut(BufferedReader reader) throws IOException {
        Input input = InputParser.parse(reader);
        put(input);
    }

    public void put(Input input) {
        inputMap.put(input.getName(), input);
    }

    public Map<String, Input> getInputMap() {
        return inputMap;
    }

    public InputMapping.Result getResult() {
        Result result = new Result();
        for(Input input: inputMap.values()) {
            List<MappedInput> list = input.toMappedInput();
            result.putAll(list);
        }
        return result;
    }

    public static class Result implements Iterable<MappedInput> {

        private Map<String, MappedInput> mapping;

        public Result() {
            this(new LinkedHashMap<>());
        }

        public Result(Map<String, MappedInput> mapping) {
            this.mapping = mapping;
        }

        private void put(MappedInput mappedInput) {
            mapping.put(mappedInput.getName(), mappedInput);
        }

        private void putAll(Collection<? extends MappedInput> coll) {
            for(MappedInput mi: coll) {
                put(mi);
            }
        }

        public MappedInput get(String name) {
            return mapping.get(name);
        }

        public int size() {
            return mapping.size();
        }

        public Collection<MappedInput> getMapping() {
            return mapping.values();
        }

        @Override
        public Iterator<MappedInput> iterator() {
            return getMapping().iterator();
        }

        public String printGAMS() {
            StringBuilder sb = new StringBuilder();
            Iterator<MappedInput> iter = iterator();
            while(iter.hasNext()) {
                MappedInput next = iter.next();
                sb.append(next.printGAMS());
                if(iter.hasNext()) {
                    sb.append("\n\n");
                }
            }
            return sb.toString();
        }
    }
}
