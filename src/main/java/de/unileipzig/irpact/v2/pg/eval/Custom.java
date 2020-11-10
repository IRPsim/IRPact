package de.unileipzig.irpact.v2.pg.eval;

import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

/**
 * @author Daniel Abitz
 */
public class Custom implements Eval {

    private String str;

    public Custom() {
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public double calculate(double input) {
        return new Expression(str, new Argument("x = " + input)).calculate();
    }
}
