package de.unileipzig.irpact.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class SetToTree {

    private static String[] split(String input, boolean skipPrefix) {
        String[] parts = input.split(Constants.DELIMITER);
        return skipPrefix
                ? Arrays.copyOfRange(parts, 1, parts.length)
                : parts;
    }

    private static String splitGetLast(String input, boolean skipPrefix) {
        String[] parts = split(input, skipPrefix);
        return parts[parts.length - 1];
    }

    private static String splitGetFirst(String input, boolean skipPrefix) {
        String[] parts = split(input, skipPrefix);
        return parts[0];
    }

    private static ArrayNode getOrCreateArray(ObjectNode root, String name) {
        if(root.has(name)) {
            return (ArrayNode) root.get(name);
        } else {
            return root.putArray(name);
        }
    }

    private static ObjectNode getOrCreateObject(ObjectNode root, String name) {
        if(root.has(name)) {
            return (ObjectNode) root.get(name);
        } else {
            return root.putObject(name);
        }
    }

    //==================================================
    //...
    //==================================================

    public static void handle(JsonNodeCreator creator, ObjectNode setRoot, ObjectNode treeRoot) {
        JsonNode setsRoot = setRoot.get(Constants.SETS);
        Cache cache = new Cache(creator, treeRoot);
        //System.out.println(setsRoot);
        cache.flatSets((ObjectNode) setsRoot);
        //cache.setEntries.forEach(System.out::println);
        cache.buildRawSets();
        //cache.rawSets.entrySet().forEach(System.out::println);
        JsonNode tablesRoot = setRoot.get(Constants.TABLES);
        cache.flatTables((ObjectNode) tablesRoot);
        //cache.tableEntries.forEach(System.out::println);
        cache.combine();
        //cache.rawSets.entrySet().forEach(System.out::println);
        cache.finish();
    }

    private static class Cache {
        private JsonNodeCreator creator;
        private ObjectNode treeRoot;
        private Map<String, ArrayNode> rawSets = new HashMap<>();
        private List<SetEntry> setEntries = new ArrayList<>();
        private List<TableEntry> tableEntries = new ArrayList<>();

        private Cache(JsonNodeCreator creator, ObjectNode treeRoot) {
            this.creator = creator;
            this.treeRoot = treeRoot;
        }

        private void flatSets(ObjectNode setsNode) {
            Iterator<Map.Entry<String, JsonNode>> setsIter = setsNode.fields();
            while(setsIter.hasNext()) {
                Map.Entry<String, JsonNode> setsNext = setsIter.next();
                String type = setsNext.getKey();
                ObjectNode namesNode = (ObjectNode) setsNext.getValue();
                Iterator<Map.Entry<String, JsonNode>> namesIter = namesNode.fields();
                while(namesIter.hasNext()) {
                    Map.Entry<String, JsonNode> namesNext = namesIter.next();
                    String name = namesNext.getKey();
                    ObjectNode dataNode = (ObjectNode) namesNext.getValue();
                    if(dataNode.isEmpty()) {
                        SetEntry entry = new SetEntry();
                        entry.type = type;
                        entry.name = name;
                        entry.trimedType = splitGetFirst(type, true);
                        entry.key = null;
                        entry.trimedKey = null;
                        entry.value = NullNode.getInstance();
                        setEntries.add(entry);
                    } else {
                        Iterator<Map.Entry<String, JsonNode>> dataIter = dataNode.fields();
                        while(dataIter.hasNext()) {
                            Map.Entry<String, JsonNode> dataNext = dataIter.next();
                            String key = dataNext.getKey();
                            JsonNode value = dataNext.getValue();
                            SetEntry entry = new SetEntry();
                            entry.type = type;
                            entry.name = name;
                            entry.trimedType = splitGetFirst(type, true);
                            entry.key = key;
                            entry.trimedKey = splitGetLast(key, true);
                            entry.value = value;
                            setEntries.add(entry);
                        }
                    }
                }
            }
        }

        private ObjectNode getSet(String type, String name) {
            ArrayNode typeNode = rawSets.computeIfAbsent(type, _type -> creator.arrayNode());
            for(int i = 0; i < typeNode.size(); i++) {
                ObjectNode child = (ObjectNode) typeNode.get(i);
                JsonNode nameNode = child.get(Constants.NAME);
                if(nameNode.isTextual() && name.equals(nameNode.textValue())) {
                    return child;
                }
            }
            ObjectNode newChild = typeNode.addObject();
            newChild.put(Constants.NAME, name);
            return newChild;
        }

        private void buildRawSets() {
            for(SetEntry entry: setEntries) {
                ObjectNode node = getSet(entry.trimedType, entry.name);
                if(entry.trimedKey != null) {
                    node.set(entry.trimedKey, entry.value);
                }
            }
        }

        private void flatTables(ObjectNode tablesNode) {
            Iterator<Map.Entry<String, JsonNode>> tablesIter = tablesNode.fields();
            while(tablesIter.hasNext()) {
                Map.Entry<String, JsonNode> tablesNext = tablesIter.next();
                String name = tablesNext.getKey();
                ObjectNode set0Node = (ObjectNode) tablesNext.getValue();
                Iterator<Map.Entry<String, JsonNode>> set0Iter = set0Node.fields();
                while(set0Iter.hasNext()) {
                    Map.Entry<String, JsonNode> set0Next = set0Iter.next();
                    String set0 = set0Next.getKey();
                    ObjectNode set1Node = (ObjectNode) set0Next.getValue();
                    Iterator<Map.Entry<String, JsonNode>> set1Iter = set1Node.fields();
                    while(set1Iter.hasNext()) {
                        Map.Entry<String, JsonNode> set1Next = set1Iter.next();
                        String set1 = set1Next.getKey();
                        JsonNode value = set1Next.getValue();
                        TableEntry entry = new TableEntry();
                        entry.fullName = name;
                        entry.splitName = split(name, true);
                        entry.set0Name = entry.splitName[entry.splitName.length - 2];
                        entry.set1Name = entry.splitName[entry.splitName.length - 1];
                        entry.set0 = set0;
                        entry.set1 = set1;
                        entry.value = value;
                        tableEntries.add(entry);
                    }
                }
            }
            tableEntries.sort(Comparator.comparingInt(TableEntry::depth));
        }

        private TableEntry getTableEntry(String name) {
            for(TableEntry entry: tableEntries) {
                if(name.equals(entry.set1)) {
                    return entry;
                }
            }
            return null;
        }

        private boolean isSubset(String name) {
            return getTableEntry(name) != null;
        }

        private void combine() {
            for(TableEntry tableEntry: tableEntries) {
                ObjectNode master = getSet(tableEntry.set0Name, tableEntry.set0);
                ObjectNode slave = getSet(tableEntry.set1Name, tableEntry.set1);
                ArrayNode arr = getOrCreateArray(master, tableEntry.set1Name);
                arr.add(slave);
            }
        }

        private void finish() {
            for(Map.Entry<String, ArrayNode> entry: rawSets.entrySet()) {
                ArrayNode arr = entry.getValue();
                for(int i = 0; i < arr.size(); i++) {
                    ObjectNode obj = (ObjectNode) arr.get(i);
                    String name = obj.get(Constants.NAME).textValue();
                    if(!isSubset(name)) {
                        ArrayNode treeArr = getOrCreateArray(treeRoot, entry.getKey());
                        treeArr.add(obj);
                    }
                }
            }
        }
    }

    private static class SetEntry {
        private String type;
        private String trimedType;
        private String name;
        private String key;
        private String trimedKey;
        private JsonNode value;

        @Override
        public String toString() {
            return trimedType + " > " + name + ": " + trimedKey + " = " + value;
        }
    }

    private static class TableEntry {
        private String fullName;
        private String[] splitName;
        private String set0Name;
        private String set1Name;
        private String set0;
        private String set1;
        private JsonNode value;

        @Override
        public String toString() {
            return Arrays.toString(splitName) + "->" + set0Name + "|" + set0 + "->" + set1Name + "|" + set1 + " = " + value;
        }

        private int depth() {
            return splitName.length;
        }
    }
}
