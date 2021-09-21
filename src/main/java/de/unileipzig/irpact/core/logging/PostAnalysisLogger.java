package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface PostAnalysisLogger {

    void startLogging();
    void finish(Path zipPath) throws IOException;

    void setupLogNonAdopter(Path target, boolean enabled) throws IOException;
    void startLogNonAdopter();
    void logNonAdopter(ConsumerAgent agent);

    void setupLogInitialAdopter(Path target, boolean enabled) throws IOException;
    void startLogInitialAdopter();
    void logInitialAdopter(ConsumerAgent agent, Product product);

    void setupLogAdoptions(Path target, boolean enabled) throws IOException;
    void startLogAdoption();
    void logAdoption(ConsumerAgent agent, Product product, AdoptionPhase phase, Timestamp stamp);

    void setupLogDecisions(Path target, boolean enabled) throws IOException;
    void startLogDecision();
    void logDecision(
            ConsumerAgent agent, Product product,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double t, boolean result,
            Timestamp stamp);

    void setupLogFinancialThresholds(Path target, boolean enabled) throws IOException;
    void startLogFinancialThreshold();
    void logFinancialThreshold(ConsumerAgent agent, Product product, double f, double t, boolean result, Timestamp stamp);

    void setupLogPhaseTransition(Path target, boolean enabled) throws IOException;
    void startLogPhaseTransition();
    void logPhaseTransition(ConsumerAgent agent, String from, String to, Timestamp stamp);
}
