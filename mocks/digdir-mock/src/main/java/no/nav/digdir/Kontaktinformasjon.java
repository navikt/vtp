package no.nav.digdir;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class Kontaktinformasjon {
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
        private String spraak;

        public String getSpraak() {
                return spraak;
        }

        public void setSpraak(String spraak) {
                this.spraak = spraak;
        }

}
