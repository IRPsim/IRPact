package de.unileipzig.irpact.io.input;

import de.unileipzig.irpact.io.input.affinity.IBasicAffinitiesEntry;
import de.unileipzig.irpact.io.input.affinity.IBasicAffinityMapping;
import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.input.awareness.IAwareness;
import de.unileipzig.irpact.io.input.awareness.IFixedProductAwareness;
import de.unileipzig.irpact.io.input.awareness.ISimpleAwareness;
import de.unileipzig.irpact.io.input.awareness.IThresholdAwareness;
import de.unileipzig.irpact.io.input.distribution.*;
import de.unileipzig.irpact.io.input.network.IFastHeterogeneousSmallWorldTopology;
import de.unileipzig.irpact.io.input.network.IFastHeterogeneousSmallWorldTopologyEntry;
import de.unileipzig.irpact.io.input.product.IFixedProduct;
import de.unileipzig.irpact.io.input.product.IFixedProductAttribute;
import de.unileipzig.irpact.io.input.product.IProductGroup;
import de.unileipzig.irpact.io.input.product.IProductGroupAttribute;
import de.unileipzig.irpact.io.input.spatial.ISpace2D;
import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irpact.io.input.time.IContinuousTimeModel;
import de.unileipzig.irpact.io.input.time.IDiscreteTimeModel;
import de.unileipzig.irpact.io.input.time.ITimeModel;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.RootClass;
import de.unileipzig.irptools.defstructure.Type;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class IRoot implements RootClass {

    public static final List<ParserInput> CLASSES = CollectionUtil.arrayListOf(
            //affinity
            ParserInput.newInstance(Type.INPUT, IBasicAffinitiesEntry.class),
            ParserInput.newInstance(Type.INPUT, IBasicAffinityMapping.class),
            //agent-consumer
            ParserInput.newInstance(Type.INPUT, IConsumerAgentGroup.class),
            ParserInput.newInstance(Type.INPUT, IConsumerAgentGroupAttribute.class),
            //awareness
            ParserInput.newInstance(Type.INPUT, IAwareness.class),
            ParserInput.newInstance(Type.INPUT, IFixedProductAwareness.class),
            ParserInput.newInstance(Type.INPUT, ISimpleAwareness.class),
            ParserInput.newInstance(Type.INPUT, IThresholdAwareness.class),
            //distribution
            ParserInput.newInstance(Type.INPUT, IBooleanDistribution.class),
            ParserInput.newInstance(Type.INPUT, IConstantUnivariateDistribution.class),
            ParserInput.newInstance(Type.INPUT, IFiniteMassPointsDiscreteDistribution.class),
            ParserInput.newInstance(Type.INPUT, IMassPoint.class),
            ParserInput.newInstance(Type.INPUT, IRandomBoundedDistribution.class),
            ParserInput.newInstance(Type.INPUT, IUnivariateDoubleDistribution.class),
            //network
            ParserInput.newInstance(Type.INPUT, IFastHeterogeneousSmallWorldTopology.class),
            ParserInput.newInstance(Type.INPUT, IFastHeterogeneousSmallWorldTopologyEntry.class),
            //product
            ParserInput.newInstance(Type.INPUT, IFixedProduct.class),
            ParserInput.newInstance(Type.INPUT, IFixedProductAttribute.class),
            ParserInput.newInstance(Type.INPUT, IProductGroup.class),
            ParserInput.newInstance(Type.INPUT, IProductGroupAttribute.class),
            //spatial
            ParserInput.newInstance(Type.INPUT, ISpace2D.class),
            //time
            ParserInput.newInstance(Type.INPUT, IContinuousTimeModel.class),
            ParserInput.newInstance(Type.INPUT, IDiscreteTimeModel.class),
            ParserInput.newInstance(Type.INPUT, ITimeModel.class),
            //rest
            ParserInput.newInstance(Type.INPUT, IGeneralSettings.class),
            ParserInput.newInstance(Type.INPUT, IRoot.class)
    );

    @FieldDefinition
    public IConsumerAgentGroup[] consumerAgentGroups;

    @Todo("remove array")
    @FieldDefinition
    public IBasicAffinityMapping[] affinityMapping;

    @FieldDefinition
    public IProductGroup[] productGroups;

    @FieldDefinition
    public IFixedProduct[] fixedProducts;

    @FieldDefinition
    public IFixedProductAwareness[] fixedProductAwarenesses;

    @Todo("remove array")
    @FieldDefinition
    public ISpace2D[] spatialModel;

    @Todo("remove array")
    @FieldDefinition
    public ITimeModel[] timeModel;

    @Todo("remove array")
    @Todo("interface einfuegen")
    @FieldDefinition
    public IFastHeterogeneousSmallWorldTopology[] topology;

    @FieldDefinition
    public IGeneralSettings generalSettings;

    public IRoot() {
    }

    @Override
    public Collection<? extends ParserInput> getInput() {
        return CLASSES;
    }
}
