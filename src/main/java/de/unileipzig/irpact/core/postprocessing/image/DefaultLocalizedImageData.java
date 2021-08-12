package de.unileipzig.irpact.core.postprocessing.image;

import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.util.R.builder.Element;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public final class DefaultLocalizedImageData {

    protected DefaultLocalizedImageData() {
    }

    public static LocalizedImageData get() {
        LocalizedImageDataYaml data = new LocalizedImageDataYaml(Locale.GERMAN, JsonUtil.YAML.createObjectNode());
        data.setEscapeSpecialCharacters(true);
        DataToVisualize mode;
        //ANNUAL_ZIP
        mode = DataToVisualize.ANNUAL_ZIP;
        data.setTitle(mode, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete");
        data.setXArg(mode, "year");
        data.setYArg(mode, "adoptions");
        data.setGrpArg(mode, "zip");
        data.setXLab(mode, "Jahre");
        data.setYLab(mode, "Adoptionen");
        data.setGrpLab(mode, "PLZ");
        data.setSep(mode, ";");
        data.setEncoding(mode, Element.UTF8);
        //COMPARED_ANNUAL_ZIP
        mode = DataToVisualize.COMPARED_ANNUAL_ZIP;
        data.setTitle(mode, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete\nim Vergleich zu realen Daten");
        data.setXArg(mode, "year");
        data.setYArg(mode, "adoptions");
        data.setGrpArg(mode, "zip");
        data.setDistinctArg(mode, "real");
        data.setXLab(mode, "Jahre");
        data.setYLab(mode, "Adoptionen");
        data.setGrpLab(mode, "PLZ");
        data.setDistinctLab(mode, "Reale Daten");
        data.setDistinct0Lab(mode, "nein");
        data.setDistinct1Lab(mode, "ja");
        data.setSep(mode, ";");
        data.setEncoding(mode, Element.UTF8);
        //CUMULATIVE_ANNUAL_PHASE
        mode = DataToVisualize.CUMULATIVE_ANNUAL_PHASE;
        data.setTitle(mode, "J\u00e4hrlich kumulierte Adoptionen in Bezug auf die Adoptionsphasen");
        data.setXArg(mode, "year");
        data.setYArg(mode, "adoptionsCumulative");
        data.setFillArg(mode, "phase");
        data.setXLab(mode, "Jahre");
        data.setYLab(mode, "Adoptionen (kumuliert)");
        data.setFillLab(mode, "Adoptionsphase");
        data.setSep(mode, ";");
        data.setEncoding(mode, Element.UTF8);
        //CUMULATIVE_ANNUAL_PHASE2
        mode = DataToVisualize.CUMULATIVE_ANNUAL_PHASE2;
        data.setTitle(mode, "J\u00e4hrlich kumulierte Adoptionen in Bezug auf die Adoptionsphasen\n(initiale Adopter: {0})");
        data.setXArg(mode, "year");
        data.setYArg(mode, "adoptionsCumulative");
        data.setFillArg(mode, "phase");
        data.setXLab(mode, "Jahre");
        data.setYLab(mode, "Adoptionen (kumuliert)");
        data.setFillLab(mode, "Adoptionsphase");
        data.setSep(mode, ";");
        data.setEncoding(mode, Element.UTF8);
        data.setPhase0(mode, "Start-Mitte");
        data.setPhase0(mode, "Mitte-Ende");
        data.setPhase0(mode, "Ende-Start");

        return data;
    }
}
