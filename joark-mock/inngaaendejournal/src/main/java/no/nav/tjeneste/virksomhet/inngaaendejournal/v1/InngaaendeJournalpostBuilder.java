package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.*;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.modell.StaticModelData;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class InngaaendeJournalpostBuilder {

    InngaaendeJournalpost buildFrom(List<JournalDokument> journalDokListe) {

        InngaaendeJournalpost inngJournalpost = buildBasic();

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

        //TODO (rune) arkivSak ?

        //TODO (rune) brukerListe ?

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

    //----------------------

    InngaaendeJournalpost buildFrom(Journalpost journalpost) {

        InngaaendeJournalpost inngaaendeJournalpost = buildBasic();

        //TODO (rune) ...
        //TODO (rune) Dokumentinformasjon hoveddokument;
        //TODO (rune) List<Dokumentinformasjon> vedleggListe;

        return inngaaendeJournalpost;
    }

    //----------------------

    private InngaaendeJournalpost buildBasic() {

        InngaaendeJournalpost inngaaendeJournalpost = new InngaaendeJournalpost();
        return inngaaendeJournalpost;
    }

    private boolean erHoveddokument(JournalDokument journalDok) {
        return StaticModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(journalDok.getTilknJpSom());
    }

    private boolean erVedlegg(JournalDokument journalDok) {
        return StaticModelData.TILKNYTTET_SOM_VEDLEGG.equals(journalDok.getTilknJpSom());
    }
}
