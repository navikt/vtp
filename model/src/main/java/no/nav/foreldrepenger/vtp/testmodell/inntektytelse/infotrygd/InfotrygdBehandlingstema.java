package no.nav.foreldrepenger.vtp.testmodell.inntektytelse.infotrygd;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Infotrygd stønadsklasse 2.
 *
 * @see https://confluence.adeo.no/pages/viewpage.action?pageId=220537850
 */
public enum InfotrygdBehandlingstema {
    @JsonEnumDefaultValue
    SP("Sykepenger", "SP"),
    RS("Sykepenger utenlandsopphold", "SP"),
    RT("Sykepenger reisetilskudd", "SP"),
    SU("Sykepenger forsikringsrisiko", "SP"),

    BT("Stønad til barnetilsyn", "EF"),
    FL("Tilskudd til flytting", "EF"),
    OG("Overgangsstønad", "EF"),
    UT("Skolepenger", "EF"),

    FØ("Foreldrepenger fødsel", "FA"),
    AP("Foreldrepenger Adopsjon", "FA"),
    SV("Svangerskapspenger", "FA"),
    AE("Engangsstønad Adopsjon", "FA"),
    FE("Engansstønad Fødsel", "FA"),
    FU("Foreldrepenger fødsel utland", "FA"),
    Z("Svangerskapspenger", "FA"),

    OM("Omsorgspenger", "BS"),
    OP("Opplæringspenger", "BS"),
    PB("Pleiepenger sykt barn (identdato før 1.10.2017)", "BS"),
    PI("Pleiepenger (identdato før 1.10.2017)", "BS"),
    PP("Pleiepenger pårørende", "BS"),
    PN("Pleiepenger ny ordning (identdato etter 1.10.2017)", "BS");

    private final String beskrivelse;
    private final String tema;

    InfotrygdBehandlingstema(String beskrivelse, String tema) {
        this.beskrivelse = beskrivelse;
        this.tema = tema;
    }

    public String getTema() {
        return tema;
    }
}
