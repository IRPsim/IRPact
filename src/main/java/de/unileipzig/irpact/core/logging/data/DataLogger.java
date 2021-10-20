package de.unileipzig.irpact.core.logging.data;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface DataLogger {

    String NO_VALUE = "-";

    //=========================
    //evaluation csv -> xlsx
    //=========================

    void setLogEvaluationTarget(Path target);

    Path getLogEvaluationTarget();

    void enableLogEvaluation(boolean enabled);

    boolean isLogEvaluation();

    void startLogEvaluation();

    void finishLogEvaluation();

    void logEvaluationFailed(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double financialThreshold, double financialValue
    );

    void logEvaluationSuccess(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double aWeight, double bWeight, double cWeight, double dWeight,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double aa, double bb, double cc, double dd,
            double weightedAA, double weightedBB, double weightedCC, double weightedDD,
            double financialThreshold, double financialValue,
            double adoptionThreshold, double adoptionValue
    );

    //=========================
    //TODO evaluation csv -> xlsx
    //=========================

    //ASD gucke impl
    void logEvaluationFailed(ConsumerAgent agent, Product product, Timestamp stamp, AgentDataState data);

    //ASD gucke impl
    void logEvaluationSuccess(ConsumerAgent agent, Product product, Timestamp stamp, AgentDataState data);

    //=========================
    //financial component csv -> xlsx
    //=========================

    void setLogFinancialComponentTarget(Path target);

    Path getLogFinancialComponentTarget();

    void enableLogFinancialComponent(boolean enabled);

    boolean isLogFinancialComponent();

    void startLogFinancialComponent();

    void finishLogFinancialComponent();

    void logFinancialComponent(
            ConsumerAgent agent, Product product, Timestamp stamp,
            double logisticFactor,
            double weightFt, double ftAvg, double ftThis, double ft, double logisticFt,
            double weightNpv, double npvAvg, double npvThis, double npv, double logisticNpv,
            double fin
    );
}
