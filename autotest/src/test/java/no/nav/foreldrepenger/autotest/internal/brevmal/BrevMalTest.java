package no.nav.foreldrepenger.autotest.internal.brevmal;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import no.nav.foreldrepenger.autotest.domain.foreldrepenger.BrevMalXml;
import no.nav.foreldrepenger.autotest.domain.foreldrepenger.VedtakOmMedhold000114;

@Execution(ExecutionMode.CONCURRENT)
@Tag("internal")
public class BrevMalTest {

    @Test
    public void skalKunneOprettesFraText() throws Exception {
        BrevMalXml xml = VedtakOmMedhold000114.fromString("<root></root>");
    }
    
    @Test
    public void skalKunneOprettesFraFil() throws Exception {
        BrevMalXml xml = VedtakOmMedhold000114.fromFile("src/test/resources/docprodXml/klageMedholUgunstNFP61.xml");
    }
    
    @Test
    public void skalKunneOprettesFraRessurs() throws Exception {
        BrevMalXml xml = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP61.xml");
    }
    
    @Test
    public void skalVæreLike() throws Exception {
        BrevMalXml xml1 = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP61.xml");
        BrevMalXml xml2 = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP61.xml");
        
        assertTrue(xml1.isComparable(xml2));
    }
    
    @Test
    public void skalIkkeVæreLike() throws Exception {
        BrevMalXml xml1 = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP61.xml");
        BrevMalXml xml2 = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP114.xml");
        
        assertTrue(!xml1.isComparable(xml2));
    }
    
    @Test
    public void skalKunnePrintes() throws Exception {
        BrevMalXml xml1 = VedtakOmMedhold000114.fromResource("klageMedholUgunstNFP61.xml");
        System.out.println(xml1.toString());
    }
}
