package de.unileipzig.irpact.core.product.initial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image.RealAdoptionData;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class ConsumerGroupBasedInitialAdoptionWithRealData extends NameableBase implements NewProductHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConsumerGroupBasedInitialAdoptionWithRealData.class);

    protected String zipAttributeName; //zip
    protected String validationAttributeName; //hh
    protected String shareAttributeName; //adopter
    protected RealAdoptionData adoptionData;
    protected Rnd rnd;

    public ConsumerGroupBasedInitialAdoptionWithRealData() {
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

    @Override
    public void handleProduct(SimulationEnvironment environment, Product product) {
        int startYear = getStartYear(environment);
        int initialAdoptionYear = startYear - 1;
        Set<String> zips = getAllZIPs(environment);

        for(String zip: zips) {
            int numberOfAdoptions = adoptionData.get(initialAdoptionYear, zip);
            if(numberOfAdoptions < 1) {
                trace("skip '{}', number of adoptions: {}", zip, numberOfAdoptions);
            }

            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                if(cag.getNumberOfAgents() == 0) {
                    trace("skip '{}', number of agents ({}): 0", zip, cag.getName());
                }
                double share = getShare(cag);
                List<ConsumerAgent> validAgents = getValidConsumerAgents(cag, zip);
                int initialAdopterCount = (int) (validAgents.size() * share);
                trace("handle zip={}, cag={}, share={}, validAgents={}, initialAdopter={}", zip, cag.getName(), share, validAgents.size(), initialAdopterCount);
                List<ConsumerAgent> initialAdopters = rnd.drawRandom(validAgents, initialAdopterCount);
                for(ConsumerAgent ca: initialAdopters) {
                    trace("set initial adopter: agent={} product={}", ca.getName(), product.getName());
                    ca.adoptInitial(product);
                }
            }
        }
    }

    protected int getStartYear(SimulationEnvironment environment) {
        return environment.getTimeModel().getFirstSimulationYear();
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
                    trace("add valid agent '{}' ({})", ca.getName(), agentZIP);
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

    protected double getShare(ConsumerAgent ca) {
        return ca.findAttribute(shareAttributeName)
                .asValueAttribute()
                .getDoubleValue();
    }

    protected double getShare(ConsumerAgentGroup cag) {
        return getShare(CollectionUtil.getFirst(cag.getAgents()));
    }
}
