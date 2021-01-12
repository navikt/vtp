package no.nav.tjeneste.virksomhet.organisasjon.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.organisasjon.AdresseEReg;
import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;
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
public class OrganisasjonResponse {
    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    @JsonProperty("type")
    private OrganisasjonstypeEReg type;
    @JsonProperty("navn")
    private Navn navn;
    @JsonProperty("organisasjonDetaljer")
    private OrganisasjonDetaljer organisasjonDetaljer;

    public OrganisasjonResponse(OrganisasjonModell modell) {
        this.organisasjonsnummer = modell.getOrgnummer();
        if (modell.getType() != null) {
            this.type = modell.getType();
        } else {
            this.type = OrganisasjonstypeEReg.VIRKSOMHET;
        }
        this.navn = Navn.fra(modell.getNavn());
        this.organisasjonDetaljer = new OrganisasjonDetaljer();
        if (modell.getOrganisasjonDetaljer() != null && modell.getOrganisasjonDetaljer().getRegistreringsDato() != null) {
            this.organisasjonDetaljer.registreringsdato = modell.getOrganisasjonDetaljer().getRegistreringsDato().atStartOfDay();
        } else {
            this.organisasjonDetaljer.registreringsdato = LocalDateTime.now().minusYears(1);
        }
        if (modell.getOrganisasjonDetaljer() != null && modell.getOrganisasjonDetaljer().getForretningsadresser() != null) {
            this.organisasjonDetaljer.forretningsadresser = modell.getOrganisasjonDetaljer().getForretningsadresser();
        }
        if (modell.getOrganisasjonDetaljer() != null && modell.getOrganisasjonDetaljer().getPostadresser() != null) {
            this.organisasjonDetaljer.postadresser = modell.getOrganisasjonDetaljer().getPostadresser();
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
        return !this.getPostadresser().isEmpty() ? this.getPostadresser().get(0) : this.getForretningsadresser().get(0);
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
    static class OrganisasjonDetaljer {
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

        String getNavn() {
            return Stream.of(this.navnelinje1, this.navnelinje2, this.navnelinje3, this.navnelinje4, this.navnelinje5)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter((n) -> !n.isEmpty())
                    .reduce("", (a, b) -> a + " " + b)
                    .trim();
        }

        static Navn fra(OrganisasjonModell.Navn navn) {
            var svar = new Navn();
            var max = Arrays.stream(navn.getNavnelinje()).count();
            if (max > 0)
                svar.navnelinje1 = navn.getNavnelinje()[0];
            if (max > 1)
                svar.navnelinje2 = navn.getNavnelinje()[1];
            if (max > 2)
                svar.navnelinje3 = navn.getNavnelinje()[2];
            if (max > 3)
                svar.navnelinje4 = navn.getNavnelinje()[3];
            if (max > 4)
                svar.navnelinje5 = navn.getNavnelinje()[4];
            return svar;
        }
    }
}
