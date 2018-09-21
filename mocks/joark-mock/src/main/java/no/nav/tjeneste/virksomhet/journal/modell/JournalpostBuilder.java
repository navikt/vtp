package no.nav.tjeneste.virksomhet.journal.modell;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinnhold;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivtemaer;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalstatuser;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonsretninger;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.TilknyttetJournalpostSom;

public class JournalpostBuilder {

    public static Journalpost buildFrom(JournalpostModell modell){
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(modell.getId());
        journalpost.setJournalstatus(lagJournalstatus(modell.getJournalStatus()));
        journalpost.setArkivtema(lagArkivtema(modell.getArkivtema()));
        journalpost.setKommunikasjonsretning(lagKommunikasjonsretning(modell.getKommunikasjonsretning()));

        for(DokumentModell dokumentModell : modell.getDokumentModellList()){
            DokumentinfoRelasjon dokinfo = new DokumentinfoRelasjon();

            for(DokumentVariantInnhold dokumentVariantInnhold : dokumentModell.getDokumentVariantInnholdListe()){
                DokumentInnhold dokumentInnhold = new DokumentInnhold();
                //dokumentInnhold.setFiltype(dokumentVariantInnhold.getFilType());
                //dokumentInnhold.setVariantformat(dokumentVariantInnhold.getVariantFormat());

            }

            journalpost.getDokumentinfoRelasjonListe().add(dokinfo);
        }


        return journalpost;
    }












   private static Journalstatuser lagJournalstatus(Journalstatus status){
        Journalstatuser journalstatuser = new Journalstatuser();
        journalstatuser.setValue(status.getKode());
        return journalstatuser;
   }

   private static Arkivtemaer lagArkivtema(Arkivtema arkivtema){
        Arkivtemaer arkivtemaer = new Arkivtemaer();
        arkivtemaer.setValue(arkivtema.getKode());
        return arkivtemaer;
   }

   //TODO: Lag kodeverk
   private static Kommunikasjonsretninger lagKommunikasjonsretning(String kommunikasjonsretning){
        Kommunikasjonsretninger kommunikasjonsretninger = new Kommunikasjonsretninger();
        kommunikasjonsretninger.setValue(kommunikasjonsretning);
        return kommunikasjonsretninger;
   }



    private TilknyttetJournalpostSom tilknyttetJournalpostSom(String tilknyttetJournalpost){
        TilknyttetJournalpostSom tilknyttetJournalpostSom = new TilknyttetJournalpostSom();
        tilknyttetJournalpostSom.setValue(tilknyttetJournalpost);
        return tilknyttetJournalpostSom;
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


}
