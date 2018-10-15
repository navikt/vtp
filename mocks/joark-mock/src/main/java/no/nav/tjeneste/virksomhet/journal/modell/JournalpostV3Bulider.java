package no.nav.tjeneste.virksomhet.journal.modell;

import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.JournalpostModell;
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.*;
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.DetaljertDokumentinformasjon;
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.DokumentInnhold;
import no.nav.tjeneste.virksomhet.journal.v3.informasjon.hentkjernejournalpostliste.Journalpost;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JournalpostV3Bulider {

    public static Journalpost buildFrom(JournalpostModell modell) {
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(modell.getJournalpostId());
        journalpost.getGjelderArkivSak().setArkivSakId(modell.getSakId());

        if (finnHoveddokumentFraJournalpost(modell).isPresent()) {
            DokumentModell hoveddokument = finnHoveddokumentFraJournalpost(modell).get();
            journalpost.setHoveddokument(lagDetaljertDokumentinformasjon(hoveddokument));
        }
        for (DokumentModell dokumentModell : finnVedleggFraJournalpost(modell)) {
            journalpost.getVedleggListe().add(lagDetaljertDokumentinformasjon(dokumentModell));
        }

        journalpost.setJournaltilstand(Journaltilstand.fromValue(modell.getJournaltilstand()));

        Journalposttyper journalposttype = new Journalposttyper();
        journalposttype.setKodeverksRef(modell.getJournalposttype().getKode());
        journalpost.setJournalposttype(journalposttype);

       return journalpost;
    }

    private static Optional<DokumentModell> finnHoveddokumentFraJournalpost(JournalpostModell journalpostModell){
        return journalpostModell.getDokumentModellList().stream().filter(t -> t.getDokumentTilknyttetJournalpost().getKode().equals("HOVEDDOKUMENT")).findFirst();
    }

    private static List<DokumentModell> finnVedleggFraJournalpost(JournalpostModell journalpostModell){
        return journalpostModell.getDokumentModellList().stream().filter(t -> t.getDokumentTilknyttetJournalpost().getKode().equals("VEDLEGG")).collect(Collectors.toList());
    }

    private static DetaljertDokumentinformasjon lagDetaljertDokumentinformasjon(DokumentModell dokumentModell) {
        DetaljertDokumentinformasjon detaljertDokumentinformasjon = new DetaljertDokumentinformasjon();
        detaljertDokumentinformasjon.setDokumentId(dokumentModell.getDokumentId());

        Dokumentkategorier dokumentkategorier = new Dokumentkategorier();
        dokumentkategorier.setKodeverksRef(dokumentModell.getDokumentkategori().getKode());
        detaljertDokumentinformasjon.setDokumentkategori(dokumentkategorier);

        DokumenttypeIder dokumenttypeIder = new DokumenttypeIder();
        dokumenttypeIder.setKodeverksRef(dokumentModell.getDokumentType().getKode());
        detaljertDokumentinformasjon.setDokumentTypeId(dokumenttypeIder);

        for (DokumentVariantInnhold innhold : dokumentModell.getDokumentVariantInnholdListe()) {
            String arkivfiltype = innhold.getFilType().getKode();
            String variantformat = innhold.getVariantFormat().getKode();
            DokumentInnhold dokumentInnhold = new DokumentInnhold();
            Arkivfiltyper at = new Arkivfiltyper();
            at.setKodeverksRef(arkivfiltype);
            dokumentInnhold.setArkivfiltype(at);
            Variantformater vf = new Variantformater();
            vf.setKodeverksRef(variantformat);
            dokumentInnhold.setVariantformat(vf);
            detaljertDokumentinformasjon.getDokumentInnholdListe().add(dokumentInnhold);
        }
        return detaljertDokumentinformasjon;
    }


}
