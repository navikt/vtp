package no.nav.foreldrepenger.vtp.kontrakter.person.v2;

import java.util.UUID;

/** Referanse til en annen person i samme scenario som privat arbeidsgiver. */
public record PrivatArbeidsgiverDto(UUID uuid) implements ArbeidsgiverDto {
}
