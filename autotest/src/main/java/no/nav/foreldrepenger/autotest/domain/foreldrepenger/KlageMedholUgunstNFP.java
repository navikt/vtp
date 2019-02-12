package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import org.w3c.dom.Document;

public class KlageMedholUgunstNFP extends BrevMalXml{

    public KlageMedholUgunstNFP(Document root) {
        super(root);
    }
    
    public static KlageMedholUgunstNFP fromString(String text) throws Exception {
        return fromString(text, KlageMedholUgunstNFP.class);
    }
    
    public static KlageMedholUgunstNFP fromFile(String path) throws Exception {
        return fromFile(path, KlageMedholUgunstNFP.class);
    }
    
    public static KlageMedholUgunstNFP fromResource(String path) throws Exception  {
        return fromResource(path, KlageMedholUgunstNFP.class);
    }

    public boolean isComparable(BrevMalXml otherXml) {
        boolean result = true;
        
        result = result && isComparableNode("fag dagsats", otherXml);
        
        return result;
    }
}
