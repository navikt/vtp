package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AnsettelsesperiodeRS {

    @JsonProperty("periode")
    private PeriodeRS periode;

    public PeriodeRS getPeriode() {
        return periode;
    }

    public AnsettelsesperiodeRS(PeriodeRS periode) {
        this.periode = periode;
    }

}
