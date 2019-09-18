package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganisasjonModell {
    @JsonProperty("orgnummer")
    private String orgnummer;

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
