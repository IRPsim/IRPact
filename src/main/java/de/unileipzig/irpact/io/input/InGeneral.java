package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class InGeneral {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Logging")
                .setEdnDescription("Einstellungen für das Logging und die Ausgabe von Informationen und Daten. Unabhängig von den individuellen Logging-Einstellungen werden wichtige Informationen weiterhin geloggt. Diese können nur mittels dem Logging-Level deaktiviert werden (nicht empfohlen).")
                .putCache("Logging");

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
                .setGamsDescription("Ob die Erstellung der Simulation (Agentenplatform) geloggt werden soll.")
                .setGamsIdentifier("log Platformerstellung")
                .setGamsDomain("[0|1]")
                .store(InGeneral.class, "logPlatformCreation");
        res.putPath(
                InGeneral.class, "logPlatformCreation",
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
    }

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
    public boolean logParamInit;

    @FieldDefinition
    public boolean logGraphCreation;

    @FieldDefinition
    public boolean logAgentCreation;

    @FieldDefinition
    public boolean logPlatformCreation;

    @FieldDefinition
    public boolean logTools;

    public InGeneral() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InGeneral)) return false;
        InGeneral general = (InGeneral) o;
        return seed == general.seed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed);
    }

    @Override
    public String toString() {
        return "InGeneral{" +
                "seed=" + seed +
                '}';
    }
}
