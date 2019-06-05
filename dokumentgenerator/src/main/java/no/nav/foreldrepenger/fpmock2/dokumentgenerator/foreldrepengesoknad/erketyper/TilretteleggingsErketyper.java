package no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.glassfish.pfl.dynamic.copyobject.spi.ObjectCopierFactory;

import no.nav.vedtak.felles.xml.soeknad.felles.v3.Vedlegg;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Innsendingstype;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Arbeidsforhold;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.DelvisTilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.HelTilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.IngenTilrettelegging;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.ObjectFactory;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;

public class TilretteleggingsErketyper{

    public static Tilrettelegging helTilrettelegging(LocalDate behovForTilretteleggingFom, LocalDate tilrettelagtArbeidFom, Arbeidsforhold arbeidsforhold){
        Tilrettelegging tilrettelegging = new Tilrettelegging();
        tilrettelegging.setBehovForTilretteleggingFom(behovForTilretteleggingFom);
        tilrettelegging.setArbeidsforhold(arbeidsforhold);

        ObjectFactory of = new ObjectFactory();
        Innsendingstype innsendingstype = new Innsendingstype();
        innsendingstype.setKode("LASTET_OPP");
        innsendingstype.setKodeverk("INNSENDINGSVALG");
        Vedlegg vedlegg = new Vedlegg();
        vedlegg.setId("A"+UUID.randomUUID().toString().substring(0,6));
        vedlegg.setInnsendingstype(innsendingstype);

        HelTilrettelegging helTilrettelegging = new HelTilrettelegging();
        helTilrettelegging.setTilrettelagtArbeidFom(tilrettelagtArbeidFom);
        tilrettelegging.setHelTilrettelegging(helTilrettelegging);
        tilrettelegging.getVedlegg().add(of.createTilretteleggingVedlegg(vedlegg));
        return tilrettelegging;
    }

    public static Tilrettelegging delvisTilrettelegging(){
        Tilrettelegging tilrettelegging = new Tilrettelegging();
        tilrettelegging.setDelvisTilrettelegging(new DelvisTilrettelegging());
        return tilrettelegging;
    }

    public static Tilrettelegging ingenTilrettelegging() {
        Tilrettelegging tilrettelegging = new Tilrettelegging();
        tilrettelegging.setIngenTilrettelegging(new IngenTilrettelegging());
        return tilrettelegging;
    }

}
