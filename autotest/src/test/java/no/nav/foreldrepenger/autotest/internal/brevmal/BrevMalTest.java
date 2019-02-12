package no.nav.foreldrepenger.autotest.internal.brevmal;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.domain.foreldrepenger.BrevMalXml;
import no.nav.foreldrepenger.autotest.domain.foreldrepenger.KlageMedholUgunstNFP;

@Tag("internal")
public class BrevMalTest {

    @Test
    public void skalKunneOprettesFraText() throws Exception {
        BrevMalXml xml = KlageMedholUgunstNFP.fromString("<root></root>");
    }
    
    @Test
    public void skalKunneOprettesFraFil() throws Exception {
        BrevMalXml xml = KlageMedholUgunstNFP.fromFile("src/test/resources/docprodXml/klageMedholUgunstNFP61.xml");
    }
    
    @Test
    public void skalKunneOprettesFraRessurs() throws Exception {
        BrevMalXml xml = KlageMedholUgunstNFP.fromResource("klageMedholUgunstNFP61.xml");
    }
    
    @Test
    public void skalVæreLike() throws Exception {
        BrevMalXml xml1 = KlageMedholUgunstNFP.fromResource("klageMedholUgunstNFP61.xml");
        BrevMalXml xml2 = KlageMedholUgunstNFP.fromResource("klageMedholUgunstNFP61.xml");
        
        assertTrue(xml1.isComparable(xml2));
    }
    
    @Test
    public void skalIkkeVæreLike() throws Exception {
        BrevMalXml xml1 = KlageMedholUgunstNFP.fromResource("klageMedholUgunstNFP61.xml");
        BrevMalXml xml2 = KlageMedholUgunstNFP.fromResource("klageMedholUgunstNFP114.xml");
        
        assertTrue(!xml1.isComparable(xml2));
    }
}
