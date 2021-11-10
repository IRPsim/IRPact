package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InCommunicationModule_actiongraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InMulScalarModule_calcgraphnode2;

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

    public InMulScalarModule_calcgraphnode2 getNpvWeightModule() {
        return findModule(nonNull(npvWeightName), InMulScalarModule_calcgraphnode2.class);
    }

    public void setPpWeightName(String ppWeightName) {
        this.ppWeightName = ppWeightName;
    }

    public InMulScalarModule_calcgraphnode2 getPpWeightModule() {
        return findModule(nonNull(ppWeightName), InMulScalarModule_calcgraphnode2.class);
    }

    public void setNovWeightName(String novWeightName) {
        this.novWeightName = novWeightName;
    }

    public void setCommunicationName(String commuName) {
        this.commuName = commuName;
    }

    public InCommunicationModule_actiongraphnode2 getCommunicationModule() {
        return findModule(nonNull(commuName), InCommunicationModule_actiongraphnode2.class);
    }

    public InMulScalarModule_calcgraphnode2 getNovWeightModule() {
        return findModule(nonNull(novWeightName), InMulScalarModule_calcgraphnode2.class);
    }

    public void setEnvWeightName(String envWeightName) {
        this.envWeightName = envWeightName;
    }

    public InMulScalarModule_calcgraphnode2 getEnvWeightModule() {
        return findModule(nonNull(envWeightName), InMulScalarModule_calcgraphnode2.class);
    }

    public void setLocalWeightName(String localWeightName) {
        this.localWeightName = localWeightName;
    }

    public InMulScalarModule_calcgraphnode2 getLocalWeightModule() {
        return findModule(nonNull(localWeightName), InMulScalarModule_calcgraphnode2.class);
    }

    public void setSocialWeightName(String socialWeightName) {
        this.socialWeightName = socialWeightName;
    }

    public InMulScalarModule_calcgraphnode2 getSocialWeightModule() {
        return findModule(nonNull(socialWeightName), InMulScalarModule_calcgraphnode2.class);
    }
}
