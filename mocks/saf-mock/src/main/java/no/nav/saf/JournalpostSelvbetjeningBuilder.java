package no.nav.saf;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.saf.selvbetjening.AvsenderMottaker;
import no.nav.saf.selvbetjening.AvsenderMottakerIdType;
import no.nav.saf.selvbetjening.Datotype;
import no.nav.saf.selvbetjening.DokumentInfo;
import no.nav.saf.selvbetjening.Dokumentvariant;
import no.nav.saf.selvbetjening.Journalpost;
import no.nav.saf.selvbetjening.Journalposttype;
import no.nav.saf.selvbetjening.Journalstatus;
import no.nav.saf.selvbetjening.Kanal;
import no.nav.saf.selvbetjening.RelevantDato;
import no.nav.saf.selvbetjening.Sak;
import no.nav.saf.selvbetjening.Sakstype;
import no.nav.saf.selvbetjening.Variantformat;

import java.sql.Timestamp;
import java.util.List;

public final class JournalpostSelvbetjeningBuilder {

    private JournalpostSelvbetjeningBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static Journalpost buildFrom(JournalpostModell modell) {
        var builder = Journalpost.builder();
        builder.setJournalpostId(modell.getJournalpostId());
        builder.setJournalstatus(Journalstatus.JOURNALFOERT);
        var journalposttype = Journalposttype.valueOf(modell.getJournalposttype().getKode());
        builder.setJournalposttype(journalposttype);

        var avsenderMottaker = new AvsenderMottaker(modell.getAvsenderMottaker().getIdent(), AvsenderMottakerIdType.FNR);
        if (journalposttype == Journalposttype.I) {
            builder.setAvsender(avsenderMottaker);
        } else {
            builder.setMottaker(avsenderMottaker);
        }

        builder.setEksternReferanseId(modell.getEksternReferanseId());
        builder.setTittel(modell.getTittel());
        builder.setTema(modell.getArkivtema().getKode());
        var sak = Sak.builder()
                     .setFagsakId(modell.getSakId())
                     .setFagsaksystem("FS36") // foreldrepenger
                     .setSakstype(Sakstype.FAGSAK)
                     .build();
        builder.setSak(sak);
        builder.setKanal(Kanal.valueOf(modell.getMottakskanal().getKode()));
        builder.setRelevanteDatoer(List.of(new RelevantDato(Timestamp.valueOf(modell.getMottattDato()), Datotype.DATO_OPPRETTET)));

        var dokumentListe = modell.getDokumentModellList().stream()
              .map(JournalpostSelvbetjeningBuilder::tilDokumentInfo)
              .toList();
        builder.setDokumenter(dokumentListe);
        return builder.build();
    }

    private static DokumentInfo tilDokumentInfo(DokumentModell modell) {
        var dokumentVarianter = modell.getDokumentVariantInnholdListe().stream()
                .map(dvi -> new Dokumentvariant(Variantformat.ARKIV, "xxx", "PDF",
                        dvi.getDokumentInnhold().length, true, List.of()))
                .toList();
        return new DokumentInfo(modell.getDokumentId(), modell.getTittel(), modell.getBrevkode(), dokumentVarianter);
    }

}
