package de.unileipzig.irpact.OLD.io.config;

/**
 * @author Daniel Abitz
 */
public class LogConfig {

    protected String logFileName = "";
    protected boolean useConsole = true;
    protected boolean useFile = true;
    protected boolean levelTrace = true;
    protected boolean levelDebug = false;

    public LogConfig() {
    }

    public LogConfig(
            String logFileName,
            boolean useConsole,
            boolean useFile,
            boolean levelTrace,
            boolean levelDebug) {
        this.logFileName = logFileName;
        this.useConsole = useConsole;
        this.useFile = useFile;
        this.levelTrace = levelTrace;
        this.levelDebug = levelDebug;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setUseConsole(boolean useConsole) {
        this.useConsole = useConsole;
    }

    public boolean isUseConsole() {
        return useConsole;
    }

    public void setUseFile(boolean useFile) {
        this.useFile = useFile;
    }

    public boolean isUseFile() {
        return useFile;
    }

    public void setLevelTrace(boolean levelTrace) {
        this.levelTrace = levelTrace;
    }

    public boolean isLevelTrace() {
        return levelTrace;
    }

    public void setLevelDebug(boolean levelDebug) {
        this.levelDebug = levelDebug;
    }

    public boolean isLevelDebug() {
        return levelDebug;
    }
}
