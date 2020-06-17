package no.nav.tjeneste.virksomhet.organisasjon.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.AdresseEReg;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonAdresseModell;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonstypeEReg;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrganisasjonAdresse {
    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    @JsonProperty("type")
    private OrganisasjonstypeEReg type;
    @JsonProperty("navn")
    private Navn navn;
    @JsonProperty("organisasjonDetaljer")
    public OrganisasjonDetaljer organisasjonDetaljer;

    public OrganisasjonAdresse(OrganisasjonAdresseModell adresseModell) {
        this.organisasjonsnummer = adresseModell.getOrganisasjonsnummer();
        if (adresseModell.getType() != null){
            this.type = adresseModell.getType();
        }
        this.navn = new Navn();
        var max = Arrays.stream(adresseModell.getNavn().getNavnelinje()).count();
        if (max > 0)
            this.navn.navnelinje1 = adresseModell.getNavn().getNavnelinje()[0];
        if (max > 1)
            this.navn.navnelinje2 = adresseModell.getNavn().getNavnelinje()[1];
        if (max > 2)
            this.navn.navnelinje3 = adresseModell.getNavn().getNavnelinje()[2];
        if (max > 3)
            this.navn.navnelinje4 = adresseModell.getNavn().getNavnelinje()[3];
        if (max > 4)
            this.navn.navnelinje5 = adresseModell.getNavn().getNavnelinje()[4];
        this.organisasjonDetaljer = new OrganisasjonDetaljer();
        if (adresseModell.getOrganisasjonDetaljer() != null && adresseModell.getRegistreringsdato() != null) {
            this.organisasjonDetaljer.registreringsdato = adresseModell.getRegistreringsdato().atStartOfDay();
        } else {
            this.organisasjonDetaljer.registreringsdato = LocalDateTime.now().minusYears(1);
        }
        if (adresseModell.getOrganisasjonDetaljer() != null && adresseModell.getForretningsadresser() != null){
            this.organisasjonDetaljer.forretningsadresser = adresseModell.getForretningsadresser();
        }
        if (adresseModell.getOrganisasjonDetaljer() != null && adresseModell.getPostadresser() != null){
            this.organisasjonDetaljer.postadresser = adresseModell.getPostadresser();
        }
    }

    public String getOrganisasjonsnummer() {
        return this.organisasjonsnummer;
    }

    public OrganisasjonstypeEReg getType() {
        return this.type;
    }

    public String getNavn() {
        return this.navn != null ? this.navn.getNavn() : null;
    }

    public AdresseEReg getKorrespondanseadresse() {
        return !this.getPostadresser().isEmpty() ? (AdresseEReg)this.getPostadresser().get(0) : (AdresseEReg)this.getForretningsadresser().get(0);
    }

    public List<AdresseEReg> getForretningsadresser() {
        return this.organisasjonDetaljer != null ? this.organisasjonDetaljer.getForretningsadresser() : Collections.emptyList();
    }

    public List<AdresseEReg> getPostadresser() {
        return this.organisasjonDetaljer != null ? this.organisasjonDetaljer.getPostadresser() : Collections.emptyList();
    }

    public LocalDate getRegistreringsdato() {
        return this.organisasjonDetaljer != null && this.organisasjonDetaljer.getRegistreringsdato() != null ? this.organisasjonDetaljer.getRegistreringsdato().toLocalDate() : null;
    }

    public LocalDate getOpphørsdato() {
        return this.organisasjonDetaljer != null ? this.organisasjonDetaljer.getOpphoersdato() : null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
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
            return this.forretningsadresser != null ? this.forretningsadresser : Collections.emptyList();
        }

        private List<AdresseEReg> getPostadresser() {
            return this.postadresser != null ? this.postadresser : Collections.emptyList();
        }
    }

    static class Navn {
        @JsonProperty("navnelinje1")
        private String navnelinje1;
        @JsonProperty("navnelinje2")
        private String navnelinje2;
        @JsonProperty("navnelinje3")
        private String navnelinje3;
        @JsonProperty("navnelinje4")
        private String navnelinje4;
        @JsonProperty("navnelinje5")
        private String navnelinje5;

        private Navn() {
        }

        private String getNavn() {
            return ((String) Stream.of(this.navnelinje1, this.navnelinje2, this.navnelinje3, this.navnelinje4, this.navnelinje5).filter(Objects::nonNull).map(String::trim).filter((n) -> {
                return !n.isEmpty();
            }).reduce("", (a, b) -> {
                return a + " " + b;
            })).trim();
        }
    }
}
