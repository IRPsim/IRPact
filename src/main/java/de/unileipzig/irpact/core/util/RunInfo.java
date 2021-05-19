package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
public final class RunInfo implements ChecksumComparable {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RunInfo.class);

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    public RunInfo() {
    }

    public void setStartTime() {
        setStartTime(ZonedDateTime.now());
    }

    public void setStartTime(long epochMillis) {
        setStartTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault()));
    }

    public void setStartTime(ZonedDateTime startTime) {
        LOGGER.trace(IRPSection.GENERAL, "set start time: {}", startTime);
        this.startTime = startTime;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public long getStartTimeMillis() {
        return startTime.toInstant().toEpochMilli();
    }

    public void setEndTime() {
        setEndTime(ZonedDateTime.now());
    }

    public void setEndTime(long epochMillis) {
        setEndTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault()));
    }

    public void setEndTime(ZonedDateTime endTime) {
        LOGGER.trace(IRPSection.GENERAL, "set end time: {}", endTime);
        this.endTime = endTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public long getEndTimeMillis() {
        return endTime.toInstant().toEpochMilli();
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(startTime, endTime);
    }
}
