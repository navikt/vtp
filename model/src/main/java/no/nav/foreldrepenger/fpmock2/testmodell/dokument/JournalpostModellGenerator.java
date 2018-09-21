package no.nav.foreldrepenger.fpmock2.testmodell.dokument;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Variantformat;

public class JournalpostModellGenerator {



    public static JournalpostModell foreldrepengeSøknadFødselJournalpost(String xml, String fnr){
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setAvsenderFnr(fnr);

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setInnhold(xml);
        dokumentModell.setDokumentType("I000005");
        dokumentModell.setDokumentTilknyttetJournalpost("HOVEDDOKUMENT");
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.XML,Variantformat.FULLVERSJON,xml.getBytes()
        ));

        journalpostModell.getDokumentModellList().add(dokumentModell);

        return journalpostModell;
    }

    public static JournalpostModell makeInntektsmeldingJournalpost(String xml, String fnr){
        throw new UnsupportedOperationException("Ikke implementert");

    }

    public static JournalpostModell makeUstrukturertDokumentJournalpost(DokumenttypeId dokumenttypeId, String fnr){
        throw new UnsupportedOperationException("Ikke implementert");
    }





}
