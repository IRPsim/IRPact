package de.unileipzig.irpact.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class TreeToSet {

    static ObjectNode getOrCreate(ObjectNode root, String first, String second) {
        ObjectNode firstNode = getOrCreate(root, first);
        return getOrCreate(firstNode, second);
    }

    static ObjectNode getOrCreate(ObjectNode root, String key) {
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
        ObjectNode setsRoot = getOrCreate(setRoot, Constants.SETS);
        ObjectNode tablesRoot = getOrCreate(setRoot, Constants.TABLES);
        handleObject(new ArrayDeque<>(), new ArrayDeque<>(), treeRoot, setsRoot, tablesRoot);
    }

    private static void handleObjectWithoutName(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        Iterator<Map.Entry<String, JsonNode>> iter = current.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String key = entry.getKey();
            JsonNode node = entry.getValue();
            stack.push(key);
            if(node.isArray()) {
                handleArray(stack, names, (ArrayNode) node, setsRoot, tablesRoot);
            }
            else if(node.isObject()) {
                handleObject(stack, names, (ObjectNode) node, setsRoot, tablesRoot);
            }
            else {
                throw new IllegalStateException();
            }
            stack.pop();
        }
    }

    private static void handleObjectWithName(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        String name = current.get(Constants.NAME).textValue();
        names.push(name);
        ObjectNode setNode = getOrCreate(setsRoot, Constants.SET + stack.peek(), names.peek());
        Iterator<Map.Entry<String, JsonNode>> iter = current.fields();
        while(iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String key = entry.getKey();
            JsonNode node = entry.getValue();
            if(Constants.NAME.equals(key)) continue;
            if(node.isContainerNode()) {
                stack.push(key);
                if(node.isArray()) {
                    handleArray(stack, names, (ArrayNode) node, setsRoot, tablesRoot);
                } else {
                    handleObject(stack, names, (ObjectNode) node, setsRoot, tablesRoot);
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

            ObjectNode linkSetNode = getOrCreate(tablesRoot, Constants.PAR + setName, setEntryName);
            linkSetNode.put(name, 1.0);
        }
    }

    private static void handleObject(
            Deque<String> stack,
            Deque<String> names,
            ObjectNode current,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        if(!current.has(Constants.NAME)) {
            handleObjectWithoutName(stack, names, current, setsRoot, tablesRoot);
        } else {
            handleObjectWithName(stack, names, current, setsRoot, tablesRoot);
        }
    }

    private static void handleArray(
            Deque<String> stack,
            Deque<String> names,
            ArrayNode current,
            ObjectNode setsRoot,
            ObjectNode tablesRoot) {
        Iterator<JsonNode> iter = current.elements();
        while(iter.hasNext()) {
            JsonNode node = iter.next();
            if(node.isObject()) {
                handleObject(stack, names, (ObjectNode) node, setsRoot, tablesRoot);
            } else {
                throw new UnsupportedOperationException();
            }
        }
    }
}
