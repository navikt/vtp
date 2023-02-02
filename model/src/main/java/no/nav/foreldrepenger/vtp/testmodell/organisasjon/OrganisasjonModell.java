package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrganisasjonModell(String orgnummer,
                                 OrganisasjonstypeEReg type,
                                 Navn navn,
                                 OrganisasjonDetaljerModell organisasjonDetaljer) {

    public record Navn(@JsonProperty("navnelinje") String[] navnelinje) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Navn navn = (Navn) o;
            return Arrays.equals(navnelinje, navn.navnelinje);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(navnelinje);
        }

        @Override
        public String toString() {
            return "Navn{" +
                    "navnelinje=" + Arrays.toString(navnelinje) +
                    '}';
        }
    }
}
