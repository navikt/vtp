package no.nav.tjeneste.virksomhet.journal.modell;

import java.util.List;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Arkivfiltyper;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinformasjon;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentinnhold;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumentkategorier;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Dokumenttilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.DokumenttypeIder;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Journaltilstand;
import no.nav.tjeneste.virksomhet.inngaaendejournal.v1.informasjon.Variantformater;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.DokumentinfoRelasjon;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.JournalfoertDokumentInfo;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalpost;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Journalstatuser;
import no.nav.tjeneste.virksomhet.journal.v2.informasjon.Statuser;

public class JournalpostBuilder {

    public static Journalpost buildFrom(JournalpostModell modell){
        Journalpost journalpost = new Journalpost();

        for(DokumentModell dokumentModell : modell.getDokumentModellList()){
            DokumentinfoRelasjon dokinfo = new DokumentinfoRelasjon();
            JournalfoertDokumentInfo journalfortdokinfo = new JournalfoertDokumentInfo();

            //dokinfo.setDokumentTilknyttetJournalpost(dokumentModell.getDokumentTilknyttetJournalpost());
            journalpost.getDokumentinfoRelasjonListe().add(dokinfo);
        }


        return journalpost;
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


    private static boolean erHoveddokument(DokumentinfoRelasjon dokinfoRel) {
        return JournalpostModelData.TILKNYTTET_SOM_HOVEDDOKUMENT.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }

    private static boolean erVedlegg(DokumentinfoRelasjon dokinfoRel) {
        return JournalpostModelData.TILKNYTTET_SOM_VEDLEGG.equals(dokinfoRel.getDokumentTilknyttetJournalpost().getValue());
    }
}
