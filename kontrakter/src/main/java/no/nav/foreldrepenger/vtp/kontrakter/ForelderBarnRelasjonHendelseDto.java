package no.nav.foreldrepenger.vtp.kontrakter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForelderBarnRelasjonHendelseDto(String endringstype,
                                              String fnr,
                                              String relatertPersonsFnr,
                                              String relatertPersonsRolle,
                                              String minRolleForPerson) implements PersonhendelseDto {

}
