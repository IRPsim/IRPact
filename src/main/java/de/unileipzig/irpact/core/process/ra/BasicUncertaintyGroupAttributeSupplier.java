package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyGroupAttributeSupplier extends NameableBase implements UncertaintyGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyGroupAttributeSupplier.class);

    protected Map<ConsumerAgentGroup, List<String>> attrNames;
    protected Map<ConsumerAgentGroup, List<UnivariateDoubleDistribution>> uncertDists;
    protected Map<ConsumerAgentGroup, List<UnivariateDoubleDistribution>> convDists;

    public BasicUncertaintyGroupAttributeSupplier() {
        this(null);
    }

    public BasicUncertaintyGroupAttributeSupplier(String name) {
        this(name, new LinkedHashMap<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public BasicUncertaintyGroupAttributeSupplier(
            String name,
            Map<ConsumerAgentGroup, List<String>> attrNames,
            Map<ConsumerAgentGroup, List<UnivariateDoubleDistribution>> uncertDists,
            Map<ConsumerAgentGroup, List<UnivariateDoubleDistribution>> convDists) {
        setName(name);
        this.attrNames = attrNames;
        this.uncertDists = uncertDists;
        this.convDists = convDists;
    }

    public void add(
            ConsumerAgentGroup cag,
            String attrName,
            UnivariateDoubleDistribution uncertDist,
            UnivariateDoubleDistribution convDist) {
        List<String> names = attrNames.computeIfAbsent(cag, _cag -> new ArrayList<>());
        List<UnivariateDoubleDistribution> uncerts = uncertDists.computeIfAbsent(cag, _cag -> new ArrayList<>());
        List<UnivariateDoubleDistribution> convs = convDists.computeIfAbsent(cag, _cag -> new ArrayList<>());

        names.add(attrName);
        uncerts.add(uncertDist);
        convs.add(convDist);
    }

    public Collection<ConsumerAgentGroup> getConsumerAgentGroups() {
        return attrNames.keySet();
    }

    public List<String> getAttributeNames(ConsumerAgentGroup cag) {
        return attrNames.get(cag);
    }

    public List<UnivariateDoubleDistribution> getUncertaintyeDistributions(ConsumerAgentGroup cag) {
        return uncertDists.get(cag);
    }

    public List<UnivariateDoubleDistribution> getConvergenceeDistributions(ConsumerAgentGroup cag) {
        return convDists.get(cag);
    }

    @Override
    public void applyTo(ConsumerAgentGroup cag) {
        List<String> names = attrNames.computeIfAbsent(cag, _cag -> new ArrayList<>());
        List<UnivariateDoubleDistribution> uncerts = uncertDists.computeIfAbsent(cag, _cag -> new ArrayList<>());
        List<UnivariateDoubleDistribution> convs = convDists.computeIfAbsent(cag, _cag -> new ArrayList<>());

        for(int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            String uncertName = RAConstants.getUncertaintyAttributeName(name);
            if(cag.hasGroupAttribute(uncertName)) {
                LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "cag '{}' already has '{}'", cag.getName(), uncertName);
                continue;
            }

            UnivariateDoubleDistribution uncert = uncerts.get(i);
            UnivariateDoubleDistribution conv = convs.get(i);

            UncertaintyGroupAttribute cagAttr = new UncertaintyGroupAttribute();
            cagAttr.setName(uncertName);
            cagAttr.setUncertainty(uncert);
            cagAttr.setConvergence(conv);
            cagAttr.setAutoAdjustment(false);

            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add uncertainty '{}={},{}' to '{}->{}'", cagAttr.getName(), uncert.getName(), conv.getName(), cag.getName(), name);
            cag.addGroupAttribute(cagAttr);
        }
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                IsEquals.getMapCollHashCodeWithMappedKey(attrNames, Nameable::getName),
                IsEquals.getMapCollHashCodeWithMappedKey(uncertDists, Nameable::getName),
                IsEquals.getMapCollHashCodeWithMappedKey(convDists, Nameable::getName)
        );
    }
}
