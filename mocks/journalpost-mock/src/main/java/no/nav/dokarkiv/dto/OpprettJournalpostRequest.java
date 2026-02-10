package no.nav.dokarkiv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpprettJournalpostRequest(JournalpostType journalpostType,
                                        String tittel,
                                        AvsenderMottaker avsenderMottaker,
                                        String kanal,
                                        Bruker bruker,
                                        String tema,
                                        String behandlingstema,
                                        Sak sak,
                                        String journalfoerendeEnhet,
                                        String datoMottatt,  // Bør være LocalDateTime, men kan være LocalDate eller eldre format
                                        String eksternReferanseId,
                                        List<Tilleggsopplysning> tilleggsopplysninger,
                                        List<DokumentInfoOpprett> dokumenter,
                                        String datoDokument, // Bør være LocalDateTime, men kan være LocalDate eller eldre format
                                        String overstyrInnsynsregler) {

    public record DokumentInfoOpprett(String tittel, String brevkode, Integer rekkefoelge, List<Dokumentvariant> dokumentvarianter) {

    }
}
