package no.nav.tjeneste.virksomhet.inngaaendejournal.v1;

import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.InngaaendeJournalpost;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journalmodell.JournalDokument;

import java.util.List;

class InngaaendeJournalpostBuilder {

    /*
    protected String avsenderId;
    @XmlSchemaType(
        name = "dateTime"
    )
    protected XMLGregorianCalendar forsendelseMottatt;
    protected Mottakskanaler mottakskanal;
    protected Tema tema;
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

        InngaaendeJournalpost inngaaendeJournalpost = buildBasic();

        //TODO (rune) ...
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

        return inngaaendeJournalpost;
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
}
