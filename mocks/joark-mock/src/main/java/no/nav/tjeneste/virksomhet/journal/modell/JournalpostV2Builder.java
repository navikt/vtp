package no.nav.tjeneste.virksomhet.journal.modell;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.util.DateUtil;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Behandlingstema;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.Variantformat;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Arkivtemaer;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Dokumenttyper;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalstatuser;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Kommunikasjonsretninger;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.TilknyttetJournalpostSom;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Variantformater;

public class JournalpostV2Builder {

    public static Journalpost buildFrom(JournalpostModell modell){
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(modell.getJournalpostId());
        journalpost.setJournalstatus(lagJournalstatus(modell.getJournalStatus()));
        //journalpost.setArkivtema(lagArkivtema(modell.getArkivtema()));
        //journalpost.setKommunikasjonsretning(lagKommunikasjonsretning(modell.getKommunikasjonsretning()));
        //TODO: OL Fjern hardkoding om nødvendig og xmlGregorianConvert
        Kommunikasjonsretninger kommunikasjonsretninger = new Kommunikasjonsretninger();
        kommunikasjonsretninger.setValue("INN");
        journalpost.setKommunikasjonsretning(kommunikasjonsretninger);
        XMLGregorianCalendar xmlGregorianCalendar = null;

        try {
            xmlGregorianCalendar = DateUtil.convertToXMLGregorianCalendar(LocalDate.now());
            journalpost.setMottatt(xmlGregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }


        for(DokumentModell dokumentModell : modell.getDokumentModellList()){
            DokumentinfoRelasjon dokinfo = new DokumentinfoRelasjon();
            JournalfoertDokumentInfo journalfoertDokinfo = new JournalfoertDokumentInfo();
            journalfoertDokinfo.setDokumentType(lagDokumenttyper(dokumentModell.getDokumentType()));
            dokinfo.setJournalfoertDokument(journalfoertDokinfo);
            dokinfo.setDokumentTilknyttetJournalpost(lagTilknyttetJournalpostSom(dokumentModell.getDokumentTilknyttetJournalpost()));

            for(DokumentVariantInnhold dokumentVariantInnhold : dokumentModell.getDokumentVariantInnholdListe()){
                DokumentInnhold dokumentInnhold = new DokumentInnhold();
                dokumentInnhold.setFiltype(lagArkivfiltype(dokumentVariantInnhold.getFilType()));
                dokumentInnhold.setVariantformat(lagVariantformat(dokumentVariantInnhold.getVariantFormat()));
                journalfoertDokinfo.getBeskriverInnholdListe().add(dokumentInnhold);
            }

            journalpost.getDokumentinfoRelasjonListe().add(dokinfo);
        }


        return journalpost;
    }

    private static Dokumenttyper lagDokumenttyper(DokumenttypeId dokumenttypeId) {
        Dokumenttyper dokumenttyper = new Dokumenttyper();
        dokumenttyper.setValue(dokumenttypeId.getKode());
        return dokumenttyper;
    }

    private static TilknyttetJournalpostSom lagTilknyttetJournalpostSom(DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost){
        TilknyttetJournalpostSom tilknyttetJournalpostSom = new TilknyttetJournalpostSom();
        tilknyttetJournalpostSom.setValue(dokumentTilknyttetJournalpost.getKode());
        return tilknyttetJournalpostSom;
    }

   private static Journalstatuser lagJournalstatus(Journalstatus status){
        Journalstatuser journalstatuser = new Journalstatuser();
        journalstatuser.setValue(status.getKode());
        return journalstatuser;
   }

   private static Variantformater lagVariantformat(Variantformat variantformat){
        Variantformater variantformater = new Variantformater();
        variantformater.setValue(variantformat.getKode());
        return variantformater;
   }

   private static Arkivfiltyper lagArkivfiltype(Arkivfiltype arkivfiltype){
        Arkivfiltyper arkivfiltyper = new Arkivfiltyper();
        arkivfiltyper.setValue(arkivfiltype.getKode());
        return arkivfiltyper;
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

    public static Behandlingstema getBehandlingstemaFromDokumenttypeId(DokumenttypeId dokumenttypeId){
        return Behandlingstema.FORELDREPENGER_ADOPSJON;
        }

}
