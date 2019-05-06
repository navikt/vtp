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

    @Override
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
        result = result && !getNodeText("felles signerendeSaksbehandler signerendeSaksbehandlerNavn").equals("");
        result = result && !getNodeText("felles automatiskBehandlet").equals("");
        result = result && !getNodeText("felles sakspart sakspartId").equals("");
        result = result && !getNodeText("felles sakspart sakspartTypeKode").equals("");
        result = result && !getNodeText("felles sakspart sakspartNavn").equals("");
        result = result && !getNodeText("felles signerendeBeslutter signerendeBeslutterNavn").equals("");   //Denne ser ut til å være optional
        result = result && !getNodeText("felles signerendeBeslutter geografiskEnhet").equals("");           ////Denne ser ut til å være optional
        result = result && !getNodeText("felles mottaker mottakerId").equals("");
        result = result && !getNodeText("felles mottaker mottakerTypeKode").equals("");
        result = result && !getNodeText("felles mottaker mottakerNavn").equals("");
        result = result && !getNodeText("felles mottaker mottakerAdresse adresselinje1").equals("");
        result = result && !getNodeText("felles mottaker mottakerAdresse postNr").equals("");
        result = result && !getNodeText("felles mottaker mottakerAdresse poststed").equals("");
        result = result && !getNodeText("felles navnAvsenderEnhet").equals("");
        result = result && !getNodeText("felles kontaktInformasjon kontaktTelefonnummer").equals("");
        result = result && !getNodeText("felles kontaktInformasjon returadresse navEnhetsNavn").equals("");
        result = result && !getNodeText("felles kontaktInformasjon returadresse adresselinje").equals("");
        result = result && !getNodeText("felles kontaktInformasjon returadresse postNr").equals("");
        result = result && !getNodeText("felles kontaktInformasjon returadresse poststed").equals("");
        result = result && !getNodeText("felles kontaktInformasjon postadresse navEnhetsNavn").equals("");
        result = result && !getNodeText("felles kontaktInformasjon postadresse adresselinje").equals("");
        result = result && !getNodeText("felles kontaktInformasjon postadresse postNr").equals("");
        result = result && !getNodeText("felles kontaktInformasjon postadresse poststed").equals("");
        result = result && !getNodeText("felles dokumentDato").equals("");
        return result;
    }
    
}
