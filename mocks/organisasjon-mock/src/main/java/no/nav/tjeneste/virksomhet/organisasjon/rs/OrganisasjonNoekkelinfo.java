package no.nav.tjeneste.virksomhet.organisasjon.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.organisasjon.AdresseEReg;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonstypeEReg;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrganisasjonNoekkelinfo {
    @JsonProperty("adresse")
    private AdresseEReg adresse;
    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    @JsonProperty("enhetstype")
    private String enhetstype;
    @JsonProperty("navn")
    private OrganisasjonResponse.Navn navn;
    @JsonProperty("opphoersdato")
    private String opphoersdato;

    public OrganisasjonNoekkelinfo(OrganisasjonModell modell) {
        this.organisasjonsnummer = modell.getOrgnummer();
        if (modell.getType() != null) {
            this.enhetstype = modell.getType().getKode();
        } else {
            this.enhetstype = OrganisasjonstypeEReg.VIRKSOMHET.getKode();
        }
        this.navn = OrganisasjonResponse.Navn.fra(modell.getNavn());

        if (modell.getOrganisasjonDetaljer() != null
                && modell.getOrganisasjonDetaljer().getForretningsadresser() != null
                && !modell.getOrganisasjonDetaljer().getForretningsadresser().isEmpty()) {
            this.adresse = modell.getOrganisasjonDetaljer().getForretningsadresser().get(0);
        }
    }

    public AdresseEReg getAdresse() {
        return adresse;
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public String getEnhetstype() {
        return enhetstype;
    }

    public OrganisasjonResponse.Navn getNavn() {
        return navn;
    }

    public String getOpphoersdato() {
        return opphoersdato;
    }
}
