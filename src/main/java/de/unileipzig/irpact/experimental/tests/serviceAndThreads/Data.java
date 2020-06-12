package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class Data {

    public String msg;
    public DeepData deepData;

    public Data(String msg, DeepData deepData) {
        this.msg = msg;
        this.deepData = deepData;
    }

    @Override
    public String toString() {
        return "Data{" +
                "msg='" + msg + '\'' +
                ", deepData=" + deepData +
                '}';
    }
}
