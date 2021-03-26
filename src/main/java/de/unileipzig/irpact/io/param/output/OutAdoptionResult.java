package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutAdoptionResult {

    //cag_year
    public String _name;

    @FieldDefinition
    public int adoptions;

    @FieldDefinition
    public double adoptionShare;

    public OutAdoptionResult() {
    }

    public OutAdoptionResult(String name) {
        this._name = name;
    }

    public OutAdoptionResult(String name, int adoptions, double share) {
        this._name = name;
        this.adoptions = adoptions;
        this.adoptionShare = share;
    }

    public String getName() {
        return _name;
    }

    public int getAdoptions() {
        return adoptions;
    }

    public void setAdoptions(int adoptions) {
        this.adoptions = adoptions;
    }

    public double getShare() {
        return adoptionShare;
    }

    public void setShare(double adoptionShare) {
        this.adoptionShare = adoptionShare;
    }
}
