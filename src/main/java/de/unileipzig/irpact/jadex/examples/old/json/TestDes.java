package de.unileipzig.irpact.jadex.examples.old.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class TestDes {

    public static void main(String[] args) throws JsonProcessingException {
        String text = "{\n" +
                "    \"id\": 25,\n" +
                "    \"itemName\": \"FEDUfRgS\",\n" +
                "    \"owner\": 15\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Item.class, new ItemDes());
        mapper.registerModule(module);
        Item item = mapper.readValue(text, Item.class);
    }
}
