package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

/**
 * @author Daniel Abitz
 */
public class DeepData {

    public String msg;

    public DeepData(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "DeepData{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
