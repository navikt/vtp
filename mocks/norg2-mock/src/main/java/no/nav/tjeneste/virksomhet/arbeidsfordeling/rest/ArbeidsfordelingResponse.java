package no.nav.tjeneste.virksomhet.arbeidsfordeling.rest;

import java.util.Objects;

public record ArbeidsfordelingResponse(String enhetNr, String navn, String status, String type) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArbeidsfordelingResponse that)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return Objects.equals(that.enhetNr, this.enhetNr);
    }

}
