package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InCommunicationModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InMulScalarModule3;

/**
 * @author Daniel Abitz
 */
public class PVactModularProcessModelManager extends ModularProcessModelManager {

    protected String commuName;
    protected String npvWeightName;
    protected String ppWeightName;
    protected String localWeightName;
    protected String socialWeightName;
    protected String envWeightName;
    protected String novWeightName;

    public PVactModularProcessModelManager() {
    }

    protected static String nonNull(String str) {
        if(str == null) {
            throw new NullPointerException("name is null");
        }
        return str;
    }

    public void setNpvWeightName(String npvWeightName) {
        this.npvWeightName = npvWeightName;
    }

    public InMulScalarModule3 getNpvWeightModule() {
        return findModule(nonNull(npvWeightName), InMulScalarModule3.class);
    }

    public void setPpWeightName(String ppWeightName) {
        this.ppWeightName = ppWeightName;
    }

    public InMulScalarModule3 getPpWeightModule() {
        return findModule(nonNull(ppWeightName), InMulScalarModule3.class);
    }

    public void setNovWeightName(String novWeightName) {
        this.novWeightName = novWeightName;
    }

    public void setCommunicationName(String commuName) {
        this.commuName = commuName;
    }

    public InCommunicationModule3 getCommunicationModule() {
        return findModule(nonNull(commuName), InCommunicationModule3.class);
    }

    public InMulScalarModule3 getNovWeightModule() {
        return findModule(nonNull(novWeightName), InMulScalarModule3.class);
    }

    public void setEnvWeightName(String envWeightName) {
        this.envWeightName = envWeightName;
    }

    public InMulScalarModule3 getEnvWeightModule() {
        return findModule(nonNull(envWeightName), InMulScalarModule3.class);
    }

    public void setLocalWeightName(String localWeightName) {
        this.localWeightName = localWeightName;
    }

    public InMulScalarModule3 getLocalWeightModule() {
        return findModule(nonNull(localWeightName), InMulScalarModule3.class);
    }

    public void setSocialWeightName(String socialWeightName) {
        this.socialWeightName = socialWeightName;
    }

    public InMulScalarModule3 getSocialWeightModule() {
        return findModule(nonNull(socialWeightName), InMulScalarModule3.class);
    }
}
