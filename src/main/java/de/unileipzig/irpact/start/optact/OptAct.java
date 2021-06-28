package de.unileipzig.irpact.start.optact;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.graph.DirectedAdjacencyListMultiGraph;
import de.unileipzig.irpact.commons.graph.topology.AbstractMultiGraphTopology;
import de.unileipzig.irpact.commons.graph.topology.GraphTopology;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.io.param.irpopt.SideCustom;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.graphviz.DotProcess;
import de.unileipzig.irptools.graphviz.GraphvizGenerator;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.def.GraphvizColor;
import de.unileipzig.irptools.io.ContentType;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.data.AnnualEntry;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class OptAct {

    private static final IRPLogger logger = IRPLogging.getLogger(OptAct.class);

    private final MainCommandLineOptions clOptions;
    private final ObjectNode inRoot;

    public OptAct(MainCommandLineOptions clOptions, ObjectNode inRoot) {
        this.clOptions = clOptions;
        this.inRoot = inRoot;
    }

    private static double calc_par_out_IuOSonnentankNetzversorgung_Summe(SideCustom agentGrp, double mwst) {
        double sumOrId = agentGrp.timeStuff.hasData()
                ? Arrays.stream(agentGrp.timeStuff.toArray()).sum()
                : Integer.parseInt(agentGrp.timeStuff.getId());
        return sumOrId * mwst;
    }

    private static int calc_par_out_S_DS(SideCustom agentGrp) {
        return agentGrp.number + agentGrp.delta;
    }

    private Converter createInputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(InRoot.INPUT_WITH_GRAPHVIZ);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private Converter createOutputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(OutRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private AnnualEntry<InRoot> loadInput(Converter converter) throws IOException {
        logger.trace("parse input file");
        ContentType contentType = ContentTypeDetector.detect(inRoot);
        logger.trace("content type: {}", contentType);
        logger.trace("deserialize input file");
        return ContentTypeDetector.parseFirstEntry(inRoot, contentType, converter);
    }

    private OutRoot createOutputRoot(InRoot inputData) {
        List<OutCustom> outGrps = new ArrayList<>();
        for(SideCustom agentGrp: inputData.customs) {
            OutCustom outGrp = new OutCustom(agentGrp._name);
            outGrp.summe = calc_par_out_IuOSonnentankNetzversorgung_Summe(agentGrp, inputData.global.mwst);
            outGrp.numberOut = calc_par_out_S_DS(agentGrp);
            outGrps.add(outGrp);
        }

        OutRoot outRoot = new OutRoot();
        outRoot.outGrps = outGrps.toArray(new OutCustom[0]);
        return outRoot;
    }

    private AnnualData<OutRoot> createOutputData(AnnualEntry<InRoot> inputEntry, OutRoot outRoot) {
        AnnualData<OutRoot> outData = new AnnualData<>(outRoot);
        outData.getConfig().copyFrom(inputEntry.getConfig());
        return outData;
    }

    public void start() {
        AnnualEntry<InRoot> inputEntry;
        try {
            inputEntry = loadInput(createInputConverter());
        } catch (IOException e) {
            logger.error("parsing and deserialization failed", e);
            return;
        }

        try {
            if(clOptions.getImagePath() != null) {
                runImageDemo(inputEntry);
            }
            if(!clOptions.isNoSimulation()) {
                runOptActDemo(inputEntry);
            }
            logger.trace("IRPact finished");
        } catch (IOException e) {
            logger.error("IRPact failed", e);
        }
    }

    private void runImageDemo(AnnualEntry<InRoot> inputEntry) throws IOException {
        logger.trace("run image demo");
        InRoot root = inputEntry.getData();

        LayoutAlgorithm layoutAlgorithm = root.getLayoutAlgorithm();
        if(layoutAlgorithm == null) {
            logger.error("Keine gültiger LayoutAlgorithm gefunden!");
            return;
        }

        OutputFormat outputFormat = root.getOutputFormat();
        if(outputFormat == null) {
            logger.error("Keine gültiges OutputFormat gefunden!");
            return;
        }

        DirectedAdjacencyListMultiGraph<Node, Link, String> g = new DirectedAdjacencyListMultiGraph<>();
        for(AgentGroup grp: root.agentGroups) {
            logger.trace("Gruppe: '{}', Agenten: {}", grp._name, grp.numberOfAgents);
            for(int i = 0; i < grp.numberOfAgents; i++) {
                g.addVertex(new Node(grp._name + "#" + i));
            }
        }
        logger.trace("agents: " + g.vertexCount());

        GraphTopology<Node, Link> topo = root.getTopology();
        if(topo == null) {
            logger.error("Keine gültige Topologie gefunden!");
            return;
        }
        ((AbstractMultiGraphTopology<Node, Link, String>) topo).setEdgeType("x");
        ((AbstractMultiGraphTopology<Node, Link, String>) topo).setMultiEdgeCreatorFunction(Link::create);

        logger.trace("initalize edges");
        topo.initalizeEdges(g);
        logger.trace("edges: " + g.edgeCount());

        GraphvizGenerator<Node, Link> gen = new GraphvizGenerator<>(
                Node::getLabel,
                Link::getSource,
                Link::getTarget
        );
        gen.setDirected(true);
        gen.addAllNodes(g.getVertices());
        gen.addAllLinks(g.getEdges("x"));
        gen.configureNodes((n, gv) -> {
            GraphvizColor gcolor = root.getColor(n.getLabel());
            if(gcolor == null) {
                gcolor = GraphvizColor.BLACK;
            }
            gv.add(gcolor.toGraphvizColor());
        });
        gen.configureLinks((l, gl) -> {
            GraphvizColor gcolor = root.getColor(l.getSource().getLabel());
            if(gcolor == null) {
                gcolor = GraphvizColor.BLACK;
            }
            gl.add(gcolor.toGraphvizColor());
        });
        gen.setHideNodeLabels(true);
        gen.setNodeShape(guru.nidi.graphviz.attribute.Shape.POINT);

        Path imagePath = clOptions.getImagePath();
        Path dotPath = imagePath.resolveSibling(imagePath.getFileName().toString() + ".dot");
        try {
            logger.trace("store temp-dot file: {}", dotPath);
            gen.store(dotPath);

            logger.trace("init dot process");
            DotProcess process;
            try {
                process = new DotProcess()
                        .setInputFile(dotPath)
                        .setOutputFile(imagePath)
                        .setLayout(layoutAlgorithm)
                        .setFormat(outputFormat)
                        .peek(dp -> logger.trace("cmd: {}", Arrays.toString(dp.buildCommand())))
                        .validate();
            } catch (Exception e) {
                logger.error("dot process validation failed", e);
                return;
            }

            logger.trace("execute dot process");

            ProcessResult result;
            try {
                result = process.execute();
            } catch (Exception e) {
                logger.error("dot process execution failed", e);
                return;
            }

            if(result.isOk()) {
                logger.trace("OK {}", result.printData(Util.IBM850()));
            } else {
                logger.error("ERR {}", result.printErr(Util.IBM850()));
            }

            if(Files.exists(imagePath)) {
                logger.trace("image created ({})", imagePath);
            } else {
                logger.error("image not created ({})", imagePath);
            }
        } finally {
            if(Files.deleteIfExists(dotPath)) {
                logger.trace("temp-dot file deleted: {}", dotPath);
            }
        }
    }

    private void runOptActDemo(AnnualEntry<InRoot> inputEntry) {
        logger.trace("run optact demo");
        if(clOptions.isNoSimulation()) {
            logger.warn("no simulation");
            return;
        }

        storeOutputToAnnualFile(inputEntry);
    }

    private void storeOutputToAnnualFile(AnnualEntry<InRoot> inputEntry) {
        logger.trace("generate output data");
        OutRoot outRoot = createOutputRoot(inputEntry.getData());
        logger.trace("create raw output");
        AnnualData<OutRoot> outData = createOutputData(inputEntry, outRoot);
        logger.trace("serialize output");
        try {
            AnnualFile outFile = outData.serialize(createOutputConverter());
            outFile.store(clOptions.getOutputPath());
        } catch (Throwable t) {
            logger.error("serialization failed", t);
        }
    }

    //Helper Graph
    private static class Node {
        private final String label;

        private Node(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    //Helper Graph
    private static class Link {
        private final Node source;
        private final Node target;

        private Link(Node source, Node target) {
            this.source = source;
            this.target = target;
        }

        private static Link create(Node source, Node target, String type) {
            return new Link(source, target);
        }

        public Node getSource() {
            return source;
        }

        public Node getTarget() {
            return target;
        }
    }
}
