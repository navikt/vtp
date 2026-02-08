package no.nav.dokarkiv.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OppdaterJournalpostRequest(String tittel,
                                         AvsenderMottaker avsenderMottaker,
                                         Bruker bruker,
                                         String tema,
                                         String behandlingstema,
                                         Sak sak,
                                         List<Tilleggsopplysning> tilleggsopplysninger,
                                         List<DokumentInfoOppdater> dokumenter,
                                         String datoDokument,  // Bør være LocalDateTime, men kan være LocalDate eller eldre format
                                         String overstyrInnsynsregler) {



    public record DokumentInfoOppdater(String dokumentInfoId, String tittel, String brevkode) {
    }

}
