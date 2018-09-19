package no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.arbeid;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Arbeidsforhold {
    
    public String id = null;
    public String navn = null;
    public String arbeidsgiverIdentifikator = null;
    public String arbeidsforholdId = null;
    public LocalDate fomDato = null;
    public LocalDate tomDato = null;
    public ArbeidsforholdKilde kilde = null;
    public LocalDate mottattDatoInntektsmelding = null;
    public String beskrivelse = null;
    public BigDecimal stillingsprosent = null;
    public Boolean brukArbeidsforholdet = null;
    public Boolean fortsettBehandlingUtenInntektsmelding = null;
    public Boolean erNyttArbeidsforhold = null;
    public Boolean erEndret = null;
    public Boolean erSlettet = null;
    public String erstatterArbeidsforholdId = null;
    public Boolean harErstattetEttEllerFlere = null;
    public Boolean ikkeRegistrertIAaRegister = null;
    public Boolean tilVurdering = null;
    public Boolean vurderOmSkalErstattes = null;
}
