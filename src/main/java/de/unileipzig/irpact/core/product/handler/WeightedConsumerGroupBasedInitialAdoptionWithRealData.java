package de.unileipzig.irpact.core.product.handler;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.commons.util.data.weighted.NavigableMapWeightedMapping;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class WeightedConsumerGroupBasedInitialAdoptionWithRealData extends NameableBase implements NewProductHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WeightedConsumerGroupBasedInitialAdoptionWithRealData.class);

    protected String zipAttributeName; //zip
    protected String validationAttributeName; //hh
    protected String shareAttributeName; //adopter
    protected RealAdoptionData adoptionData;
    protected Rnd rnd;
    protected boolean scale = false;
    protected boolean fixError = false;

    public WeightedConsumerGroupBasedInitialAdoptionWithRealData() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    public void setAdoptionData(RealAdoptionData adoptionData) {
        this.adoptionData = adoptionData;
    }

    public RealAdoptionData getAdoptionData() {
        return adoptionData;
    }

    public void setZipAttributeName(String zipAttributeName) {
        this.zipAttributeName = zipAttributeName;
    }

    public String getZipAttributeName() {
        return zipAttributeName;
    }

    public void setValidationAttributeName(String validationAttributeName) {
        this.validationAttributeName = validationAttributeName;
    }

    public String getValidationAttributeName() {
        return validationAttributeName;
    }

    public void setShareAttributeName(String shareAttributeName) {
        this.shareAttributeName = shareAttributeName;
    }

    public String getShareAttributeName() {
        return shareAttributeName;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void setScale(boolean scale) {
        this.scale = scale;
    }

    public boolean isScale() {
        return scale;
    }

    public void setFixError(boolean fixError) {
        this.fixError = fixError;
    }

    public boolean isFixError() {
        return fixError;
    }

    protected void analyseZips(Set<String> zips) {
        List<String> validZips = new ArrayList<>();
        List<String> invalidZips = new ArrayList<>();
        List<String> unusedZips = new ArrayList<>();
        adoptionData.getValidZips(zips, validZips);
        adoptionData.getInvalidZips(zips, invalidZips);
        adoptionData.getUnusedZips(zips, unusedZips);
        trace("valid zips: {}", validZips);
        trace("invalid zips: {}", invalidZips);
        trace("unused zips: {}", unusedZips);
    }

    protected double getScaleFactor(SimulationEnvironment environment) {
        return environment.getAgents().getInitialAgentPopulation().hasScale()
                ? environment.getAgents().getInitialAgentPopulation().getScale()
                : 1.0;
    }

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        int initialAdoptionYear = getInitialAdopterYear(environment);
        Set<String> zips = getAllZIPs(environment);

        analyseZips(zips);

        trace("zipAttr={}, validationAttr={}, shareAttr={}", zipAttributeName, validationAttributeName, shareAttributeName);

        int unscaledTotalNumberOfRealAdoptions = 0;
        int totalNumberOfRealAdoptions = 0;
        int totalNumberOfSimulationAdoptions = 0;

        double scaleFactor = scale ?
                getScaleFactor(environment)
                : 1.0;
        trace("scale={}, scaleFactor={}", scale, scaleFactor);

        Map3<String, ConsumerAgentGroup, List<ConsumerAgent>> validAgentsMapping = Map3.newHashMap();
        for(String zip: zips) {
            int numberOfRealAdoptions = adoptionData.getCumulated(initialAdoptionYear, zip);
            if(numberOfRealAdoptions > 0) {
                unscaledTotalNumberOfRealAdoptions += numberOfRealAdoptions;
            }

            getShuffledValidAgents(environment, zip, validAgentsMapping);

            int scaledNumberOfRealAdoptions = (int) (scaleFactor * numberOfRealAdoptions);
            if(scaledNumberOfRealAdoptions < 1) {
                trace("skip '{}', number of adoptions in year {}: {} (unscaled={})", zip, initialAdoptionYear, scaledNumberOfRealAdoptions, numberOfRealAdoptions);
                continue;
            }

            trace("'{}' number of adoptions in year {}: {} (unscaled={})", zip, initialAdoptionYear, scaledNumberOfRealAdoptions, numberOfRealAdoptions);
            int numberOfSimulationAdoptions = 0;

            for(Map.Entry<ConsumerAgentGroup, List<ConsumerAgent>> entry: validAgentsMapping.entrySet(zip)) {
                trace("cag '{}' valid agents: {}", entry.getKey().getName(), entry.getValue().size());
            }

            Map<ConsumerAgentGroup, Integer> initialAdopterCount = new HashMap<>();
            WeightedMapping<ConsumerAgentGroup> cagsShare = buildWeightedMapping(environment, product, validAgentsMapping.keySet(zip));

            for(int i = 0; i < scaledNumberOfRealAdoptions; i++) {
                if(validAgentsMapping.isEmpty(zip)) {
                    warn("zip '{}' - not enough agents, real={} != simulation={}", zip, scaledNumberOfRealAdoptions, numberOfSimulationAdoptions);
                    break;
                }

                ConsumerAgentGroup cag = cagsShare.getWeightedRandom(rnd);
                List<ConsumerAgent> validAgents = validAgentsMapping.get(zip, cag);
                if(validAgents != null && validAgents.size() > 0) {
                    ConsumerAgent ca = validAgents.remove(validAgents.size() - 1);
                    trace("set initial adopter: agent={} product={}", ca.getName(), product.getName());
                    ca.adoptInitial(product);
                    initialAdopterCount.put(cag, initialAdopterCount.computeIfAbsent(cag, _cag -> 0) + 1);
                    numberOfSimulationAdoptions++;
                }

                if(validAgents == null || validAgents.isEmpty()) {
                    validAgentsMapping.remove(zip, cag);
                    if(validAgentsMapping.isEmpty(zip)) {
                        validAgentsMapping.remove(zip);
                    }
                    cagsShare = cagsShare.copyWithout(cag);
                }
            }


            trace("'{}' number of initial adoptions: real={}, simularion={} ({})", zip, scaledNumberOfRealAdoptions, numberOfSimulationAdoptions, initialAdopterCount.values().stream().mapToInt(x -> x).sum());
            for(Map.Entry<ConsumerAgentGroup, Integer> entry: initialAdopterCount.entrySet()) {
                trace("cag '{}': {}", entry.getKey().getName(), entry.getValue());
            }

            totalNumberOfRealAdoptions += scaledNumberOfRealAdoptions;
            totalNumberOfSimulationAdoptions += numberOfSimulationAdoptions;
        }

        trace("total number of initial adoptions: real={}, simulation={}", totalNumberOfRealAdoptions, totalNumberOfSimulationAdoptions);

        int realScaledNumberOfRealAdoptions = (int) (scaleFactor * unscaledTotalNumberOfRealAdoptions);
        boolean doFixError = fixError && totalNumberOfSimulationAdoptions < realScaledNumberOfRealAdoptions;
        trace("fix error: {} (flag={} && {} < {})", doFixError, fixError, totalNumberOfSimulationAdoptions, realScaledNumberOfRealAdoptions);
        if(doFixError) {
            fixError(
                    environment, product,
                    initialAdoptionYear,
                    realScaledNumberOfRealAdoptions,
                    totalNumberOfSimulationAdoptions,
                    validAgentsMapping
            );
        }
    }

    protected void fixError(
            SimulationEnvironment environment, Product product,
            int initialAdoptionYear,
            int totalNumberOfRealAdoptions,
            int totalNumberOfSimulationAdoptions,
            Map3<String, ConsumerAgentGroup, List<ConsumerAgent>> validAgentsMapping) {
        int remaining = totalNumberOfRealAdoptions - totalNumberOfSimulationAdoptions;
        trace("fix error, remaining: {}", remaining);
        WeightedMapping<String> zipMapping = buildWeightedZipMapping(initialAdoptionYear, validAgentsMapping.keySet());
        for(int i = 0; i < remaining; i++) {
            if(zipMapping.isEmpty()) {
                warn("not enough agents, missing: {}", remaining - 1 - i);
                return;
            }

            String zip = zipMapping.getWeightedRandom(rnd);
            WeightedMapping<ConsumerAgentGroup> cagsShare = buildWeightedMapping(environment, product, validAgentsMapping.keySet(zip));
            ConsumerAgentGroup cag = cagsShare.getWeightedRandom(rnd);
            List<ConsumerAgent> validAgents = validAgentsMapping.get(zip, cag);
            ConsumerAgent ca = validAgents.remove(validAgents.size() - 1);
            trace("set initial adopter: agent={} product={} (i={})", ca.getName(), product.getName(), i);
            ca.adoptInitial(product);
            if(validAgents.isEmpty()) {
                validAgentsMapping.remove(zip, cag);
                if(validAgentsMapping.isEmpty(zip)) {
                    trace("remove empty zip: {}", zip);
                    validAgentsMapping.remove(zip);
                    zipMapping = buildWeightedZipMapping(initialAdoptionYear, validAgentsMapping.keySet());
                }
            }
        }
    }

    protected WeightedMapping<String> buildWeightedZipMapping(
            int initialAdoptionYear,
            Set<String> zips) {
        NavigableMapWeightedMapping<String> mapping = new NavigableMapWeightedMapping<>();
        for(String zip: zips) {
                int numberOfRealAdoptions = adoptionData.getCumulated(initialAdoptionYear, zip);
                if(numberOfRealAdoptions > 0) {
                    mapping.set(zip, numberOfRealAdoptions);
                }
        }
        return mapping;
    }

    protected WeightedMapping<ConsumerAgentGroup> buildWeightedMapping(SimulationEnvironment environment, Product product, Collection<ConsumerAgentGroup> validCags) {
        NavigableMapWeightedMapping<ConsumerAgentGroup> mapping = new NavigableMapWeightedMapping<>();

        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            if(!validCags.contains(cag)) {
                trace("skip '{}', no valid agents", cag.getName());
                continue;
            }
            if(cag.getNumberOfAgents() == 0) {
                trace("skip '{}', number of agents: 0", cag.getName());
                continue;
            }
            double share = getShare(cag, product);
            trace("cag '{}' share: {}", cag.getName(), share);
            mapping.set(cag, share);
        }

        return mapping;
    }

    protected void getShuffledValidAgents(
            SimulationEnvironment environment,
            String zip,
            Map3<String, ConsumerAgentGroup, List<ConsumerAgent>> validAgentsMapping) {
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            List<ConsumerAgent> validAgents = getValidConsumerAgents(cag, zip);
            if(validAgents.isEmpty()) {
                trace("skip '{}', number of valid agents: 0", cag.getName());
                continue;
            } else {
                trace("'{}' number of valid agents: {}", cag.getName(), validAgents.size());
            }
            rnd.shuffle(validAgents);
            validAgentsMapping.put(zip, cag, validAgents);
        }
    }

    protected int getStartYear(SimulationEnvironment environment) {
        return environment.getTimeModel().getFirstSimulationYear();
    }

    protected int getInitialAdopterYear(SimulationEnvironment environment) {
        return getStartYear(environment) - 1;
    }

    protected Set<String> getAllZIPs(SimulationEnvironment environment) {
        Set<String> out = new LinkedHashSet<>();
        for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
            for(ConsumerAgent ca: cag.getAgents()) {
                Attribute attribute = ca.findAttribute(zipAttributeName);
                String value = attribute.asValueAttribute().getStringValue();
                out.add(value);
            }
        }
        return out;
    }

    protected List<ConsumerAgent> getValidConsumerAgents(ConsumerAgentGroup cag, String zip) {
        List<ConsumerAgent> agents = new ArrayList<>();
        for(ConsumerAgent ca: cag.getAgents()) {
            String agentZIP = getZIP(ca);
            if(zip.equals(agentZIP)) {
                Attribute validationAttribute = ca.findAttribute(validationAttributeName);
                if(validationAttribute.asValueAttribute().getBooleanValue()) {
                    //trace("add valid agent '{}' ({})", ca.getName(), agentZIP);
                    agents.add(ca);
                }
            }
        }
        return agents;
    }

    protected String getZIP(ConsumerAgent ca) {
        Attribute keyAttribute = ca.findAttribute(zipAttributeName);
        if(keyAttribute == null) {
            return null;
        } else {
            return keyAttribute.asValueAttribute().getStringValue();
        }
    }

    protected double getShare(ConsumerAgent ca, Product product) {
        return ca.getProductRelatedAttribute(shareAttributeName)
                .getAttribute(product)
                .asValueAttribute()
                .getDoubleValue();
    }

    protected double getShare(ConsumerAgentGroup cag, Product product) {
        return getShare(CollectionUtil.getFirst(cag.getAgents()), product);
    }
}
