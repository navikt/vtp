package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.*;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Person;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.*;
import no.nav.tjeneste.virksomhet.journal.v2.modell.StaticModelData;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class InngaaendeJournalpostBuilder {

    //---------  List<JournalDokument> -> InngaaendeJournalpost  -------------

    InngaaendeJournalpost buildFrom(List<JournalDokument> journalDokListe) {

        InngaaendeJournalpost inngJournalpost = new InngaaendeJournalpost();

        JournalDokument foersteDok = journalDokListe.get(0);
            // første og beste - de skal være like på feltene vi setter her

        if (foersteDok.getBrukerFnr() != null) {
            inngJournalpost.setAvsenderId(foersteDok.getBrukerFnr());
        }
        if (foersteDok.getDatoMottatt() != null) {
            inngJournalpost.setForsendelseMottatt(ConversionUtils.convertToXMLGregorianCalendar(foersteDok.getDatoMottatt()));
        }
        if (foersteDok.getKommunikasjonskanal() != null) {
            Mottakskanaler mottakskanal = new Mottakskanaler();
            mottakskanal.setValue(foersteDok.getKommunikasjonskanal());
            inngJournalpost.setMottakskanal(mottakskanal);
        }
        if (foersteDok.getArkivtema() != null) {
            Tema tema = new Tema();
            tema.setValue(foersteDok.getArkivtema());
            inngJournalpost.setTema(tema);
        }
        if (foersteDok.getJournaltilstand() != null) {
            Journaltilstand journaltilstand = Journaltilstand.fromValue(foersteDok.getJournaltilstand());
            inngJournalpost.setJournaltilstand(journaltilstand);
        }

        ArkivSak arkivSak = lagArkivSak(foersteDok);
        inngJournalpost.setArkivSak(arkivSak);

        if (foersteDok.getBrukerFnr() != null) {
            Person bruker = new Person();
            bruker.setIdent(foersteDok.getBrukerFnr());
            inngJournalpost.getBrukerListe().add(bruker);
        }

        Dokumentinformasjon dokInfoHoved = lagDokumentinformasjonHoved(journalDokListe);
        inngJournalpost.setHoveddokument(dokInfoHoved);

        List<Dokumentinformasjon> dokInfoVedleggListe = lagDokumentinformasjonVedlegg(journalDokListe);
        inngJournalpost.getVedleggListe().addAll(dokInfoVedleggListe);

        return inngJournalpost;
    }

    private Dokumentinformasjon lagDokumentinformasjonHoved(List<JournalDokument> journalDokListe) {

        List<JournalDokument> hovedListe = journalDokListe.stream()
                .filter(this::erHoveddokument)
                .collect(Collectors.toList());

        Dokumentinformasjon dokinfo = lagDokumentinformasjon(hovedListe);
        return dokinfo;
    }

    private List<Dokumentinformasjon> lagDokumentinformasjonVedlegg(List<JournalDokument> journalDokListe) {

        List<JournalDokument> vedleggListe = journalDokListe.stream()
                .filter(this::erVedlegg)
                .collect(Collectors.toList());

        Map<String, List<JournalDokument>> vedleggPrDokumentId = vedleggListe.stream()
                .collect(Collectors.groupingBy(JournalDokument::getDokumentId));

        List<Dokumentinformasjon> dokInfoVedleggListe = vedleggPrDokumentId.entrySet().stream()
                .map(entry -> lagDokumentinformasjon(entry.getValue()))
                .collect(Collectors.toList());

        return dokInfoVedleggListe;
    }

    private Dokumentinformasjon lagDokumentinformasjon(List<JournalDokument> journalDokListe) {

        Dokumentinformasjon dokinfo = null;

        if (! journalDokListe.isEmpty()) {
            dokinfo = new Dokumentinformasjon();
            JournalDokument journalDokFoerste = journalDokListe.get(0);
                // første og beste - de skal være like på feltene vi setter her
            if (journalDokFoerste.getKategori() != null) {
                Dokumentkategorier dokumentkategori = new Dokumentkategorier();
                dokumentkategori.setValue(journalDokFoerste.getKategori());
                dokinfo.setDokumentkategori(dokumentkategori);
            }
            if (journalDokFoerste.getDokumentType() != null) {
                DokumenttypeIder dokumenttypeId = new DokumenttypeIder();
                dokumenttypeId.setValue(journalDokFoerste.getDokumentType());
                dokinfo.setDokumenttypeId(dokumenttypeId);
            }
            dokinfo.setDokumentId(journalDokFoerste.getDokumentId());
            if (journalDokFoerste.getDokumenttilstand() != null) {
                Dokumenttilstand dokumenttilstand = Dokumenttilstand.fromValue(journalDokFoerste.getDokumenttilstand());
                dokinfo.setDokumenttilstand(dokumenttilstand);
            }
            for (JournalDokument journalDok : journalDokListe) {
                Dokumentinnhold dokInnhold = new Dokumentinnhold();
                boolean harVerdier = false;
                if (journalDok.getFilType() != null) {
                    Arkivfiltyper arkivfiltype = new Arkivfiltyper();
                    arkivfiltype.setValue(journalDok.getFilType());
                    dokInnhold.setArkivfiltype(arkivfiltype);
                    harVerdier = true;
                }
                if (journalDok.getVariantformat() != null) {
                    Variantformater variantformat = new Variantformater();
                    variantformat.setValue(journalDok.getVariantformat());
                    dokInnhold.setVariantformat(variantformat);
                    harVerdier = true;
                }
                if (harVerdier) {
                    dokinfo.getDokumentInnholdListe().add(dokInnhold);
                }
            }
        }

        return dokinfo;
    }

    private ArkivSak lagArkivSak(JournalDokument journalDok) {
        ArkivSak arkivSak = null;
        if (journalDok.getSakId() != null || journalDok.getFagsystem() != null) {
            arkivSak = new ArkivSak();
            arkivSak.setArkivSakId(journalDok.getSakId());
            arkivSak.setArkivSakSystem(journalDok.getFagsystem());
        }
        return arkivSak;
    }

    private boolean erHoveddokument(JournalDokument journalDok) {
        return StaticModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(journalDok.getTilknJpSom());
    }

    private boolean erVedlegg(JournalDokument journalDok) {
        return StaticModelData.TILKNYTTET_SOM_VEDLEGG.equals(journalDok.getTilknJpSom());
    }

    //---------  Journalpost -> InngaaendeJournalpost  -------------

    InngaaendeJournalpost buildFrom(Journalpost journalpost) {

        InngaaendeJournalpost inngJournalpost = new InngaaendeJournalpost();

        // AvsenderId - har ikke data

        inngJournalpost.setForsendelseMottatt(journalpost.getMottatt());

        if (journalpost.getKommunikasjonskanal() != null) {
            Mottakskanaler mottakskanal = new Mottakskanaler();
            mottakskanal.setValue(journalpost.getKommunikasjonskanal().getValue());
            inngJournalpost.setMottakskanal(mottakskanal);
        }

        if (journalpost.getArkivtema() != null) {
            Tema tema = new Tema();
            tema.setValue(journalpost.getArkivtema().getValue());
            inngJournalpost.setTema(tema);
        }

        inngJournalpost.setJournaltilstand(lagJournaltilstand(journalpost.getJournalstatus()));

        ArkivSak arkivSak = lagArkivSak(journalpost);
        inngJournalpost.setArkivSak(arkivSak);

        // brukerListe - har ikke data

        Dokumentinformasjon dokInfoHoved = lagDokumentinformasjonHoved(journalpost);
        inngJournalpost.setHoveddokument(dokInfoHoved);

        List<Dokumentinformasjon> dokInfoVedleggListe = lagDokumentinformasjonVedlegg(journalpost);
        inngJournalpost.getVedleggListe().addAll(dokInfoVedleggListe);

        return inngJournalpost;
    }

    private Journaltilstand lagJournaltilstand(Journalstatuser journalstatus) {
        Journaltilstand journaltilstand = null;
        if (journalstatus != null) {
            switch (journalstatus.getValue()) {
                case "M":
                    journaltilstand = Journaltilstand.MIDLERTIDIG;
                    break;
                case "J":
                    journaltilstand = Journaltilstand.ENDELIG;
                    break;
                case "U":
                    journaltilstand = Journaltilstand.UTGAAR;
                    break;
            }
        }
        return journaltilstand;
    }

    private Dokumentinformasjon lagDokumentinformasjonHoved(Journalpost journalpost) {

        Optional<DokumentinfoRelasjon> dokInfoRelHoved = journalpost.getDokumentinfoRelasjonListe().stream()
                .filter(this::erHoveddokument)
                .findFirst();

        Dokumentinformasjon dokinfo = null;
        if (dokInfoRelHoved.isPresent()) {
            dokinfo = lagDokumentinformasjon(dokInfoRelHoved.get());
        }
        return dokinfo;
    }

    private List<Dokumentinformasjon> lagDokumentinformasjonVedlegg(Journalpost journalpost) {

        List<DokumentinfoRelasjon> dokInfoRelVedleggListe = journalpost.getDokumentinfoRelasjonListe().stream()
                .filter(this::erVedlegg)
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

    private boolean erHoveddokument(DokumentinfoRelasjon dokinfoRel) {
        return StaticModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }

    private boolean erVedlegg(DokumentinfoRelasjon dokinfoRel) {
        return StaticModelData.TILKNYTTET_SOM_VEDLEGG.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }
}
