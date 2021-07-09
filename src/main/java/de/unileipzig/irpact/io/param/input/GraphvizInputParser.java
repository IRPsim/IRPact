package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.misc.graphviz.BasicGraphvizConfiguration;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class GraphvizInputParser implements InputParser {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GraphvizInputParser.class);

    protected SimulationEnvironment environment;
    protected Path downloadDir;
    protected Path imageOutputPath;
    protected InRoot root;

    public GraphvizInputParser() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setDownloadDir(Path downloadDir) {
        this.downloadDir = downloadDir;
    }

    public void setImageOutputPath(Path imageOutputPath) {
        this.imageOutputPath = imageOutputPath;
    }

    @Override
    public void cache(InIRPactEntity key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCached(InIRPactEntity key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getCached(InIRPactEntity key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public GraphvizConfiguration parseRoot(InRoot root) throws ParsingException {
        this.root = root;
        ParsingJob job = new ParsingJob(environment, downloadDir, imageOutputPath, root);
        return job.run();
    }

    @Override
    public Object parseEntity(InIRPactEntity input) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
    }

    /**
     * @author Daniel Abitz
     */
    private static class ParsingJob {

        private final InRoot root;
        private final SimulationEnvironment environment;
        private final Path downloadDir;
        private final Path imageOutputPath;
        private final BasicGraphvizConfiguration configuration = new BasicGraphvizConfiguration();

        private ParsingJob(SimulationEnvironment environment, Path downloadDir, Path imageOutputPath, InRoot root) {
            this.environment = environment;
            this.downloadDir = downloadDir;
            this.imageOutputPath = imageOutputPath;
            this.root = root;
        }

        private static void log(String msg) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, msg);
        }

        private static void log(String pattern, Object arg) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, pattern, arg);
        }

        private static void log(String pattern, Object arg1, Object arg2) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, pattern, arg1, arg2);
        }

        private GraphvizConfiguration run() throws ParsingException {
            parseGeneral();
            parseColors();
            parseLayoutAlgorithm();
            parseOutputFormat();
            configuration.setImageOutputPath(imageOutputPath);
            return configuration;
        }

        private void parseGeneral() {
            configuration.setDownloadDir(downloadDir);
            configuration.setFixedNeatoPosition(root.getGraphvizGeneral().isFixedNeatoPosition());
            configuration.setScaleFactor(root.getGraphvizGeneral().getScaleFactor());
            configuration.setStoreDotFile(root.getGraphvizGeneral().isStoreDotFile());
        }

        private void parseColors() throws ParsingException {
            log("parse Colors");
            for(InConsumerAgentGroupColor grpColor: root.getConsumerAgentGroupColors()) {
                ConsumerAgentGroup cag = environment.getAgents().getConsumerAgentGroup(grpColor.getGroup().getName());
                if(cag == null) {
                    throw new IllegalArgumentException("ConsumerGroup '" + grpColor.getGroup().getName() + "' not found");
                }
                GraphvizColor color = grpColor.getColor();

                log("set color '{}' for agent group '{}'", color.getColorCode(), cag.getName());
                configuration.putColor(cag, color);
            }
        }

        private void parseLayoutAlgorithm() throws ParsingException {
            log("parse LayoutAlgorithm");
            LayoutAlgorithm algorithm = root.getGraphvizGeneral().getLayoutAlgorithm();
            log("use layout {}", algorithm.print());
            configuration.setLayoutAlgorithm(algorithm);
        }

        private void parseOutputFormat() throws ParsingException {
            log("parse OutputFormat");
            OutputFormat format = root.getGraphvizGeneral().getOutputFormat();
            log("use layout {}", format.print());
            configuration.setOutputFormat(format);
        }
    }
}
