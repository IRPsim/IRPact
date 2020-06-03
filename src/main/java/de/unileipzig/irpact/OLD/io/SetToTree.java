package de.unileipzig.irpact.OLD.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public final class SetToTree {

    private SetToTree() {
    }

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

    public static void handle(JsonNodeCreator creator, ObjectNode setRoot, ObjectNode treeRoot) {
        Cache cache = new Cache(creator, treeRoot);
        JsonNode scalarsRoot = setRoot.get(Constants.SCALARS);
        if(scalarsRoot != null) {
            cache.flatScalars((ObjectNode) scalarsRoot);
        }
        JsonNode setsRoot = setRoot.get(Constants.SETS);
        cache.flatSets((ObjectNode) setsRoot);
        cache.buildRawSetObjects();
        JsonNode tablesRoot = setRoot.get(Constants.TABLES);
        cache.flatTables((ObjectNode) tablesRoot);
        cache.combine();
        cache.finish();
    }

    //==================================================
    //Cache
    //==================================================

    private static class Cache {
        private JsonNodeCreator creator;
        private ObjectNode treeRoot;
        private Map<String, ArrayNode> rawSetObjects = new HashMap<>();
        private List<ScalarEntry> scalarEntries = new ArrayList<>();
        private List<SetEntry> setEntries = new ArrayList<>();
        private List<TableEntry> tableEntries = new ArrayList<>();

        private Cache(JsonNodeCreator creator, ObjectNode treeRoot) {
            this.creator = creator;
            this.treeRoot = treeRoot;
        }

        private void flatScalars(ObjectNode scalarsNode) {
            Iterator<Map.Entry<String, JsonNode>> scaIters = scalarsNode.fields();
            while(scaIters.hasNext()) {
                Map.Entry<String, JsonNode> scaNext = scaIters.next();
                String name = scaNext.getKey();
                JsonNode node = scaNext.getValue();
                ScalarEntry entry = new ScalarEntry();
                entry.name = name;
                entry.trimedName = splitGetFirst(name, true);
                entry.value = node;
                scalarEntries.add(entry);
            }
        }

        /**
         * Faechtert das Set-Objekt in seine einzelnen Bestandteile auf.
         *
         * <pre>
         *     {
         *         "set_Beispiel0" : {
         *             "name0" : {
         *                 "par_value0" : 0,
         *                 "par_value1" : 1,
         *             },
         *             "name1" : {
         *                 "par_value0" : 2,
         *                 "par_value1" : 3,
         *             }
         *         },
         *         "set_Beispiel1" : {
         *              "name2" : {
         *                  "par_value3" : 10
         *              },
         *              "name3" : {
         *                  "par_value3" : 20
         *              }
         *         }
         *     }
         * </pre>
         *
         * wuerde zu
         *
         * <pre>
         *     "set_Beispiel0", "name0", "par_value0", 0
         *     "set_Beispiel0", "name0", "par_value1", 1
         *     "set_Beispiel0", "name1", "par_value0", 2
         *     "set_Beispiel0", "name1", "par_value1", 3
         *     "set_Beispiel1", "name2", "par_value3", 10
         *     "set_Beispiel1", "name3", "par_value3", 20
         * </pre>
         *
         * aufgefaechert werden.
         * Leere Set-Elemente werden entsprechend behandelt.
         *
         * @param setsNode Json-Knoten des Sets-Elementes
         * @since 0.0
         */
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

        private ObjectNode getSetObject(String type, String name) {
            ArrayNode typeNode = rawSetObjects.computeIfAbsent(type, _type -> creator.arrayNode());
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

        /**
         * Erstellt aus der Set-Struktur die Object-Struktur.
         *
         * So wird
         *
         * <pre>
         *     "set_Beispiel0" : {
         *         "name0" : {
         *             "par_value0" : 0,
         *             "par_value1" : 1,
         *         },
         *         "name1" : {
         *             "par_value0" : 2,
         *             "par_value1" : 3,
         *         }
         *     }
         * </pre>
         *
         * in die folgenden zwei ObjectNodes umgewandelt.
         *
         * <pre>
         *     {
         *         "$name" : "name0",
         *         "value0" : 0,
         *         "value1" : 1
         *     },
         *     {
         *         "$name" : "name1",
         *         "value0" : 2,
         *         "value1" : 3
         *     },
         * </pre>
         *
         * umgewandelt. Prefixe werden dabei entfernt.
         *
         * @since 0.0
         */
        private void buildRawSetObjects() {
            for(SetEntry entry: setEntries) {
                ObjectNode node = getSetObject(entry.trimedType, entry.name);
                if(entry.trimedKey != null) {
                    node.set(entry.trimedKey, entry.value);
                }
            }
        }

        /**
         * Faechtert das Tables-Objekt in seine einzelnen Bestandteile auf.
         *
         * <pre>
         *     {
         *         "par_ConsumerAgentGroup_ConsumerAgentGroupAttribute" : {
         *             "group1" : {
         *                 "attr1" : 1,
         *                 "attr2" : 1,
         *             },
         *             "group2" : {
         *                 "attr3" : 1
         *             }
         *         }
         *     }
         * </pre>
         *
         * wuerde zu
         *
         * <pre>
         *     "par_ConsumerAgentGroup_ConsumerAgentGroupAttribute", "group1", "attr1", 1
         *     "par_ConsumerAgentGroup_ConsumerAgentGroupAttribute", "group1", "attr2", 1
         *     "par_ConsumerAgentGroup_ConsumerAgentGroupAttribute", "group2", "attr3", 1
         * </pre>
         *
         * aufgefaechert werden.
         *
         * @param tablesNode Json-Knoten des Tables-Elementes
         * @since 0.0
         */
        private void flatTables(ObjectNode tablesNode) {
            Iterator<Map.Entry<String, JsonNode>> tablesIter = tablesNode.fields();
            while(tablesIter.hasNext()) {
                Map.Entry<String, JsonNode> tablesNext = tablesIter.next();
                String name = tablesNext.getKey();
                ObjectNode set0Node = (ObjectNode) tablesNext.getValue();
                Iterator<Map.Entry<String, JsonNode>> set0Iter = set0Node.fields();
                while(set0Iter.hasNext()) {
                    Map.Entry<String, JsonNode> set0Next = set0Iter.next();
                    String set0Name = set0Next.getKey();
                    ObjectNode set1Node = (ObjectNode) set0Next.getValue();
                    Iterator<Map.Entry<String, JsonNode>> set1Iter = set1Node.fields();
                    while(set1Iter.hasNext()) {
                        Map.Entry<String, JsonNode> set1Next = set1Iter.next();
                        String set1Name = set1Next.getKey();
                        JsonNode value = set1Next.getValue();
                        TableEntry entry = new TableEntry();
                        entry.fullName = name;
                        entry.splitName = split(name, true);
                        entry.set0Type = entry.splitName[entry.splitName.length - 2];
                        entry.set1Type = entry.splitName[entry.splitName.length - 1];
                        entry.set0Name = set0Name;
                        entry.set1Name = set1Name;
                        entry.value = value;
                        tableEntries.add(entry);
                    }
                }
            }
            tableEntries.sort(Comparator.comparingInt(TableEntry::depth));
        }

        private TableEntry getTableEntry(String name) {
            for(TableEntry entry: tableEntries) {
                if(name.equals(entry.set1Name)) {
                    return entry;
                }
            }
            return null;
        }

        private boolean isSubset(String name) {
            return getTableEntry(name) != null;
        }

        /**
         * Kombiniert die RawSetObjects mittels der Informationen aus dem Tables-Node miteinander.
         *
         * @since 0.0
         */
        private void combine() {
            for(TableEntry tableEntry: tableEntries) {
                ObjectNode master = getSetObject(tableEntry.set0Type, tableEntry.set0Name);
                ObjectNode slave = getSetObject(tableEntry.set1Type, tableEntry.set1Name);
                ArrayNode arr = getOrCreateArray(master, tableEntry.set1Type);
                arr.add(slave);
            }
        }

        /**
         * Fuegt nun alle gueltigen RawSetObjects zum finalen JsonNode zusammen.
         * Gueltig sind jene Knoten, welche selber nicht als Kindsknoten bei einem anderen Knoten vorkommen.
         *
         * @since 0.0
         */
        private void finish() {
            if(!scalarEntries.isEmpty()) {
                ObjectNode global = getOrCreateObject(treeRoot, Constants.GLOBAL);
                for(ScalarEntry entry: scalarEntries) {
                    global.set(entry.trimedName, entry.value);
                }
            }
            for(Map.Entry<String, ArrayNode> entry: rawSetObjects.entrySet()) {
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

    private static class ScalarEntry {
        private String name;
        private String trimedName;
        private JsonNode value;

        @Override
        public String toString() {
            return trimedName + " > " + value;
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
        private String set0Type;
        private String set1Type;
        private String set0Name;
        private String set1Name;
        private JsonNode value;

        @Override
        public String toString() {
            return Arrays.toString(splitName) + "->" + set0Type + "|" + set0Name + "->" + set1Type + "|" + set1Name + " = " + value;
        }

        private int depth() {
            return splitName.length;
        }
    }
}
