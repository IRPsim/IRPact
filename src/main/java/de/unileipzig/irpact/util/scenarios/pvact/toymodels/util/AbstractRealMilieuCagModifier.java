package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.util.pvact.Milieu;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRealMilieuCagModifier implements PVactCagModifier {

    public AbstractRealMilieuCagModifier() {
    }

    @Override
    public void modify(InPVactConsumerAgentGroup cag) {
        String name = cag.getName();
        Milieu milieu = Milieu.get(name);
        if(milieu == null) {
            throw new NullPointerException("not found: " + name);
        }

        switch (milieu) {
            case PRA:
                modifiyPRA(cag);
                break;

            case PER:
                modifiyPER(cag);
                break;

            case SOK:
                modifiySOK(cag);
                break;

            case BUM:
                modifiyBUM(cag);
                break;

            case PRE:
                modifiyPRE(cag);
                break;

            case EPE:
                modifiyEPE(cag);
                break;

            case TRA:
                modifiyTRA(cag);
                break;

            case KET:
                modifiyKET(cag);
                break;

            case LIB:
                modifiyLIB(cag);
                break;

            case HED:
                modifiyHED(cag);
                break;

            default:
                throw new IllegalArgumentException("unsupported: " + name);
        }
    }

    public abstract void modifiyPRA(InPVactConsumerAgentGroup cag);

    public abstract void modifiyPER(InPVactConsumerAgentGroup cag);

    public abstract void modifiySOK(InPVactConsumerAgentGroup cag);

    public abstract void modifiyBUM(InPVactConsumerAgentGroup cag);

    public abstract void modifiyPRE(InPVactConsumerAgentGroup cag);

    public abstract void modifiyEPE(InPVactConsumerAgentGroup cag);

    public abstract void modifiyTRA(InPVactConsumerAgentGroup cag);

    public abstract void modifiyKET(InPVactConsumerAgentGroup cag);

    public abstract void modifiyLIB(InPVactConsumerAgentGroup cag);

    public abstract void modifiyHED(InPVactConsumerAgentGroup cag);
}
