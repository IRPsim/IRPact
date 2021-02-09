package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.commons.persistence.Persistable;
import de.unileipzig.irpact.commons.persistence.PersistenceException;
import de.unileipzig.irpact.commons.persistence.SimpleUIDManager;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.core.persistence.obj2json.MapperManager;
import de.unileipzig.irpact.core.persistence.obj2json.Object2JsonModul;
import de.unileipzig.irpact.core.persistence.obj2json.Object2JsonModul2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo {

    @Test
    void runDemo1() throws PersistenceException {
        MapperManager mm = new MapperManager();
        mm.register(Car.class, new Car2Json(), new Json2Car());
        mm.register(Person.class, new Person2Json(), new Json2Person());
        Object2JsonModul modul = new Object2JsonModul();
        modul.setManager(mm);

        ObjectNode out = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputNode(out);

        SimpleUIDManager uidManager = new SimpleUIDManager();
        Car c0 = new Car(uidManager.getUID(), 10);
        Car c1 = new Car(uidManager.getUID(), 20);
        Person p0 = new Person(uidManager.getUID(), 30, c0);
        Person p1 = new Person(uidManager.getUID(), 40, c1);
        Person p2 = new Person(uidManager.getUID(), 50, c1);

        modul.persist(c0);
        modul.persist(c1);
        modul.persist(p0);
        modul.persist(p1);
        modul.persist(p2);

        System.out.println(de.unileipzig.irptools.util.Util.printJson(out));

        List<Persistable> cache = modul.restore(out);
        assertEquals(c0, cache.get(0));
        assertEquals(c1, cache.get(1));
        assertEquals(p0, cache.get(2));
        assertEquals(p1, cache.get(3));
        assertEquals(p2, cache.get(4));

        Person pp1 = (Person) cache.get(3);
        Person pp2 = (Person) cache.get(4);
        assertSame(pp1.getCar(), pp2.getCar());
        assertNotSame(p1.getCar(), pp1.getCar());
    }

    @Test
    void runDemoB32() throws PersistenceException {
        MapperManager mm = new MapperManager();
        mm.register(Car.class, new Car2Json(), new Json2Car());
        mm.register(Person.class, new Person2Json(), new Json2Person());
        Object2JsonModul modul = new Object2JsonModul();
        modul.setManager(mm);

        ObjectNode out = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputNode(out);

        SimpleUIDManager uidManager = new SimpleUIDManager();
        Car c0 = new Car(uidManager.getUID(), 10);
        Car c1 = new Car(uidManager.getUID(), 20);
        Person p0 = new Person(uidManager.getUID(), 30, c0);
        Person p1 = new Person(uidManager.getUID(), 40, c1);
        Person p2 = new Person(uidManager.getUID(), 50, c1);

        modul.persist(c0);
        modul.persist(c1);
        modul.persist(p0);
        modul.persist(p1);
        modul.persist(p2);


        ObjectNode out32 = Util.JSON_MAPPER.createObjectNode();

        for(Map.Entry<String, JsonNode> entry: de.unileipzig.irptools.util.Util.iterateFields(out)) {
            String text = de.unileipzig.irptools.util.Util.printJson(entry.getValue());
            String b32 = IRPactBase32.utf8ToBase32(text);
            System.out.println(b32);
            out32.put(entry.getKey(), b32);
        }

        System.out.println(de.unileipzig.irptools.util.Util.printJson(out32));
    }

    @Test
    void runDemo2() throws PersistenceException {
        MapperManager mm = new MapperManager();
        mm.register(Car.class, new Car2Json(), new Json2Car());
        mm.register(Person.class, new Person2Json(), new Json2Person());
        Object2JsonModul2 modul = new Object2JsonModul2();
        modul.setManager(mm);
        modul.setClassField(Object2JsonModul.CLASS_FIELD_SHORT);

        ObjectNode out = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputNode(out);
        ObjectNode outClasses = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputClassesNode(outClasses);
        modul.setClassIdManager(new SimpleUIDManager(100));

        SimpleUIDManager uidManager = new SimpleUIDManager();
        Car c0 = new Car(uidManager.getUID(), 10);
        Car c1 = new Car(uidManager.getUID(), 20);
        Person p0 = new Person(uidManager.getUID(), 30, c0);
        Person p1 = new Person(uidManager.getUID(), 40, c1);
        Person p2 = new Person(uidManager.getUID(), 50, c1);

        modul.persist(c0);
        modul.persist(c1);
        modul.persist(p0);
        modul.persist(p1);
        modul.persist(p2);

        System.out.println(de.unileipzig.irptools.util.Util.printJson(outClasses));
        System.out.println(de.unileipzig.irptools.util.Util.printJson(out));

        List<Persistable> cache = modul.restore(out);
        assertEquals(c0, cache.get(0));
        assertEquals(c1, cache.get(1));
        assertEquals(p0, cache.get(2));
        assertEquals(p1, cache.get(3));
        assertEquals(p2, cache.get(4));

        Person pp1 = (Person) cache.get(3);
        Person pp2 = (Person) cache.get(4);
        assertSame(pp1.getCar(), pp2.getCar());
        assertNotSame(p1.getCar(), pp1.getCar());
    }

    @Test
    void runDemo2B32() throws PersistenceException {
        MapperManager mm = new MapperManager();
        mm.register(Car.class, new Car2Json(), new Json2Car());
        mm.register(Person.class, new Person2Json(), new Json2Person());
        Object2JsonModul2 modul = new Object2JsonModul2();
        modul.setManager(mm);
        modul.setClassField(Object2JsonModul.CLASS_FIELD_SHORT);

        ObjectNode out = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputNode(out);
        ObjectNode outClasses = Util.JSON_MAPPER.createObjectNode();
        modul.setOutputClassesNode(outClasses);
        modul.setClassIdManager(new SimpleUIDManager(100));

        SimpleUIDManager uidManager = new SimpleUIDManager();
        Car c0 = new Car(uidManager.getUID(), 10);
        Car c1 = new Car(uidManager.getUID(), 20);
        Person p0 = new Person(uidManager.getUID(), 30, c0);
        Person p1 = new Person(uidManager.getUID(), 40, c1);
        Person p2 = new Person(uidManager.getUID(), 50, c1);

        modul.persist(c0);
        modul.persist(c1);
        modul.persist(p0);
        modul.persist(p1);
        modul.persist(p2);


        ObjectNode out32 = Util.JSON_MAPPER.createObjectNode();

        for(Map.Entry<String, JsonNode> entry: de.unileipzig.irptools.util.Util.iterateFields(out)) {
            String text = de.unileipzig.irptools.util.Util.printJson(entry.getValue());
            String b32 = IRPactBase32.utf8ToBase32(text);
            System.out.println(b32);
            out32.put(entry.getKey(), b32);
        }

        System.out.println(de.unileipzig.irptools.util.Util.printJson(out32));
    }
}
