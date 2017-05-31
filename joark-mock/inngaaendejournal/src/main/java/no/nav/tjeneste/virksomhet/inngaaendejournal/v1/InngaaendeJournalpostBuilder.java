package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.foreldrepenger.mock.felles.ConversionUtils;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Mottakskanaler;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Tema;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.modell.StaticModelData;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

import java.util.List;

class InngaaendeJournalpostBuilder {

    /*
    OK: protected String avsenderId;
    OK: protected XMLGregorianCalendar forsendelseMottatt;
    OK: protected Mottakskanaler mottakskanal;
    OK: protected Tema tema;
    @XmlElement(
        required = true
    )
    protected Journaltilstand journaltilstand;
    protected ArkivSak arkivSak;
    protected List<Aktoer> brukerListe;
    @XmlElement(
        required = true
    )
    protected Dokumentinformasjon hoveddokument;
    protected List<Dokumentinformasjon> vedleggListe;
     */

    InngaaendeJournalpost buildFrom(List<JournalDokument> journalDokListe) {

        InngaaendeJournalpost inngJournalpost = buildBasic();

        JournalDokument foersteDok = journalDokListe.get(0);
            //TODO (rune) riktig å bare ta første og beste?
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
        //TODO (rune) journaltilstand

        //TODO (rune) arkivSak ?

        //TODO (rune) brukerListe ?

        //TODO (rune) hoveddokument

        //TODO (rune) vedleggListe
        /*
        for (JournalDokument journalDok : journalDokListe) {
            DokumentinformasjonBuilder dokinfoBuilder = new DokumentinformasjonBuilder(journalDok);
            Dokumentinformasjon dokInfo = dokinfoBuilder.build();
            if (erHoveddokument(journalDok)) {
                response.getInngaaendeJournalpost().setHoveddokument(dokInfo);
            } else {
                response.getInngaaendeJournalpost().getVedleggListe().add(dokInfo);
            }
        }*/

        return inngJournalpost;
    }

    InngaaendeJournalpost buildFrom(Journalpost journalpost) {

        InngaaendeJournalpost inngaaendeJournalpost = buildBasic();

        //TODO (rune) ...
        //TODO (rune) Dokumentinformasjon hoveddokument;
        //TODO (rune) List<Dokumentinformasjon> vedleggListe;

        return inngaaendeJournalpost;
    }

    private InngaaendeJournalpost buildBasic() {

        InngaaendeJournalpost inngaaendeJournalpost = new InngaaendeJournalpost();
        inngaaendeJournalpost.setJournaltilstand(Journaltilstand.ENDELIG);
        return inngaaendeJournalpost;
    }

    private boolean erHoveddokument(JournalDokument journalDok) {
        return StaticModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(journalDok.getTilknJpSom());
    }
}
