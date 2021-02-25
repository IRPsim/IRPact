package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.SectionLoggingFilter;
import de.unileipzig.irpact.core.simulation.BasicInitializationData;
import de.unileipzig.irpact.jadex.simulation.BasicJadexLifeCycleControl;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class InGeneral {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InGeneral.class,
                res.getCachedElement("Allgemeine Einstellungen")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Zufallsgenerator (seed)")
                .setGamsDescription("Setzt den Seed für den Zufallsgenerator der Simulation. Falls er auf -1 gesetzt wird, wird ein zufälliger Seed generiert.")
                .setGamsDefault("-1")
                .store(InGeneral.class, "seed");
        res.newEntryBuilder()
                .setGamsIdentifier("Timeout")
                .setGamsDescription("Setzt den Timeout der Simulation in Millisekunden. Diese Einstellung dient dazu die Simulation zu beenden, falls sie unerwartet abstürzt. Im Laufe der Simulation wird der Timeout unentwegt zurück gesetzt. Sollte es zu einem unerwarteten Fehler kommen und die Rücksetzung ausbleiben, wird die Simulation abgebrochen und beendet. Werte kleiner 1 deaktivieren den Timeout vollständig.")
                .setGamsDefault(Long.toString(TimeUnit.MINUTES.toMinutes(5)))
                .setGamsUnit("[ms]")
                .store(InGeneral.class, "timeout");
        res.newEntryBuilder()
                .setGamsIdentifier("Endjahr der Simulation")
                .setGamsDescription("[TEMPORÄRE OPTION] Setzt das finale Jahr der Simulation. Wichtig: Der Wert ist inklusiv. Es wird dabei immer mindestens ein Jahr simuliert, auch wenn der Wert kleiner ist als das des Ausgangsjahres.")
                .store(InGeneral.class, "endYear");

        res.newEntryBuilder()
                .setGamsIdentifier("optact-Testmodell ausführen")
                .setGamsDescription("[TEMPORÄRE OPTION] Falls gesetzt, wird die optact-Demo gestartet. Anderenfalls wird IRPact gestartet.")
                .store(InGeneral.class, "runOptActDemo");

        res.newEntryBuilder()
                .setGamsDescription("Setzt das zu nutzende Logging-Level in IRPact, folgende Werte werden unterstützt: 0 = OFF, 1 = TRACE, 2 = DEBUG, 3 = INFO, 4 = WARN, 5 = ERROR, 6 = ALL. Das Level ist der Hauptfilter für alle log-Operationen.")
                .setGamsIdentifier("Logging-Level")
                .setGamsDomain(IRPLevel.getDomain())
                .setGamsDefault(IRPLevel.getDefaultAsString())
                .store(InGeneral.class, "logLevel");
        res.putPath(
                InGeneral.class, "logLevel",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob alles geloggt werden soll. Falls ja, überschreibt diese Option alles.")
                .setGamsIdentifier("log alles")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logAll");
        res.putPath(
                InGeneral.class, "logAll",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob Aufrufe der Tools-Bibliothek geloggt werden sollen.")
                .setGamsIdentifier("log Tools-Aufrufe")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logTools");
        res.putPath(
                InGeneral.class, "logTools",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );


        res.newEntryBuilder()
                .setGamsDescription("Ob die Initialisierung der Parameter geloggt werden soll.")
                .setGamsIdentifier("log Initialisierung der Parameter")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logParamInit");
        res.putPath(
                InGeneral.class, "logParamInit",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob die Grapherzeugung geloggt werden soll.")
                .setGamsIdentifier("log Grapherzeugung")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logGraphCreation");
        res.putPath(
                InGeneral.class, "logGraphCreation",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob die Agentenerzeugung geloggt werden soll.")
                .setGamsIdentifier("log Agentenerzeugung")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logAgentCreation");
        res.putPath(
                InGeneral.class, "logAgentCreation",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob die Erstellung der Simulation (Agentenplatform) geloggt werden soll.")
                .setGamsIdentifier("log Platformerstellung")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logPlatformCreation");
        res.putPath(
                InGeneral.class, "logPlatformCreation",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob der Lebenszyklus geloggt werden soll.")
                .setGamsIdentifier("log Simulationszyklen")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logSimulationLifecycle");
        res.putPath(
                InGeneral.class, "logSimulationLifecycle",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob die Agenten während der Simulation geloggt werden sollen.")
                .setGamsIdentifier("log Agenten während Simulation")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logSimulationAgent");
        res.putPath(
                InGeneral.class, "logSimulationAgent",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );

        res.newEntryBuilder()
                .setGamsDescription("[SPAM] Ob spezielle Jadex-Ausgaben geloggt werden sollen. (Anmerkung: Diese Ausgaben von Jadex selbst generiert.)")
                .setGamsIdentifier("log Jadex Systemnachrichten")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logJadexSystemOut");
        res.putPath(
                InGeneral.class, "logJadexSystemOut",
                res.getCachedElements("Allgemeine Einstellungen", "Logging")
        );
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InGeneral.class);

    public static final String RUN_OPTACT_DEMO_PARAM_NAME = "sca_InGeneral_runOptActDemo";

    @FieldDefinition
    public long seed;

    @FieldDefinition
    public long timeout;

    //fuer den anderen Spec
    public int startYear = -1;

    @FieldDefinition
    public int endYear;

    @FieldDefinition
    public boolean runOptActDemo;

    @FieldDefinition
    public int logLevel;

    @FieldDefinition
    public boolean logAll;

    @FieldDefinition
    public boolean logTools;

    @FieldDefinition
    public boolean logParamInit;
    @FieldDefinition
    public boolean logGraphCreation;
    @FieldDefinition
    public boolean logAgentCreation;
    @FieldDefinition
    public boolean logPlatformCreation;

    @FieldDefinition
    public boolean logSimulationLifecycle;
    @FieldDefinition
    public boolean logSimulationAgent;

    @FieldDefinition
    public boolean logJadexSystemOut;

    public InGeneral() {
    }

    public void setup(InputParser parser) throws ParsingException {
        parseLoggingSetup(parser);
        parseSeed(parser);
        parseLifeCycleControl(parser);
    }

    private void parseLoggingSetup(@SuppressWarnings("unused") InputParser parser) {
        IRPLevel level = IRPLevel.get(logLevel);
        if(level == null) {
            LOGGER.warn("invalid log level {}, set level to default ({}) ", logLevel, IRPLevel.getDefault());
            level = IRPLevel.getDefault();
        }
        IRPLogging.setLevel(level);

        SectionLoggingFilter filter = new SectionLoggingFilter();
        IRPLogging.setFilter(filter);

        if(logAll) {
            IRPSection.addAllTo(filter);
            return;
        }

        if(logTools) filter.add(IRPSection.TOOLS);

        if(logParamInit) filter.add(IRPSection.INITIALIZATION_PARAMETER);
        if(logAgentCreation) filter.add(IRPSection.INITIALIZATION_AGENT);
        if(logGraphCreation) filter.add(IRPSection.INITIALIZATION_NETWORK);
        if(logPlatformCreation) filter.add(IRPSection.INITIALIZATION_PLATFORM);

        if(logSimulationLifecycle) filter.add(IRPSection.SIMULATION_LICECYCLE);
        if(logSimulationAgent) filter.add(IRPSection.SIMULATION_AGENT);

        if(logJadexSystemOut) filter.add(IRPSection.JADEX_SYSTEM_OUT);
    }

    private void parseSeed(InputParser parser) {
        BasicJadexSimulationEnvironment env = (BasicJadexSimulationEnvironment) parser.getEnvironment();
        final Rnd rnd;
        if(seed == -1L) {
            rnd = new Rnd();
            LOGGER.info("use random master seed: {}", rnd.getInitialSeed());
        } else {
            LOGGER.info("use master seed: {}", seed);
            rnd = new Rnd(seed);
        }
        env.setSimulationRandom(rnd);
    }

    private void parseLifeCycleControl(InputParser parser) {
        BasicJadexLifeCycleControl lifeCycleControl = (BasicJadexLifeCycleControl) parser.getEnvironment().getLiveCycleControl();
        BasicInitializationData initData = (BasicInitializationData) parser.getEnvironment().getInitializationData();
        if(timeout < 1L) {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout disabled");
        } else {
            LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "timeout: {}", timeout);
        }
        lifeCycleControl.setKillSwitchTimeout(timeout);

        initData.setStartYear(startYear);
        initData.setEndYear(endYear);
        LOGGER.info(IRPSection.INITIALIZATION_PARAMETER, "custom endyear: {}", endYear);
    }
}
