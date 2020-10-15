package no.nav.dkif;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DigitalKontaktinfoResponsDto {

    @JsonProperty("kontaktinfo")
    private Map<String, Kontaktinformasjon> kontaktinfo = new HashMap<>();

    public Optional<String> getSpraak(String ident) {
        return Optional.ofNullable(kontaktinfo.get(ident)).map(Kontaktinformasjon::getSpraak);
    }


    public void leggTilSpråk(String fnr, String spraak) {
        Kontaktinformasjon kontaktinformasjon = new Kontaktinformasjon();
        kontaktinformasjon.setSpraak(Optional.ofNullable(spraak).orElse("NB"));
        kontaktinfo.put(fnr, kontaktinformasjon);
    }


    private static class Kontaktinformasjon {
        @JsonProperty("spraak")
        private String spraak;

        private String getSpraak() {
            return spraak;
        }

        public void setSpraak(String spraak) {
            this.spraak = spraak;
        }
    }

}

