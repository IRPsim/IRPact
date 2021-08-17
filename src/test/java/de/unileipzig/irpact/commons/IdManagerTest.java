package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.commons.util.IdManager;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class IdManagerTest {

    @Test
    void testNextId() {
        IdManager idm = new IdManager(0L);
        assertEquals(0L, idm.nextId());
        assertEquals(1L, idm.nextId());
    }

    @Test
    void restResetId() {
        IdManager idm = new IdManager(0L);
        assertEquals(0L, idm.nextId());
        idm.reset();
        assertEquals(0L, idm.nextId());
    }

    @Test
    void restResetId2() {
        IdManager idm = new IdManager(0L);
        assertEquals(0L, idm.nextId());
        idm.reset(11L);
        assertEquals(11L, idm.nextId());
    }

    @Test
    void testLastId() {
        IdManager idm = new IdManager(0L);
        assertEquals(0L, idm.nextId());
        assertEquals(0L, idm.lastId());
    }

    @Test
    void testLastIdError() {
        IdManager idm = new IdManager(0L);
        assertThrows(NoSuchElementException.class, idm::lastId);
    }
}