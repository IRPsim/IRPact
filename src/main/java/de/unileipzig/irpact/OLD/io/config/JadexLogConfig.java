package de.unileipzig.irpact.OLD.io.config;

/**
 * @author Daniel Abitz
 */
public class JadexLogConfig extends LogConfig {

    protected boolean agentLevelTrace = true;
    protected boolean agentLevelDebug = false;

    public JadexLogConfig() {
        super();
    }

    public JadexLogConfig(
            String logFileName,
            boolean useConsole,
            boolean useFile,
            boolean levelTrace,
            boolean levelDebug,
            boolean agentLevelTrace,
            boolean agentLevelDebug) {
        super(logFileName, useConsole, useFile, levelTrace, levelDebug);
        this.agentLevelTrace = agentLevelTrace;
        this.agentLevelDebug = agentLevelDebug;
    }

    public void setAgentLevelTrace(boolean agentLevelTrace) {
        this.agentLevelTrace = agentLevelTrace;
    }

    public boolean isAgentLevelTrace() {
        return agentLevelTrace;
    }

    public void setAgentLevelDebug(boolean agentLevelDebug) {
        this.agentLevelDebug = agentLevelDebug;
    }

    public boolean isAgentLevelDebug() {
        return agentLevelDebug;
    }
}
