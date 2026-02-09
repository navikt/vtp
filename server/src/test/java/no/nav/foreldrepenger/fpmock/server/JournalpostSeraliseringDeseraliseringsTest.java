package no.nav.foreldrepenger.fpmock.server;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostBruker;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BehandlingsTema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

class JournalpostSeraliseringDeseraliseringsTest extends SerializationTestBase {

    @Test
    void DokumentVariantInnholdSeraliseringDeseraliseringTest() {
        test(lagDokumentVariant());
    }

    private DokumentVariantInnhold lagDokumentVariant() {
        return new DokumentVariantInnhold(Arkivfiltype.AXML, Variantformat.ORIGINAL, null);
    }

    @Test
    void DokumentModellSeraliseringDeseraliseringTest() {
        test(lagDokumentModell());
    }

    private DokumentModell lagDokumentModell() {
        return new DokumentModell("12345678", DokumenttypeId.INNTEKTSMELDING, true, "tittel",
                "brevkode", "innholder her!", DokumentTilknyttetJournalpost.HOVEDDOKUMENT,
                List.of(lagDokumentVariant()));
    }


    @Test
    void JournalpostModellSeraliseringDeseraliseringTest() {
        test(new JournalpostModell("123456789", "AR123344566", "Inntekstmelding", List.of(lagDokumentModell()), "test",
                "sakid", "fagsystemId", Journalstatus.MOTTATT, "kommunikasjonsretning", LocalDateTime.now(), Mottakskanal.ALTINN,
                Arkivtema.FOR, "journaltilstand", Journalposttyper.INNGAAENDE_DOKUMENT, new JournalpostBruker("12345", BrukerType.FNR),
                BehandlingsTema.FORELDREPENGER.getOffisiellKode()));
    }
}
