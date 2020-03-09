package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ArbeidsfordelingResponse {
    private String enhetNr;
    private String enhetNavn;
    private String enhetType;
    private String status;

    @JsonCreator
    public ArbeidsfordelingResponse(@JsonProperty("enhetNr") String enhetNr,
                                    @JsonProperty("navn") String enhetNavn,
                                    @JsonProperty("status") String status,
                                    @JsonProperty("type") String enhetType) {
        this.status = status;
        this.enhetNavn = enhetNavn;
        this.enhetNr = enhetNr;
        this.enhetType = enhetType;
    }

    public ArbeidsfordelingResponse() {
        // Jackson
    }

    public String getEnhetNr() {
        return enhetNr;
    }

    public String getEnhetNavn() {
        return enhetNavn;
    }

    public String getEnhetType() {
        return enhetType;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enhetNr, enhetNavn, enhetType, status);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ArbeidsfordelingResponse)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        ArbeidsfordelingResponse that = (ArbeidsfordelingResponse) obj;
        return Objects.equals(that.enhetNr, this.enhetNr);
    }

    @Override
    public String toString() {
        return "ArbeidsfordelingResponse{" +
            "enhetNr='" + enhetNr + '\'' +
            ", enhetNavn='" + enhetNavn + '\'' +
            ", enhetType='" + enhetType + '\'' +
            ", status='" + status + '\'' +
            '}';
    }
}
