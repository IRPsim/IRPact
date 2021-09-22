package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.Zip;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class BasicPostAnalysisLogger implements PostAnalysisLogger {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicPostAnalysisLogger.class);

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private SimulationEnvironment environment;

    public BasicPostAnalysisLogger() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    //=========================
    //general
    //=========================

    public static final String DEFAULT_PATTERN = "%msg%n";

    public static String printStamp(Timestamp stamp) {
        return stamp == null
                ? null
                : stamp.getTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    private static String printStampMillis(Timestamp stamp) {
        return stamp == null
                ? null
                : Long.toString(stamp.getEpochMilli());
    }

    public static String createFormat(int count) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < count; i++) {
            if(i > 0) sb.append(";");
            sb.append("{}");
        }
        return sb.toString();
    }

    private static Object[] buildArr(int len, Object... args) {
        if(len == args.length) {
            return args;
        } else {
            Object[] out = new Object[len];
            Arrays.fill(out, "");
            System.arraycopy(args, 0, out, 0, args.length);
            return out;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static Object[] buildComment(int len, String msg) {
        return buildArr(len, "#Comment: " + msg);
    }

    private static Object[] buildVersion(int len, String version) {
        return buildArr(len, "#Version: " + version);
    }

    private List<Path> getOutputs() {
        List<Path> outputs = new ArrayList<>();
        if(logAdoptions) outputs.add(adoptionPath);
        if(logDecisions) outputs.add(decisionPath);
        if(logFTs) outputs.add(ftPath);
        if(logNonAdopter) outputs.add(nonAdopterPath);
        if(logInitialAdopter) outputs.add(initialAdopterPath);
        if(logPhaseTransition) outputs.add(phaseTransitionPath);
        return outputs;
    }

    private List<SimplifiedFileLogger> getLoggers() {
        List<SimplifiedFileLogger> outputs = new ArrayList<>();
        if(logAdoptions) outputs.add(adoptionLogger);
        if(logDecisions) outputs.add(decisionLogger);
        if(logFTs) outputs.add(ftLogger);
        if(logNonAdopter) outputs.add(nonAdopterLogger);
        if(logInitialAdopter) outputs.add(initialAdopterLogger);
        if(logPhaseTransition) outputs.add(phaseTransitionLogger);
        return outputs;
    }

    @Override
    public void startLogging() {
        startLogNonAdopter();
        startLogInitialAdopter();
        startLogAdoption();
        startLogDecision();
        startLogFinancialThreshold();
        startLogPhaseTransition();
    }

    @Override
    public void finish(Path zipPath) throws IOException {
        List<Path> outputs = getOutputs();
        if(outputs.isEmpty()) {
            LOGGER.info("no output");
        } else {
            LOGGER.trace("create zip: {}", zipPath);

            LOGGER.trace("stop loggers");
            List<SimplifiedFileLogger> loggers = getLoggers();
            for(SimplifiedFileLogger logger: loggers) {
                logger.stop();
            }

            LOGGER.info("zip entries: {}", outputs.size());
            Zip zip = new Zip(zipPath);
            try(Zip.CloseHandle ignored = zip.startWriting()) {
                for(Path path: outputs) {
                    LOGGER.trace("add {}", path);
                    zip.putNext(
                            path,
                            path.getFileName().toString()
                    );
                }
            }
            cleanUp(outputs);
        }
    }

    private void cleanUp(List<Path> outputs) {
        for(Path path: outputs) {
            try {
                LOGGER.trace("delete {}", path);
                Files.delete(path);
            } catch (IOException e) {
                LOGGER.error("failed cleanUp", e);
            }
        }
    }

    //=========================
    //adoptions
    //=========================

    private static final int NON_ADOPTER_FORMAT_LEN = 1;
    private static final String NON_ADOPTER_LOGGER_NAME = "NON_ADOPTER_LOGGER";
    private static final String NON_ADOPTER_APPENDER_NAME = "NON_ADOPTER_APPENDER";
    private static final String NON_ADOPTER_FORMAT = createFormat(NON_ADOPTER_FORMAT_LEN);
    private static final String NON_ADOPTER_VERSION = "1";
    private SimplifiedFileLogger nonAdopterLogger;
    private Path nonAdopterPath;
    private boolean logNonAdopter = false;

    @Override
    public void setupLogNonAdopter(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log adoptions: {}", target);
            nonAdopterLogger = new SimplifiedFileLogger(
                    NON_ADOPTER_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            NON_ADOPTER_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logNonAdopter = true;
            nonAdopterPath = target;
        } else {
            LOGGER.trace("disable log adoptions");
            nonAdopterLogger = null;
            logNonAdopter = false;
            nonAdopterPath = null;
        }
    }

    @Override
    public void startLogNonAdopter() {
        if(logNonAdopter) {
            nonAdopterLogger.log(NON_ADOPTER_FORMAT, buildComment(NON_ADOPTER_FORMAT_LEN, ""));
            nonAdopterLogger.log(NON_ADOPTER_FORMAT, buildVersion(NON_ADOPTER_FORMAT_LEN, NON_ADOPTER_VERSION));
        }
    }

    @Override
    public void logNonAdopter(ConsumerAgent agent) {
        if(logNonAdopter) {
            nonAdopterLogger.log(NON_ADOPTER_FORMAT, agent.getName());
        }
    }

    //=========================
    //adoptions
    //=========================

    private static final int INITIAL_ADOPTER_FORMAT_LEN = 2;
    private static final String INITIAL_ADOPTER_LOGGER_NAME = "INITIAL_ADOPTER_LOGGER";
    private static final String INITIAL_ADOPTER_APPENDER_NAME = "INITIAL_ADOPTER_APPENDER";
    private static final String INITIAL_ADOPTER_FORMAT = createFormat(INITIAL_ADOPTER_FORMAT_LEN);
    private static final String INITIAL_ADOPTER_VERSION = "1";
    private SimplifiedFileLogger initialAdopterLogger;
    private Path initialAdopterPath;
    private boolean logInitialAdopter = false;

    @Override
    public void setupLogInitialAdopter(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log adoptions: {}", target);
            initialAdopterLogger = new SimplifiedFileLogger(
                    INITIAL_ADOPTER_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            INITIAL_ADOPTER_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logInitialAdopter = true;
            initialAdopterPath = target;
        } else {
            LOGGER.trace("disable log adoptions");
            initialAdopterLogger = null;
            logInitialAdopter = false;
            initialAdopterPath = null;
        }
    }

    @Override
    public void startLogInitialAdopter() {
        if(logInitialAdopter) {
            initialAdopterLogger.log(INITIAL_ADOPTER_FORMAT, buildComment(INITIAL_ADOPTER_FORMAT_LEN, ""));
            initialAdopterLogger.log(INITIAL_ADOPTER_FORMAT, buildVersion(INITIAL_ADOPTER_FORMAT_LEN, INITIAL_ADOPTER_VERSION));
        }
    }

    @Override
    public void logInitialAdopter(ConsumerAgent agent, Product product) {
        if(logInitialAdopter) {
            initialAdopterLogger.log(INITIAL_ADOPTER_FORMAT, agent.getName(), product.getName());
        }
    }

    //=========================
    //adoptions
    //=========================

    private static final int ADOPTIONS_FORMAT_LEN = 4;
    private static final String ADOPTIONS_LOGGER_NAME = "ADOPTIONS_LOGGER";
    private static final String ADOPTIONS_APPENDER_NAME = "ADOPTIONS_APPENDER";
    private static final String ADOPTIONS_FORMAT = createFormat(ADOPTIONS_FORMAT_LEN);
    private static final String ADOPTIONS_VERSION = "1";
    private SimplifiedFileLogger adoptionLogger;
    private Path adoptionPath;
    private boolean logAdoptions = false;

    @Override
    public void setupLogAdoptions(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log adoptions: {}", target);
            adoptionLogger = new SimplifiedFileLogger(
                    ADOPTIONS_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            ADOPTIONS_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logAdoptions = true;
            adoptionPath = target;
        } else {
            LOGGER.trace("disable log adoptions");
            adoptionLogger = null;
            logAdoptions = false;
            adoptionPath = null;
        }
    }

    @Override
    public void startLogAdoption() {
        if(logAdoptions) {
            adoptionLogger.log(ADOPTIONS_FORMAT, buildComment(ADOPTIONS_FORMAT_LEN, ""));
            adoptionLogger.log(ADOPTIONS_FORMAT, buildVersion(ADOPTIONS_FORMAT_LEN, ADOPTIONS_VERSION));
        }
    }

    @Override
    public void logAdoption(ConsumerAgent agent, Product product, AdoptionPhase phase, Timestamp stamp) {
        if(logAdoptions) {
            adoptionLogger.log(ADOPTIONS_FORMAT, agent.getName(), product.getName(), phase.name(), printStamp(stamp));
        }
    }

    //=========================
    //decision
    //=========================

    private static final int DECISION_FORMAT_LEN = 13;
    private static final String DECISION_LOGGER_NAME = "DECISION_LOGGER";
    private static final String DECISION_APPENDER_NAME = "DECISION_APPENDER";
    private static final String DECISION_FORMAT = createFormat(DECISION_FORMAT_LEN);
    private static final String DECISION_VERSION = "1";
    private SimplifiedFileLogger decisionLogger;
    private Path decisionPath;
    private boolean logDecisions = false;

    @Override
    public void setupLogDecisions(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log decision: {}", target);
            decisionLogger = new SimplifiedFileLogger(
                    DECISION_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            DECISION_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logDecisions = true;
            decisionPath = target;
        } else {
            LOGGER.trace("disable log decision");
            decisionLogger = null;
            logDecisions = false;
            decisionPath = null;
        }
    }

    @Override
    public void startLogDecision() {
        if(logDecisions) {
            decisionLogger.log(DECISION_FORMAT, buildComment(DECISION_FORMAT_LEN, ""));
            decisionLogger.log(DECISION_FORMAT, buildVersion(DECISION_FORMAT_LEN, DECISION_VERSION));
        }
    }

    @Override
    public void logDecision(
            ConsumerAgent agent, Product product,
            double a, double b, double c, double d,
            double aValue, double bValue, double cValue, double dValue,
            double t, boolean result, Timestamp stamp) {
        if(logDecisions) {
            decisionLogger.log(
                    DECISION_FORMAT,
                    agent.getName(), product.getName(),
                    a, b, c, d,
                    aValue, bValue, cValue, dValue,
                    t, result ? 1 : 0, printStamp(stamp));
        }
    }

    //=========================
    //decision
    //=========================

    private static final int FT_FORMAT_LEN = 6;
    private static final String FT_LOGGER_NAME = "FT_LOGGER";
    private static final String FT_APPENDER_NAME = "FT_APPENDER";
    private static final String FT_FORMAT = createFormat(FT_FORMAT_LEN);
    private static final String FT_VERSION = "1";
    private SimplifiedFileLogger ftLogger;
    private Path ftPath;
    private boolean logFTs = false;

    @Override
    public void setupLogFinancialThresholds(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log financial: {}", target);
            ftLogger = new SimplifiedFileLogger(
                    FT_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            FT_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logFTs = true;
            ftPath = target;
        } else {
            LOGGER.trace("disable log financial");
            ftLogger = null;
            logFTs = false;
            ftPath = null;
        }
    }

    @Override
    public void startLogFinancialThreshold() {
        if(logFTs) {
            ftLogger.log(FT_FORMAT, buildComment(FT_FORMAT_LEN, ""));
            ftLogger.log(FT_FORMAT, buildVersion(FT_FORMAT_LEN, FT_VERSION));
        }
    }

    @Override
    public void logFinancialThreshold(ConsumerAgent agent, Product product, double f, double t, boolean result, Timestamp stamp) {
        if(logFTs) {
            ftLogger.log(FT_FORMAT, agent.getName(), product.getName(), f, t, result ? 1 : 0, printStamp(stamp));
        }
    }

    //=========================
    //phase transition
    //=========================

    private static final int PHASE_TRANSITION_FORMAT_LEN = 4;
    private static final String PHASE_TRANSITION_LOGGER_NAME = "PHASE_TRANSITION_LOGGER";
    private static final String PHASE_TRANSITION_APPENDER_NAME = "PHASE_TRANSITION_APPENDER";
    private static final String PHASE_TRANSITION_FORMAT = createFormat(PHASE_TRANSITION_FORMAT_LEN);
    private static final String PHASE_TRANSITION_VERSION = "0";
    private SimplifiedFileLogger phaseTransitionLogger;
    private Path phaseTransitionPath;
    private boolean logPhaseTransition = false;

    @Override
    public void setupLogPhaseTransition(Path target, boolean enabled) throws IOException {
        if(enabled) {
            LOGGER.trace("enable log financial: {}", target);
            phaseTransitionLogger = new SimplifiedFileLogger(
                    PHASE_TRANSITION_LOGGER_NAME,
                    SimplifiedFileLogger.createNew(
                            PHASE_TRANSITION_APPENDER_NAME,
                            DEFAULT_PATTERN,
                            target
                    )
            );
            logPhaseTransition = true;
            phaseTransitionPath = target;
        } else {
            LOGGER.trace("disable log financial");
            phaseTransitionLogger = null;
            logPhaseTransition = false;
            phaseTransitionPath = null;
        }
    }

    @Override
    public void startLogPhaseTransition() {
        if(logPhaseTransition) {
            phaseTransitionLogger.log(PHASE_TRANSITION_FORMAT, buildComment(PHASE_TRANSITION_FORMAT_LEN, ""));
            phaseTransitionLogger.log(PHASE_TRANSITION_FORMAT, buildVersion(PHASE_TRANSITION_FORMAT_LEN, PHASE_TRANSITION_VERSION));
        }
    }

    @Override
    public void logPhaseTransition(ConsumerAgent agent, String from, String to, Timestamp stamp) {
        if(logPhaseTransition) {
            phaseTransitionLogger.log(PHASE_TRANSITION_FORMAT, agent.getName(), from, to, printStampMillis(stamp));
        }
    }
}
