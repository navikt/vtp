package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.personopplysning.UstrukturertAdresseModell;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganisasjonDetaljerModell {

    @JsonProperty("registreringsDato")
    private LocalDate registreringsDato;

    @JsonProperty("datoSistEndret")
    private LocalDate datoSistEndret;

    @JsonProperty("postadresse")
    @JsonIgnoreProperties
    private UstrukturertAdresseModell postadresse;

    public OrganisasjonDetaljerModell() {
    }

    public LocalDate getRegistreringsDato() {
        return registreringsDato;
    }

    public void setRegistreringsDato(LocalDate registreringsDato) {
        this.registreringsDato = registreringsDato;
    }

    public LocalDate getDatoSistEndret() {
        return datoSistEndret;
    }

    public void setDatoSistEndret(LocalDate datoSistEndret) {
        this.datoSistEndret = datoSistEndret;
    }

    public UstrukturertAdresseModell getPostadresse() { return postadresse; }

    public void setPostadresse(UstrukturertAdresseModell postadresse) { this.postadresse = postadresse; }
}
