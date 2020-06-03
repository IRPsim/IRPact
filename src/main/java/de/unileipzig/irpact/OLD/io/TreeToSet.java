package de.unileipzig.irpact.OLD.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

/**
 * Wandelt die Baum-Struktur in die Set-Struktur um.
 * Essenziell fuer die Set-Struktur sind Namen/Bezeichner fuer einzelne Set-Elemente.
 * Daher muessen diese im Baum-Modell beachtet werden. Es werden somit zwischen
 * Elementen mit Namen und jenen ohne Namen unterschieden.
 *
 * Der grundlegende Gedanke fuer die Umwandlung ist das "umdrehen" der Struktur.
 * Fuer den Typ in der Set-Struktur wird einfach der Typ aus dem Baum genommen.
 *
 * Aus den folgenden Daten vom Typ "TestData"
 *
 * <pre>
 *     {
 *         "$name" : "meinName",
 *         "wert1" : 1,
 *         "wert2": 2
 *     }
 * </pre>
 *
 * wird
 *
 * <pre>
 *     "sets" : {
 *         "set_TestData" : {
 *             "meinName" :{
 *                 "wert1" : 1,
 *                 "wert2" : 2
 *             }
 *         }
 *     }
 * </pre>
 *
 * Tiefer verschachtelte Strukturen muessen nun in ihre Bestandteile aufgespalten werden.
 * Hierbei ist es wichtig, dass die tieferen Strukturen ebenfalls ueber einen Namen verfuegen.
 *
 * <pre>
 *     {
 *         "$name" : "meinName",
 *         "wert1" : 1,
 *         "komplexerWert" : {
 *             "$name" : "meinKomplexerWert",
 *             "a" : 10,
 *             "b" : 20
 *         }
 *     }
 * </pre>
 *
 * wird nun zu den Set-Elementen
 *
 * <pre>
 *     "sets" : {
 *         "set_TestData" : {
 *             "meinName" :{
 *                 "wert1" : 1
 *             }
 *         },
 *         "set_KomplexerWert" : {
 *             "meinKomplexerWert" : {
 *                 "a" : 10,
 *                 "b" : 20
 *             }
 *         }
 *     }
 * </pre>
 *
 * Um nun die Zusammengehoerigkeit zu erhalten, muss nun in den Tables ein neuer Eintrag erstellt werden.
 * Dazu wird ein neuer Parameter erstellt, welcher die Zusammengehoerigkeit beschreibt.
 * Diese Parameter werden aus den Typen der jeweiligen Bestandteile erstellt.
 * Ausgehend von den Typen im vorherigem Beispiel heißen dieser Parameter nun "par_TestData_KomplexerWert".
 *
 * Als Table-Eintrag gibt es nun:
 *
 * <pre>
 *     "tables" : {
 *         "par_TestData_KomplexerWert" : {
 *             "meinName" : {
 *                 "meinKomplexerWert" : 1
 *             }
 *         }
 *     }
 * </pre>
 *
 * Der Wert 1 symbolisiert die Zusammengehoerigkeit.
 * Auf diese Weise koennen belieg verschachtelte Strukturen erstellt werden.
 * Benoetigte Table-Parameter werden automatisch erstellt.
 *
 * @author Daniel Abitz
 */
public final class TreeToSet {

    private TreeToSet() {
    }

    private static ObjectNode getOrCreateObject(ObjectNode root, String first, String second) {
        ObjectNode firstNode = getOrCreateObject(root, first);
        return getOrCreateObject(firstNode, second);
    }

    private static ObjectNode getOrCreateObject(ObjectNode root, String key) {
        if(root.has(key)) {
            return (ObjectNode) root.get(key);
        } else {
            return root.putObject(key);
        }
    }

    private static String print(Deque<String> stack) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> descIter = stack.descendingIterator();
        while(descIter.hasNext()) {
            sb.append(descIter.next());
            if(descIter.hasNext()) {
                sb.append(Constants.DELIMITER);
            }
        }
        return sb.toString();
    }

    //==================================================
    //...
    //==================================================

    public static void handle(ObjectNode treeRoot, ObjectNode setRoot) {
        ObjectNode scalarsRoot = getOrCreateObject(setRoot, Constants.SCALARS);
        ObjectNode setsRoot = getOrCreateObject(setRoot, Constants.SETS);
        ObjectNode tablesRoot = getOrCreateObject(setRoot, Constants.TABLES);
        handleObject(new ArrayDeque<>(), new ArrayDeque<>(), treeRoot, scalarsRoot, setsRoot, tablesRoot);
    }

    private static void handleObjectWithoutName(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode scalarsRoot,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        Iterator<Map.Entry<String, JsonNode>> iter = current.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String key = entry.getKey();
            JsonNode node = entry.getValue();
            stack.push(key);
            if(node.isArray()) {
                handleArray(stack, names, (ArrayNode) node, scalarsRoot, setsRoot, tablesRoot);
            }
            else if(node.isObject()) {
                handleObject(stack, names, (ObjectNode) node, scalarsRoot, setsRoot, tablesRoot);
            }
            else {
                if(Constants.GLOBAL.equals(stack.getLast())) {
                    String tempName = Constants.SCA + stack.peek();
                    scalarsRoot.set(tempName, node);
                } else {
                    throw new IllegalStateException();
                }
            }
            stack.pop();
        }
    }

    private static void handleObjectWithName(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode scalarsRoot,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        String name = current.get(Constants.NAME).textValue();
        names.push(name);
        ObjectNode setNode = getOrCreateObject(setsRoot, Constants.SET + stack.peek(), names.peek());
        Iterator<Map.Entry<String, JsonNode>> iter = current.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String key = entry.getKey();
            JsonNode node = entry.getValue();
            if(Constants.NAME.equals(key)) {
                continue;
            }
            if(node.isContainerNode()) {
                stack.push(key);
                if(node.isArray()) {
                    handleArray(stack, names, (ArrayNode) node, scalarsRoot, setsRoot, tablesRoot);
                } else {
                    handleObject(stack, names, (ObjectNode) node, scalarsRoot, setsRoot, tablesRoot);
                }
                stack.pop();
            } else {
                //String tempName = Constants.PAR + key;
                String tempName = Constants.PAR + stack.peek() + Constants.DELIMITER + key;
                setNode.set(tempName, node);
            }
        }

        names.pop();
        if(!names.isEmpty()) {
            String setName = print(stack);
            String setEntryName = names.peek();

            ObjectNode linkSetNode = getOrCreateObject(tablesRoot, Constants.PAR + setName, setEntryName);
            linkSetNode.put(name, 1.0);
        }
    }

    private static void handleObject(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode scalarsRoot,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        if(!current.has(Constants.NAME)) {
            handleObjectWithoutName(stack, names, current, scalarsRoot, setsRoot, tablesRoot);
        } else {
            handleObjectWithName(stack, names, current, scalarsRoot, setsRoot, tablesRoot);
        }
    }

    private static void handleArray(
            Deque<String> stack,
            Deque<String> names,
            ArrayNode current,
            ObjectNode scalarsRoot,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        Iterator<JsonNode> iter = current.elements();
        while(iter.hasNext()) {
            JsonNode node = iter.next();
            if(node.isObject()) {
                handleObject(stack, names, (ObjectNode) node, scalarsRoot, setsRoot, tablesRoot);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }
}
