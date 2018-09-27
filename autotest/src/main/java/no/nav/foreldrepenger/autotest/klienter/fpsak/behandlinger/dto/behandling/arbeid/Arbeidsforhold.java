package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arbeidsforhold {
    
    protected String id = null;
    private String navn = null;
    protected String arbeidsgiverIdentifikator = null;
    protected String arbeidsforholdId = null;
    protected LocalDate fomDato = null;
    protected LocalDate tomDato = null;
    protected ArbeidsforholdKilde kilde = null;
    protected LocalDate mottattDatoInntektsmelding = null;
    protected String beskrivelse = null;
    protected BigDecimal stillingsprosent = null;
    private Boolean brukArbeidsforholdet = null;
    private Boolean fortsettBehandlingUtenInntektsmelding = null;
    protected Boolean erNyttArbeidsforhold = null;
    protected Boolean erEndret = null;
    protected Boolean erSlettet = null;
    protected String erstatterArbeidsforholdId = null;
    protected Boolean harErstattetEttEllerFlere = null;
    protected Boolean ikkeRegistrertIAaRegister = null;
    protected Boolean tilVurdering = null;
    protected Boolean vurderOmSkalErstattes = null;
    public Boolean getBrukArbeidsforholdet() {
        return brukArbeidsforholdet;
    }
    public void setBrukArbeidsforholdet(Boolean brukArbeidsforholdet) {
        this.brukArbeidsforholdet = brukArbeidsforholdet;
    }
    public Boolean getFortsettBehandlingUtenInntektsmelding() {
        return fortsettBehandlingUtenInntektsmelding;
    }
    public void setFortsettBehandlingUtenInntektsmelding(Boolean fortsettBehandlingUtenInntektsmelding) {
        this.fortsettBehandlingUtenInntektsmelding = fortsettBehandlingUtenInntektsmelding;
    }
    public String getNavn() {
        return navn;
    }
    public void setNavn(String navn) {
        this.navn = navn;
    }
}
