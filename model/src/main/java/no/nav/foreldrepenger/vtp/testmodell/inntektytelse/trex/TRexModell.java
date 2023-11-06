package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public record TRexModell(List<Grunnlag> sykepenger, List<Grunnlag> barnsykdom){

    public TRexModell() {
        this(null, null);
    }

    @JsonCreator
    public TRexModell(List<Grunnlag> sykepenger, List<Grunnlag> barnsykdom) {
        this.sykepenger = Optional.ofNullable(sykepenger).orElse(List.of());
        this.barnsykdom = Optional.ofNullable(barnsykdom).orElse(List.of());
    }
}
