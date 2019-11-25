package no.nav.saf.domain.visningsmodell;

import no.nav.saf.domain.kode.Journalposttype;
import no.nav.saf.domain.kode.Journalstatus;
import no.nav.saf.domain.kode.Kanal;
import no.nav.saf.domain.kode.Skjerming;
import no.nav.saf.domain.kode.Tema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Journalpost {
    private final String journalpostId;
    private final String tittel;
    private final Journalposttype journalposttype;
    private final Journalstatus journalstatus;
    private final Tema tema;
    private final String temanavn;
    private final String behandlingstema;
    private final String behandlingstemanavn;
    private final Sak sak;
    private final Bruker bruker;
    private final AvsenderMottaker avsenderMottaker;

    /**
     * @deprecated Konsumenter bes bruke {@code AvsenderMottaker.id} i stedet. Feltet overvåkes for bruk og vil bli fjernet i fremtiden.
     * @since 4.5.0
     */
    @Deprecated(since = "4.5.0", forRemoval = true)
    private final String avsenderMottakerId;
    /**
     * @deprecated Konsumenter bes bruke {@code AvsenderMottaker.navn} i stedet. Feltet overvåkes for bruk og vil bli fjernet i fremtiden.
     * @since 4.5.0
     */
    @Deprecated(since = "4.5.0", forRemoval = true)
    private final String avsenderMottakerNavn;
    /**
     * @deprecated Konsumenter bes bruke {@code AvsenderMottaker.land} i stedet. Feltet overvåkes for bruk og vil bli fjernet i fremtiden.
     * @since 4.5.0
     */
    @Deprecated(since = "4.5.0", forRemoval = true)
    private final String avsenderMottakerLand;
    /**
     * @deprecated Konsumenter bes bruke {@code journalfoerendeEnhet} i stedet. Feltet overvåkes for bruk og vil bli fjernet i fremtiden.
     * @since 6.2.0
     */
    @Deprecated(since = "6.2.0", forRemoval = true)
    private final String journalforendeEnhet;
    private final String journalfoerendeEnhet;
    private final String journalfortAvNavn;
    private final String opprettetAvNavn;
    private final Kanal kanal;
    private final String kanalnavn;
    private final Skjerming skjerming;
    private final LocalDateTime datoOpprettet;
    //@Builder.Default
    private final List<RelevantDato> relevanteDatoer = new ArrayList<>();
    private final String antallRetur;
    //@Builder.Default
    private final List<Tilleggsopplysning> tilleggsopplysninger = new ArrayList<>();
    //@Builder.Default
    private final List<DokumentInfo> dokumenter = new ArrayList<>();

    private Journalpost(Builder builder) {
        journalpostId = builder.journalpostId;
        tittel = builder.tittel;
        journalposttype = builder.journalposttype;
        journalstatus = builder.journalstatus;
        tema = builder.tema;
        temanavn = builder.temanavn;
        behandlingstema = builder.behandlingstema;
        behandlingstemanavn = builder.behandlingstemanavn;
        sak = builder.sak;
        bruker = builder.bruker;
        avsenderMottaker = builder.avsenderMottaker;
        avsenderMottakerId = builder.avsenderMottakerId;
        avsenderMottakerNavn = builder.avsenderMottakerNavn;
        avsenderMottakerLand = builder.avsenderMottakerLand;
        journalforendeEnhet = builder.journalforendeEnhet;
        journalfoerendeEnhet = builder.journalfoerendeEnhet;
        journalfortAvNavn = builder.journalfortAvNavn;
        opprettetAvNavn = builder.opprettetAvNavn;
        kanal = builder.kanal;
        kanalnavn = builder.kanalnavn;
        skjerming = builder.skjerming;
        datoOpprettet = builder.datoOpprettet;
        antallRetur = builder.antallRetur;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Journalpost copy) {
        Builder builder = new Builder();
        builder.journalpostId = copy.getJournalpostId();
        builder.tittel = copy.getTittel();
        builder.journalposttype = copy.getJournalposttype();
        builder.journalstatus = copy.getJournalstatus();
        builder.tema = copy.getTema();
        builder.temanavn = copy.getTemanavn();
        builder.behandlingstema = copy.getBehandlingstema();
        builder.behandlingstemanavn = copy.getBehandlingstemanavn();
        builder.sak = copy.getSak();
        builder.bruker = copy.getBruker();
        builder.avsenderMottaker = copy.getAvsenderMottaker();
        builder.avsenderMottakerId = copy.getAvsenderMottakerId();
        builder.avsenderMottakerNavn = copy.getAvsenderMottakerNavn();
        builder.avsenderMottakerLand = copy.getAvsenderMottakerLand();
        builder.journalforendeEnhet = copy.getJournalforendeEnhet();
        builder.journalfoerendeEnhet = copy.getJournalfoerendeEnhet();
        builder.journalfortAvNavn = copy.getJournalfortAvNavn();
        builder.opprettetAvNavn = copy.getOpprettetAvNavn();
        builder.kanal = copy.getKanal();
        builder.kanalnavn = copy.getKanalnavn();
        builder.skjerming = copy.getSkjerming();
        builder.datoOpprettet = copy.getDatoOpprettet();
        builder.antallRetur = copy.getAntallRetur();
        return builder;
    }

    public String getJournalpostId() {
        return journalpostId;
    }

    public String getTittel() {
        return tittel;
    }

    public Journalposttype getJournalposttype() {
        return journalposttype;
    }

    public Journalstatus getJournalstatus() {
        return journalstatus;
    }

    public Tema getTema() {
        return tema;
    }

    public String getTemanavn() {
        return temanavn;
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public String getBehandlingstemanavn() {
        return behandlingstemanavn;
    }

    public Sak getSak() {
        return sak;
    }

    public Bruker getBruker() {
        return bruker;
    }

    public AvsenderMottaker getAvsenderMottaker() {
        return avsenderMottaker;
    }

    public String getAvsenderMottakerId() {
        return avsenderMottakerId;
    }

    public String getAvsenderMottakerNavn() {
        return avsenderMottakerNavn;
    }

    public String getAvsenderMottakerLand() {
        return avsenderMottakerLand;
    }

    public String getJournalforendeEnhet() {
        return journalforendeEnhet;
    }

    public String getJournalfoerendeEnhet() {
        return journalfoerendeEnhet;
    }

    public String getJournalfortAvNavn() {
        return journalfortAvNavn;
    }

    public String getOpprettetAvNavn() {
        return opprettetAvNavn;
    }

    public Kanal getKanal() {
        return kanal;
    }

    public String getKanalnavn() {
        return kanalnavn;
    }

    public Skjerming getSkjerming() {
        return skjerming;
    }

    public LocalDateTime getDatoOpprettet() {
        return datoOpprettet;
    }

    public List<RelevantDato> getRelevanteDatoer() {
        return relevanteDatoer;
    }

    public String getAntallRetur() {
        return antallRetur;
    }

    public List<Tilleggsopplysning> getTilleggsopplysninger() {
        return tilleggsopplysninger;
    }

    public List<DokumentInfo> getDokumenter() {
        return dokumenter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Journalpost that = (Journalpost) o;
        return Objects.equals(getJournalpostId(), that.getJournalpostId()) &&
                Objects.equals(getTittel(), that.getTittel()) &&
                getJournalposttype() == that.getJournalposttype() &&
                getJournalstatus() == that.getJournalstatus() &&
                getTema() == that.getTema() &&
                Objects.equals(getTemanavn(), that.getTemanavn()) &&
                Objects.equals(getBehandlingstema(), that.getBehandlingstema()) &&
                Objects.equals(getBehandlingstemanavn(), that.getBehandlingstemanavn()) &&
                Objects.equals(getSak(), that.getSak()) &&
                Objects.equals(getBruker(), that.getBruker()) &&
                Objects.equals(getAvsenderMottaker(), that.getAvsenderMottaker()) &&
                Objects.equals(getAvsenderMottakerId(), that.getAvsenderMottakerId()) &&
                Objects.equals(getAvsenderMottakerNavn(), that.getAvsenderMottakerNavn()) &&
                Objects.equals(getAvsenderMottakerLand(), that.getAvsenderMottakerLand()) &&
                Objects.equals(getJournalforendeEnhet(), that.getJournalforendeEnhet()) &&
                Objects.equals(getJournalfoerendeEnhet(), that.getJournalfoerendeEnhet()) &&
                Objects.equals(getJournalfortAvNavn(), that.getJournalfortAvNavn()) &&
                Objects.equals(getOpprettetAvNavn(), that.getOpprettetAvNavn()) &&
                getKanal() == that.getKanal() &&
                Objects.equals(getKanalnavn(), that.getKanalnavn()) &&
                getSkjerming() == that.getSkjerming() &&
                Objects.equals(getDatoOpprettet(), that.getDatoOpprettet()) &&
                Objects.equals(getRelevanteDatoer(), that.getRelevanteDatoer()) &&
                Objects.equals(getAntallRetur(), that.getAntallRetur()) &&
                Objects.equals(getTilleggsopplysninger(), that.getTilleggsopplysninger()) &&
                Objects.equals(getDokumenter(), that.getDokumenter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getJournalpostId(), getTittel(), getJournalposttype(), getJournalstatus(), getTema(), getTemanavn(), getBehandlingstema(), getBehandlingstemanavn(), getSak(), getBruker(), getAvsenderMottaker(), getAvsenderMottakerId(), getAvsenderMottakerNavn(), getAvsenderMottakerLand(), getJournalforendeEnhet(), getJournalfoerendeEnhet(), getJournalfortAvNavn(), getOpprettetAvNavn(), getKanal(), getKanalnavn(), getSkjerming(), getDatoOpprettet(), getRelevanteDatoer(), getAntallRetur(), getTilleggsopplysninger(), getDokumenter());
    }

    public static final class Builder {
        private String journalpostId;
        private String tittel;
        private Journalposttype journalposttype;
        private Journalstatus journalstatus;
        private Tema tema;
        private String temanavn;
        private String behandlingstema;
        private String behandlingstemanavn;
        private Sak sak;
        private Bruker bruker;
        private AvsenderMottaker avsenderMottaker;
        private String avsenderMottakerId;
        private String avsenderMottakerNavn;
        private String avsenderMottakerLand;
        private String journalforendeEnhet;
        private String journalfoerendeEnhet;
        private String journalfortAvNavn;
        private String opprettetAvNavn;
        private Kanal kanal;
        private String kanalnavn;
        private Skjerming skjerming;
        private LocalDateTime datoOpprettet;
        private String antallRetur;

        private Builder() {
        }

        public Builder withJournalpostId(String journalpostId) {
            this.journalpostId = journalpostId;
            return this;
        }

        public Builder withTittel(String tittel) {
            this.tittel = tittel;
            return this;
        }

        public Builder withJournalposttype(Journalposttype journalposttype) {
            this.journalposttype = journalposttype;
            return this;
        }

        public Builder withJournalstatus(Journalstatus journalstatus) {
            this.journalstatus = journalstatus;
            return this;
        }

        public Builder withTema(Tema tema) {
            this.tema = tema;
            return this;
        }

        public Builder withTemanavn(String temanavn) {
            this.temanavn = temanavn;
            return this;
        }

        public Builder withBehandlingstema(String behandlingstema) {
            this.behandlingstema = behandlingstema;
            return this;
        }

        public Builder withBehandlingstemanavn(String behandlingstemanavn) {
            this.behandlingstemanavn = behandlingstemanavn;
            return this;
        }

        public Builder withSak(Sak sak) {
            this.sak = sak;
            return this;
        }

        public Builder withBruker(Bruker bruker) {
            this.bruker = bruker;
            return this;
        }

        public Builder withAvsenderMottaker(AvsenderMottaker avsenderMottaker) {
            this.avsenderMottaker = avsenderMottaker;
            return this;
        }

        public Builder withAvsenderMottakerId(String avsenderMottakerId) {
            this.avsenderMottakerId = avsenderMottakerId;
            return this;
        }

        public Builder withAvsenderMottakerNavn(String avsenderMottakerNavn) {
            this.avsenderMottakerNavn = avsenderMottakerNavn;
            return this;
        }

        public Builder withAvsenderMottakerLand(String avsenderMottakerLand) {
            this.avsenderMottakerLand = avsenderMottakerLand;
            return this;
        }

        public Builder withJournalforendeEnhet(String journalforendeEnhet) {
            this.journalforendeEnhet = journalforendeEnhet;
            return this;
        }

        public Builder withJournalfoerendeEnhet(String journalfoerendeEnhet) {
            this.journalfoerendeEnhet = journalfoerendeEnhet;
            return this;
        }

        public Builder withJournalfortAvNavn(String journalfortAvNavn) {
            this.journalfortAvNavn = journalfortAvNavn;
            return this;
        }

        public Builder withOpprettetAvNavn(String opprettetAvNavn) {
            this.opprettetAvNavn = opprettetAvNavn;
            return this;
        }

        public Builder withKanal(Kanal kanal) {
            this.kanal = kanal;
            return this;
        }

        public Builder withKanalnavn(String kanalnavn) {
            this.kanalnavn = kanalnavn;
            return this;
        }

        public Builder withSkjerming(Skjerming skjerming) {
            this.skjerming = skjerming;
            return this;
        }

        public Builder withDatoOpprettet(LocalDateTime datoOpprettet) {
            this.datoOpprettet = datoOpprettet;
            return this;
        }

        public Builder withAntallRetur(String antallRetur) {
            this.antallRetur = antallRetur;
            return this;
        }

        public Journalpost build() {
            return new Journalpost(this);
        }
    }
}
