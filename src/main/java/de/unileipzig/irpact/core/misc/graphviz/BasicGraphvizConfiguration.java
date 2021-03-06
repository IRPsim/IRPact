package de.unileipzig.irpact.core.misc.graphviz;

import de.unileipzig.irpact.commons.concurrent.TimedTask;
import de.unileipzig.irpact.commons.util.PositionMapper;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.twodim.Point2D;
import de.unileipzig.irptools.graphviz.*;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.log.IRPLogger;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.model.MutableNode;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Daniel Abitz
 */
public class BasicGraphvizConfiguration implements GraphvizConfiguration {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicGraphvizConfiguration.class);
    protected static final double DEFAULT_PREFERRED_WIDTH = 1000;
    protected static final double DEFAULT_PREFERRED_HEIGHT = 1000;
    protected static final long TIMEOUT = TimeUnit.MINUTES.toMillis(2);

    protected Map<ConsumerAgentGroup, Color> colorMapping;
    protected LayoutAlgorithm layoutAlgorithm;
    protected OutputFormat outputFormat;
    protected boolean fixedNeatoPosition;
    protected Charset terminalCharset = StandardCharsets.UTF_8;
    protected double preferredWidth;
    protected double preferredHeight;
    protected boolean useDefaultPositionIfMissing = false;
    protected PositionMapper positionMapper;
    protected boolean keepAspectRatio = false;
    protected boolean preferSmallerArea = true;

    public BasicGraphvizConfiguration() {
        this(new HashMap<>());
    }

    public BasicGraphvizConfiguration(Map<ConsumerAgentGroup, Color> colorMapping) {
        this.colorMapping = colorMapping;
    }

    public void setLayoutAlgorithm(LayoutAlgorithm layoutAlgorithm) {
        this.layoutAlgorithm = layoutAlgorithm;
    }

    public LayoutAlgorithm getLayoutAlgorithm() {
        return layoutAlgorithm;
    }

    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    public boolean hasColor(ConsumerAgentGroup cag) {
        return colorMapping.containsKey(cag);
    }

    public void putColor(ConsumerAgentGroup cag, Color color) {
        colorMapping.put(cag, color);
    }

    public Color getColor(ConsumerAgentGroup cag) {
        return colorMapping.get(cag);
    }

    public void setFixedNeatoPosition(boolean fixedNeatoPosition) {
        this.fixedNeatoPosition = fixedNeatoPosition;
    }

    public boolean isFixedNeatoPosition() {
        return fixedNeatoPosition;
    }

    protected boolean isFdpOrSfdp() {
        return layoutAlgorithm == StandardLayoutAlgorithm.FDP || layoutAlgorithm == StandardLayoutAlgorithm.SFDP;
    }

    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
    }

    public Charset getTerminalCharset() {
        return terminalCharset;
    }

    public void setPreferredHeight(double preferredHeight) {
        this.preferredHeight = preferredHeight;
    }

    public double getPreferredHeight() {
        return preferredHeight;
    }

    public void setPreferredWidth(double preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public double getPreferredWidth() {
        return preferredWidth;
    }

    protected double getPreferredHeightOrDefault() {
        return preferredHeight == 0
                ? DEFAULT_PREFERRED_HEIGHT
                : preferredHeight;
    }

    protected double getPreferredWidthOrDefault() {
        return preferredWidth == 0
                ? DEFAULT_PREFERRED_WIDTH
                : preferredWidth;
    }

    protected double getWidthToUse() {
        return hasPositionMapper()
                ? getPositionMapper().getBoundingBoxWidth()
                : getPreferredWidth();
    }

    protected double getHeightToUse() {
        return hasPositionMapper()
                ? getPositionMapper().getBoundingBoxHeight()
                : getPreferredHeight();
    }

    protected boolean hasSizeToUse() {
        return getWidthToUse() > 0 && getHeightToUse() > 0;
    }

    public void setUseDefaultPositionIfMissing(boolean useDefaultPositionIfMissing) {
        this.useDefaultPositionIfMissing = useDefaultPositionIfMissing;
    }

    public boolean isUseDefaultPositionIfMissing() {
        return useDefaultPositionIfMissing;
    }

    public void setPositionMapper(PositionMapper positionMapper) {
        this.positionMapper = positionMapper;
    }

    public PositionMapper getPositionMapper() {
        return positionMapper;
    }

    public boolean hasPositionMapper() {
        return positionMapper != null;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }

    public boolean shouldKeepAspectRatio() {
        return keepAspectRatio;
    }

    private static GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> newGenerator() {
        return new GraphvizGenerator<>(
                SocialGraph.Node::getLabel,
                SocialGraph.Edge::getSource,
                SocialGraph.Edge::getTarget
        );
    }

    private GraphvizGenerator.NodeConfigurator<SocialGraph.Node> getNodeConfiguration() {
        return (node, graphvisNode) -> {
            ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
            ConsumerAgentGroup cag = ca.getGroup();
            Color color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black for node", cag.getName());
                color = Color.BLACK;
            }
            graphvisNode.add(color);
            if(isFixedNeatoPosition()) {
                setPosition(graphvisNode, ca);
            }
        };
    }

    private void setPosition(MutableNode node, ConsumerAgent agent) {
        SpatialInformation information = agent.getSpatialInformation();
        if(information == null) {
            if(useDefaultPositionIfMissing) {
                LOGGER.warn("agent '{}' has no position, using default position (0,0)", agent.getName());
                setPosition(node, 0, 0);
            } else {
                throw new IllegalStateException("position based layout selected, but no position for agent '" + agent.getName() + "'");
            }
        }
        else if(information instanceof Point2D) {
            Point2D p2D = (Point2D) information;
            setPosition(node, mapX(p2D.getX()), mapY(p2D.getY()));
        }
        else {
            throw new IllegalStateException("position is not 2D: " + information.getClass());
        }
    }

    private double mapX(double x) {
        double xx = hasPositionMapper() ? getPositionMapper().mapX(x) : x;
        if(Double.isNaN(xx)) {
            LOGGER.warn("x position is NaN, return 0");
            return 0;
        } else {
            return xx;
        }
    }

    private double mapY(double y) {
        double yy = hasPositionMapper() ? getPositionMapper().mapY(y) : y;
        if(Double.isNaN(yy)) {
            LOGGER.warn("y position is NaN, return 0");
            return 0;
        } else {
            return yy;
        }
    }

    private static void setPosition(MutableNode node, double x, double y) {
        node.add("pos", x + "," + y);
    }

    private GraphvizGenerator.LinkConfigurator<SocialGraph.Edge> getLinkConfiguration() {
        return (edge, graphvizLink) -> {
            SocialGraph.Node source = edge.getSource();
            ConsumerAgent ca = source.getAgent(ConsumerAgent.class);
            ConsumerAgentGroup cag = ca.getGroup();
            Color color = getColor(cag);
            if(color == null) {
                LOGGER.info("no color set for ConsumerAgentGroup '{}', using black for links", cag.getName());
                color = Color.BLACK;
            }
            graphvizLink.add(color);
        };
    }

    private GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> createGenerator() {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = newGenerator();
        gen.setDirected(true);
        return gen;
    }

    private void applySocialNetwork(
            GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen,
            SocialGraph graph,
            SocialGraph.Type edgeType) {
        updatePositionMapper(graph.getNodes());
        gen.addAllNodes(graph.getNodes());
        gen.addAllLinks(graph.getEdges(edgeType));
    }

    private void updatePositionMapper(Collection<? extends SocialGraph.Node> nodes) {
        if(hasPositionMapper()) {
            positionMapper.reset();
            for(SocialGraph.Node node: nodes) {
                ConsumerAgent ca = node.getAgent(ConsumerAgent.class);
                SpatialInformation information = ca.getSpatialInformation();
                if(information instanceof Point2D) {
                    Point2D p2d = (Point2D) information;
                    positionMapper.update(p2d.getX(), p2d.getY());
                } else {
                    LOGGER.warn("skip unsupported information (agent={}, information={})", ca.getName(), information);
                }
            }
            if(shouldKeepAspectRatio()) {
                positionMapper.computeBoundingBox(0, 0, getPreferredWidthOrDefault(), getPreferredHeightOrDefault(), preferSmallerArea);
            } else {
                positionMapper.setBoundingBox(0, 0, getPreferredWidthOrDefault(), getPreferredHeightOrDefault());
            }
        }
    }

    private void setupGenerator(GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen) {
        gen.configureNodes(getNodeConfiguration());
        gen.configureLinks(getLinkConfiguration());
        gen.setHideNodeLabels(true);
        gen.setNodeShape(Shape.POINT);
        if(isFdpOrSfdp()) {
            gen.setOverlap(StandardOverlap.PRISM);
        }

        if(hasSizeToUse()) {
            gen.setPixelSizePreferred(getWidthToUse(), getHeightToUse());
        }
    }

    protected Path createTempDotPathIfNull(Path inputDotPath, Path outputFile) {
        if(inputDotPath == null) {
            return outputFile.resolveSibling(outputFile.getFileName().toString() + ".dot");
        } else {
            return inputDotPath;
        }
    }

    @Override
    public void printSocialGraph(
            SocialGraph graph,
            SocialGraph.Type edgeType,
            Path outputFile,
            Path dotFile) throws Exception {
        GraphvizGenerator<SocialGraph.Node, SocialGraph.Edge> gen = createGenerator();
        applySocialNetwork(gen, graph, edgeType);
        setupGenerator(gen);

        boolean isTempDotFile = dotFile == null;
        Path usedDotFile = createTempDotPathIfNull(dotFile, outputFile);
        try {
            LOGGER.trace("store dot file (temp={}): {}", isTempDotFile, usedDotFile);
            gen.store(usedDotFile);

//            TESTZWECKE
//            if(true) {
//                Path xxx = usedDotFile.resolveSibling("Daten-NoRatio.dot");
//                Files.copy(usedDotFile, xxx);
//                LOGGER.info("width={} height={}", getWidthToUse(), getHeightToUse());
//                throw new RuntimeException("KILL ME");
//            }

            LOGGER.trace("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(usedDotFile)
                        .setOutputFile(outputFile)
                        .setLayout(layoutAlgorithm)
                        .setFormat(outputFormat)
                        .enableNeatoPos(fixedNeatoPosition)
                        .peek(dp -> LOGGER.trace("cmd: {}", Arrays.toString(dp.buildCommand())))
                        .validate();
            } catch (Exception e) {
                LOGGER.error("dot process validation failed", e);
                return;
            }

            MutableBoolean canceled = MutableBoolean.falseValue();
            TimedTask timeout = new TimedTask(
                    TIMEOUT,
                    () -> {
                        LOGGER.warn("timeout! cancel process");
                        canceled.set(true);
                        process.cancelForcibly();
                    }
            );
            LOGGER.trace("start timeout: {} ms", TIMEOUT);
            timeout.start();

            LOGGER.trace("execute dot process");
            ProcessResult result;
            try {
                result = process.execute();
                timeout.cancel();
                LOGGER.trace("timeout canceled");
            } catch (Exception e) {
                LOGGER.error("dot process execution failed", e);
                return;
            }

            if(canceled.get()) {
                LOGGER.warn("process canceled -> timeout");
                throw new TimeoutException("process timeout");
            }

            if(result.isOk()) {
                LOGGER.trace("OK {}", result.printData(terminalCharset));
            } else {
                LOGGER.error("ERR {}", result.printErr(terminalCharset));
            }

            if(Files.exists(outputFile)) {
                LOGGER.trace("image created ({})", outputFile);
            } else {
                LOGGER.error("image not created ({})", outputFile);
            }

        } finally {
            if(isTempDotFile) {
                LOGGER.trace("temp-dot file ({}) deleted: {}", usedDotFile, Files.deleteIfExists(usedDotFile));
            } else {
                LOGGER.trace("keep dot file: {}", usedDotFile);
            }
        }
    }
}
