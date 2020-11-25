package de.unileipzig.irpact.experimental.input2;

import de.unileipzig.irpact.experimental.eval.Custom;
import de.unileipzig.irpact.v2.io.input2.InputResource;
import de.unileipzig.irpact.v2.io.input2.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.v2.io.input2.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.v2.io.input2.network.IGraphTopology;
import de.unileipzig.irpact.v2.io.input2.network.IWattsStrogatzModel;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.ParserInput;
import de.unileipzig.irptools.defstructure.Type;
import org.apache.commons.codec.binary.Base32;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
class Asd {

    @Test
    void asd() {
        List<ParserInput> classes = ParserInput.listOf(Type.INPUT,
                IConsumerAgentGroup.class,
                IFreeMultiGraphTopology.class,
                IGraphTopology.class,
                IWattsStrogatzModel.class
        );
        DefinitionCollection dcoll = AnnotationParser.parse(InputResource.DEFAULT, classes);
        dcoll.validate(true);
    }

    private static String toB32(String input) {
        return new String(new Base32().encode(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    private static String fromB32(String input) {
        return new String(new Base32().decode(input.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    @Test
    void lol() {
        String f = "plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))plus(plus(11,22),plus(2.22,3.33))";
        String b32 = new Base32().encodeAsString(f.getBytes(StandardCharsets.UTF_8));
        b32 = b32.replaceAll("=", "_");
        System.out.println(b32);
        b32 = b32.replaceAll("_", "=");
        String ff = new String(new Base32().decode(b32.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        System.out.println(ff);
    }

    @Test
    void lol2() {
        String f = "x * 2/3 + (4*5 - x)";
        String b32 = toB32(f);
        String ff = fromB32(b32);

        System.out.println(new Expression(ff, new Argument("x = 1")).calculate());
        System.out.println(new Expression(ff, new Argument("x = 2")).calculate());
        System.out.println(new Expression(ff, new Argument("x = 3")).calculate());
    }
}
