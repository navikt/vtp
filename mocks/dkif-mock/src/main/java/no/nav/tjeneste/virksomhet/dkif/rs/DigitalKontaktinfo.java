package no.nav.tjeneste.virksomhet.dkif.rs;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DigitalKontaktinfo {

    @JsonProperty("kontaktinfo")
    private Map<String, Kontaktinformasjon> kontaktinfo;

    public DigitalKontaktinfo(String personident) {
        this.kontaktinfo = new HashMap<>();
        this.kontaktinfo.putIfAbsent(personident, new Kontaktinformasjon(personident));
    }

    private static class Kontaktinformasjon {

        @JsonProperty("epostadresse")
        private String epostadresse = "noreply@nav.no";
        @JsonProperty("kanVarsles")
        private boolean kanVarsles = true;
        @JsonProperty("mobiltelefonnummer")
        private String mobiltelefonnummer = "99999999";
        @JsonProperty("personident")
        private String personident;
        @JsonProperty("reservert")
        private boolean reservert = false;
        @JsonProperty("spraak")
        private String spraak = "nb";

        private Kontaktinformasjon(String personident) {
            this.personident = personident;
        }

        private String getSpraak() {
            return spraak;
        }
    }

}

