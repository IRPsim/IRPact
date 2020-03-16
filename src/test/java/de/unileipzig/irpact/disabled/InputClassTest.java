package de.unileipzig.irpact.disabled;

import de.unileipzig.irpact.experimental.tools.InputClass;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Daniel Abitz
 */
@Disabled
class InputClassTest {

    @Test
    void testX() {
        InputClass inputClass = new InputClass();
        inputClass.setPackagePath("de.unileipzig.irpact.input");
        inputClass.setClass("TestClass");
        System.out.println(inputClass.print());
    }

    @Test
    void testStuff() {
        Class<?> t = double.class;
        System.out.println(t.getSimpleName());
    }
}