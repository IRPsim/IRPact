package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.io.param.EdnPath;
import de.unileipzig.irpact.io.param.IOConstants;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.ListEdnPath;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.distribution.*;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InROutputImage;
import de.unileipzig.irpact.io.param.input.interest.InProductGroupThresholdEntry;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.param.input.process.ra.InDisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InEntireNetworkNodeFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGroupBasedDeffuantUncertainty;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.*;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class InRootUI {

    public static final List<EdnPath> PATHS = new ArrayList<>();
    public static final EdnPath ROOT = new ListEdnPath(IOConstants.ROOT).addTo(PATHS); //!!!

    public static final EdnPath INFO = ROOT.resolve(IOConstants.INFORMATIONS).addTo(PATHS);
    public static final EdnPath INFO_ABOUTIRPACT = INFO.resolve(InIRPactVersion.thisName()).addTo(PATHS);
    public static final EdnPath INFO_ABOUTSCENARIO = INFO.resolve(InScenarioVersion.thisName()).addTo(PATHS);
    public static final EdnPath INFO_INFO = INFO.resolve(InInformation.thisName()).addTo(PATHS);

    public static final EdnPath SETT = ROOT.resolve(IOConstants.SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_GENERAL = SETT.resolve(IOConstants.GENERAL_SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_GENERAL_LOG = SETT_GENERAL.resolve(IOConstants.LOGGING).addTo(PATHS);
    public static final EdnPath SETT_SPECIAL = SETT.resolve(IOConstants.SPECIAL_SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_SPECIAL_BIN = SETT_SPECIAL.resolve(VisibleBinaryData.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT = SETT.resolve(IOConstants.RESULT_VISUALISATION).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_GENERIC = SETT_VISURESULT.resolve(InGenericOutputImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_GNU = SETT_VISURESULT.resolve(InGnuPlotOutputImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_R = SETT_VISURESULT.resolve(InROutputImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISUNETWORK = SETT.resolve(IOConstants.NETWORK_VISUALISATION).addTo(PATHS);
    public static final EdnPath SETT_VISUNETWORK_GENERAL = SETT_VISUNETWORK.resolve(InGraphvizGeneral.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISUNETWORK_AGENTCOLOR = SETT_VISUNETWORK.resolve(IOConstants.GRAPHVIZ_AGENT_COLOR_MAPPING).addTo(PATHS);
    public static final EdnPath SETT_DATAOUTPUT = SETT.resolve(IOConstants.DATA_OUTPUT).addTo(PATHS);

    public static final EdnPath ATTRNAMES = ROOT.resolve(InAttributeName.thisName()).addTo(PATHS);

    public static final EdnPath FILES = ROOT.resolve(IOConstants.FILES).addTo(PATHS);
    public static final EdnPath FILES_PV = FILES.resolve(InPVFile.thisName()).addTo(PATHS);
    public static final EdnPath FILES_SPATIAL = FILES.resolve(InSpatialTableFile.thisName()).addTo(PATHS);

    public static final EdnPath DISTRIBUTIONS = ROOT.resolve(IOConstants.DISTRIBUTIONS).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BERNOULLI = DISTRIBUTIONS.resolve(InBernoulliDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOOLEAN = DISTRIBUTIONS.resolve(InBooleanDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_DIRAC = DISTRIBUTIONS.resolve(InDiracUnivariateDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_MP = DISTRIBUTIONS.resolve(InFiniteMassPointsDiscreteDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_MP_MP = DISTRIBUTIONS_MP.resolve(InMassPoint.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_NORM = DISTRIBUTIONS.resolve(InNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDNORM = DISTRIBUTIONS.resolve(InBoundedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_SLOWTRUNCNORM = DISTRIBUTIONS.resolve(InSlowTruncatedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_TRUNCNORM = DISTRIBUTIONS.resolve(InTruncatedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIDOUBLE = DISTRIBUTIONS.resolve(InBoundedUniformDoubleDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIINT = DISTRIBUTIONS.resolve(InBoundedUniformIntegerDistribution.thisName()).addTo(PATHS);

    public static final EdnPath AGENTS = ROOT.resolve(IOConstants.AGENTS).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER = AGENTS.resolve(IOConstants.CONSUMER).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_GROUP).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP_GENERAL = AGENTS_CONSUMER_GROUP.resolve(InGeneralConsumerAgentGroup.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP_PVACT = AGENTS_CONSUMER_GROUP.resolve(InPVactConsumerAgentGroup.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_ATTR).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALGRP = AGENTS_CONSUMER_ATTR.resolve(InGeneralConsumerAgentGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALANNUAL = AGENTS_CONSUMER_ATTR.resolve(InGeneralConsumerAgentAnnualGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITGRP = AGENTS_CONSUMER_ATTR.resolve(InNameSplitConsumerAgentGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITANNUAL = AGENTS_CONSUMER_ATTR.resolve(InNameSplitConsumerAgentAnnualGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_AFFINITY).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_GRP = AGENTS_CONSUMER_AFF.resolve(InAffinities.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_COMPLEX = AGENTS_CONSUMER_AFF.resolve(InComplexAffinityEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_NAMESPLIT = AGENTS_CONSUMER_AFF.resolve(InNameSplitAffinityEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_INTEREST).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD = AGENTS_CONSUMER_INTEREST.resolve(InProductThresholdInterestSupplyScheme.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY = AGENTS_CONSUMER_INTEREST_THRESHOLD.resolve(InProductGroupThresholdEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP = AGENTS.resolve(IOConstants.POPULATION).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FIX = AGENTS_POP.resolve(InFixConsumerAgentPopulation.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FILE = AGENTS_POP.resolve(InFileBasedConsumerAgentPopulation.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FILEPVACT = AGENTS_POP.resolve(InFileBasedPVactConsumerAgentPopulation.thisName()).addTo(PATHS);

    public static final EdnPath NETWORK = ROOT.resolve(IOConstants.NETWORK).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO = NETWORK.resolve(IOConstants.TOPOLOGY).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_UNLINKED = NETWORK_TOPO.resolve(InUnlinkedGraphTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_COMPLETE = NETWORK_TOPO.resolve(InCompleteGraphTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_FREE = NETWORK_TOPO.resolve(InFreeNetworkTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_FREE_TIES = NETWORK_TOPO_FREE.resolve(InNumberOfTies.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC = NETWORK.resolve(IOConstants.DIST_FUNC).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC_NO = NETWORK.resolve(InNoDistance.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC_INVERSE = NETWORK.resolve(InInverse.thisName()).addTo(PATHS);

    public static final EdnPath PRODUCTS = ROOT.resolve(IOConstants.PRODUCTS).addTo(PATHS);
    public static final EdnPath PRODUCTS_GROUP = PRODUCTS.resolve(IOConstants.PRODUCTS_GROUP).addTo(PATHS);
    public static final EdnPath PRODUCTS_GROUP_BASIC = PRODUCTS_GROUP.resolve(InBasicProductGroup.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR = PRODUCTS.resolve(IOConstants.PRODUCTS_ATTR).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR_BASIC = PRODUCTS_ATTR.resolve(InBasicProductGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR_SPLIT = PRODUCTS_ATTR.resolve(InNameSplitProductGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FIX = PRODUCTS.resolve(InFixProduct.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FIXATTR = PRODUCTS.resolve(InFixProductAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FINDSCHE = PRODUCTS.resolve(IOConstants.PRODUCTS_FINDING_SCHEME).addTo(PATHS);
    public static final EdnPath PRODUCTS_FINDSCHE_FIX = PRODUCTS_FINDSCHE.resolve(InFixProductFindingScheme.thisName()).addTo(PATHS);

    public static final EdnPath PROCESS = ROOT.resolve(IOConstants.PROCESS_MODEL).addTo(PATHS);
    public static final EdnPath PROCESS_RA = PROCESS.resolve(InRAProcessModel.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_RA_UNCERT = PROCESS_RA.resolve(IOConstants.PROCESS_MODEL_RA_UNCERT).addTo(PATHS);
    public static final EdnPath PROCESS_RA_UNCERT_GLDEFF = PROCESS_RA_UNCERT.resolve(InGlobalDeffuantUncertainty.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_RA_UNCERT_GLDEFFPV = PROCESS_RA_UNCERT.resolve(InPVactGlobalDeffuantUncertainty.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_RA_UNCERT_GBDEFF = PROCESS_RA_UNCERT.resolve(InGroupBasedDeffuantUncertainty.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_RA_UNCERT_BGDEFFPV = PROCESS_RA_UNCERT.resolve(InPVactGroupBasedDeffuantUncertainty.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_FILTER = PROCESS.resolve(IOConstants.PROCESS_FILTER).addTo(PATHS);
    public static final EdnPath PROCESS_FILTER_DISABLED = PROCESS_FILTER.resolve(InDisabledProcessPlanNodeFilterScheme.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_FILTER_ENTIRE = PROCESS_FILTER.resolve(InEntireNetworkNodeFilterScheme.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_FILTER_MAX = PROCESS_FILTER.resolve(InRAProcessPlanMaxDistanceFilterScheme.thisName()).addTo(PATHS);

    public static final EdnPath SPATIAL = ROOT.resolve(IOConstants.SPATIAL).addTo(PATHS);
    public static final EdnPath SPATIAL_MODEL = SPATIAL.resolve(IOConstants.SPATIAL_MODEL).addTo(PATHS);
    public static final EdnPath SPATIAL_MODEL_2D = SPATIAL_MODEL.resolve(InSpace2D.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST = SPATIAL.resolve(IOConstants.SPATIAL_MODEL_DIST).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE = SPATIAL_DIST.resolve(IOConstants.SPATIAL_MODEL_DIST_FILE).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS = SPATIAL_DIST_FILE.resolve(IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_ALL = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECT = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSelectSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSelectGroupSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEU = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedPVactMilieuSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedPVactMilieuZipSupplier.thisName()).addTo(PATHS);

    public static final EdnPath TIME = ROOT.resolve(IOConstants.TIME).addTo(PATHS);
    public static final EdnPath TIME_DISCRET = TIME.resolve(InDiscreteTimeModel.thisName()).addTo(PATHS);
    public static final EdnPath TIME_UNITDISCRET = TIME.resolve(InUnitStepDiscreteTimeModel.thisName()).addTo(PATHS);

    public static EdnPath SUBMODULE = ROOT.resolve(IOConstants.SUBMODULE).addTo(PATHS);
    public static EdnPath SUBMODULE_GV = SUBMODULE.resolve(IOConstants.SUBMODULE_GRAPHVIZDEMO).addTo(PATHS);

    public static EdnPath DEV = ROOT.resolve(IOConstants.DEV).addTo(PATHS);
    public static EdnPath DEV_TEST = DEV.resolve(IOConstants.TEST).addTo(PATHS);
    public static EdnPath DEV_TEST_DATA = DEV_TEST.resolve(InTestData.thisName()).addTo(PATHS);

    public InRootUI() {
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static void initRes(TreeAnnotationResource res) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();

        for(EdnPath path: PATHS) {
            if(path == ROOT) {
                //ignore
            }
            else {
                addPathElement(res, path);
            }
        }
    }

    public static void applyRes(TreeAnnotationResource res) {
        //optact rest
        res.getCachedElement("OPTACT").setParent(res.getCachedElement(SUBMODULE.getLast()));
        res.getCachedElement("AgentGroup_Element").setParent(res.getCachedElement("OPTACT"));
        res.getCachedElement(IOConstants.GRAPHVIZ).setParent(res.getCachedElement("OPTACT"));
    }
}
