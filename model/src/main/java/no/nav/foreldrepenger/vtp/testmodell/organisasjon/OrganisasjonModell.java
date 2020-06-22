package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisasjonModell {
    @JsonProperty("orgnummer")
    private String orgnummer;
    @JsonProperty("type")
    private OrganisasjonstypeEReg type;
    @JsonProperty("navn")
    private Navn navn;
    @JsonProperty("organisasjonDetaljer")
    private OrganisasjonDetaljerModell organisasjonDetaljer;

    public OrganisasjonModell() {
    }

    public String getOrgnummer() {
        return orgnummer;
    }

    public void setOrgnummer(String orgnummer) {
        this.orgnummer = orgnummer;
    }

    public Navn getNavn() {
        return navn;
    }

    public void setNavn(Navn navn) {
        this.navn = navn;
    }

    public OrganisasjonDetaljerModell getOrganisasjonDetaljer() {
        return organisasjonDetaljer;
    }

    public void setOrganisasjonDetaljer(OrganisasjonDetaljerModell organisasjonDetaljer) {
        this.organisasjonDetaljer = organisasjonDetaljer;
    }

    public OrganisasjonstypeEReg getType() {
        return type;
    }

    public class Navn {

        public Navn() {
        }

        @JsonProperty("navnelinje")
        private String[] navnelinje;

        public String[] getNavnelinje() {
            return navnelinje;
        }

        public void setNavnelinje(String[] navnelinje) {
            this.navnelinje = navnelinje;
        }
    }
}
