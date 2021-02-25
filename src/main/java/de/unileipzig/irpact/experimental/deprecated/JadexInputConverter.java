package de.unileipzig.irpact.experimental.deprecated;

import de.unileipzig.irpact.core.misc.DebugLevel;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.experimental.deprecated.input.IRoot;
import de.unileipzig.irpact.experimental.deprecated.input.affinity.IBasicAffinitiesEntry;
import de.unileipzig.irpact.experimental.deprecated.input.affinity.IBasicAffinityMapping;
import de.unileipzig.irpact.experimental.deprecated.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.experimental.deprecated.input.agent.consumer.IConsumerAgentGroupAttribute;
import de.unileipzig.irpact.experimental.deprecated.input.awareness.IFixedProductAwareness;
import de.unileipzig.irpact.experimental.deprecated.input.network.IFastHeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.experimental.deprecated.input.network.IFastHeterogeneousSmallWorldTopologyEntry;
import de.unileipzig.irpact.experimental.deprecated.input.product.IFixedProduct;
import de.unileipzig.irpact.experimental.deprecated.input.product.IFixedProductAttribute;
import de.unileipzig.irpact.experimental.deprecated.input.product.IProductGroup;
import de.unileipzig.irpact.experimental.deprecated.input.product.IProductGroupAttribute;
import de.unileipzig.irpact.experimental.deprecated.input.spatial.ISpace2D;
import de.unileipzig.irpact.experimental.deprecated.input.time.ITimeModel;
import de.unileipzig.irpact.commons.Util;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.network.BasicGraphConfiguration;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.FastHeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.util.TodoOLD;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.simulation.BasicJadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@TodoOLD("selbe inputinstance -> selbe instance hier machen - keine neue erstellen (cachen)")
public class JadexInputConverter implements InputConverter {

    public static JadexInputConverter INSTANCE = new JadexInputConverter();

    public JadexInputConverter() {
    }

    protected void initConsumerAgentGroups(BasicJadexSimulationEnvironment env, IRoot input) {
        for(IConsumerAgentGroup grp: input.consumerAgentGroups) {
            JadexConsumerAgentGroup jgrp = new JadexConsumerAgentGroup();
            jgrp.setEnvironment(env);
            jgrp.setInformationAuthority(grp.getInformationAuthority());
            jgrp.setName(grp.getName());
            //jgrp.setSpatialDistribution(new RandomPoint2DDistribution(0, 1, 0, 1, Util.RND.nextLong()));
            //jgrp.setProductAwareness(grp.getProductAwareness().createInstance());
            for(IConsumerAgentGroupAttribute attr: grp.getCagAttributes()) {
                BasicConsumerAgentGroupAttribute battr = new BasicConsumerAgentGroupAttribute();
                battr.setName(attr.getName());
                battr.setDistribution(attr.getCagAttrDistribution().createInstance());
                //jgrp.addAttribute(battr);
            }
            //env.getAgents().add(jgrp);
            //env.getInitializationData().setInitialNumberOfConsumerAgents(jgrp, grp.getNumberOfAgents());
        }
        initAffinities(env, input);
    }

    protected void initAffinities(BasicJadexSimulationEnvironment env, IRoot input) {
        IBasicAffinityMapping iAffinityMapping = input.affinityMapping[0];
        ConsumerAgentGroupAffinityMapping affinityMapping = env.getAgents().getConsumerAgentGroupAffinityMapping();
        for(IBasicAffinitiesEntry entry: iAffinityMapping.affinityEntries) {
            ConsumerAgentGroup from = env.getAgents().getConsumerAgentGroup(entry.from.getName());
            ConsumerAgentGroup to = env.getAgents().getConsumerAgentGroup(entry.to.getName());
            affinityMapping.put(from, to, entry.affinityValue);
        }
    }

    protected void initProducts(BasicJadexSimulationEnvironment env, IRoot input) {
        for(IProductGroup grp: input.productGroups) {
            BasicProductGroup pg = new BasicProductGroup();
            pg.setEnvironment(env);
            pg.setName(grp.getName());
            for(IProductGroupAttribute attr: grp.getPgAttributes()) {
                BasicProductGroupAttribute pga = new BasicProductGroupAttribute();
                pga.setName(attr.getName());
                pga.setDistribution(attr.getPgAttrDistribution().createInstance());
                pg.addAttribute(pga);
            }
            //env.getProducts().add(pg);
        }
        initFixedProducts(env, input);
    }

    protected void initFixedProducts(BasicJadexSimulationEnvironment env, IRoot input) {
        for(IFixedProduct product: input.fixedProducts) {
            ProductGroup pg = env.getProducts().getGroup(product.getFpGroup().getName());
            BasicProduct fp = new BasicProduct();
            fp.setFixed(false);
            fp.setName(product.getName());
            fp.setGroup(pg);
            for(IFixedProductAttribute attr: product.getFpAttributes()) {
                BasicProductAttribute battr = new BasicProductAttribute();
                battr.setName(attr.getName());
                battr.setDoubleValue(attr.getFpaValue());
                battr.setGroup(pg.getAttribute(attr.getFpaGroupAttribute().getName()));
                fp.addAttribute(battr);
            }
            //pg.registerFixed(fp);
        }
        initFixedProductAwareness(env, input);
    }

    protected void initFixedProductAwareness(BasicJadexSimulationEnvironment env, IRoot input) {
        for(IFixedProductAwareness awareness: input.fixedProductAwarenesses) {
            ConsumerAgentGroup cag = env.getAgents().getConsumerAgentGroup(awareness.awarenessAgentGroup.getName());
            //Product fp = env.getProducts().getFixedProduct(awareness.awarenessFixedProduct.getName());
            //UnivariateDoubleDistribution dist = awareness.awarenessDistribution.createInstance();
            //cag.getFixedProductAwarenessMapping().put(fp, dist);
        }
    }

    protected void initSpatialModel(BasicJadexSimulationEnvironment env, IRoot input) {
        ISpace2D iSpatial = input.spatialModel[0];
        Space2D space2D = new Space2D();
        Metric metric = Metric2D.get(iSpatial.getMetricID());
        space2D.setName(iSpatial.getName());
        space2D.setMetric(metric);
        env.setSpatialModel(space2D);
    }

    protected void initTimeModel(BasicJadexSimulationEnvironment env, IRoot input) {
        ITimeModel iTime = input.timeModel[0];
        env.setTimeModel(iTime.createInstance());
    }

    protected void initNetwork(BasicJadexSimulationEnvironment env, IRoot input) {
        IFastHeterogeneousSmallWorldTopology iTopology = input.topology[0];
        BasicSocialNetwork network = new BasicSocialNetwork();
        BasicGraphConfiguration configuration = new BasicGraphConfiguration();
        network.setConfiguration(configuration);

        Map<ConsumerAgentGroup, Double> betaMapping = new HashMap<>();
        Map<ConsumerAgentGroup, Integer> zMapping = new HashMap<>();
        for(IFastHeterogeneousSmallWorldTopologyEntry entry: iTopology.topoEntries) {
            ConsumerAgentGroup cag = env.getAgents().getConsumerAgentGroup(entry.topoGroup.getName());
            betaMapping.put(cag, entry.beta);
            zMapping.put(cag, entry.z);
        }

        FastHeterogeneousSmallWorldTopology topology = new FastHeterogeneousSmallWorldTopology(
                SocialGraph.Type.get(iTopology.edgeType),
                betaMapping,
                zMapping,
                iTopology.isSelfReferential,
                iTopology.initialWeight,
                iTopology.topoSeed
        );
        configuration.setGraphTopologyScheme(topology);

        //env.setSocialNetwork(network);
    }

    protected void initGlobal(BasicJadexSimulationEnvironment environment, IRoot input) {
        DebugLevel debugLevel = DebugLevel.get(input.generalSettings.getDebugLevel());
        //environment.setDebugLevel(debugLevel);
    }

    @Override
    public JadexSimulationEnvironment build(IRoot input) {
        BasicJadexSimulationEnvironment env = new BasicJadexSimulationEnvironment();
        initConsumerAgentGroups(env, input);
        initProducts(env, input);
        initSpatialModel(env, input);
        initTimeModel(env, input);
        initNetwork(env, input);
        initGlobal(env, input);
        return env;
    }
}
