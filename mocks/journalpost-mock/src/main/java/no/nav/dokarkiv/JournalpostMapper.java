package no.nav.dokarkiv;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.nav.dokarkiv.generated.model.Bruker;
import no.nav.dokarkiv.generated.model.Dokument;
import no.nav.dokarkiv.generated.model.DokumentVariant;
import no.nav.dokarkiv.generated.model.OpprettJournalpostRequest;
import no.nav.dokarkiv.generated.model.Sak;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostBruker;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Dokumentkategori;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalposttyper;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

public class JournalpostMapper {
    private static final Logger LOG = LoggerFactory.getLogger(JournalpostMapper.class);


    //TODO: utvid med nødvendig mapping
    public JournalpostModell tilModell(OpprettJournalpostRequest journalpostRequest){
        JournalpostModell modell = new JournalpostModell();
        modell.setJournalposttype(mapJournalposttype(journalpostRequest.getJournalpostType()));
        modell.setArkivtema(mapArkivtema(journalpostRequest.getTema()));
        modell.setBruker(mapAvsenderFraBruker(journalpostRequest.getBruker()));
        var sak = journalpostRequest.getSak();
        if (sak.getSakstype() == Sak.SakstypeEnum.FAGSAK) {
            modell.setSakId(journalpostRequest.getSak().getFagsakId());
            modell.setFagsystemId(journalpostRequest.getSak().getFagsaksystem().value());
        } else {
            modell.setSakId(journalpostRequest.getSak().getArkivsaksnummer());
        }

        var datoMottatt = journalpostRequest.getDatoMottatt();
        if (datoMottatt == null) {
            modell.setMottattDato(LocalDateTime.now());
        } else {
            modell.setMottattDato(convertToLocalDateTimeViaInstant(datoMottatt));
        }

        List<DokumentModell> dokumentModeller = new ArrayList<>();
        if(!journalpostRequest.getDokumenter().isEmpty()) {
            dokumentModeller.add(mapDokument(journalpostRequest.getDokumenter().remove(0), DokumentTilknyttetJournalpost.HOVEDDOKUMENT));
            dokumentModeller.addAll(
                    journalpostRequest
                            .getDokumenter().stream()
                            .map(it -> mapDokument(it, DokumentTilknyttetJournalpost.VEDLEGG))
                            .collect(Collectors.toList()));
        }
        modell.setDokumentModellList(dokumentModeller);
        modell.setTittel(journalpostRequest.getTittel());
        modell.setEksternReferanseId(journalpostRequest.getEksternReferanseId());
        modell.setMottakskanal(journalpostRequest.getKanal());

        //TODO: Hvordan håndteres denne (getAvsenderMottaker) sammenlignet med bruker? & Map felter videre
        journalpostRequest.getAvsenderMottaker();
        journalpostRequest.getBehandlingstema();
        journalpostRequest.getTilleggsopplysninger();

        return modell;

    }


    public JournalpostBruker mapAvsenderFraBruker(Bruker bruker){
        switch (bruker.getIdType()){
            case FNR:
                return new JournalpostBruker(bruker.getId(),BrukerType.FNR);
            case AKTOERID:
                return new JournalpostBruker(bruker.getId(),BrukerType.AKTOERID);
            case ORGNR:
                return new JournalpostBruker(bruker.getId(),BrukerType.ORGNR);
            default:
                LOG.warn("Ikke støtte for annen brukertype enn person i journalpostmodell");
                throw new UnsupportedOperationException("Kan ikke opprette journalpost for brukertype");
        }
    }

    private DokumentModell mapDokument(Dokument dokument, DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost){
        DokumentModell dokumentModell = new DokumentModell();
        dokumentModell.setDokumentkategori(new Dokumentkategori(dokument.getDokumentKategori()));
        dokumentModell.setTittel(dokument.getTittel());

        List<DokumentVariantInnhold> dokumentVariantInnholds =
                dokument.getDokumentvarianter().stream()
                .map( it -> mapDokumentVariant(it))
                .collect(Collectors.toList());

        dokumentModell.setDokumentVariantInnholdListe(dokumentVariantInnholds);
        dokumentModell.setDokumentTilknyttetJournalpost(dokumentTilknyttetJournalpost);
        dokumentModell.setBrevkode(dokument.getBrevkode());

        return dokumentModell;
    }

    private DokumentVariantInnhold mapDokumentVariant(DokumentVariant dokumentVariant){
        return new DokumentVariantInnhold(
                new Arkivfiltype(dokumentVariant.getFiltype()),
                new Variantformat(dokumentVariant.getVariantformat()),
                List.of("JSON","XML","PDFA")
                        .contains(dokumentVariant.getFiltype()) ?
                        dokumentVariant.getFysiskDokument() :
                        new byte[0]
        );
    }

    private Arkivtema mapArkivtema(String tema){
        return new Arkivtema(tema);
    }

    private Journalposttyper mapJournalposttype(OpprettJournalpostRequest.JournalpostTypeEnum type){
        if(type.value().equalsIgnoreCase("INNGAAENDE")){
            return Journalposttyper.INNGAAENDE_DOKUMENT;
        } else if (type.value().equalsIgnoreCase("UTGAAENDE")){
            return Journalposttyper.UTGAAENDE_DOKUMENT;
        } else if(type.value().equalsIgnoreCase("NOTAT")){
            return Journalposttyper.NOTAT;
        } else {
            throw new IllegalArgumentException("Verdi journalposttype ikke støttet");
        }
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
