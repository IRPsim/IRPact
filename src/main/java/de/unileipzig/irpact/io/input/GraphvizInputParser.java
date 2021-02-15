package de.unileipzig.irpact.io.input;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.misc.graphviz.BasicGraphvizConfiguration;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.io.input.graphviz.InConsumerAgentGroupColor;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.graphviz.def.GraphvizLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.def.GraphvizOutputFormat;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class GraphvizInputParser implements InputParser<GraphvizConfiguration> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GraphvizInputParser.class);

    protected SimulationEnvironment environment;
    protected Path imageOutputPath;

    public GraphvizInputParser() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setImageOutputPath(Path imageOutputPath) {
        this.imageOutputPath = imageOutputPath;
    }

    @Override
    public GraphvizConfiguration parse(InRoot root, ObjectNode jsonRoot) throws Exception {
        ParsingJob job = new ParsingJob(environment, imageOutputPath, root, jsonRoot);
        return job.run();
    }

    /**
     * @author Daniel Abitz
     */
    private static class ParsingJob {

        @SuppressWarnings({"FieldCanBeLocal", "unused"})
        private final ObjectNode jsonRoot;
        private final InRoot root;
        private final SimulationEnvironment environment;
        private final Path imageOutputPath;
        private final BasicGraphvizConfiguration configuration = new BasicGraphvizConfiguration();

        private ParsingJob(SimulationEnvironment environment, Path imageOutputPath, InRoot root, ObjectNode jsonRoot) {
            this.environment = environment;
            this.imageOutputPath = imageOutputPath;
            this.root = root;
            this.jsonRoot = jsonRoot;
        }

        private static void log(String msg) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, msg);
        }

        private static void log(String pattern, Object arg) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, pattern, arg);
        }

        private static void log(String pattern, Object arg1, Object arg2) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, pattern, arg1, arg2);
        }

        private GraphvizConfiguration run() {
            parseColors();
            parseLayoutAlgorithm();
            parseOutputFormat();
            configuration.setImageOutputPath(imageOutputPath);
            return configuration;
        }

        private void parseColors() {
            log("parse Colors");
            for(InConsumerAgentGroupColor grpColor: root.consumerAgentGroupColors) {
                ConsumerAgentGroup cag = environment.getAgents().getConsumerAgentGroup(grpColor.getGroup().getName());
                if(cag == null) {
                    throw new IllegalArgumentException("ConsumerGroup '" + grpColor.getGroup().getName() + "' not found");
                }
                GraphvizColor color = grpColor.getColor();

                log("set color '{}' for agent group '{}'", color.getColorCode(), cag.getName());
                configuration.putColor(cag, color);
            }
        }

        private void parseLayoutAlgorithm() {
            log("parse LayoutAlgorithm");
            for(GraphvizLayoutAlgorithm a: root.layoutAlgorithms) {
                if(a.useLayout) {
                    log("use layout {} with id {}", a._name, a.layoutId);
                    configuration.setLayoutAlgorithm(a.toLayoutAlgorithm());
                    return;
                }
            }
            throw new IllegalArgumentException("no layout found (layouts=" + root.layoutAlgorithms.length + ", no useLayout set)");
        }

        private void parseOutputFormat() {
            log("parse OutputFormat");
            for(GraphvizOutputFormat o: root.outputFormats) {
                if(o.useFormat) {
                    log("use layout {} with id {}", o._name, o.formatId);
                    configuration.setOutputFormat(o.toOutputFormat());
                    return;
                }
            }
            throw new IllegalArgumentException("no format found (formats=" + root.outputFormats.length + ", no useFormat set)");
        }
    }
}
