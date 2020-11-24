package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PermisjonPermitteringRS {

    @JsonProperty("periode")
    private PeriodeRS periode;
    @JsonProperty("prosent")
    private Double prosent;
    @JsonProperty("type")
    private String type; // kodeverk: PermisjonsOgPermitteringsBeskrivelse

    public PeriodeRS getPeriode() {
        return periode;
    }

    public Double getProsent() {
        return prosent;
    }

    public String getType() {
        return type;
    }

    public PermisjonPermitteringRS(Permisjon permisjon) {
        this.prosent = permisjon.getStillingsprosent() != null ? permisjon.getStillingsprosent().doubleValue() : null;
        this.type = permisjon.getPermisjonstype().getKode();
        this.periode = new PeriodeRS(permisjon.getFomGyldighetsperiode(), permisjon.getTomGyldighetsperiode());
    }

}
