package de.unileipzig.irpact.experimental.b32act;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.util.IRPactJson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
@Disabled
class Xxxx {

    @Test
    void runIt() throws IOException {
        String b32 = "78KGK0FQG0OC501HS1I6ABJLDPKMOPB9E1T6IPPED5P70OB3EGN6KOB4CLS2SOB7CLN78SPECDNMSSRLDLIN4BIAC5I6AU23DTN76TBDCLP42PR5DPQ4ESJFELOFP01I8915AJC06CKG0FRO0000000000080D64G0QS901MV33CLJ6EQ39D9LMORBEDS9504IH29914L0IAK95G4IPFJ01N4IQ80E14N203I901GA06290CCL02H3FR";
        byte[] b = IRPactBase32.decodeString(b32);
        JsonNode node = IRPactJson.fromBytesWithSmile(b);
        System.out.println(IRPactJson.toString(node));
    }
}
/*
78KGK0FQG0OC501HS1I6ABJLDPKMOPB9E1T6IPPED5P70OB3EG
N6KOB4CLS2SOB7CLN78SPECDNMSSRLDLIN4BIAC5I6AU23DTN7
6TBDCLP42PR5DPQ4ESJFELOFP01I8915AJC06CKG0FRO000000
0000080D64G0QS901MV33CLJ6EQ39D9LMORBEDS9504IH29914
L0IAK95G4IPFJ01N4IQ80E14N203I901GA06290CCL02H3FR


 */