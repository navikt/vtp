package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import org.w3c.dom.Document;

public class VedtakOmMedhold000114 extends BrevMalXml{

    public VedtakOmMedhold000114(Document root) {
        super(root);
    }
    
    public static VedtakOmMedhold000114 fromString(String text) throws Exception {
        return fromString(text, VedtakOmMedhold000114.class);
    }
    
    public static VedtakOmMedhold000114 fromFile(String path) throws Exception {
        return fromFile(path, VedtakOmMedhold000114.class);
    }
    
    public static VedtakOmMedhold000114 fromResource(String path) throws Exception  {
        return fromResource(path, VedtakOmMedhold000114.class);
    }

    public boolean isComparable(BrevMalXml otherXml) {
        boolean result = true;
        
        result = result && isComparableNode("fag ytelseType", otherXml);
        result = result && isComparableNode("fag opphavType", otherXml);
        result = result && isComparableNode("fag fritekst", otherXml);
        result = result && isComparableNode("fag klageFristUker", otherXml);
        
        return result;
    }
}
