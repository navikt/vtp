package no.nav.foreldrepenger.vtp.testmodell.dokument;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

public class JournalpostModellGenerator {

    public static JournalpostModell lagJournalpostJsonDokument(String innhold, String fnr, DokumenttypeId dokumenttypeId, Mottakskanal mottakskanal) {
        return lagJournalpostStrukturertDokument(innhold, fnr, dokumenttypeId, mottakskanal, Arkivfiltype.JSON, null);
    }

    public static JournalpostModell lagJournalpostStrukturertDokument(String innhold, String fnr, DokumenttypeId dokumenttypeId) {
        return lagJournalpostStrukturertDokument(innhold, fnr, dokumenttypeId, Mottakskanal.UKJENT, Arkivfiltype.XML, null);
    }

    public static JournalpostModell lagJournalpostStrukturertDokument(String innhold, String fnr, DokumenttypeId dokumenttypeId, Mottakskanal mottakskanal, Arkivfiltype arkivfiltype, String brevkode) {
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setJournalStatus(Journalstatus.JOURNALFØRT);
        journalpostModell.setAvsenderFnr(fnr);
        journalpostModell.setJournalposttype(Journalposttyper.INNGAAENDE_DOKUMENT);
        journalpostModell.setMottakskanal(mottakskanal);

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setInnhold(innhold);
        dokumentModell.setBrevkode(brevkode);
        dokumentModell.setDokumentType(dokumenttypeId);
        dokumentModell.setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                arkivfiltype, Variantformat.FULLVERSJON, innhold != null ? innhold.getBytes() : new byte[0]
        ));
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.PDF, Variantformat.ARKIV, new byte[0]
        ));
        journalpostModell.getDokumentModellList().add(dokumentModell);

        return journalpostModell;
    }

    public static JournalpostModell lagJournalpostUstrukturertDokument(String fnr, DokumenttypeId dokumenttypeId) {
        JournalpostModell journalpostModell = new JournalpostModell();
        journalpostModell.setJournalStatus(Journalstatus.JOURNALFØRT);
        journalpostModell.setAvsenderFnr(fnr);

        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentType(dokumenttypeId);
        dokumentModell.setInnhold("innhold: " + dokumenttypeId);
        dokumentModell.setDokumentTilknyttetJournalpost(DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        dokumentModell.getDokumentVariantInnholdListe().add(new DokumentVariantInnhold(
                Arkivfiltype.PDF, Variantformat.ARKIV, new byte[0]
        ));
        journalpostModell.getDokumentModellList().add(dokumentModell);
        return journalpostModell;
    }


}
