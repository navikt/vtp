package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OpplysningspliktigArbeidsgiverRS {

    @JsonProperty("type")
    private Type type;
    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    @JsonProperty("aktoerId")
    private String aktoerId;
    @JsonProperty("offentligIdent")
    private String offentligIdent;

    public enum Type {
        Organisasjon,
        Person
    }

    public Type getType() {
        return type;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public String getAktoerId() {
        return aktoerId;
    }

    public String getOffentligIdent() {
        return offentligIdent;
    }

    public OpplysningspliktigArbeidsgiverRS(String organisasjonsnummer, String aktoerId) {
        this.type = organisasjonsnummer != null ? Type.Organisasjon : Type.Person;
        this.organisasjonsnummer = organisasjonsnummer;
        if (aktoerId != null) {
            if (aktoerId.length() == 13) {
                this.aktoerId = aktoerId;
            } else if (aktoerId.length() == 11) {
                this.offentligIdent = aktoerId;
            }
        }
    }
}
