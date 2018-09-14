package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.fpmock2.felles.ConversionUtils;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.journal.dokument.DokumentVariantInnhold;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.ArkivSak;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinformasjon;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinnhold;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentkategorier;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumenttilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.DokumenttypeIder;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Mottakskanaler;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Person;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.modell.JournalV2Constants;
import no.nav.tjeneste.virksomhet.journal.modell.JournalpostModelData;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalstatuser;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.RegistertSak;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Statuser;


class InngaaendeJournalpostBuilder {

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
            tema.setValue(journalpostModell.getArkivtema());
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
                dokumenttypeId.setValue(dokumentModell.getDokumentType());
                dokinfo.setDokumenttypeId(dokumenttypeId);
            }
            dokinfo.setDokumentId(dokumentModell.getDokumentId());

            for(DokumentVariantInnhold innhold : dokumentModell.getDokumentVariantInnholdListe()){
                Dokumentinnhold dokumentInnhold = new Dokumentinnhold();
                dokumentInnhold.setArkivfiltype(new Arkivfiltyper().withValue(innhold.getFilType()));
                dokumentInnhold.setVariantformat(new Variantformater().withValue(innhold.getVariantFormat()));
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
        return JournalpostModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(dokumentModell.getDokumentTilknyttetJournalpost());
    }

    private static boolean erVedlegg(DokumentModell dokumentModell) {
        return JournalpostModelData.TILKNYTTET_SOM_VEDLEGG.equals(dokumentModell.getDokumentTilknyttetJournalpost());
    }

    private Journaltilstand lagJournaltilstand(Journalstatuser journalstatus) {
        Journaltilstand journaltilstand = null;
        if (journalstatus != null) {
            switch (journalstatus.getValue()) {
                case JournalV2Constants.JOURNALSTATUS_MIDLERTIDIG:
                    journaltilstand = Journaltilstand.MIDLERTIDIG;
                    break;
                case JournalV2Constants.JOURNALSTATUS_JOURNALFØRT:
                    journaltilstand = Journaltilstand.ENDELIG;
                    break;
                case JournalV2Constants.JOURNALSTATUS_UTGAAR:
                    journaltilstand = Journaltilstand.UTGAAR;
                    break;
            }
        }
        return journaltilstand;
    }

    private Dokumentinformasjon lagDokumentinformasjonHoved(Journalpost journalpost) {

        Optional<DokumentinfoRelasjon> dokInfoRelHoved = journalpost.getDokumentinfoRelasjonListe().stream()
                .filter(t-> erHoveddokument(t))
                .findFirst();

        Dokumentinformasjon dokinfo = null;
        if (dokInfoRelHoved.isPresent()) {
            dokinfo = lagDokumentinformasjon(dokInfoRelHoved.get());
        }
        return dokinfo;
    }

    private List<Dokumentinformasjon> lagDokumentinformasjonVedlegg(Journalpost journalpost) {

        List<DokumentinfoRelasjon> dokInfoRelVedleggListe = journalpost.getDokumentinfoRelasjonListe().stream()
                .filter(t-> erVedlegg(t))
                .collect(Collectors.toList());

        List<Dokumentinformasjon> dokInfoVedleggListe = dokInfoRelVedleggListe.stream()
                .map(dokInfoRel -> lagDokumentinformasjon(dokInfoRel))
                .collect(Collectors.toList());

        return dokInfoVedleggListe;
    }

    private Dokumentinformasjon lagDokumentinformasjon(DokumentinfoRelasjon dokinfoRel) {

        Dokumentinformasjon dokinfo = new Dokumentinformasjon();

        JournalfoertDokumentInfo journalfoertDokInfo = dokinfoRel.getJournalfoertDokument();
        if (journalfoertDokInfo != null) {
            if (journalfoertDokInfo.getKategori() != null) {
                Dokumentkategorier dokumentkategori = new Dokumentkategorier();
                dokumentkategori.setValue(journalfoertDokInfo.getKategori().getValue());
                dokinfo.setDokumentkategori(dokumentkategori);
            }
            if (journalfoertDokInfo.getDokumentType() != null) {
                DokumenttypeIder dokumenttypeId = new DokumenttypeIder();
                dokumenttypeId.setValue(journalfoertDokInfo.getDokumentType().getValue());
                dokinfo.setDokumenttypeId(dokumenttypeId);
            }
            dokinfo.setDokumentId(journalfoertDokInfo.getDokumentId());
            dokinfo.setDokumenttilstand(lagDokumenttilstand(journalfoertDokInfo.getStatus()));
            if (journalfoertDokInfo.getBeskriverInnholdListe() != null) {
                List<Dokumentinnhold> dokInnholdListe = journalfoertDokInfo.getBeskriverInnholdListe().stream()
                        .map(journV2dokInnhold -> lagDokumentinnhold(journV2dokInnhold))
                        .collect(Collectors.toList());
                dokinfo.getDokumentInnholdListe().addAll(dokInnholdListe);
            }
        }

        return dokinfo;
    }

    private Dokumenttilstand lagDokumenttilstand(Statuser status) {
        Dokumenttilstand dokumenttilstand = null;
        if (status != null) {
            switch (status.getValue()) {
                case "UNDER_REDIGERING":
                    dokumenttilstand = Dokumenttilstand.UNDER_REDIGERING;
                    break;
                case "FERDIGSTILT":
                    dokumenttilstand = Dokumenttilstand.FERDIGSTILT;
                    break;
                case "AVBRUTT":
                    dokumenttilstand = Dokumenttilstand.AVBRUTT;
                    break;
            }
        }
        return dokumenttilstand;
    }

    private Dokumentinnhold lagDokumentinnhold(DokumentInnhold journV2dokInnhold) {

        Dokumentinnhold dokInnhold = new Dokumentinnhold();
        boolean harVerdier = false;
        if (journV2dokInnhold.getFiltype() != null) {
            Arkivfiltyper arkivfiltype = new Arkivfiltyper();
            arkivfiltype.setValue(journV2dokInnhold.getFiltype().getValue());
            dokInnhold.setArkivfiltype(arkivfiltype);
            harVerdier = true;
        }
        if (journV2dokInnhold.getVariantformat() != null) {
            Variantformater variantformat = new Variantformater();
            variantformat.setValue(journV2dokInnhold.getVariantformat().getValue());
            dokInnhold.setVariantformat(variantformat);
            harVerdier = true;
        }
        if (! harVerdier) {
            dokInnhold = null;
        }
        return dokInnhold;
    }

    private ArkivSak lagArkivSak(Journalpost journalpost) {
        ArkivSak arkivSak = null;
        RegistertSak registertSak = journalpost.getGjelderSak();
        if (registertSak != null) {
            if (registertSak.getSakId() != null || registertSak.getFagsystem() != null) {
                arkivSak = new ArkivSak();
                arkivSak.setArkivSakId(registertSak.getSakId());
                arkivSak.setArkivSakSystem(registertSak.getFagsystem().getValue());
            }
        }
        return arkivSak;
    }

    private static boolean erHoveddokument(DokumentinfoRelasjon dokinfoRel) {
        return JournalpostModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }

    private static boolean erVedlegg(DokumentinfoRelasjon dokinfoRel) {
        return JournalpostModelData.TILKNYTTET_SOM_VEDLEGG.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }
}
