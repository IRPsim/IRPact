package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.geo.LatLng2XY;
import de.unileipzig.irpact.commons.util.PositionMapper;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.misc.graphviz.BasicGraphvizConfiguration;
import de.unileipzig.irpact.core.misc.graphviz.GraphvizConfiguration;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.AbstractMetricalSpatialModel;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.util.log.IRPLogger;
import guru.nidi.graphviz.attribute.Color;

/**
 * @author Daniel Abitz
 */
public class GraphvizInputParser implements InputParser {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GraphvizInputParser.class);

    protected SimulationEnvironment environment;
    protected InRoot root;

    public GraphvizInputParser() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
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
        ParsingJob job = new ParsingJob(environment, root);
        return job.run();
    }

    @Override
    public Object parseEntity(InIRPactEntity input) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object parseEntity(InIRPactEntity input, Object param) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(InIRPactEntity input, Object original, Object param) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
    }

    /**
     * @author Daniel Abitz
     */
    private static class ParsingJob implements LoggingHelper {

        private final InRoot root;
        private final SimulationEnvironment environment;
        private final BasicGraphvizConfiguration configuration = new BasicGraphvizConfiguration();

        private ParsingJob(
                SimulationEnvironment environment,
                InRoot root) {
            this.environment = environment;
            this.root = root;
        }

        @Override
        public IRPLogger getDefaultLogger() {
            return LOGGER;
        }

        @Override
        public IRPSection getDefaultSection() {
            return IRPSection.INITIALIZATION_PARAMETER;
        }

        private GraphvizConfiguration run() throws ParsingException {
            parseGeneral();
            parseColors();
            parseLayoutAlgorithm();
            parseOutputFormat();
            return configuration;
        }

        private void parseGeneral() {
            InGraphvizGeneral general = root.getGraphvizGeneral();
            configuration.setFixedNeatoPosition(general.isFixedNeatoPosition());
            configuration.setTerminalCharset(general.getTerminalCharset());
            configuration.setPreferredHeight(general.getPreferredImageHeight());
            configuration.setPreferredWidth(general.getPreferredImageWidth());
            configuration.setUseDefaultPositionIfMissing(general.isUseDefaultPositionIfMissing());
            configuration.setPositionMapper(getPositionMapper());
            configuration.setKeepAspectRatio(general.shouldKeepAspectRatio());
        }

        private PositionMapper getPositionMapper() {
            SpatialModel model = environment.getSpatialModel();
            if(model instanceof AbstractMetricalSpatialModel) {
                AbstractMetricalSpatialModel metricModel = (AbstractMetricalSpatialModel) model;
                Metric metric = metricModel.getMetric();
                if(metric == Metric2D.HAVERSINE_KM) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "using LatLng2XY with km");
                    return new LatLng2XY(true);
                }
                if(metric == Metric2D.HAVERSINE_M) {
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "using LatLng2XY with m");
                    return new LatLng2XY(false);
                }
            }
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "using no position mapper");
            return null;
        }

        private void parseColors() throws ParsingException {
            trace("parse Colors");
            for(InConsumerAgentGroupColor grpColor: root.getConsumerAgentGroupColors()) {
                if(grpColor.hasConsumerAgentGroups()) {
                    for(InConsumerAgentGroup inCag: grpColor.getGroups()) {
                        ConsumerAgentGroup cag = toCag(inCag);
                        Color color = getColor(inCag, cag);
                        trace("set color '{}' for cag '{}", color.value, cag.getName());
                        configuration.putColor(cag, color);
                    }
                }
            }
        }

        private ConsumerAgentGroup toCag(InConsumerAgentGroup inCag) throws ParsingException {
            ConsumerAgentGroup cag = environment.getAgents()
                    .getConsumerAgentGroup(inCag.getName());
            if(cag == null) {
                throw new ParsingException("cag '{}' not found", inCag.getName());
            }
            return cag;
        }

        private Color getColor(InConsumerAgentGroup inCag, ConsumerAgentGroup cag) throws ParsingException {
            Color color = null;
            for(InConsumerAgentGroupColor grpColor: root.getConsumerAgentGroupColors()) {
                if(grpColor.hasConsumerAgentGroup(inCag)) {
                    if(color == null) {
                        color = grpColor.toGraphvizColor();
                    } else {
                        throw new ParsingException("cag '{}' already has a color (current={}, new={})", cag.getName(), color.value, grpColor.toGraphvizColor().value);
                    }
                }
            }
            if(color == null) {
                info("no color set for ConsumerAgentGroup '{}', using black", inCag.getName());
                color = Color.BLACK;
            }
            return color;
        }

        private void parseLayoutAlgorithm() throws ParsingException {
            trace("parse LayoutAlgorithm");
            LayoutAlgorithm algorithm = root.getGraphvizGeneral().getLayoutAlgorithm();
            trace("use layout {}", algorithm.print());
            configuration.setLayoutAlgorithm(algorithm);
        }

        private void parseOutputFormat() throws ParsingException {
            trace("parse OutputFormat");
            OutputFormat format = root.getGraphvizGeneral().getOutputFormat();
            trace("use layout {}", format.print());
            configuration.setOutputFormat(format);
        }
    }
}
