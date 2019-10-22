package no.nav.foreldrepenger.vtp.server.api.scenario_over_rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.inntektytelse.InntektYtelseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModeller;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.Personopplysninger;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class TestscenarioRequestDto {

    @JsonProperty("personopplysning")
    private Personopplysninger personopplysninger;

    @JsonProperty("inntektytelse-søker")
    private InntektYtelseModell inntektytelseSøker;

    @JsonProperty("inntektytelse-annenpart")
    private InntektYtelseModell inntektYtelseAnnenpart;

    @JsonProperty("organisasjon")
    private OrganisasjonModeller organisasjon;


    public Personopplysninger getPersonopplysninger() {
        return personopplysninger;
    }

    public void setPersonopplysninger(Personopplysninger personopplysninger) {
        this.personopplysninger = personopplysninger;
    }

    public InntektYtelseModell getInntektytelseSøker() {
        return inntektytelseSøker;
    }

    public void setInntektytelseSøker(InntektYtelseModell inntektytelseSøker) {
        this.inntektytelseSøker = inntektytelseSøker;
    }

    public InntektYtelseModell getInntektYtelseAnnenpart() {
        return inntektYtelseAnnenpart;
    }

    public void setInntektYtelseAnnenpart(InntektYtelseModell inntektYtelseAnnenpart) {
        this.inntektYtelseAnnenpart = inntektYtelseAnnenpart;
    }

    public OrganisasjonModeller getOrganisasjon() {
        return organisasjon;
    }

    public void setOrganisasjon(OrganisasjonModeller organisasjon) {
        this.organisasjon = organisasjon;
    }
}
