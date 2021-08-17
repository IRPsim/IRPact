package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.ChecksumValue;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.IdManager;

/**
 * @author Daniel Abitz
 */
public class BasicPoint2DFactory extends NameableBase implements Point2DFactory {

    @ChecksumValue
    protected UnivariateDoubleDistribution xDist;
    @ChecksumValue
    protected UnivariateDoubleDistribution yDist;
    @ChecksumValue
    protected IdManager idManager;

    public BasicPoint2DFactory() {
    }

    public void setXDistribution(UnivariateDoubleDistribution xDist) {
        this.xDist = xDist;
    }

    public void setyDistribution(UnivariateDoubleDistribution yDist) {
        this.yDist = yDist;
    }

    public void setIdManager(IdManager idManager) {
        this.idManager = idManager;
    }

    @Override
    public Point2D createNext() {
        double x = xDist.drawDoubleValue();
        double y = yDist.drawDoubleValue();
        long id = idManager.nextId();
        BasicPoint2D point = new BasicPoint2D(x, y);
        point.setId(id);
        return point;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(xDist, yDist, idManager);
    }
}
