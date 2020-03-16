package de.unileipzig.irpact.jadex.examples.old.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ItemDes extends StdDeserializer<Item> {

    protected ItemDes() {
        this(null);
    }

    protected ItemDes(Class<?> vc) {
        super(vc);
    }

    @Override
    public Item deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        p.nextValue();
        System.out.println(p.getValueAsInt());
        System.out.println(p.nextFieldName());
        p.nextValue();
        System.out.println(p.getValueAsString());
        p.nextValue();
        System.out.println(p.getValueAsInt());
        return new Item();
    }
}
