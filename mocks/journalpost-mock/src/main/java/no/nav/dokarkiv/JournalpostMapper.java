package no.nav.dokarkiv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import no.nav.dokarkiv.dto.Bruker;
import no.nav.dokarkiv.dto.Dokumentvariant;
import no.nav.dokarkiv.dto.JournalpostType;
import no.nav.dokarkiv.dto.OppdaterJournalpostRequest;
import no.nav.dokarkiv.dto.OpprettJournalpostRequest;
import no.nav.dokarkiv.dto.Sak;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.DokumentVariantInnhold;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostBruker;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.JournalpostModell;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.Tilleggsopplysning;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivfiltype;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.Arkivtema;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.BrukerType;
import no.nav.foreldrepenger.vtp.testmodell.dokument.modell.koder.DokumentTilknyttetJournalpost;
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
        modell.setJournalposttype(mapJournalposttype(journalpostRequest.journalpostType()));
        modell.setArkivtema(mapArkivtema(journalpostRequest.tema()));
        modell.setBruker(mapAvsenderFraBruker(journalpostRequest.bruker()));
        tilSak(journalpostRequest, modell);
        tilMottattDato(journalpostRequest, modell);
        tilAvsenderMottaker(journalpostRequest, modell);
        modell.setDokumentModellList(tilDokumentModeller(journalpostRequest.dokumenter()));
        modell.setTittel(journalpostRequest.tittel());
        modell.setEksternReferanseId(journalpostRequest.eksternReferanseId());
        modell.setMottakskanal(Mottakskanal.fraKode(journalpostRequest.kanal()));
        modell.setJournalStatus(erKnyttetTilSak(journalpostRequest.sak()) ? Journalstatus.JOURNALFØRT : Journalstatus.MOTTATT);
        modell.setBehandlingTema(journalpostRequest.behandlingstema());
        modell.setTilleggsopplysninger(mapTilleggsOpplysninger(journalpostRequest.tilleggsopplysninger()));
        return modell;
    }

    private List<Tilleggsopplysning> mapTilleggsOpplysninger(List<no.nav.dokarkiv.dto.Tilleggsopplysning> tilleggsOpplysninger) {
        if (tilleggsOpplysninger == null) {
            return List.of();
        }
        return tilleggsOpplysninger.stream()
                .map(to -> new Tilleggsopplysning(to.nokkel(), to.verdi()))
                .toList();
    }

    private boolean erKnyttetTilSak(Sak sak) {
        return sak != null && sak.fagsakId() != null;
    }

    private List<DokumentModell> tilDokumentModeller(List<OpprettJournalpostRequest.DokumentInfoOpprett> dokumenter) {
        var dokumentModeller = new ArrayList<DokumentModell>();
        if (dokumenter == null) {
            return dokumentModeller;
        }
        var hoveddokument = tilDokumentModell(dokumenter.removeFirst(), DokumentTilknyttetJournalpost.HOVEDDOKUMENT);
        var vedlegg = dokumenter.stream()
                .map(dokument -> tilDokumentModell(dokument, DokumentTilknyttetJournalpost.VEDLEGG))
                .toList();

        dokumentModeller.add(hoveddokument);
        dokumentModeller.addAll(vedlegg);
        return dokumentModeller;
    }

    private static DokumentModell tilDokumentModell(OpprettJournalpostRequest.DokumentInfoOpprett dokument, DokumentTilknyttetJournalpost dokumentTilknyttetJournalpost) {
        return new DokumentModell(
                null,
                null,
                false,
                dokument.tittel(),
                dokument.brevkode(),
                null,
                dokumentTilknyttetJournalpost,
                tilDokumentVarianter(dokument.dokumentvarianter())
        );
    }

    private static List<DokumentVariantInnhold> tilDokumentVarianter(List<Dokumentvariant> dokumentvarianter) {
        return dokumentvarianter.stream()
                .map(JournalpostMapper::tilDokumentVariant)
                .toList();
    }

    private static DokumentVariantInnhold tilDokumentVariant(Dokumentvariant dokumentVariant) {
        return new DokumentVariantInnhold(
                new Arkivfiltype(dokumentVariant.filtype()),
                new Variantformat(dokumentVariant.variantformat()),
                dokumentVariant.fysiskDokument()
        );
    }

    private void tilAvsenderMottaker(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        Optional.ofNullable(journalpostRequest.avsenderMottaker()).ifPresent(it -> {
            if (it.idType() != null && it.id() != null) {
                var idType = new BrukerType(it.idType().name());
                modell.setAvsenderMottaker(new JournalpostBruker(it.id(), idType));
            }
        });
    }

    private void tilMottattDato(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        var datoMottatt = journalpostRequest.datoMottatt();
        if (datoMottatt == null) {
            modell.setMottattDato(LocalDateTime.now());
        } else {
            modell.setMottattDato(fraDatoString(datoMottatt));
        }
    }

    private LocalDateTime fraDatoString(@NotNull String dato) {
        try {
            return LocalDateTime.parse(dato);
        } catch (Exception _) {
            try {
                var localDate = LocalDate.parse(dato);
                return localDate.atStartOfDay();
            } catch (Exception ex) {
                throw new IllegalArgumentException("Ugyldig datoformat: " + dato, ex);
            }
        }
    }

    private void tilSak(OpprettJournalpostRequest journalpostRequest, JournalpostModell modell) {
        var sak = journalpostRequest.sak();
        if (sak != null && sak.sakstype() == Sak.Sakstype.FAGSAK) {
            modell.setSakId(journalpostRequest.sak().fagsakId());
            modell.setFagsystemId(journalpostRequest.sak().fagsaksystem());
        }
    }

    // Utvid etter behov (https://confluence.adeo.no/display/BOA/oppdaterJournalpost)
    public void oppdaterJournalpost(OppdaterJournalpostRequest oppdaterJournalpostRequest, JournalpostModell modell) {
        Optional.ofNullable(oppdaterJournalpostRequest.avsenderMottaker()).ifPresent(am -> {
            var brukerType = new BrukerType(!am.id().equals(" ") ? am.idType().name() : null);
            var journalpostBruker = new JournalpostBruker(!am.id().equals(" ") ? am.id() : null, brukerType);
            modell.setAvsenderMottaker(journalpostBruker);
        });
        Optional.ofNullable(oppdaterJournalpostRequest.bruker()).ifPresent(bruker -> modell.setBruker(mapAvsenderFraBruker(bruker)));
        Optional.ofNullable(oppdaterJournalpostRequest.sak()).ifPresent(sak -> modell.setSakId(sak.fagsakId()));
        Optional.ofNullable(oppdaterJournalpostRequest.tema()).ifPresent(tema -> modell.setArkivtema(mapArkivtema(tema)));
        Optional.ofNullable(oppdaterJournalpostRequest.tittel()).ifPresent(modell::setTittel);
    }


    private JournalpostBruker mapAvsenderFraBruker(Bruker bruker) {
        return switch (bruker.idType()) {
            case FNR -> new JournalpostBruker(bruker.id(), BrukerType.FNR);
            case AKTOERID -> new JournalpostBruker(bruker.id(), BrukerType.AKTOERID);
            case ORGNR -> new JournalpostBruker(bruker.id(), BrukerType.ORGNR);
            case UKJENT -> throw new IllegalArgumentException(bruker.idType().name());
        };
    }


    private Arkivtema mapArkivtema(String tema) {
        return new Arkivtema(tema);
    }

    private Journalposttyper mapJournalposttype(JournalpostType type) {
        return switch (type) {
            case INNGAAENDE -> Journalposttyper.INNGAAENDE_DOKUMENT;
            case UTGAAENDE -> Journalposttyper.UTGAAENDE_DOKUMENT;
            case NOTAT -> Journalposttyper.NOTAT;
        };
    }

}
