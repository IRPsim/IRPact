package de.unileipzig.irpact.util.R.sbuilder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class Args implements Element {

    protected String returnName;
    protected boolean trailingOnly = true;
    protected int numberOfArgs = -1;

    public Args(String returnName) {
        this(returnName, -1);
    }

    public Args(String returnName, int numberOfArgs) {
        this.returnName = returnName;
        setNumberOfArgs(numberOfArgs);
    }

    public String getReturnName() {
        return returnName;
    }

    public String getNamedIndex(int index) {
        return returnName + "[" + index + "]";
    }

    public void setTrailingOnly(boolean trailingOnly) {
        this.trailingOnly = trailingOnly;
    }

    public boolean isTrailingOnly() {
        return trailingOnly;
    }

    public void setNumberOfArgs(int numberOfArgs) {
        this.numberOfArgs = Math.max(-1, numberOfArgs);
    }

    public int getNumberOfArgs() {
        return numberOfArgs;
    }

    @Override
    public boolean print(StringSettings settings, Appendable target) throws IOException {
        target.append(returnName);
        target.append(settings.getEqualSign());
        target.append("commandArgs(trailingOnly");
        target.append(settings.getEqualSign());
        target.append(isTrailingOnly() ? "TRUE" : "FALSE");
        target.append(")");

        if(getNumberOfArgs() > 0) {
            target.append(settings.getNewLine());
            target.append("if(length(");
            target.append(returnName);
            target.append(")");
            target.append(settings.getInequalSign());
            target.append(Integer.toString(getNumberOfArgs()));
            target.append(") {");
            target.append(settings.getNewLine());
            target.append(settings.getTab());
            target.append("stop(\"requires ");
            target.append(Integer.toString(getNumberOfArgs()));
            target.append(" arguments\")");
            target.append(settings.getNewLine());
            target.append("}");
        }

        return true;
    }
}
