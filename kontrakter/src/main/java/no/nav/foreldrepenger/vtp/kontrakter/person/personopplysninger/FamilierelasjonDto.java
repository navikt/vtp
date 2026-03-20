package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

public record FamilierelasjonDto(Relasjon relasjon, String relatertTilId) {
    public enum Relasjon {
        EKTE, SAMBOER, BARN, FAR, MOR, MEDMOR
    }
}

