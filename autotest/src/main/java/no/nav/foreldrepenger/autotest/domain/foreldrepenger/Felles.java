package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import org.w3c.dom.Document;

public class Felles extends BrevMalXml{

    public Felles(Document root) {
        super(root);
    }
    
    public static Felles fromString(String text) throws Exception {
        return fromString(text, Felles.class);
    }
    
    public static Felles fromFile(String path) throws Exception {
        return fromFile(path, Felles.class);
    }
    
    public static Felles fromResource(String path) throws Exception  {
        return fromResource(path, Felles.class);
    }

    public boolean isComparable(BrevMalXml otherXml) {
        /*
        boolean result = true;
        
        result = result && isComparableNode("fell dagsats", otherXml);
        
        
        return result;
        */
        return false;
    }
    
    public boolean valider() {
        boolean result = true;
        
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles fagsaksnummer").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        result = result && !getNodeText("felles spraakkode").equals("");
        
        return result;
    }
    
}
