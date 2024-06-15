package no.nav.saf;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
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

public final class JournalpostSelvbetjeningBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostSelvbetjeningBuilder.class);

    private JournalpostSelvbetjeningBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static Journalpost buildFrom(JournalpostModell modell) {
        var builder = Journalpost.builder();
        builder.setJournalpostId(modell.getJournalpostId());
        builder.setJournalstatus(tilJournalStatus(modell.getJournalStatus()));
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

    private static Journalstatus tilJournalStatus(no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus journalStatus) {
        if (no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus.JOURNALFØRT.equals(journalStatus)) return Journalstatus.JOURNALFOERT;
        if (no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus.MOTTATT.equals(journalStatus)) return Journalstatus.MOTTATT;
        if (no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus.AVBRUTT.equals(journalStatus)) return Journalstatus.AVBRUTT;
        if (no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus.MIDLERTIDIG_JOURNALFØRT.equals(journalStatus)) {
            return Journalstatus.MOTTATT;
        } else {
            LOG.info("Ukjent journalstatus på journalpost '{}'", journalStatus);
            return Journalstatus.UKJENT;
        }
    }

    private static DokumentInfo tilDokumentInfo(DokumentModell modell) {
        var dokumentVarianter = modell.getDokumentVariantInnholdListe().stream()
                .map(dvi -> new Dokumentvariant(Variantformat.ARKIV, UUID.randomUUID().toString(), "PDF",
                        dvi.getDokumentInnhold().length, true, List.of()))
                .toList();
        return new DokumentInfo(modell.getDokumentId(), modell.getTittel(), modell.getBrevkode(), false, dokumentVarianter);
    }

}
