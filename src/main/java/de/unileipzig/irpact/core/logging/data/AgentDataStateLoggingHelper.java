package de.unileipzig.irpact.core.logging.data;

/**
 * @author Daniel Abitz
 */
public enum AgentDataStateLoggingHelper {
    AVG_NPV {
        @Override
        public void set(AgentDataState state, double value) {
            state.avgNpv = value;
        }
    },
    AGENT_NPV {
        @Override
        public void set(AgentDataState state, double value) {
            state.agentNpv = value;
        }
    },
    RAW_NPV {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawNpv = value;
        }
    },
    NPV {
        @Override
        public void set(AgentDataState state, double value) {
            state.npv = value;
        }
    },

    AVG_FIN {
        @Override
        public void set(AgentDataState state, double value) {
            state.avgFin = value;
        }
    },
    AGENT_FIN {
        @Override
        public void set(AgentDataState state, double value) {
            state.agentFin = value;
        }
    },
    RAW_FIN {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawFin = value;
        }
    },
    FIN {
        @Override
        public void set(AgentDataState state, double value) {
            state.fin = value;
        }
    },

    RAW_LOCAL_SHARE {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawLocalShare = value;
        }
    },
    LOCAL_SHARE {
        @Override
        public void set(AgentDataState state, double value) {
            state.localShare = value;
        }
    },
    RAW_SOCIAL_SHARE {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawSocialShare = value;
        }
    },
    SOCIAL_SHARE {
        @Override
        public void set(AgentDataState state, double value) {
            state.socialShare = value;
        }
    },

    PP {
        @Override
        public void set(AgentDataState state, double value) {
            state.pp = value;
        }
    },
    PP_THRESHOLD {
        @Override
        public void set(AgentDataState state, double value) {
            state.ppThreshold = value;
        }
    },

    RAW_FIN_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawFinComp = value;
        }
    },
    RAW_ENV_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawEnvComp = value;
        }
    },
    RAW_NOV_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawNovComp = value;
        }
    },
    RAW_SOC_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.rawSocComp = value;
        }
    },

    FIN_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.finComp = value;
        }
    },
    ENV_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.envComp = value;
        }
    },
    NOV_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.novComp = value;
        }
    },
    SOC_COMP {
        @Override
        public void set(AgentDataState state, double value) {
            state.socComp = value;
        }
    },

    UTILITY {
        @Override
        public void set(AgentDataState state, double value) {
            state.utility = value;
        }
    },
    UTILITY_THRESHOLD {
        @Override
        public void set(AgentDataState state, double value) {
            state.utilityThreshold = value;
        }
    };

    public abstract void set(AgentDataState state, double value);
}
