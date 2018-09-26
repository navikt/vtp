package no.nav.tjeneste.virksomhet.inngaaendejournal.modell;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.ArkivSak;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinformasjon;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinnhold;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.DokumenttypeIder;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Mottakskanaler;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Person;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Variantformater;


public class InngaaendeJournalpostBuilder {

    public static InngaaendeJournalpost buildFrom(JournalpostModell journalpostModell) {

        InngaaendeJournalpost inngJournalpost = new InngaaendeJournalpost();

            // første og beste - de skal være like på feltene vi setter her

        if (journalpostModell.getAvsenderFnr() != null) {
            inngJournalpost.setAvsenderId(journalpostModell.getAvsenderFnr());
        }
        if (journalpostModell.getMottattDato() != null) {
            inngJournalpost.setForsendelseMottatt(ConversionUtils.convertToXMLGregorianCalendar(journalpostModell.getMottattDato()));
        }
        if (journalpostModell.getMottakskanal() != null) {
            Mottakskanaler mottakskanal = new Mottakskanaler();
            mottakskanal.setValue(journalpostModell.getMottakskanal());
            inngJournalpost.setMottakskanal(mottakskanal);
        }
        if (journalpostModell.getArkivtema() != null) {
            Tema tema = new Tema();
            tema.setValue(journalpostModell.getArkivtema().getKode());
            inngJournalpost.setTema(tema);
        }
        if (journalpostModell.getJournaltilstand() != null) {
            Journaltilstand journaltilstand = Journaltilstand.fromValue(journalpostModell.getJournaltilstand());
            inngJournalpost.setJournaltilstand(journaltilstand);
        }

        ArkivSak arkivSak = lagArkivSak(journalpostModell);
        inngJournalpost.setArkivSak(arkivSak);

        if (journalpostModell.getAvsenderFnr() != null) {
            Person bruker = new Person();
            bruker.setIdent(journalpostModell.getAvsenderFnr());
            inngJournalpost.getBrukerListe().add(bruker);
        }

        Dokumentinformasjon dokInfoHoved = lagDokumentinformasjonHoved(journalpostModell);
        inngJournalpost.setHoveddokument(dokInfoHoved);

        List<Dokumentinformasjon> dokInfoVedleggListe = lagDokumentinformasjonVedlegg(journalpostModell);
        inngJournalpost.getVedleggListe().addAll(dokInfoVedleggListe);

        return inngJournalpost;
    }

    private static Dokumentinformasjon lagDokumentinformasjonHoved(JournalpostModell journalpostModell) {

        DokumentModell dokumentModell = journalpostModell.getDokumentModellList().stream()
                .filter(t-> erHoveddokument(t))
                .findFirst().orElseThrow(() -> new IllegalStateException("Journalpost mangler hoveddokument"));

        Dokumentinformasjon dokinfo = lagDokumentinformasjon(dokumentModell);
        return dokinfo;
    }

    private static List<Dokumentinformasjon> lagDokumentinformasjonVedlegg(JournalpostModell journalpostModell) {

        List<DokumentModell> vedleggListe = journalpostModell.getDokumentModellList().stream()
                .filter(t-> erVedlegg(t))
                .collect(Collectors.toList());


        List<Dokumentinformasjon> dokInfoVedleggListe = vedleggListe.stream()
                .map(entry -> lagDokumentinformasjon(entry))
                .collect(Collectors.toList());

        return dokInfoVedleggListe;
    }

    private static Dokumentinformasjon lagDokumentinformasjon(DokumentModell dokumentModell) {

        Dokumentinformasjon dokinfo = new Dokumentinformasjon();

            if (dokumentModell.getDokumentType() != null) {
                DokumenttypeIder dokumenttypeId = new DokumenttypeIder();
                dokumenttypeId.setValue(dokumentModell.getDokumentType().getKode());
                dokinfo.setDokumenttypeId(dokumenttypeId);
            }
            dokinfo.setDokumentId(dokumentModell.getDokumentId());

            for(DokumentVariantInnhold innhold : dokumentModell.getDokumentVariantInnholdListe()){
                Dokumentinnhold dokumentInnhold = new Dokumentinnhold();
                dokumentInnhold.setArkivfiltype(new Arkivfiltyper().withValue(innhold.getFilType().getKode()));
                dokumentInnhold.setVariantformat(new Variantformater().withValue(innhold.getVariantFormat().getKode()));
                dokinfo.getDokumentInnholdListe().add(dokumentInnhold);

            }

        return dokinfo;
    }

    private static ArkivSak lagArkivSak(JournalpostModell journalpostmodell) {
        ArkivSak arkivSak = null;
        if (journalpostmodell.getSakId() != null || journalpostmodell.getFagsystemId() != null) {
            arkivSak = new ArkivSak();
            arkivSak.setArkivSakId(journalpostmodell.getSakId());
            arkivSak.setArkivSakSystem(journalpostmodell.getFagsystemId());
        }
        return arkivSak;
    }

    private static boolean erHoveddokument(DokumentModell dokumentModell) {
        return DokumentTilknyttetJournalpost.HOVEDDOKUMENT.equals(dokumentModell.getDokumentTilknyttetJournalpost());
    }

    private static boolean erVedlegg(DokumentModell dokumentModell) {
        return DokumentTilknyttetJournalpost.VEDLEGG.equals(dokumentModell.getDokumentTilknyttetJournalpost());
    }

}
