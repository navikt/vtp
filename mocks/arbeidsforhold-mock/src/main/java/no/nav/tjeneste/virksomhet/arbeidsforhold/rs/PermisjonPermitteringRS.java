package no.nav.tjeneste.virksomhet.arbeidsforhold.rs;

import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.arbeidsforhold.Permisjon;

public record PermisjonPermitteringRS(PeriodeRS periode, Double prosent, String type) {

    public static PermisjonPermitteringRS fra(Permisjon permisjon) {
        return new PermisjonPermitteringRS(
                new PeriodeRS(permisjon.fomGyldighetsperiode(), permisjon.tomGyldighetsperiode()),
                permisjon.stillingsprosent() != null ? permisjon.stillingsprosent().doubleValue() : null,
                permisjon.permisjonstype().getKode()
        );
    }
}
