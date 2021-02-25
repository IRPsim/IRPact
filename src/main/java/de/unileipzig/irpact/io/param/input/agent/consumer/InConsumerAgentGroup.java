package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwarenessSupplyScheme;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.awareness.InProductAwarenessSupplyScheme;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConsumerAgentGroup implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConsumerAgentGroup.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen")
        );

        res.putPath(
                InConsumerAgentGroup.class, "cagAttributes",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Attribut-Mapping")
        );

        res.putPath(
                InConsumerAgentGroup.class, "cagAwareness",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Awareness-Mapping")
        );

        res.putPath(
                InConsumerAgentGroup.class, "productFindingSchemes",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-ProductFinding-Mapping")
        );

        res.putPath(
                InConsumerAgentGroup.class, "spatialDistribution",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Spatial-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Attribute der KG")
                .setGamsDescription("Attribute")
                .store(InConsumerAgentGroup.class, "cagAttributes");

        res.newEntryBuilder()
                .setGamsIdentifier("Awareness der KG")
                .setGamsDescription("genutzte Awareness")
                .store(InConsumerAgentGroup.class, "cagAwareness");

        res.newEntryBuilder()
                .setGamsIdentifier("[ungenutzt] informationAuthority")
                .setGamsDescription("informationAuthority")
                .store(InConsumerAgentGroup.class, "informationAuthority");

        res.newEntryBuilder()
                .setGamsIdentifier("Agenten in der KG")
                .setGamsDescription("Anzahl der Agenten in der Konsumergruppe")
                .store(InConsumerAgentGroup.class, "numberOfAgentsX");

        res.newEntryBuilder()
                .setGamsIdentifier("Schema für die Produktfindung")
                .setGamsDescription("Legt das Schema für das finden von passenden Produkten fest")
                .store(InConsumerAgentGroup.class, "productFindingSchemes");

        res.newEntryBuilder()
                .setGamsIdentifier("Räumliche Verteilungsfunktion")
                .setGamsDescription("Legt die Verteilungsfunktion für diese Gruppe fest")
                .store(InConsumerAgentGroup.class, "spatialDistribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InConsumerAgentGroup.class);

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroupAttribute[] cagAttributes;

    @FieldDefinition
    public InProductAwarenessSupplyScheme[] cagAwareness;

    @FieldDefinition
    public InProductFindingScheme[] productFindingSchemes;

    @FieldDefinition
    public InSpatialDistribution[] spatialDistribution;

    @FieldDefinition
    public double informationAuthority;

    @FieldDefinition
    public int numberOfAgentsX;

    public InConsumerAgentGroup() {
    }

    public InConsumerAgentGroup(
            String name,
            double informationAuthority,
            int numberOfAgents,
            Collection<? extends InConsumerAgentGroupAttribute> attributes,
            InProductAwarenessSupplyScheme awareness) {
        this._name = name;
        this.informationAuthority = informationAuthority;
        this.numberOfAgentsX = numberOfAgents;
        this.cagAttributes = attributes.toArray(new InConsumerAgentGroupAttribute[0]);
        this.cagAwareness = new InProductAwarenessSupplyScheme[]{awareness};
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public int getNumberOfAgents() {
        return numberOfAgentsX;
    }

    public InConsumerAgentGroupAttribute[] getAttributes() {
        return cagAttributes;
    }

    public InProductAwarenessSupplyScheme getAwareness() {
        return cagAwareness[0];
    }

    public void setAwareness(InProductAwarenessSupplyScheme awareness) {
        this.cagAwareness = new InProductAwarenessSupplyScheme[]{awareness};
    }

    public InProductFindingScheme getProductFindingScheme() {
        return productFindingSchemes[0];
    }

    public InSpatialDistribution getSpatialDistribution() {
        return spatialDistribution[0];
    }

    @Override
    public JadexConsumerAgentGroup parse(InputParser parser) throws ParsingException {
        AgentManager agentManager = parser.getEnvironment().getAgents();

        JadexConsumerAgentGroup jCag = new JadexConsumerAgentGroup();
        jCag.setEnvironment(parser.getEnvironment());
        jCag.setName(getName());
        jCag.setInformationAuthority(getInformationAuthority());

        if(agentManager.hasConsumerAgentGroup(getName())) {
            throw new ParsingException("ConsumerAgentGroup '" + getName() + "' already exists");
        }

        ProductAwarenessSupplyScheme awarenessSupplyScheme = parser.parseEntityTo(getAwareness());
        jCag.setAwarenessSupplyScheme(awarenessSupplyScheme);

        ProductFindingScheme productFindingScheme = parser.parseEntityTo(getProductFindingScheme());
        jCag.setProductFindingScheme(productFindingScheme);

        getSpatialDistribution().setup(parser, jCag);

        for(InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            ConsumerAgentGroupAttribute cagAttr = parser.parseEntityTo(inCagAttr);
            if(jCag.hasGroupAttribute(cagAttr.getName())) {
                throw new ParsingException("ConsumerAgentGroupAttribute '" + cagAttr.getName() + "' already exists in " + jCag.getName());
            }
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), inCagAttr.getName(), jCag.getName());
            jCag.addGroupAttribute(cagAttr);
        }

        return jCag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConsumerAgentGroup)) return false;
        InConsumerAgentGroup that = (InConsumerAgentGroup) o;
        return Double.compare(that.informationAuthority, informationAuthority) == 0
                && numberOfAgentsX == that.numberOfAgentsX
                && Objects.equals(_name, that._name)
                && Arrays.equals(cagAttributes, that.cagAttributes)
                && Arrays.equals(cagAwareness, that.cagAwareness);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, informationAuthority, numberOfAgentsX, cagAwareness);
        result = 31 * result + Arrays.hashCode(cagAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InConsumerAgentGroup{" +
                "_name='" + _name + '\'' +
                ", cagAttributes=" + Arrays.toString(cagAttributes) +
                ", informationAuthority=" + informationAuthority +
                ", numberOfAgents=" + numberOfAgentsX +
                ", cagAwareness4" + Arrays.toString(cagAwareness) +
                '}';
    }
}
