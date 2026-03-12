package no.nav.vtp.personopplysninger;

import java.time.LocalDate;

public record Sivilstand(Type sivilstand, LocalDate fom, LocalDate tom){

    public enum Type {
        UOPPGITT,
        UGIFT,
        GIFT,
        ENKE_ELLER_ENKEMANN,
        SKILT,
        SEPARERT,
        REGISTRERT_PARTNER,
        SEPARERT_PARTNER,
        SKILT_PARTNER,
        GJENLEVENDE_PARTNER,
    }

}
