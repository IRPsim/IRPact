package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.BasicStringAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ExactAdoptionPrinterXlsx extends XlsxResultProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ExactAdoptionPrinterXlsx.class);

    public ExactAdoptionPrinterXlsx() {
    }

    @Override
    protected void handleApplyError(Exception e) {
        LOGGER.error("writing '" + target + "' failed", e);
    }

    @Override
    protected void addColumns(XlsxTable<Attribute> table) {
        table.addColumns("name", "id", "product", "phase", "time");
    }

    @Override
    protected void handleTotalCount(long total) {
        LOGGER.trace("write xlsx '{}' entries: {}", total, total);
    }

    @Override
    public int handleAgent(ConsumerAgent agent) {
        if(agent.getAdoptedProducts().isEmpty()) {
            handleNonadopter(agent);
            return 0;
        } else {
            for(AdoptedProduct product: agent.getAdoptedProducts()) {
                handleAdoption(agent, product);
            }
            return agent.getAdoptedProducts().size();
        }
    }

    public void handleNonadopter(ConsumerAgent agent) {
        table.addRow(
                new BasicStringAttribute(agent.getName()),
                NA_STR,
                NA_STR,
                NA_STR,
                NA_STR
        );
    }

    public void handleAdoption(ConsumerAgent agent, AdoptedProduct product) {
        table.addRow(
                new BasicStringAttribute(agent.getName()),
                new BasicStringAttribute(Long.toString(getSpatialInformationId(agent))),
                new BasicStringAttribute(product.getProduct().getName()),
                new BasicStringAttribute(product.getPhase().name()),
                new BasicStringAttribute(product.getTimestamp().getTime().toString())
        );
    }

    protected static long getSpatialInformationId(ConsumerAgent agent) {
        SpatialInformation information = agent.getSpatialInformation();
        if(information == null) {
            return -2;
        }
        if(information.hasId()) {
            return information.getId();
        } else {
            SpatialAttribute attr = information.getAttribute(RAConstants.ID);
            if(attr == null) {
                return -1;
            } else {
                return attr.asValueAttribute().getLongValue();
            }
        }
    }
}
