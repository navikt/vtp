package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganisasjonAdresseModell {
    @JsonProperty("orgnummer")
    private String orgnummer;
    @JsonProperty("type")
    private OrganisasjonstypeEReg type;
    @JsonProperty("navn")
    private Navn navn;
    @JsonProperty("organisasjonDetaljer")
    private OrganisasjonDetaljer organisasjonDetaljer;

    public OrganisasjonAdresseModell() {
    }

    public String getOrganisasjonsnummer() {
        return orgnummer;
    }

    public OrganisasjonstypeEReg getType() {
        return this.type;
    }

    public Navn getNavn() {
        return navn;
    }

    public List<AdresseEReg> getForretningsadresser() {
        return organisasjonDetaljer != null ? this.organisasjonDetaljer.getForretningsadresser() : Collections.emptyList();
    }

    public List<AdresseEReg> getPostadresser() {
        return organisasjonDetaljer != null ? this.organisasjonDetaljer.getPostadresser() : Collections.emptyList();
    }

    public LocalDate getRegistreringsdato() {
        return organisasjonDetaljer != null && this.organisasjonDetaljer.getRegistreringsdato() != null ? this.organisasjonDetaljer.getRegistreringsdato().toLocalDate() : null;
    }

    public LocalDate getOpphørsdato() {
        return organisasjonDetaljer != null ? this.organisasjonDetaljer.getOpphoersdato() : null;
    }

    public OrganisasjonDetaljer getOrganisasjonDetaljer(){
        return organisasjonDetaljer;
    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private static class OrganisasjonDetaljer {
        @JsonProperty("registreringsdato")
        private LocalDateTime registreringsdato;
        @JsonProperty("opphoersdato")
        private LocalDate opphoersdato;
        @JsonProperty("forretningsadresser")
        private List<AdresseEReg> forretningsadresser;
        @JsonProperty("postadresser")
        private List<AdresseEReg> postadresser;

        private OrganisasjonDetaljer() {
        }

        private LocalDateTime getRegistreringsdato() {
            return this.registreringsdato;
        }

        private LocalDate getOpphoersdato() {
            return this.opphoersdato;
        }

        private List<AdresseEReg> getForretningsadresser() {
            return forretningsadresser != null ? this.forretningsadresser : Collections.emptyList();
        }

        private List<AdresseEReg> getPostadresser() {
            return postadresser != null ? this.postadresser : Collections.emptyList();
        }
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
