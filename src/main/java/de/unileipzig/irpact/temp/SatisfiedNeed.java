package de.unileipzig.irpact.temp;

import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import jadex.bridge.service.annotation.Reference;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
@Reference(local = true, remote = true)
public class SatisfiedNeed {

    private ZonedDateTime time;
    private Need need;
    private FixedProduct product;
    private String agentName;
    private AgentGroup group;

    public SatisfiedNeed(ZonedDateTime time, Need need, FixedProduct product, String agentName, AgentGroup group) {
        this.time = time;
        this.need = need;
        this.product = product;
        this.agentName = agentName;
        this.group = group;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public Need getNeed() {
        return need;
    }

    public FixedProduct getProduct() {
        return product;
    }

    public String getAgentName() {
        return agentName;
    }

    public AgentGroup getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return "SatisfiedNeed{" +
                "agentName=" + agentName +
                ", time=" + time +
                ", need=" + need._name +
                ", product=" + product._name +
                '}';
    }
}
