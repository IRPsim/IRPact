package de.unileipzig.irpact.experimental.tools;

import de.unileipzig.irpact.commons.annotation.Experimental;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Experimental
public class InputClass {

    private static final String nl = "\n";

    private String packagePath;
    private Set<String> importList = new HashSet<>();
    private String className;
    private String name;

    public InputClass() {
    }

    public String getPackageClassPath() {
        return packagePath + "." + className;
    }

    public String getClassName() {
        return className;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public void setClass(String className) {
        this.className = className;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packagePath).append(";").append(nl);
        for(String importItem: importList) {
            sb.append("import ").append(importItem).append(";").append(nl);
        }
        sb.append(nl);
        sb.append("class ").append(className).append(" {").append(nl);

        sb.append("}");
        return sb.toString();
    }
}
