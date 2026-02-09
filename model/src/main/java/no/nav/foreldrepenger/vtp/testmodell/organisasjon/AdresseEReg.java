package no.nav.foreldrepenger.vtp.testmodell.organisasjon;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.ANY
)
public record AdresseEReg(String adresselinje1,
                          String adresselinje2,
                          String adresselinje3,
                          String kommunenummer,
                          String landkode,
                          String postnummer,
                          String poststed) {
}
