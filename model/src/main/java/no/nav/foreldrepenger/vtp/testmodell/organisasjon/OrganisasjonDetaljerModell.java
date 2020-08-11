package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisasjonDetaljerModell {

    @JsonProperty("registreringsDato")
    private LocalDate registreringsDato;
    @JsonProperty("datoSistEndret")
    private LocalDate datoSistEndret;
    @JsonProperty("forretningsadresser")
    private List<AdresseEReg> forretningsadresser;
    @JsonProperty("postadresser")
    private List<AdresseEReg> postadresser;

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

    public List<AdresseEReg> getForretningsadresser() {
        return forretningsadresser;
    }

    public List<AdresseEReg> getPostadresser() {
        return postadresser;
    }
}
