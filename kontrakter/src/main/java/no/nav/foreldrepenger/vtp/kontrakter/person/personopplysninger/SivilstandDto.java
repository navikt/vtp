package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

import java.time.LocalDate;

public record SivilstandDto(Type sivilstand, LocalDate fom, LocalDate tom) {
    public enum Type {
        UOPPGITT, UGIFT, GIFT, ENKE_ELLER_ENKEMANN, SKILT, SEPARERT,
        REGISTRERT_PARTNER, SEPARERT_PARTNER, SKILT_PARTNER, GJENLEVENDE_PARTNER
    }
}

