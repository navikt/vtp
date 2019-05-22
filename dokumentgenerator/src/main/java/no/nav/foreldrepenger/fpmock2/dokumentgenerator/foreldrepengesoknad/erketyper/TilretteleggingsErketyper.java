package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.DelvisTilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.HelTilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.IngenTilrettelegging;

public class TilretteleggingsErketyper{

    public static HelTilrettelegging helTilrettelegging(){
        return new HelTilrettelegging();
    }

    public static DelvisTilrettelegging delvisTilrettelegging(){
        return new DelvisTilrettelegging();
    }

    public static IngenTilrettelegging ingenTilrettelegging() {
        return new IngenTilrettelegging();
    }

}
