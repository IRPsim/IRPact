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

    protected static LocalizedImageData get() {
        LocalizedImageDataYaml data = new LocalizedImageDataYaml(Locale.GERMAN, JsonUtil.YAML.createObjectNode());
        data.setEscapeSpecialCharacters(true);
        //ANNUAL_ZIP
        data.setTitle(DataToVisualize.ANNUAL_ZIP, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete");
        data.setXArg(DataToVisualize.ANNUAL_ZIP, "year");
        data.setYArg(DataToVisualize.ANNUAL_ZIP, "adoptions");
        data.setGrpArg(DataToVisualize.ANNUAL_ZIP, "zip");
        data.setXLab(DataToVisualize.ANNUAL_ZIP, "Jahre");
        data.setYLab(DataToVisualize.ANNUAL_ZIP, "Adoptionen");
        data.setGrpLab(DataToVisualize.ANNUAL_ZIP, "PLZ");
        data.setSep(DataToVisualize.ANNUAL_ZIP, ";");
        data.setEncoding(DataToVisualize.ANNUAL_ZIP, Element.UTF8);
        //COMPARED_ANNUAL_ZIP
        data.setTitle(DataToVisualize.COMPARED_ANNUAL_ZIP, "J\u00e4hrliche Adoptionen in Bezug auf die Postleitzahlgebiete\nim Vergleich zu realen Daten");
        data.setXArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "year");
        data.setYArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "adoptions");
        data.setGrpArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "zip");
        data.setDistinctArg(DataToVisualize.COMPARED_ANNUAL_ZIP, "real");
        data.setXLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Jahre");
        data.setYLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Adoptionen");
        data.setGrpLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "PLZ");
        data.setDistinctLab(DataToVisualize.COMPARED_ANNUAL_ZIP, "Reale Daten");
        data.setDistinct0Lab(DataToVisualize.COMPARED_ANNUAL_ZIP, "nein");
        data.setDistinct1Lab(DataToVisualize.COMPARED_ANNUAL_ZIP, "ja");
        data.setSep(DataToVisualize.COMPARED_ANNUAL_ZIP, ";");
        data.setEncoding(DataToVisualize.COMPARED_ANNUAL_ZIP, Element.UTF8);
        //CUMULATIVE_ANNUAL_PHASE
        data.setTitle(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "J\u00e4hrlich kumulierte Adoptionen in Bezug auf die Adoptionsphasen");
        data.setXArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "year");
        data.setYArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "adoptionsCumulative");
        data.setFillArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "phase");
        data.setXLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Jahre");
        data.setYLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Adoptionen (kumuliert)");
        data.setFillLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, "Adoptionsphase");
        data.setSep(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, ";");
        data.setEncoding(DataToVisualize.CUMULATIVE_ANNUAL_PHASE, Element.UTF8);

        return data;
    }
}
