package no.nav.foreldrepenger.fpmock2.testmodell.dokument;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Variantformat;

public class JournalpostModellGenerator {


    public static JournalpostModell lagJournalpost(String innhold, String fnr, DokumentTilknyttetJournalpost dokumenttype, DokumenttypeId dokumenttypeId) {
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setJournalStatus(Journalstatus.JOURNALFØRT);
        journalpostModell.setAvsenderFnr(fnr);

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setInnhold(innhold);
        dokumentModell.setDokumentType(dokumenttypeId);
        dokumentModell.setDokumentTilknyttetJournalpost(dokumenttype);
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.XML, Variantformat.FULLVERSJON, innhold.getBytes()
        ));
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.PDF, Variantformat.ARKIV, new byte[0]
        ));

        journalpostModell.getDokumentModellList().add(dokumentModell);

        return journalpostModell;
    }

    public static JournalpostModell makeInntektsmeldingJournalpost(String xml, String fnr) {
        throw new UnsupportedOperationException("Ikke implementert");

    }

    public static JournalpostModell makeUstrukturertDokumentJournalpost(String dokumenttypeId, String fnr) {
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setJournalStatus(Journalstatus.JOURNALFØRT);
        journalpostModell.setAvsenderFnr(fnr);

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setInnhold("innhold: " + dokumenttypeId);
        dokumentModell.setDokumentType(DokumenttypeId.ADOPSJONSDOKUMENTASJON); //TODO: lag oversetter fra Id til object
        dokumentModell.setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.PDF, Variantformat.ARKIV, new byte[0]
        ));
        journalpostModell.getDokumentModellList().add(dokumentModell);
        return journalpostModell;
    }


}
