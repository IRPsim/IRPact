package de.unileipzig.irpact.core.process.ra.npv;

import de.unileipzig.irpact.commons.util.xlsx.KeyValueXlsxTable;
import de.unileipzig.irpact.commons.util.xlsx.SimplifiedXlsxTable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class NPVXlsxData implements NPVData {

    protected static final String STROMKOSTENANSTIEG = "p";
    protected static final String DEGRADATION = "D";
    protected static final String EIGENVERBRAUCH = "SC";
    protected static final String T_FIT = "tFIT";

    protected static final String STRAHLUNG = "Strahlung";
    protected static final String AUSRICHTUNG = "Ausrichtung";
    protected static final String STROM = "Strom";
    protected static final String WIRKUNGSGRAD = "Wirkungsgrad";

    protected static final String BERECHNUNGSZEITPUNKT = "Berechnungszeitpunkt";
    protected static final String NETTOSYSTEMPREIS = "Nettosystempreis";
    protected static final String STROMPREIS = "Strompreis";
    protected static final String EINSPEIS = "Einspeiseverguetung";
    protected static final String ZINSSATZ = "Zinssatz";

    protected static final String NEIGUNGSWINKEL = "Neigungswinkel";
    protected static final String GLOBALSTRAHLUNG = "Globalstrahlung";

    protected static final String AUSRICHTUNG_AGENT = "Ausrichtung";
    protected static final String AUSRICHTUNG_FAKTOR = "Faktor";

    protected static final String MODULWIRKUNGSGRAD = "Modulwirkungsgrad";
    protected static final String PERFORMANCE_RATIO = "Performance_Ratio";

    protected KeyValueXlsxTable allgemeinSheet;
    protected Map<String, SimplifiedXlsxTable> tables;

    public NPVXlsxData() {
        this(new HashMap<>());
    }

    public NPVXlsxData(Map<String, SimplifiedXlsxTable> tables) {
        this.tables = tables;
    }

    public void setAllgemeinSheet(KeyValueXlsxTable allgemeinSheet) {
        this.allgemeinSheet = allgemeinSheet;
    }

    public void putAllTables(Map<String, SimplifiedXlsxTable> tables) {
        this.tables.putAll(tables);
    }

    protected double getValue(String tableName, String referenceColumnName, double referenceValue, String targetColumnName) {
        SimplifiedXlsxTable table = tables.get(tableName);
        if(table == null) {
            throw new IllegalArgumentException("table '" + tableName + "' not found");
        }

        int referenceColumnIndex = table.getColumnIndex(referenceColumnName);
        if(referenceColumnIndex == -1) {
            throw new IllegalArgumentException("column '" + referenceColumnName + "' not found");
        }

        int rowIndex = table.getRowIndex(referenceColumnIndex, referenceValue);
        if(rowIndex == -1) {
            throw new IllegalArgumentException("row value '" + referenceValue + "' not found in column '" + referenceColumnName + "'");
        }

        int targetColumnIndex = table.getColumnIndex(targetColumnName);
        if(targetColumnIndex == -1) {
            throw new IllegalArgumentException("column '" + targetColumnName + "' not found");
        }

        return table.getRowValue(targetColumnIndex, rowIndex);
    }

    @Override
    public double getNettosystempreis(int t0) {
        return getValue(STROM, BERECHNUNGSZEITPUNKT, t0, NETTOSYSTEMPREIS);
    }

    @Override
    public double getEinspeiseverguetung(int t0) {
        return getValue(STROM, BERECHNUNGSZEITPUNKT, t0, EINSPEIS);
    }

    @Override
    public double getTFit() {
        return allgemeinSheet.get(T_FIT);
    }

    @Override
    public double getEigenverbrauch() {
        return allgemeinSheet.get(EIGENVERBRAUCH);
    }

    @Override
    public double getStrompreis(int t0) {
        return getValue(STROM, BERECHNUNGSZEITPUNKT, t0, STROMPREIS);
    }

    @Override
    public double getStromkostenanstieg() {
        return allgemeinSheet.get(STROMKOSTENANSTIEG);
    }

    @Override
    public double getZinssatzProzent(int t0) {
        return getValue(STROM, BERECHNUNGSZEITPUNKT, t0, ZINSSATZ) / 100.0;
    }

    @Override
    public double getGlobalstrahlung(double N) {
        return getValue(STRAHLUNG, NEIGUNGSWINKEL, N, GLOBALSTRAHLUNG);
    }

    @Override
    public double getAusrichtungsfaktor(double A) {
        return getValue(AUSRICHTUNG, AUSRICHTUNG_AGENT, A, AUSRICHTUNG_FAKTOR);
    }

    @Override
    public double getModulwirkungsgradProzent(int t0) {
        return getValue(WIRKUNGSGRAD, BERECHNUNGSZEITPUNKT, t0, MODULWIRKUNGSGRAD) / 100.0;
    }

    @Override
    public double getDegradation() {
        return allgemeinSheet.get(DEGRADATION);
    }

    @Override
    public double getPerformanceRatioProzent(int t0) {
        return getValue(WIRKUNGSGRAD, BERECHNUNGSZEITPUNKT, t0, PERFORMANCE_RATIO) / 100.0;
    }
}
