package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.grouping.Grouping2;
import de.unileipzig.irpact.commons.util.data.grouping.Grouping3;
import de.unileipzig.irpact.commons.util.data.grouping.GroupingUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.InfoTag;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class PVactResultLogging {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PVactResultLogging.class);

    protected final Function<? super List<AdoptionResult>, ? extends String> sumAdoptions = list -> {
        if(list == null) {
            return "0";
        }
        int sum = 0;
        for(AdoptionResult r: list) {
            if(r.getProduct().getProduct() == getProduct()) {
                sum++;
            }
        }
        return Integer.toString(sum);
    };

    protected MainCommandLineOptions clOptions;
    protected SimulationEnvironment environment;
    protected boolean useCagNameForMilieu;

    public PVactResultLogging(
            MainCommandLineOptions clOptions,
            SimulationEnvironment environment,
            boolean useCagNameForMilieu) {
        this.clOptions = clOptions;
        this.environment = environment;
        this.useCagNameForMilieu = useCagNameForMilieu;
    }

    public void execute() {
        Settings settings = environment.getSettings();

        LOGGER.trace("isLogResultGroupedByZip: {}", settings.isLogResultGroupedByZip());
        LOGGER.trace("isLogResultGroupedByMilieu: {}", settings.isLogResultGroupedByMilieu());
        LOGGER.trace("isLogResultGroupedByZipAndMilieu: {}", settings.isLogResultGroupedByZipAndMilieu());
        LOGGER.trace("isLogProductAdoptions: {}", settings.isLogProductAdoptions());

        if(settings.isLogResultGroupedByZip()) {
            logResultGroupedByZip();
        }

        if(settings.isLogResultGroupedByMilieu()) {
            logResultGroupedByMilieu();
        }

        if(settings.isLogResultGroupedByZipAndMilieu()) {
            logResultGroupedByZipAndMilieu();
        }

        if(settings.isLogProductAdoptions()) {
            logProductAdoptions();
        }
    }

    protected void logResultGroupedByZip() {
        logResultGroupedBy(InfoTag.RESULT + " grouped by zip:", RAConstants.ZIP);
    }

    protected void logResultGroupedByMilieu() {
        logResultGroupedBy(InfoTag.RESULT + " grouped by milieu:", RAConstants.DOM_MILIEU);
    }

    protected void logResultGroupedByZipAndMilieu() {
        logResultGroupedBy(InfoTag.RESULT + " grouped by zip and milieu:", RAConstants.ZIP, RAConstants.DOM_MILIEU);
    }

    protected void logProductAdoptions() {
        printResult(InfoTag.RESULT + " all product adoptions:");
        MutableDouble count = MutableDouble.zero();
        streamResults().forEach(r -> {
            String text = r.getAgent().getName() + "(ID:" + getId(r.getAgent()) + ") : "
                    + " Product = " + r.getProduct().getProduct().getName()
                    + " adopted @ " + getTimestamp(r);
            printResult(text);
            count.inc();
        });
        printResult("total results: " + count.get());
    }

    protected void logResultGroupedBy(String description, String key) {
        Grouping2<Number, String, AdoptionResult> g = groupAdoptions(key);

        streamInitialResults().forEach(r -> {
            for(int year: getYears()) {
                g.add(year, r);
            }
        });

        if(g.isEmpty()) {
            printResult(description, "EMPTY");
            return;
        }

        List<Number> years = environment.getSettings().listYears();
        List<String> zips = g.listSecondComponents();

        String[][] rawData = GroupingUtil.toRawTable(
                g.getGrouping(),
                years,
                zips,
                sumAdoptions
        );

        String output = GroupingUtil.print(
                years,
                zips,
                rawData,
                ";"
        );

        printResult(
                description,
                output
        );
    }

    @SuppressWarnings("SameParameterValue")
    protected void logResultGroupedBy(String description, String key1, String key2) {
        Grouping3<Number, String, String, AdoptionResult> g = groupAdoptions(key1, key2);

        streamInitialResults().forEach(r -> {
            for(int year: getYears()) {
                g.add(year, r);
            }
        });

        if(g.isEmpty()) {
            printResult(description, "EMPTY");
            return;
        }

        List<Number> years = environment.getSettings().listYears();
        List<String> k1 = g.listSecondComponents();
        List<String> k2 = g.listThirdComponents();

        for(Number year: years) {
            String[][] rawData = GroupingUtil.toRawTable(
                    g.get(year),
                    k1,
                    k2,
                    sumAdoptions
            );

            String output = GroupingUtil.print(
                    k1,
                    k2,
                    rawData,
                    ";"
            );

            printResult(
                    "(" + year + ") " + description,
                    output
            );
        }
    }

    protected int[] getYears() {
        return environment.getSettings().getSimulationYears();
    }

    protected Product getProduct() {
        Optional<Product> product = environment.getProducts()
                .streamProducts()
                .findAny();
        if(product.isPresent()) {
            return product.get();
        } else {
            throw new IllegalStateException("PVact product not found");
        }
    }

    protected void printResult(
            String description,
            String text) {
        IRPLogging.getResultLogger().info(description);
        IRPLogging.getResultLogger().info(text);
    }

    protected void printResult(String text) {
        IRPLogging.getResultLogger().info(text);
    }

    protected static int getId(ConsumerAgent agent) {
        return agent.findAttribute(RAConstants.ID).asValueAttribute().getIntValue();
    }

    protected static String getTimestamp(AdoptionResult result) {
        return result.getProduct().isInitial()
                ? "initial"
                : result.getProduct().getTimestamp().printPretty();
    }

    protected static Function<? super ConsumerAgent, ? extends Stream<AdoptionResult>> agentsToAdoptionResult() {
        return ca -> ca.getAdoptedProducts()
                        .stream()
                        .map(product -> new AdoptionResult(ca, product));
    }

    protected static Stream<AdoptionResult> streamResults(SimulationEnvironment environment) {
        return environment.getAgents()
                .streamConsumerAgents()
                .flatMap(agentsToAdoptionResult());
    }

    protected Stream<AdoptionResult> streamResults() {
        return streamResults(environment);
    }

    protected Stream<AdoptionResult> streamInitialResults() {
        return streamResults().filter(r -> r.getProduct().isInitial());
    }

    protected Stream<AdoptionResult> streamNonInitialResults() {
        return streamResults().filter(r -> r.getProduct().isNotInitial());
    }

    protected Grouping3<Number, String, String, AdoptionResult> groupAdoptions(String key1, String key2) {
        Grouping3<Number, String, String, AdoptionResult> grouping = new Grouping3<>(
                r -> r.getProduct().getYear(),
                r -> getData(r, key1),
                r -> getData(r, key2)
        );

        streamNonInitialResults().forEach(grouping::add);

        return grouping;
    }

    protected Grouping2<Number, String, AdoptionResult> groupAdoptions(String key) {
        Grouping2<Number, String, AdoptionResult> grouping = new Grouping2<>(
                r -> r.getProduct().getYear(),
                r -> getData(r, key)
        );

        streamNonInitialResults().forEach(grouping::add);

        return grouping;
    }

    protected String getData(AdoptionResult r, String key) {
        if(useCagNameForMilieu && Objects.equals(key, RAConstants.DOM_MILIEU)) {
            return r.getAgent().getGroup().getName();
        } else {
            return r.getAgent().findAttribute(key).asValueAttribute().getValueAsString();
        }
    }
}
