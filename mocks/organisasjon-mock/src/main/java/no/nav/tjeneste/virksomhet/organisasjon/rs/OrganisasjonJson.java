package no.nav.tjeneste.virksomhet.organisasjon.rs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import no.nav.foreldrepenger.vtp.testmodell.organisasjon.OrganisasjonModell;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE, fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrganisasjonJson {

    @JsonProperty("organisasjonsnummer")
    private String organisasjonsnummer;
    @JsonProperty("type")
    private String type;
    @JsonProperty("navn")
    private Navn navn;
    @JsonProperty("organisasjonDetaljer")
    private OrganisasjonDetaljer organisasjonDetaljer;

    public OrganisasjonJson(OrganisasjonModell org) {
        this.type = "Virksomhet";
        this.organisasjonsnummer = org.getOrgnummer();
        this.navn = new Navn();
        var max = Arrays.stream(org.getNavn().getNavnelinje()).count();
        if (max > 0)
            this.navn.navnelinje1 = org.getNavn().getNavnelinje()[0];
        if (max > 1)
            this.navn.navnelinje2 = org.getNavn().getNavnelinje()[1];
        if (max > 2)
            this.navn.navnelinje3 = org.getNavn().getNavnelinje()[2];
        if (max > 3)
            this.navn.navnelinje4 = org.getNavn().getNavnelinje()[3];
        if (max > 4)
            this.navn.navnelinje5 = org.getNavn().getNavnelinje()[4];
        this.organisasjonDetaljer = new OrganisasjonDetaljer();
        if (org.getOrganisasjonDetaljer() != null && org.getOrganisasjonDetaljer().getRegistreringsDato() != null) {
            this.organisasjonDetaljer.registreringsdato = org.getOrganisasjonDetaljer().getRegistreringsDato().atStartOfDay();
        } else {
            this.organisasjonDetaljer.registreringsdato = LocalDateTime.now().minusYears(1);
        }
    }

    private OrganisasjonJson() {
        this.type = "Virksomhet";
    }

    public String getOrganisasjonsnummer() {
        return organisasjonsnummer;
    }

    public String getType() {
        return type;
    }

    public String getNavn() {
        return navn != null ? navn.getNavn() : null;
    }

    public LocalDate getRegistreringsdato() {
        return organisasjonDetaljer != null ? organisasjonDetaljer.getRegistreringsdato().toLocalDate() : null;
    }

    public LocalDate getOpphørsdato() {
        return organisasjonDetaljer != null ? organisasjonDetaljer.getOpphoersdato() : null;
    }

    @Override
    public String toString() {
        return "OrganisasjonJson{" +
                "organisasjonsnummer='" + organisasjonsnummer + '\'' +
                ", type='" + type + '\'' +
                ", navn=" + navn +
                ", organisasjonDetaljer=" + organisasjonDetaljer +
                '}';
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
            return Stream.of(navnelinje1, navnelinje2, navnelinje3, navnelinje4, navnelinje5)
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(n -> !n.isEmpty())
                    .reduce("", (a, b) -> a + " " + b).trim();
        }

        @Override
        public String toString() {
            return "Navn{" +
                "navn='" + getNavn() + '\'' +
                '}';
        }
    }

    static class OrganisasjonDetaljer {

        @JsonProperty("registreringsdato")
        private LocalDateTime registreringsdato;
        @JsonProperty("opphoersdato")
        private LocalDate opphoersdato;

        private OrganisasjonDetaljer() {
        }

        private LocalDateTime getRegistreringsdato() {
            return registreringsdato;
        }

        private LocalDate getOpphoersdato() {
            return opphoersdato;
        }

        @Override
        public String toString() {
            return "OrganisasjonDetaljer{" +
                    "registreringsdato=" + registreringsdato +
                    ", opphoersdato=" + opphoersdato +
                    '}';
        }
    }
}

