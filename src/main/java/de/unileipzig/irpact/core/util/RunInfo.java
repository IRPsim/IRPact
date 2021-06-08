package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

/**
 * @author Daniel Abitz
 */
public final class RunInfo implements ChecksumComparable {

    public static final Comparator<RunInfo> COMPARE_ID = Comparator.comparingInt(RunInfo::getId);

    private int id = -1;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int firstSimulationYear;
    private int actualFirstSimulationYear;
    private int lastSimulationYear;

    public RunInfo() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStartTime() {
        setStartTime(ZonedDateTime.now());
    }

    public void setStartTime(long epochMillis) {
        setStartTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault()));
    }

    public void setStartTime(ZonedDateTime startTime) {
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
        this.endTime = endTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public long getEndTimeMillis() {
        return endTime.toInstant().toEpochMilli();
    }

    public int getFirstSimulationYear() {
        return firstSimulationYear;
    }

    public void setFirstSimulationYear(int firstSimulationYear) {
        this.firstSimulationYear = firstSimulationYear;
    }

    public int getActualFirstSimulationYear() {
        return actualFirstSimulationYear;
    }

    public void setActualFirstSimulationYear(int actualFirstSimulationYear) {
        this.actualFirstSimulationYear = actualFirstSimulationYear;
    }

    public int getLastSimulationYear() {
        return lastSimulationYear;
    }

    public void setLastSimulationYear(int lastSimulationYear) {
        this.lastSimulationYear = lastSimulationYear;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(id, startTime, endTime, firstSimulationYear, lastSimulationYear);
    }
}
