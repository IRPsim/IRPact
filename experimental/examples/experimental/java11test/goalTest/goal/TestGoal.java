package de.unileipzig.irpact.jadex.examples.experimental.java11test.goalTest.goal;

import jadex.bdiv3.annotation.Goal;

@Goal
public class TestGoal {

    protected String data;

    public TestGoal(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
