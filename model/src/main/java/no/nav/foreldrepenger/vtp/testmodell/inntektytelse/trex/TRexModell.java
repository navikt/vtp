package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.trex;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record TRexModell(@JsonInclude(Include.NON_EMPTY) List<Grunnlag> foreldrepenger,
                         @JsonInclude(Include.NON_EMPTY) List<Grunnlag> svangerskapspenger,
                         @JsonInclude(Include.NON_EMPTY) List<Grunnlag> sykepenger,
                         @JsonInclude(Include.NON_EMPTY) List<Grunnlag> barnsykdom){
}
