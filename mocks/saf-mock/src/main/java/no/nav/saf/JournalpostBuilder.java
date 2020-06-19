package no.nav.saf;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumenttypeId;

public class JournalpostBuilder {

    private static String BREVKODE_IM = "4036";

    public static Journalpost buildFrom(JournalpostModell modell) {
        Journalpost journalpost = new Journalpost();
        journalpost.setJournalpostId(modell.getJournalpostId());
        journalpost.setSak(new Sak(modell.getSakId(), Arkivsaksystem.GSAK, Date.from(Instant.now()),
                "fagsakId", modell.getFagsystemId()));

         List<DokumentInfo> dokumentInfoer = new ArrayList<>();
        if (finnHoveddokumentFraJournalpost(modell).isPresent()) {
            // Dokumentinfo for hoveddokument skal alltid returneres først
            DokumentModell hoveddokument = finnHoveddokumentFraJournalpost(modell).get();
            dokumentInfoer.add(lagDetaljertDokumentinformasjon(hoveddokument));
        }
        for (DokumentModell dokumentModell : finnVedleggFraJournalpost(modell)) {
            // Dokumentinfo for andre vedlegg returneres deretter
            dokumentInfoer.add(lagDetaljertDokumentinformasjon(dokumentModell));
        }
        journalpost.setDokumenter(dokumentInfoer);

        Optional.ofNullable(modell.getJournalposttype())
                .map(journalposttype -> Journalposttype.valueOf(journalposttype.getKode()))
                .ifPresent(journalpost::setJournalposttype);

        if(modell.getMottattDato() != null){
            Date mottatt = Date.from( modell.getMottattDato().atZone(ZoneId.systemDefault()).toInstant());
            journalpost.setRelevanteDatoer(List.of(
                    new RelevantDato(mottatt, Datotype.DATO_JOURNALFOERT),
                    new RelevantDato(mottatt, Datotype.DATO_REGISTRERT)));
        }
       return journalpost;
    }

    private static Optional<DokumentModell> finnHoveddokumentFraJournalpost(JournalpostModell journalpostModell){
        return journalpostModell.getDokumentModellList().stream().filter(t -> t.getDokumentTilknyttetJournalpost().getKode().equals("HOVEDDOKUMENT")).findFirst();
    }

    private static List<DokumentModell> finnVedleggFraJournalpost(JournalpostModell journalpostModell){
        return journalpostModell.getDokumentModellList().stream().filter(t -> t.getDokumentTilknyttetJournalpost().getKode().equals("VEDLEGG")).collect(Collectors.toList());
    }

    private static DokumentInfo lagDetaljertDokumentinformasjon(DokumentModell dokModell) {
        DokumentInfo dokInfo = new DokumentInfo();
        dokInfo.setTittel("tittel");
        dokInfo.setDokumentInfoId(dokModell.getDokumentId());
        if (dokModell.getBrevkode() != null) {
            dokInfo.setBrevkode(dokModell.getBrevkode());
        } else {
            dokInfo.setBrevkode(dokumentTypeIdTilBrevkode(dokModell.getDokumentType())); // TODO: Hvordan skal vi mappe denne mot DokumentTypeId og DokumentKategori?
        }
        dokInfo.setDokumentstatus(Dokumentstatus.FERDIGSTILT);
        dokInfo.setDatoFerdigstilt(Date.from(Instant.now()));

        List<Dokumentvariant> dokumentvarianter = new ArrayList<>();
        for (DokumentVariantInnhold innhold : dokModell.getDokumentVariantInnholdListe()) {
            Dokumentvariant dokVariant = new Dokumentvariant();
            dokVariant.setFiltype(innhold.getFilType().getKode());
            dokVariant.setVariantformat(
                    Optional.ofNullable(innhold.getVariantFormat())
                    .map(formatKode -> Variantformat.valueOf(formatKode.getKode()))
                    .orElse(Variantformat.ARKIV)); // Default
            dokVariant.setSaksbehandlerHarTilgang(true); // må settes til true for å bli synlig i k9-sak
            dokVariant.setFilnavn("filnavn");
            dokVariant.setFiluuid("filuuid");

            dokumentvarianter.add(dokVariant);
        }
        dokInfo.setDokumentvarianter(dokumentvarianter);
        return dokInfo;
    }

    private static String dokumentTypeIdTilBrevkode(DokumenttypeId dokumentType) {
        if (DokumenttypeId.INNTEKTSMELDING.equals(dokumentType)) {
            return BREVKODE_IM;
        }
        // Alle andre brevkoder er ukjente for k9-sak -> for denne mockens del kan vi da like gjerne returnere DokumentTypeId sin verdi
        return dokumentType != null ? dokumentType.getKode() : null;
    }


}
