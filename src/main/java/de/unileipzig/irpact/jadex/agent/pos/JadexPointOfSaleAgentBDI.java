package de.unileipzig.irpact.jadex.agent.pos;

import de.unileipzig.irpact.core.agent.pos.*;
import de.unileipzig.irpact.core.currency.NullPrice;
import de.unileipzig.irpact.core.currency.Price;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.availability.NullProductAvailability;
import de.unileipzig.irpact.core.product.availability.ProductAvailability;
import de.unileipzig.irpact.core.product.availability.ProductAvailabilityChange;
import de.unileipzig.irpact.core.product.availability.ProductSoldOut;
import de.unileipzig.irpact.core.product.price.ProductPriceChange;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices({
        @ProvidedService(type = PointOfSaleAgentService.class),
        @ProvidedService(type = JadexAgentService.class)
})
@Agent(type = BDIAgentFactory.TYPE)
public class JadexPointOfSaleAgentBDI extends JadexAgentBase
        implements PointOfSaleAgent, PointOfSaleAgentService, JadexAgentService {

    //Argument names
    public static final String AGENT_BASE = StartSimulation.AGENT_BASE;

    //general
    private static final Logger logger = LoggerFactory.getLogger(JadexPointOfSaleAgentBDI.class);

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;

    //ConsumerAgent parameter
    protected PointOfSaleAgentBase agentBase;

    //Beliefs

    //=========================
    //Constructer
    //=========================

    public JadexPointOfSaleAgentBDI() {
    }

    //=========================
    //ConsumerAgent
    //=========================


    @Override
    public double getInformationAuthority() {
        return agentBase.getInformationAuthority();
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return agentBase.getSpatialInformation();
    }

    @Override
    public String getName() {
        return agentBase.getName();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) agentBase.getEnvironment();
    }

    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case AGENT:
            case INFORMATION_AGENT:
            case SPATIAL_AGENT:
            case SPATIAL_INFORMATION_AGENT:
            case POINT_OF_SALE_AGENT:
                return true;

            default:
                return false;
        }
    }

    @Override
    public Map<Product, ProductAvailability> getProductAvailability() {
        return agentBase.getProductAvailability();
    }

    @Override
    public Map<Product, Price> getProductPrices() {
        return agentBase.getProductPrices();
    }

    @Override
    public ProductSoldOutScheme getProductSoldOutScheme() {
        return agentBase.getProductSoldOutScheme();
    }

    @Override
    public Price requestPrice(Product product) {
        return agentBase.requestPrice(product);
    }

    @Override
    public ProductPriceChangeScheme getProductPriceChangeScheme() {
        return agentBase.getProductPriceChangeScheme();
    }

    @Override
    public void updatePrice(Product product, Price newPrice) {
        Price oldPrice = getProductPrices().put(product, newPrice);
        ProductPriceChange event = new ProductPriceChange(
                product,
                NullPrice.check(oldPrice),
                newPrice
        );
        bdiFeature.dispatchTopLevelGoal(new ProductPriceChangeGoal(event));
    }

    @Override
    public boolean buyProduct(Product product) {
        ProductAvailability availability = getProductAvailability().get(product);
        if(availability == null || availability.isNull()) {
            throw new IllegalStateException("Unknown product: " + product.getName());
        }
        if(availability.isAvailable()) {
            availability.decrement();
            if(!availability.isAvailable()) {
                ProductSoldOut event = new ProductSoldOut(product, availability);
                bdiFeature.dispatchTopLevelGoal(new ProductSoldOutGoal(event));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateAvailability(Product product, ProductAvailability newAvailability) {
        ProductAvailability oldAvailability = getProductAvailability().put(product, newAvailability);
        ProductAvailabilityChange event = new ProductAvailabilityChange(
                product,
                NullProductAvailability.check(oldAvailability),
                newAvailability
        );
        bdiFeature.dispatchTopLevelGoal(new ProductAvailabilityChangeGoal(event));
    }

    @Override
    public ProductAvailabilityChangeScheme getProductAvailabilityChangeScheme() {
        return agentBase.getProductAvailabilityChangeScheme();
    }

    //=========================
    //JadexAgentBase
    //=========================

    @Override
    protected Logger logger() {
        return logger;
    }

    @Override
    protected IComponentIdentifier getCompnentIdentifier() {
        return agent.getId();
    }

    @Override
    protected IMessageFeature getMessageFeature() {
        return msgFeature;
    }

    @Override
    protected void initArgs(Map<String, Object> args) {
        try {
            agentBase = get(args, AGENT_BASE);
        } catch (Throwable t) {
            String _name = agentBase == null
                    ? getClass().getSimpleName()
                    : getName();
            logger.error("[" + _name + "] initArgs error", t);
            throw t;
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        getEnvironment().getConfiguration() .register(agent.getExternalAccess(), this);
        initMessageHandler();
        logger.trace("[{}] onInit", getName());
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.trace("[{}] onStart", getName());
    }

    @OnEnd
    @Override
    protected void onEnd() {
        logger.trace("[{}] onEnd", getName());
    }

    //=========================
    //CompanyAgentService
    //=========================

    @Override
    public JadexPointOfSaleAgentBDI getPointOfSaleAgentSyn() {
        return this;
    }

    @Override
    public IFuture<JadexPointOfSaleAgentBDI> getPointOfSaleAgentAsyn() {
        return new Future<>(this);
    }

    //=========================
    //JadexAgentService
    //=========================

    @Override
    public JadexPointOfSaleAgentBDI getAgentSyn() {
        return getPointOfSaleAgentSyn();
    }

    @Override
    public IFuture<JadexPointOfSaleAgentBDI> getAgentAsyn() {
        return getPointOfSaleAgentAsyn();
    }

    //=========================
    //announce new products goal
    //=========================

    @Goal
    public class ProductAvailabilityChangeGoal {

        protected ProductAvailabilityChange event;

        public ProductAvailabilityChangeGoal(ProductAvailabilityChange event) {
            this.event = event;
        }

        public ProductAvailabilityChange getEvent() {
            return event;
        }
    }

    @Plan(trigger = @Trigger(goals = ProductAvailabilityChangeGoal.class))
    protected void handleProductAvailabilityChange(ProductAvailabilityChangeGoal goal) {
        ProductAvailabilityChange event = goal.getEvent();
        getProductAvailabilityChangeScheme().handle(
                this,
                event.getProduct(),
                event.getOldAvailability(),
                event.getNewAvailability()
        );
    }

    //=========================
    //announce new product price
    //=========================

    @Goal
    public class ProductPriceChangeGoal {

        protected ProductPriceChange event;

        public ProductPriceChangeGoal(ProductPriceChange event) {
            this.event = event;
        }

        public ProductPriceChange getEvent() {
            return event;
        }
    }

    @Plan(trigger = @Trigger(goals = ProductPriceChangeGoal.class))
    protected void handleProductPriceChange(ProductPriceChangeGoal goal) {
        ProductPriceChange event = goal.getEvent();
        getProductPriceChangeScheme().handle(
                this,
                event.getProduct(),
                event.getOldPrice(),
                event.getNewPrice()
        );
    }

    //=========================
    //announce product sold out
    //=========================

    @Goal
    public class ProductSoldOutGoal {

        protected ProductSoldOut event;

        public ProductSoldOutGoal(ProductSoldOut event) {
            this.event = event;
        }

        public ProductSoldOut getEvent() {
            return event;
        }
    }

    @Plan(trigger = @Trigger(goals = ProductSoldOutGoal.class))
    protected void handleProductSoldOut(ProductSoldOutGoal goal) {
        ProductSoldOut event = goal.getEvent();
        getProductSoldOutScheme().handle(
                this,
                event.getProduct(),
                event.getAvailability()
        );
    }
}
