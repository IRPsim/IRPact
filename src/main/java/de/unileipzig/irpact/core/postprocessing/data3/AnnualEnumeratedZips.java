package de.unileipzig.irpact.core.postprocessing.data3;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AnnualEnumeratedZips extends AnnualEnumeratedAdoptionData<String> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualEnumeratedZips.class);

    public static final String MISSING_ZIP = "MISSING_ZIP";
    public static final String INVALID_ZIP = "INVALID_ZIP";

    protected String zipKey;

    public AnnualEnumeratedZips() {
        this(RAConstants.ZIP);
    }

    public AnnualEnumeratedZips(String zipKey) {
        setZipKey(zipKey);
    }

    public void setZipKey(String zipKey) {
        this.zipKey = zipKey;
    }

    public String getZipKey() {
        return zipKey;
    }

    protected String getZip(ConsumerAgent ca) {
        Attribute zipAttr = ca.findAttribute(getZipKey());
        if(zipAttr == null) {
            LOGGER.warn(IRPSection.RESULT, "agent '{}' has no zip", ca.getName());
            return MISSING_ZIP;
        } else {
            try {
                return zipAttr.asValueAttribute().getStringValue();
            } catch (Throwable t) {
                LOGGER.warn(IRPSection.RESULT, "agent '{}' has invalid zip", ca.getName());
                return INVALID_ZIP;
            }
        }
    }

    @Override
    public void update(ConsumerAgent ca, AdoptedProduct ap) {
        int year = ap.isInitial() ? INITIAL_YEAR : ap.getYear();
        data.update(year, ap.getProduct(), getZip(ca));
    }

    @Override
    protected AnnualEnumeratedZips newInstance() {
        return new AnnualEnumeratedZips();
    }
}
