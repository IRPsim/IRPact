package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class FileUtilTest {

    @Test
    void testChangeFileName() {
        Path p = Paths.get("a", "b", "c.txt");
        Path p2 = Paths.get("a", "b", "xxx-c-1.txt");
        assertEquals(p2, FileUtil.changeFileName(p, "xxx-", "-1"));
    }
}