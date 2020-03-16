package de.unileipzig.irpact.jadex.examples.experimental.java11test.planTest;

import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.PlanBody;

import java.util.HashMap;
import java.util.Map;

@Plan
public class TestPlan {

    protected Map<String, String> db = new HashMap<>();

    public TestPlan() {
        db.put("a", "a0");
    }

    @PlanBody
    public void doStuff() {
        System.out.println("ExternalTestPlan 'a' '" + db.get("a") + "'");
    }
}
