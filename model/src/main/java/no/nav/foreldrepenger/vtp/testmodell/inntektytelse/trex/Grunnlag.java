package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Grunnlag {
    private Status status;
    private Tema tema;
    private Prosent dekningsgrad;
    private LocalDate fødselsdatoBarn;
    private Arbeidskategori kategori;
    private List<Arbeidsforhold> arbeidsforhold;
    private Periode periode;
    private Behandlingstema behandlingstema;
    private LocalDate identdato;
    private LocalDate iverksatt;
    private LocalDate opphørFom;
    private Integer gradering;
    private LocalDate opprinneligIdentdato;
    private LocalDate registrert;
    private String saksbehandlerId;
    private List<Vedtak> vedtak;

    @JsonCreator
    public Grunnlag(@JsonProperty("status") Status status,
            @JsonProperty("tema") Tema tema,
            @JsonProperty("dekningsgrad") Prosent dekningsgrad,
            @JsonProperty("fødselsdatoBarn") @JsonAlias("foedselsdatoBarn") LocalDate fødselsdatoBarn,
            @JsonProperty("kategori") @JsonAlias("arbeidskategori") Arbeidskategori kategori,
            @JsonProperty("arbeidsforhold") List<Arbeidsforhold> arbeidsforhold,
            @JsonProperty("periode") Periode periode,
            @JsonProperty("behandlingstema") Behandlingstema behandlingstema,
            @JsonProperty("identdato") LocalDate identdato,
            @JsonProperty("iverksatt") LocalDate iverksatt,
            @JsonProperty("opphørFom") @JsonAlias("opphoerFom") LocalDate opphørFom,
            @JsonProperty("gradering") Integer gradering,
            @JsonProperty("opprinneligIdentdato") LocalDate opprinneligIdentdato,
            @JsonProperty("registrert") LocalDate registrert,
            @JsonProperty("saksbehandlerId") String saksbehandlerId,
            @JsonProperty("vedtak") List<Vedtak> vedtak) {
        this.status = status;
        this.tema = tema;
        this.dekningsgrad = dekningsgrad;
        this.fødselsdatoBarn = fødselsdatoBarn;
        this.kategori = kategori;
        this.arbeidsforhold = arbeidsforhold;
        this.periode = periode;
        this.behandlingstema = behandlingstema;
        this.identdato = identdato;
        this.iverksatt = iverksatt;
        this.opphørFom = opphørFom;
        this.gradering = gradering;
        this.opprinneligIdentdato = opprinneligIdentdato;
        this.registrert = registrert;
        this.saksbehandlerId = saksbehandlerId;
        this.vedtak = vedtak;
    }

    public Status getStatus() {
        return status;
    }

    public Tema getTema() {
        return tema;
    }

    public Prosent getDekningsgrad() {
        return dekningsgrad;
    }

    public LocalDate getFødselsdatoBarn() {
        return fødselsdatoBarn;
    }

    public Arbeidskategori getKategori() {
        return kategori;
    }

    public List<Arbeidsforhold> getArbeidsforhold() {
        return arbeidsforhold;
    }

    public Periode getPeriode() {
        return periode;
    }

    public Behandlingstema getBehandlingstema() {
        return behandlingstema;
    }

    public LocalDate getIdentdato() {
        return identdato;
    }

    public LocalDate getIverksatt() {
        return iverksatt;
    }

    public LocalDate getOpphørFom() {
        return opphørFom;
    }

    public Integer getGradering() {
        return gradering;
    }

    public LocalDate getOpprinneligIdentdato() {
        return opprinneligIdentdato;
    }

    public LocalDate getRegistrert() {
        return registrert;
    }

    public String getSaksbehandlerId() {
        return saksbehandlerId;
    }

    public List<Vedtak> getVedtak() {
        return vedtak;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arbeidsforhold, behandlingstema, dekningsgrad, fødselsdatoBarn, gradering, identdato,
                iverksatt, kategori, opphørFom, opprinneligIdentdato, periode, registrert, saksbehandlerId, status,
                tema, vedtak);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Grunnlag)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        Grunnlag that = (Grunnlag) obj;
        return Objects.equals(that.arbeidsforhold, this.arbeidsforhold) &&
                Objects.equals(that.behandlingstema, this.behandlingstema) &&
                Objects.equals(that.dekningsgrad, this.dekningsgrad) &&
                Objects.equals(that.fødselsdatoBarn, this.fødselsdatoBarn) &&
                Objects.equals(that.gradering, this.gradering) &&
                Objects.equals(that.iverksatt, this.iverksatt) &&
                Objects.equals(that.kategori, this.kategori) &&
                Objects.equals(that.opphørFom, this.opphørFom) &&
                Objects.equals(that.opprinneligIdentdato, this.opprinneligIdentdato) &&
                Objects.equals(that.periode, this.periode) &&
                Objects.equals(that.registrert, this.registrert) &&
                Objects.equals(that.saksbehandlerId, this.saksbehandlerId) &&
                Objects.equals(that.status, this.status) &&
                Objects.equals(that.tema, this.tema) &&
                Objects.equals(that.vedtak, this.vedtak);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[status=" + status + ", tema=" + tema + ", dekningsgrad=" + dekningsgrad
                + ", fødselsdatoBarn=" + fødselsdatoBarn + ", kategori=" + kategori + ", arbeidsforhold="
                + arbeidsforhold + ", periode=" + periode + ", behandlingstema=" + behandlingstema + ", identdato="
                + identdato + ", iverksatt=" + iverksatt + ", opphørFom=" + opphørFom + ", gradering=" + gradering
                + ", opprinneligIdentdato=" + opprinneligIdentdato + ", registrert=" + registrert + ", saksbehandlerId="
                + saksbehandlerId + ", vedtak=" + vedtak + "]";
    }
}
