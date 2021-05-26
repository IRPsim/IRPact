package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.logging.PercentageProgressNotifier;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public final class ProgressNotifierUtil {

    private ProgressNotifierUtil() {
    }

    public static Consumer<PercentageProgressNotifier> logPercentage(
            IRPLogger logger,
            IRPSection section,
            Level level,
            String msgPrefix) {
        return notifier -> {
            long percentageAsInteger = (long) (notifier.getPercentage() * 100);
            logger.log(section, level, "{}: {}%", msgPrefix, percentageAsInteger);
        };
    }

    public static Consumer<PercentageProgressNotifier> logPercentageAndTotal(
            IRPLogger logger,
            IRPSection section,
            Level level,
            String msgPrefix) {
        return notifier -> {
            long percentageAsInteger = (long) (notifier.getPercentage() * 100);
            logger.log(section, level, "{}: {}% ({}/{})", msgPrefix, percentageAsInteger, notifier.getFinishedWork(), notifier.getTotalWork());
        };
    }
}
