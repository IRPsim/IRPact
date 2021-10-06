package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.*;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule4_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DecisionMakingModule2
        extends AbstractUniformCAMultiModule4_2<RAStage2, Number, CalculationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DecisionMakingModule2.class);

    protected double a;
    public void setA(double a) {
        this.a = a;
    }
    public double getA() {
        return a;
    }

    protected double b;
    public void setB(double b) {
        this.b = b;
    }
    public double getB() {
        return b;
    }

    protected double c;
    public void setC(double c) {
        this.c = c;
    }
    public double getC() {
        return c;
    }

    protected double d;
    public void setD(double d) {
        this.d = d;
    }
    public double getD() {
        return d;
    }

    protected double aWeight;
    public void setAWeight(double aWeight) {
        this.aWeight = aWeight;
    }
    public double getAWeight() {
        return aWeight;
    }

    protected double bWeight;
    public void setBWeight(double bWeight) {
        this.bWeight = bWeight;
    }
    public double getBWeight() {
        return bWeight;
    }

    protected double cWeight;
    public void setCWeight(double cWeight) {
        this.cWeight = cWeight;
    }
    public double getCWeight() {
        return cWeight;
    }

    protected double dWeight;
    public void setDWeight(double dWeight) {
        this.dWeight = dWeight;
    }
    public double getDWeight() {
        return dWeight;
    }

    protected boolean forceEvaluation = false;
    public void setForceEvaluation(boolean forceEvaluation) {
        this.forceEvaluation = forceEvaluation;
    }
    public boolean isForceEvaluation() {
        return forceEvaluation;
    }
    public boolean isNotForceEvaluation() {
        return !forceEvaluation;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validateSelf() throws Throwable {
    }

    @Override
    public void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    protected double getFinancialComponent(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule1().calculate(input, actions);
    }

    protected double getEnvironmentalComponent(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule2().calculate(input, actions);
    }

    protected double getNoveltyCompoenent(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule3().calculate(input, actions);
    }

    protected double getSocialComponent(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        return getNonnullSubmodule4().calculate(input, actions);
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        doSelfActionAndAllowAttention(input);

        Timestamp now = input.now();

        ConsumerAgent agent = input.getAgent();
        Product product = input.getProduct();

        DataLogger dataLogger = input.getEnvironment().getDataLogger();
        DataAnalyser dataAnalyser = input.getEnvironment().getDataAnalyser();
        IRPLoggingMessageCollection lmc = new IRPLoggingMessageCollection()
                .setLazy(true)
                .setAutoDispose(true);

        lmc.append("[{}] calculate U", input.getAgentName());

        //threshold check
        double pp = getPurchasePower(input);
        double finThreshold = getFinancialThreshold(input);
        boolean noFinancial = pp < finThreshold;
        if(noFinancial && isNotForceEvaluation()) {
            dataLogger.logEvaluationFailed(
                    agent, product, now,
                    finThreshold, pp
            );
            lmc.append("purchase power < financial threshold ({} < {}) = {}", pp, finThreshold, true);
            traceSimulationProcess(lmc);
            updateStage(input, RAStage2.IMPEDED);
            return RAStage2.IMPEDED;
        }

        double fin;
        double env;
        double nov;
        double soc;

        double B = 0.0;

        //a
        fin = getFinancialComponent(input, actions);
        double afin = a * fin;
        double wafin = aWeight * afin;
        lmc.append("aWeight * a * financial component = {} * {} * {} = {}", aWeight, a, fin, wafin);
        B += wafin;

        //b
        env = getEnvironmentalComponent(input, actions);
        double benv = b * env;
        double wbenv = bWeight * benv;
        lmc.append("bWeight * b * environmental component = {} * {} * {} = {}", bWeight, b, env, wbenv);
        B += wbenv;

        //c
        nov = getNoveltyCompoenent(input, actions);
        double cnov = c * nov;
        double wcnov = cWeight * cnov;
        lmc.append("cWeight * c * novelty component = {} * {} * {} = {}", cWeight, c, nov, wcnov);
        B += wcnov;

        //d
        soc = getSocialComponent(input, actions);
        double dsoc = d * soc;
        double wdsoc = dWeight * dsoc;
        lmc.append("dWeight * d * social component = {} * {} * {} = {}", dWeight, d, soc, wdsoc);
        B += wdsoc;

        double adoptionThreshold = getAdoptionThreshold(input);
        boolean noAdoption = B < adoptionThreshold;

        dataAnalyser.logEvaluationData(
                product, now,
                noFinancial,
                fin, env, nov, soc,
                afin, benv, cnov, dsoc,
                wafin, wbenv, wcnov, wdsoc,
                B
        );

        dataLogger.logEvaluationSuccess(
                agent, product, now,
                aWeight, bWeight, cWeight, dWeight,
                a, b, c, d,
                fin, env, nov, soc,
                afin, benv, cnov, dsoc,
                wafin, wbenv, wcnov, wdsoc,
                finThreshold, pp,
                adoptionThreshold, B
        );

        lmc.append("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);
        lmc.append("noAdoption={}, noFinancial={}", noAdoption, noFinancial);
        traceSimulationProcess(lmc);

        if(noAdoption || noFinancial) {
            updateStage(input, RAStage2.IMPEDED);
            return RAStage2.IMPEDED;
        } else {
            logPhaseTransition(input, DataAnalyser.Phase.ADOPTED, now);
            updateStage(input, RAStage2.ADOPTED);
            return RAStage2.ADOPTED;
        }
    }
}
