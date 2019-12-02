package no.nav.oppgave;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Oppgave {

    private final Long id;
    private String tildeltEnhetsnr;
    private final String opprettetAvEnhetsnr;
    private final String endretAvEnhetsnr;
    private final String journalpostId;
    private final String journalpostkilde;
    private final String behandlesAvApplikasjon;
    private String tilordnetRessurs;
    private final String saksreferanse;
    private final String temagruppe;
    private final String beskrivelse;
    private final String tema;
    private final String behandlingstema;
    private final String oppgavetype;
    private final String behandlingstype;
    private final Integer versjon;
    private Long mappeId;
    private final LocalDate fristFerdigstillelse;
    private final LocalDate aktivDato;
    private final LocalDateTime opprettetTidspunkt;
    private final LocalDateTime endretTidspunkt;
    private final LocalDateTime ferdigstiltTidspunkt;
    private final String opprettetAv;
    private final String endretAv;
    private final Prioritet prioritet;
    private final Oppgavestatus status;
    private final Oppgavestatuskategori statuskategori;
    private final Map<MetadataKey, String> metadata;

    private Oppgave(Builder builder) {
        this.id = builder.id;
        this.tildeltEnhetsnr = builder.tildeltEnhetsnr;
        this.opprettetAvEnhetsnr = builder.opprettetAvEnhetsnr;
        this.endretAvEnhetsnr = builder.endretAvEnhetsnr;
        this.journalpostId = builder.journalpostId;
        this.journalpostkilde = builder.journalpostkilde;
        this.behandlesAvApplikasjon = builder.behandlesAvApplikasjon;
        this.saksreferanse = builder.saksreferanse;
        this.tilordnetRessurs = builder.tilordnetRessurs;
        this.temagruppe = builder.temagruppe;
        this.beskrivelse = builder.beskrivelse;
        this.tema = builder.tema;
        this.behandlingstema = builder.behandlingstema;
        this.oppgavetype = builder.oppgavetype;
        this.behandlingstype = builder.behandlingstype;
        this.versjon = builder.versjon;
        this.mappeId = builder.mappeId;
        this.fristFerdigstillelse = builder.fristFerdigstillelse;
        this.aktivDato = builder.aktivDato;
        this.opprettetTidspunkt = builder.opprettetTidspunkt;
        this.endretTidspunkt = builder.endretTidspunkt;
        this.ferdigstiltTidspunkt = builder.ferdigstiltTidspunkt;
        this.prioritet = builder.prioritet;
        this.opprettetAv = builder.opprettetAv;
        this.endretAv = builder.endretAv;
        this.status = builder.status;
        this.statuskategori = builder.statuskategori;
        this.metadata = builder.metadata;
    }

    public Long getId() {
        return id;
    }

    public String getOpprettetAv() {
        return opprettetAv;
    }

    public String getTildeltEnhetsnr() {
        return tildeltEnhetsnr;
    }

    public String getEndretAvEnhetsnr() {
        return endretAvEnhetsnr;
    }

    public String getOpprettetAvEnhetsnr() {
        return opprettetAvEnhetsnr;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getJournalpostkilde() {
        return journalpostkilde;
    }

    public String getBehandlesAvApplikasjon() {
        return behandlesAvApplikasjon;
    }

    public String getSaksreferanse() {
        return saksreferanse;
    }

    public String getTilordnetRessurs() {
        return tilordnetRessurs;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public String getTemagruppe() {
        return temagruppe;
    }

    public String getTema() {
        return tema;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public String getOppgavetype() {
        return oppgavetype;
    }

    public String getBehandlingstype() {
        return behandlingstype;
    }

    public Integer getVersjon() {
        return versjon;
    }

    public Long getMappeId() {
        return mappeId;
    }

    public LocalDate getFristFerdigstillelse() {
        return fristFerdigstillelse;
    }

    public LocalDate getAktivDato() {
        return aktivDato;
    }

    public LocalDateTime getOpprettetTidspunkt() {
        return opprettetTidspunkt;
    }

    public Oppgavestatus getStatus() {
        return status;
    }

    public Oppgavestatuskategori getStatuskategori() {
        return statuskategori;
    }

    public LocalDateTime getFerdigstiltTidspunkt() {
        return ferdigstiltTidspunkt;
    }

    public Prioritet getPrioritet() {
        return prioritet;
    }

    public LocalDateTime getEndretTidspunkt() {
        return endretTidspunkt;
    }

    public String getEndretAv() {
        return endretAv;
    }

    public Map<MetadataKey, String> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Oppgave oppgave = (Oppgave) o;
        return Objects.equals(id, oppgave.id) &&
            Objects.equals(tildeltEnhetsnr, oppgave.tildeltEnhetsnr) &&
            Objects.equals(opprettetAvEnhetsnr, oppgave.opprettetAvEnhetsnr) &&
            Objects.equals(endretAvEnhetsnr, oppgave.endretAvEnhetsnr) &&
            Objects.equals(journalpostId, oppgave.journalpostId) &&
            Objects.equals(journalpostkilde, oppgave.journalpostkilde) &&
            Objects.equals(behandlesAvApplikasjon, oppgave.behandlesAvApplikasjon) &&
            Objects.equals(saksreferanse, oppgave.saksreferanse) &&
            //Objects.equals(ident, oppgave.ident) &&
            Objects.equals(tilordnetRessurs, oppgave.tilordnetRessurs) &&
            Objects.equals(beskrivelse, oppgave.beskrivelse) &&
            Objects.equals(temagruppe, oppgave.temagruppe) &&
            Objects.equals(tema, oppgave.tema) &&
            Objects.equals(behandlingstema, oppgave.behandlingstema) &&
            Objects.equals(oppgavetype, oppgave.oppgavetype) &&
            Objects.equals(behandlingstype, oppgave.behandlingstype) &&
            Objects.equals(versjon, oppgave.versjon) &&
            Objects.equals(mappeId, oppgave.mappeId) &&
            Objects.equals(fristFerdigstillelse, oppgave.fristFerdigstillelse) &&
            Objects.equals(aktivDato, oppgave.aktivDato) &&
            Objects.equals(opprettetTidspunkt, oppgave.opprettetTidspunkt) &&
            Objects.equals(ferdigstiltTidspunkt, oppgave.ferdigstiltTidspunkt) &&
            Objects.equals(opprettetAv, oppgave.opprettetAv) &&
            Objects.equals(status, oppgave.status) &&
            Objects.equals(statuskategori, oppgave.statuskategori) &&
            Objects.equals(metadata, oppgave.metadata) &&
            Objects.equals(prioritet, oppgave.prioritet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tildeltEnhetsnr, opprettetAvEnhetsnr, endretAvEnhetsnr, journalpostId, saksreferanse, /*ident,*/ tilordnetRessurs, temagruppe, tema, behandlingstema, oppgavetype, behandlingstype, versjon, mappeId, fristFerdigstillelse, aktivDato, opprettetTidspunkt, ferdigstiltTidspunkt, opprettetAv, prioritet, status, statuskategori, metadata, endretAv, endretTidspunkt, beskrivelse, journalpostkilde, behandlesAvApplikasjon);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("tildeltEnhetsnr", tildeltEnhetsnr)
            .append("opprettetAvEnhetsnr", opprettetAvEnhetsnr)
            .append("endretAvEnhetsnr", endretAvEnhetsnr)
            .append("journalpostId", journalpostId)
            .append("journalpostkilde", journalpostkilde)
            .append("behandlesAvApplikasjon", behandlesAvApplikasjon)
            .append("saksreferanse", saksreferanse)
            //.append("ident", ident)
            .append("tilordnetRessurs", tilordnetRessurs)
            .append("beskrivelse", "*****")
            .append("temagruppe", temagruppe)
            .append("tema", tema)
            .append("behandlingstema", behandlingstema)
            .append("oppgavetype", oppgavetype)
            .append("behandlingstype", behandlingstype)
            .append("versjon", versjon)
            .append("mappeId", mappeId)
            .append("fristFerdigstillelse", fristFerdigstillelse)
            .append("aktivDato", aktivDato)
            .append("opprettetTidspunkt", opprettetTidspunkt)
            .append("ferdigstiltTidspunkt", ferdigstiltTidspunkt)
            .append("opprettetAv", opprettetAv)
            .append("prioritet", prioritet)
            .append("status", status)
            .append("statuskategori", statuskategori)
            .append("endretAv", endretAv)
            .append("endretTidspunkt", endretTidspunkt)
            .append("metadata", metadata)
            .toString();
    }

    void fordelTilEnhet(String enhetsnr) {
        this.tildeltEnhetsnr = enhetsnr;
    }

    void fordelMappeId(Long mappeId){
        this.mappeId=mappeId;
    }

    void fordelTilordnetRessurs(String tilordnetRessurs){
        this.tilordnetRessurs=tilordnetRessurs;
    }

    public static final class Builder {
        private Long id;
        private String tildeltEnhetsnr;
        private String endretAvEnhetsnr;
        private String opprettetAvEnhetsnr;
        private String journalpostId;
        private String journalpostkilde;
        private String behandlesAvApplikasjon;
        private String saksreferanse;
        private String tilordnetRessurs;
        private String beskrivelse;
        private String temagruppe;
        private String tema;
        private String behandlingstema;
        private String oppgavetype;
        private String behandlingstype;
        private Integer versjon;
        private Long mappeId;
        private LocalDate fristFerdigstillelse;
        private LocalDate aktivDato;
        private LocalDateTime opprettetTidspunkt;
        private LocalDateTime endretTidspunkt;
        private LocalDateTime ferdigstiltTidspunkt;
        private Prioritet prioritet;
        private String opprettetAv;
        private String endretAv;
        private Oppgavestatus status;
        private Oppgavestatuskategori statuskategori;
        private Map<MetadataKey, String> metadata;

        Builder() {
        }

        public static Builder enOppgave() {
            return new Builder();
        }

        Builder medId(Long id) {
            this.id = id;
            return this;
        }

        Builder medTildeltEnhetsnr(String tildeltEnhetsnr) {
            this.tildeltEnhetsnr = tildeltEnhetsnr;
            return this;
        }

        Builder medOpprettetAvEnhetsnr(String opprettetAvEnhetsnr) {
            this.opprettetAvEnhetsnr = opprettetAvEnhetsnr;
            return this;
        }

        Builder medEndretAvEnhetsnr(String endretAvEnhetsnr) {
            this.endretAvEnhetsnr = endretAvEnhetsnr;
            return this;
        }

        Builder medJournalpostId(String journalpostId) {
            this.journalpostId = journalpostId;
            return this;
        }

        Builder medJournalpostkilde(String journalpostkilde) {
            this.journalpostkilde = journalpostkilde;
            return this;
        }

        Builder medBehandlesAvApplikasjon(String behandlesAvApplikasjon) {
            this.behandlesAvApplikasjon = behandlesAvApplikasjon;
            return this;
        }

        Builder medSaksreferanse(String saksreferanse) {
            this.saksreferanse = saksreferanse;
            return this;
        }

        Builder medBeskrivelse(String beskrivelse) {
            this.beskrivelse = beskrivelse;
            return this;
        }

        Builder medTilordnetRessurs(String tilordnetRessurs) {
            this.tilordnetRessurs = tilordnetRessurs;
            return this;
        }

        Builder medTemagruppe(String temagruppe) {
            this.temagruppe = temagruppe;
            return this;
        }

        Builder medTema(String tema) {
            this.tema = tema;
            return this;
        }

        Builder medBehandlingstema(String behandlingstema) {
            this.behandlingstema = behandlingstema;
            return this;
        }

        Builder medOppgavetype(String oppgavetype) {
            this.oppgavetype = oppgavetype;
            return this;
        }

        Builder medBehandlingsType(String behandlingstype) {
            this.behandlingstype = behandlingstype;
            return this;
        }

        Builder medVersjon(Integer versjon) {
            this.versjon = versjon;
            return this;
        }

        Builder medMappeId(Long mappeId) {
            this.mappeId = mappeId;
            return this;
        }

        Builder medFristFerdigstillelse(LocalDate fristFerdigstillelse) {
            this.fristFerdigstillelse = fristFerdigstillelse;
            return this;
        }

        Builder medAktivDato(LocalDate aktivDato) {
            this.aktivDato = aktivDato;
            return this;
        }

        Builder medOpprettetTidspunkt(LocalDateTime opprettetDato) {
            this.opprettetTidspunkt = opprettetDato;
            return this;
        }

        Builder medFerdigstiltTidspunkt(LocalDateTime ferdigstiltTidspunkt) {
            this.ferdigstiltTidspunkt = ferdigstiltTidspunkt;
            return this;
        }

        Builder medPrioritet(Prioritet prioritet) {
            this.prioritet = prioritet;
            return this;
        }

        Builder medOpprettetAv(String opprettetAv) {
            this.opprettetAv = opprettetAv;
            return this;
        }

        Builder medStatus(Oppgavestatus status) {
            this.status = status;
            return this;
        }

        Builder medStatuskategori(Oppgavestatuskategori statuskategori) {
            this.statuskategori = statuskategori;
            return this;
        }

        Builder medEndretAv(String endretAv) {
            this.endretAv = endretAv;
            return this;
        }

        Builder medEndretTidspunkt(LocalDateTime endretTidspunkt) {
            this.endretTidspunkt = endretTidspunkt;
            return this;
        }

        Builder medMetadata(Map<MetadataKey, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Oppgave build() {
            Validate.validState(!StringUtils.isAllBlank(temagruppe, tema), "temagruppe eller tema må være angitt");
            Validate.notNull(versjon, "versjon må være angitt");
            Validate.notNull(aktivDato, "aktivDato må være angitt");
            Validate.notNull(status, "status må være angitt");
            Validate.notNull(prioritet, "prioritet må være angitt");
            Validate.notBlank(oppgavetype, "oppgavetype må være angitt");

            return new Oppgave(this);
        }
    }
}
