package de.unileipzig.irpact.experimental.tools;

import de.unileipzig.irpact.commons.annotation.Experimental;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Experimental
public class InputField {

    private Set<String> importSet = new HashSet<>();
    private List<String> argName;
    private List<String> argValue;

    public void add(InputClass inClass, String value) {
        argName.add(inClass.getClassName());
        argValue.add(value);
    }

    public void add(Class<?> type, String value) {
        argName.add(type.getSimpleName());
        argName.add(value);
        handleClass(type);
    }

    private void handleClass(Class<?> type) {
        if(type.isPrimitive()
                || type == String.class) {
            return;
        }
        importSet.add(type.getPackage().getName());
    }
}
