package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging;

import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InConsumerAgentCalculationModule2;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InConsumerAgentCalculationLoggingModule2 extends InConsumerAgentCalculationModule2 {

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    String getBaseName();

    default String getFileName() {
        return getBaseName() + ".csv";
    }

    boolean isPrintHeader();

    boolean isLogReevaluatorCall();

    boolean isLogDefaultCall();

    int getAgentIndex();

    int getIdIndex();

    int getTimeIndex();

    int getProductIndex();

    int getValueIndex();

    LocalDateTime toTime(String input);
}
