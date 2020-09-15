package de.unileipzig.irpact.temp;

import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import jadex.bridge.service.annotation.Reference;

import java.util.Random;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
@Reference(local = true, remote = true)
public class TConsumerAgentData {

    private AgentGroup group;
    private String name;
    private Set<Need> needs;
    private Random rnd;
    private ContinuousConverter continuousConverter;
    private TickConverter tickConverter;
    private long delay;
    private Set<FixedProduct> products;

    public TConsumerAgentData(
            AgentGroup group,
            String name,
            Set<Need> needs,
            Random rnd,
            ContinuousConverter continuousConverter,
            TickConverter tickConverter,
            long delay,
            Set<FixedProduct> products) {
        this.group = group;
        this.name = name;
        this.needs = needs;
        this.rnd = rnd;
        this.continuousConverter = continuousConverter;
        this.tickConverter = tickConverter;
        this.delay = delay;
        this.products = products;
    }

    public AgentGroup getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public Random getRandom() {
        return rnd;
    }

    public ContinuousConverter getContinuousConverter() {
        return continuousConverter;
    }

    public TickConverter getTickConverter() {
        return tickConverter;
    }

    public long getDelay() {
        return delay;
    }

    public Set<FixedProduct> getProducts() {
        return products;
    }
}
