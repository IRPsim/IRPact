package de.unileipzig.irpact.io.spec.impl.process;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.InOrientationSupplier;
import de.unileipzig.irpact.io.param.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.InSlopeSupplier;
import de.unileipzig.irpact.io.param.input.process.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.SpecUtil;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class RAProcessModelSpec implements ToSpecConverter<InRAProcessModel>, ToParamConverter<InRAProcessModel> {

    public static final RAProcessModelSpec INSTANCE = new RAProcessModelSpec();
    public static final String TYPE = "RAProcessModel";

    @Override
    public Class<InRAProcessModel> getParamType() {
        return InRAProcessModel.class;
    }

    @Override
    public void toSpec(InRAProcessModel input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getProcessModel().get(), manager, converter, inline);
    }

    @Override
    public void create(InRAProcessModel input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_a, input.getA());
        spec.setParameters(TAG_b, input.getB());
        spec.setParameters(TAG_c, input.getC());
        spec.setParameters(TAG_d, input.getD());
        spec.setParameters(TAG_adopterPoints, input.getAdopterPoints());
        spec.setParameters(TAG_interestedPoints, input.getInterestedPoints());
        spec.setParameters(TAG_awarePoints, input.getAwarePoints());
        spec.setParameters(TAG_unknownPoints, input.getUnknownPoints());
        spec.setParameters(TAG_file, input.getPvFile().getName());
        //erstmal ignorieren
        //SlopeSupplier
        //OrientationSupplier

        SpecUtil.inlineArray(
                input.getUncertaintyGroupAttributes(), InUncertaintyGroupAttribute::getName,
                spec.getArraySpec(TAG_uncertainties),
                manager,
                converter,
                true
        );

        converter.callToSpec(input.getPvFile(), manager, inline);
    }

    @Override
    public InRAProcessModel[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getProcessModel().get());

        InUncertaintyGroupAttribute[] uncertAttr = SpecUtil.parseInlinedArray(
                spec.getArraySpec(TAG_uncertainties),
                name -> { throw new UnsupportedOperationException(); },
                _spec -> UncertaintyGroupAttributeSpec.INSTANCE.toParam(_spec.rootAsObject(), manager, converter, cache),
                InUncertaintyGroupAttribute[]::new
        );

        InRAProcessModel model = new InRAProcessModel(
                spec.getName(),
                spec.getParametersDouble(TAG_a), spec.getParametersDouble(TAG_b), spec.getParametersDouble(TAG_c), spec.getParametersDouble(TAG_d),
                spec.getParametersInt(TAG_adopterPoints), spec.getParametersInt(TAG_interestedPoints), spec.getParametersInt(TAG_awarePoints), spec.getParametersInt(TAG_unknownPoints),
                (InPVFile) converter.getFile(spec.getParametersText(TAG_file), manager, cache),
                new InSlopeSupplier[0],
                new InOrientationSupplier[0],
                uncertAttr
        );

        return new InRAProcessModel[]{model};
    }
}
