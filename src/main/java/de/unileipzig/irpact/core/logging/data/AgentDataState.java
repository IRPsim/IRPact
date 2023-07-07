package de.unileipzig.irpact.core.logging.data;

/**
 * @author Daniel Abitz
 */
public class AgentDataState {
    public double avgNpv = Double.NaN;
    public double agentNpv = Double.NaN;
    public double rawNpv = Double.NaN;
    public double npv = Double.NaN;

    public double avgFin = Double.NaN;
    public double agentFin = Double.NaN;
    public double rawFin = Double.NaN;
    public double fin = Double.NaN;

    public double rawLocalShare = Double.NaN;
    public double localShare = Double.NaN;
    public double rawSocialShare = Double.NaN;
    public double socialShare = Double.NaN;

    public double pp = Double.NaN;
    public double ppThreshold = Double.NaN;

    public double rawFinComp = Double.NaN;
    public double rawEnvComp = Double.NaN;
    public double rawNovComp = Double.NaN;
    public double rawSocComp = Double.NaN;

    public double finComp = Double.NaN;
    public double envComp = Double.NaN;
    public double novComp = Double.NaN;
    public double socComp = Double.NaN;

    public double utility = Double.NaN;
    public double utilityThreshold = Double.NaN;

    public AgentDataState() {
    }

    public AgentDataState(AgentDataState other) {
        avgNpv = other.avgNpv;
        agentNpv = other.agentNpv;
        rawNpv = other.rawNpv;
        npv = other.npv;

        avgFin = other.avgFin;
        agentFin = other.agentFin;
        rawFin = other.rawFin;
        fin = other.fin;

        rawLocalShare = other.rawLocalShare;
        localShare = other.localShare;
        rawSocialShare = other.rawSocialShare;
        socialShare = other.socialShare;

        pp = other.pp;
        ppThreshold = other.ppThreshold;

        rawFinComp = other.rawFinComp;
        rawEnvComp = other.rawEnvComp;
        rawNovComp = other.rawNovComp;
        rawSocComp = other.rawSocComp;

        finComp = other.finComp;
        envComp = other.envComp;
        novComp = other.novComp;
        socComp = other.socComp;

        utility = other.utility;
        utilityThreshold = other.utilityThreshold;
    }

    public AgentDataState copy() {
        return new AgentDataState(this);
    }
}
