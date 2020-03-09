package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT, content = JsonInclude.Include.NON_EMPTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, creatorVisibility = JsonAutoDetect.Visibility.NONE)
public class ArbeidsfordelingRequest {
    @JsonProperty(value = "behandlingstema")
    private String behandlingstema;
    @JsonProperty(value = "behandlingstype")
    private String behandlingstype;
    @JsonProperty(value = "diskresjonskode")
    private String diskresjonskode;
    @JsonProperty(value = "geografiskOmraade")
    private String geografiskOmraade;
    @JsonProperty(value = "oppgavetype")
    private String oppgavetype;
    @JsonProperty(value = "tema")
    private String tema;
    @JsonProperty(value = "temagruppe")
    private String temagruppe;

    public ArbeidsfordelingRequest() {
        // Jackson
    }

    public String getBehandlingstema() {
        return behandlingstema;
    }

    public String getBehandlingstype() {
        return behandlingstype;
    }

    public String getDiskresjonskode() {
        return diskresjonskode;
    }

    public String getGeografiskOmraade() {
        return geografiskOmraade;
    }

    public String getOppgavetype() {
        return oppgavetype;
    }

    public String getTema() {
        return tema;
    }

    public String getTemagruppe() {
        return temagruppe;
    }

    public static Builder ny() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ArbeidsfordelingRequest{" +
            "behandlingstema='" + behandlingstema + '\'' +
            ", behandlingstype='" + behandlingstype + '\'' +
            ", diskresjonskode='" + diskresjonskode + '\'' +
            ", geografiskOmraade='" + geografiskOmraade + '\'' +
            ", oppgavetype='" + oppgavetype + '\'' +
            ", tema='" + tema + '\'' +
            ", temagruppe='" + temagruppe + '\'' +
            '}';
    }

    public static class Builder {
        private ArbeidsfordelingRequest arbeidsfordelingRequest;

        public Builder() {
            this.arbeidsfordelingRequest = new ArbeidsfordelingRequest();
        }

        public Builder medBehandlingstema(String behandlingstema) {
            arbeidsfordelingRequest.behandlingstema = behandlingstema;
            return this;
        }
        public Builder medBehandlingstype(String behandlingstype) {
            arbeidsfordelingRequest.behandlingstype = behandlingstype;
            return this;
        }

        public Builder medDiskresjonskode(String diskresjonskode) {
            arbeidsfordelingRequest.diskresjonskode = diskresjonskode;
            return this;
        }

        public Builder medGeografiskOmraade(String geografiskOmraade) {
            arbeidsfordelingRequest.geografiskOmraade = geografiskOmraade;
            return this;
        }

        public Builder medOppgavetype(String oppgavetype) {
            arbeidsfordelingRequest.oppgavetype = oppgavetype;
            return this;
        }

        public Builder medTema(String tema) {
            arbeidsfordelingRequest.tema = tema;
            return this;
        }

        public Builder medTemagruppe(String temagruppe) {
            arbeidsfordelingRequest.temagruppe = temagruppe;
            return this;
        }

        public ArbeidsfordelingRequest build() {
            return arbeidsfordelingRequest;
        }
    }
}
