package no.nav.foreldrepenger.vtp.kontrakter.person.arbeidsforhold;

public record PrivatArbeidsgiverDto(String fnr) implements ArbeidsgiverDto {

    @Override
    public String identifikator() {
        return fnr;
    }
}

