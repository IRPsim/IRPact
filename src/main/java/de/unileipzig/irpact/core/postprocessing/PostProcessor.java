package de.unileipzig.irpact.core.postprocessing;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.FallbackAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public abstract class PostProcessor implements LoggingHelper {

    protected final Map<InRealAdoptionDataFile, RealAdoptionData> adoptionDataCache = new HashMap<>();
    protected static final FallbackAdoptionData PLACEHOLDER_REAL_DATA = new FallbackAdoptionData(0);

    protected MetaData metaData;
    protected MainCommandLineOptions clOptions;
    protected InRoot inRoot;
    protected SimulationEnvironment environment;

    public PostProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        this.metaData = metaData;
        this.clOptions = clOptions;
        this.inRoot = inRoot;
        this.environment = environment;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    public abstract void execute();

    protected Settings getSettings() {
        return environment.getSettings();
    }

    public Path getTargetDir() throws IOException {
        return clOptions.getCreatedDownloadDir();
    }

    protected List<Integer> years;
    public List<Integer> getAllSimulationYears() {
        if(years == null) {
            int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
            int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
            years = IntStream.rangeClosed(firstYear, lastYear)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return years;
    }

    protected double getLargestInterestThreshold(Product product) {
        double threshold = Double.NaN;
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                ProductInterest interest = ca.getProductInterest();
                if(interest.hasThreshold(product.getGroup())) {
                    if(Double.isNaN(threshold) || threshold < interest.getThreshold(product.getGroup())) {
                        threshold = interest.getThreshold(product.getGroup());
                    }
                }
            }
        }
        return threshold;
    }

    protected Map<Product, List<Double>> allInterestValues = new HashMap<>();
    public List<Double> getInterestValues(Product product) {
        List<Double> values = allInterestValues.get(product);
        if(values == null) {
            values = new ArrayList<>();
            double largestThreshold = getLargestInterestThreshold(product);
            if(Double.isNaN(largestThreshold)) {
                throw new IllegalArgumentException("missing threshold for produkt '" + product.getName() + "'");
            }
            for(double t = 0.0; t <= largestThreshold; t++) {
                values.add(t);
            }
            allInterestValues.put(product, values);
        }
        return values;
    }

    protected List<String> zips;
    public List<String> getAllZips(String key) {
        if(zips == null) {
            zips = environment.getAgents().streamConsumerAgents()
                    .filter(agent -> agent.hasAnyAttribute(key))
                    .map(agent -> {
                        Attribute attr = agent.findAttribute(key);
                        return attr.asValueAttribute().getValueAsString();
                    })
                    .distinct()
                    .collect(Collectors.toList());
        }
        return zips;
    }

    protected List<Product> products;
    public List<Product> getAllProducts() {
        if(products == null) {
            Set<Product> set = new HashSet<>();
            for(ProductGroup pg: environment.getProducts().getGroups()) {
                set.addAll(pg.getProducts());
            }
            products = new ArrayList<>(set);
        }
        return products;
    }

    protected ObjectNode tryLoadYaml(String baseName, String extension) throws IOException {
        ObjectNode root = tryLoadExternalYaml(baseName, extension);
        if(root != null) return root;
        return tryLoadInternalYaml(baseName, extension);
    }

    protected ObjectNode tryLoadExternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedExternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedExternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedExternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadInternalYaml(String baseName, String extension) throws IOException {
        if(metaData.getLoader().hasLocalizedInternal(baseName, metaData.getLocale(), extension)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedInternal(baseName, metaData.getLocale(), extension));
            InputStream in = metaData.getLoader().getLocalizedInternalAsStream(baseName, metaData.getLocale(), extension);
            return tryLoadYamlAndCloseStream(in);
        } else {
            return null;
        }
    }

    protected ObjectNode tryLoadYamlAndCloseStream(InputStream in) throws IOException {
        if(in == null) {
            return null;
        }
        try {
            return JsonUtil.read(in, JsonUtil.YAML);
        } finally {
            in.close();
        }
    }

    protected RealAdoptionData getFallbackAdoptionData() {
        return PLACEHOLDER_REAL_DATA;
    }

    public RealAdoptionData getRealAdoptionData(InRealAdoptionDataFile file) {
        if(adoptionDataCache.containsKey(file)) {
            return adoptionDataCache.get(file);
        } else {
            try {
                getDefaultLogger().warn("try loading '{}'", file.getFileNameWithoutExtension());
                RealAdoptionData adoptionData = file.parse(environment.getResourceLoader());
                adoptionDataCache.put(file, adoptionData);
                return adoptionData;
            } catch (Throwable t) {
                getDefaultLogger().warn("loading '{}' failed, use fallback data, cause: {}", file.getFileNameWithoutExtension(), StringUtil.printStackTrace(t));
                RealAdoptionData adoptionData = getFallbackAdoptionData();
                adoptionDataCache.put(file, adoptionData);
                return adoptionData;
            }
        }
    }

    protected String getZIP(ConsumerAgent agent, String key) {
        return agent.findAttribute(key).asValueAttribute().getStringValue();
    }
}
