package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

/**
 * @author Daniel Abitz
 */
public class BasicDataLogger implements DataLogger {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicDataLogger.class);

    public static final String DEFAULT_PATTERN = "%msg%n";

    private SimulationEnvironment environment;

    public BasicDataLogger() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    private static String printStamp(Timestamp stamp) {
        return stamp == null
                ? null
                : stamp.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static String createFormat(int count) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < count; i++) {
            if(i > 0) sb.append(";");
            sb.append("{}");
        }
        return sb.toString();
    }

    //=========================
    //evaluation csv -> xlsx
    //=========================

    private static final int EVAL_FORMAT_LEN = 28;
    private static final String EVAL_LOGGER_NAME = "EVAL_LOGGER";
    private static final String EVAL_APPENDER_NAME = "EVAL_APPENDER";
    private static final String EVAL_FORMAT = createFormat(EVAL_FORMAT_LEN);
    private boolean logAllEvaluationData = false;
    private Path evalTarget;
    private SimplifiedFileLogger evalLogger;

    @Override
    public void setLogEvaluationTarget(Path target) {
        evalTarget = target;
    }

    @Override
    public Path getLogEvaluationTarget() {
        return evalTarget;
    }

    @Override
    public void enableLogEvaluation(boolean enabled) {
        logAllEvaluationData = enabled;
    }

    @Override
    public boolean isLogEvaluation() {
        return logAllEvaluationData;
    }

    @Override
    public void startLogEvaluation() {
        if(logAllEvaluationData) {
            try {
                LOGGER.trace("create logger: {}", evalTarget);
                evalLogger = new SimplifiedFileLogger(
                        EVAL_LOGGER_NAME,
                        SimplifiedFileLogger.createNew(
                                EVAL_APPENDER_NAME,
                                DEFAULT_PATTERN,
                                evalTarget
                        )
                );
            } catch (IOException e) {
                LOGGER.error("logger creation failed, disable log all evaluations", e);
                evalLogger = null;
            }
        }
    }

    @Override
    public void finishLogEvaluation() {
        if(evalLogger != null) {
            evalLogger.stop();
            evalLogger = null;
        }
    }

    @Override
    public void logEvaluationFailed(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double financialThreshold, double financialValue) {
        if(evalLogger != null) {
            evalLogger.log(EVAL_FORMAT,
                    agent.getName(), product.getName(), printStamp(stamp), stamp.getYear(),
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    NO_VALUE, NO_VALUE, NO_VALUE, NO_VALUE,
                    financialThreshold, financialValue,
                    NO_VALUE, NO_VALUE
            );
        }
    }

    @Override
    public void logEvaluationSuccess(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double aWeight, double bWeight, double cWeight, double dWeight,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double financialThreshold, double financialValue,
            double adoptionThreshold, double adoptionValue) {
        if(evalLogger != null) {
            evalLogger.log(EVAL_FORMAT,
                    agent.getName(), product.getName(), printStamp(stamp), stamp.getYear(),
                    aWeight, bWeight, cWeight, dWeight,
                    a, b, c, d,
                    aValue, bValue, cValue, dValue,
                    aa, bb, cc, dd,
                    weightedAA, weightedBB, weightedCC, weightedDD,
                    financialThreshold, financialValue,
                    adoptionThreshold, adoptionValue
            );
        }
    }

    //=========================
    //financial component csv -> xlsx
    //=========================

    private static final int FIN_FORMAT_LEN = 16;
    private static final String FIN_LOGGER_NAME = "EVAL_LOGGER";
    private static final String FIN_APPENDER_NAME = "EVAL_APPENDER";
    private static final String FIN_FORMAT = createFormat(FIN_FORMAT_LEN);
    private boolean logFinancialComponent = false;
    private Path finTarget;
    private SimplifiedFileLogger finLogger;

    @Override
    public void setLogFinancialComponentTarget(Path target) {
        finTarget = target;
    }

    @Override
    public Path getLogFinancialComponentTarget() {
        return finTarget;
    }

    @Override
    public void enableLogFinancialComponent(boolean enabled) {
        logFinancialComponent = enabled;
    }

    @Override
    public boolean isLogFinancialComponent() {
        return logFinancialComponent;
    }

    @Override
    public void startLogFinancialComponent() {
        if(logFinancialComponent) {
            try {
                LOGGER.trace("create logger: {}", finTarget);
                finLogger = new SimplifiedFileLogger(
                        FIN_LOGGER_NAME,
                        SimplifiedFileLogger.createNew(
                                FIN_APPENDER_NAME,
                                DEFAULT_PATTERN,
                                finTarget
                        )
                );
            } catch (IOException e) {
                LOGGER.error("logger creation failed, disable log all evaluations", e);
                finLogger = null;
            }
        }
    }

    @Override
    public void finishLogFinancialComponent() {
        if(finLogger != null) {
            finLogger.stop();
            finLogger = null;
        }
    }

    @Override
    public void logFinancialComponent(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double logisticFactor,
            double weightFt, double ftAvg, double ftThis, double ft, double logisticFt,
            double weightNpv, double npvAvg, double npvThis, double npv, double logisticNpv,
            double fin) {
        if(finLogger != null) {
            finLogger.log(FIN_FORMAT,
                    agent.getName(), product.getName(), printStamp(stamp), stamp.getYear(),
                    logisticFactor,
                    weightFt, ftAvg, ftThis, ft, logisticFt,
                    weightNpv, npvAvg, npvThis, npv, logisticNpv,
                    fin
            );
        }
    }
}
