package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BeregningsresultatPeriodeAndel {

    protected String arbeidsgiverNavn;
    protected String arbeidsgiverOrgnr;
    protected Integer refusjon;
    protected Integer tilSoker;
    protected Uttak uttak;
    protected BigDecimal utbetalingsgrad;
    protected LocalDate sisteUtbetalingsdato;
    protected Kode aktivitetStatus;
    protected String arbeidsforholdId;
    protected Kode arbeidsforholdType;

    public String getArbeidsgiverNavn() {
        return arbeidsgiverNavn;
    }

    public String getArbeidsgiverOrgnr() {
        return arbeidsgiverOrgnr;
    }

    public Integer getRefusjon() {
        return refusjon;
    }

    public Integer getTilSoker() {
        return tilSoker;
    }

    public Uttak getUttak() {
        return uttak;
    }

    public BigDecimal getUtbetalingsgrad() {
        return utbetalingsgrad;
    }

    public LocalDate getSisteUtbetalingsdato() {
        return sisteUtbetalingsdato;
    }

    public Kode getAktivitetStatus() {
        return aktivitetStatus;
    }

    public String getArbeidsforholdId() {
        return arbeidsforholdId;
    }

    public Kode getArbeidsforholdType() {
        return arbeidsforholdType;
    }

}
