package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.ChecksumComparable;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class BasicVersion implements Version {

    private static final String INVALID = "999999";

    private String major;
    private String minor;
    private String build;
    private boolean invalid;

    public BasicVersion() {
    }

    public BasicVersion(String major, String minor, String build) {
        set(major, minor, build);
    }

    public void setFunctional(String text) {
        String[] parts = text.split("_");
        if(parts.length == 3) {
            major = parts[0];
            minor = parts[1];
            build = parts[2];
        } else {
            set(INVALID, INVALID, INVALID, true);
        }
    }

    public void set(String major, String minor, String build) {
        set(major, minor, build, false);
    }

    private void set(String major, String minor, String build, boolean invalid) {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.invalid = invalid;
    }

    public boolean isInvalid() {
        return invalid;
    }

    @Override
    public boolean isMatch(Version other) {
        return Objects.equals(major(), other.major())
                && Objects.equals(minor(), other.minor())
                && Objects.equals(build(), other.build());
    }

    @Override
    public boolean supportsInput(Version other) {
        return Objects.equals(major(), other.major())
                && Objects.equals(minor(), other.minor());
    }

    @Override
    public String major() {
        return major;
    }

    @Override
    public String minor() {
        return minor;
    }

    @Override
    public String build() {
        return build;
    }

    @Override
    public String print() {
        return major + "_" + minor + "_" + build;
    }

    @Override
    public String toString() {
        return print();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicVersion)) return false;
        BasicVersion that = (BasicVersion) o;
        return Objects.equals(major, that.major)
                && Objects.equals(minor, that.minor)
                && Objects.equals(build, that.build);
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, build);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(major, minor, build);
    }
}
