package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public class PrintIdAndAttribute implements SpatialInformationPrinter {

    protected String idLabel;
    protected String label;
    protected String key;

    public PrintIdAndAttribute(String idLabel, String label, String key) {
        setIdLabel(idLabel);
        setLabel(label);
        setKey(key);
    }

    public void setIdLabel(String idLabel) {
        this.idLabel = idLabel;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String print(SpatialInformation information) {
        long id = SpatialUtil.tryGetId(information);
        int value = SpatialUtil.tryGet(information, key, -1);
        return idLabel + ":" + id + "," + label + ":" + value;
    }
}
