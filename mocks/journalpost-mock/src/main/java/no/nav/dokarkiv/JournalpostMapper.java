package no.nav.dokarkiv;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.nav.dokarkiv.generated.model.Bruker;
import no.nav.dokarkiv.generated.model.Dokument;
import no.nav.dokarkiv.generated.model.DokumentVariant;
import no.nav.dokarkiv.generated.model.OppdaterJournalpostRequest;
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
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Journalstatus;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Mottakskanal;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Variantformat;

public class JournalpostMapper {
    private static JournalpostMapper instance;

    public static synchronized JournalpostMapper getInstance() {
        if (instance == null) {
            instance = new JournalpostMapper();
        }
        return instance;
    }

    public JournalpostModell tilModell(OpprettJournalpostRequest journalpostRequest) {
        JournalpostModell modell = new JournalpostModell();
        modell.setJournalposttype(mapJournalposttype(journalpostRequest.getJournalpostType()));
        modell.setArkivtema(mapArkivtema(journalpostRequest.getTema()));
        modell.setBruker(mapAvsenderFraBruker(journalpostRequest.getBruker()));
        tilSak(journalpostRequest, modell);
        tilMottattDato(journalpostRequest, modell);
        tilAvsenderMottaker(journalpostRequest, modell);
        modell.setDokumentModellList(tilDokumentModeller(journalpostRequest.getDokumenter()));
        modell.setTittel(journalpostRequest.getTittel());
        modell.setEksternReferanseId(journalpostRequest.getEksternReferanseId());
        modell.setMottakskanal(Mottakskanal.fraKode(journalpostRequest.getKanal()));
        modell.setJournalStatus(erKnyttetTilSak(journalpostRequest.getSak()) ? Journalstatus.JOURNALFØRT : Journalstatus.MOTTATT);
        return modell;
    }

    private boolean erKnyttetTilSak(Sak sak) {
        return sak != null & sak.getFagsakId() != null;
    }

    private List<DokumentModell> tilDokumentModeller(List<Dokument> dokumenter) {
        var dokumentModeller = new ArrayList<DokumentModell>();
        if (dokumenter == null) {
            return dokumentModeller;
        }
        var hoveddokument = tilDokumentModell(dokumenter.remove(0), DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        var vedlegg = dokumenter.stream()
                .map(dokument -> tilDokumentModell(dokument, DokumentTilknyttetJournalpost.VEDLEGG))
                .toList();

        dokumentModeller.add(hoveddokument);
        dokumentModeller.addAll(vedlegg);
        return dokumentModeller;
    }

    private static DokumentModell tilDokumentModell(Dokument dokument, DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost) {
        return new DokumentModell(
                null,
                null,
                false,
                dokument.getTittel(),
                dokument.getBrevkode(),
                null,
                dokumentTilknyttetJournalpost,
                tilDokumentVarianter(dokument.getDokumentvarianter()),
                new Dokumentkategori(dokument.getDokumentKategori())
        );
    }

    private static List<DokumentVariantInnhold> tilDokumentVarianter(List<DokumentVariant> dokumentvarianter) {
        return dokumentvarianter.stream()
                .map(JournalpostMapper::tilDokumentVariant)
                .toList();
    }

    private static DokumentVariantInnhold tilDokumentVariant(DokumentVariant dokumentVariant) {
        return new DokumentVariantInnhold(
                new Arkivfiltype(dokumentVariant.getFiltype()),
                new Variantformat(dokumentVariant.getVariantformat()),
                dokumentVariant.getFysiskDokument()
        );
    }

    private void tilAvsenderMottaker(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        Optional.ofNullable(journalpostRequest.getAvsenderMottaker()).ifPresent(it -> {
            var idType = new BrukerType(it.getIdType().toString());
            modell.setAvsenderMottaker(new JournalpostBruker(it.getId(), idType));
        });
    }

    private void tilMottattDato(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        var datoMottatt = journalpostRequest.getDatoMottatt();
        if (datoMottatt == null) {
            modell.setMottattDato(LocalDateTime.now());
        } else {
            modell.setMottattDato(convertToLocalDateTimeViaInstant(datoMottatt));
        }
    }

    private void tilSak(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        var sak = journalpostRequest.getSak();
        if (sak != null) {
            if (sak.getSakstype() == Sak.SakstypeEnum.FAGSAK) {
                modell.setSakId(journalpostRequest.getSak().getFagsakId());
                modell.setFagsystemId(journalpostRequest.getSak().getFagsaksystem().value());
            } else if (sak.getSakstype() == Sak.SakstypeEnum.ARKIVSAK) {
                modell.setSakId(journalpostRequest.getSak().getArkivsaksnummer());
            }
        }
    }

    // Utvid etter behov (https://confluence.adeo.no/display/BOA/oppdaterJournalpost)
    public void oppdaterJournalpost(OppdaterJournalpostRequest oppdaterJournalpostRequest, JournalpostModell modell) {
        Optional.ofNullable(oppdaterJournalpostRequest.getAvsenderMottaker()).ifPresent(am -> {
            var brukerType = new BrukerType(!am.getId().equals(" ") ? am.getIdType().toString() : null);
            var journalpostBruker = new JournalpostBruker(!am.getId().equals(" ") ? am.getId() : null, brukerType);
            modell.setAvsenderMottaker(journalpostBruker);
        });
        Optional.ofNullable(oppdaterJournalpostRequest.getBruker()).ifPresent(bruker -> modell.setBruker(mapAvsenderFraBruker(bruker)));
        Optional.ofNullable(oppdaterJournalpostRequest.getSak()).ifPresent(sak -> modell.setSakId(sak.getFagsakId()));
        Optional.ofNullable(oppdaterJournalpostRequest.getTema()).ifPresent(tema -> modell.setArkivtema(mapArkivtema(tema)));
        Optional.ofNullable(oppdaterJournalpostRequest.getTittel()).ifPresent(modell::setTittel);
    }


    private JournalpostBruker mapAvsenderFraBruker(Bruker bruker) {
        return switch (bruker.getIdType()) {
            case FNR -> new JournalpostBruker(bruker.getId(), BrukerType.FNR);
            case AKTOERID -> new JournalpostBruker(bruker.getId(), BrukerType.AKTOERID);
            case ORGNR -> new JournalpostBruker(bruker.getId(), BrukerType.ORGNR);
        };
    }


    private Arkivtema mapArkivtema(String tema) {
        return new Arkivtema(tema);
    }

    private Journalposttyper mapJournalposttype(OpprettJournalpostRequest.JournalpostTypeEnum type) {
        if (type.value().equalsIgnoreCase("INNGAAENDE")) {
            return Journalposttyper.INNGAAENDE_DOKUMENT;
        } else if (type.value().equalsIgnoreCase("UTGAAENDE")) {
            return Journalposttyper.UTGAAENDE_DOKUMENT;
        } else if (type.value().equalsIgnoreCase("NOTAT")) {
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
