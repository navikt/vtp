package no.nav.foreldrepenger.vtp.kontrakter.person.personopplysninger;

import java.time.LocalDate;
import java.util.List;

public record PersonopplysningerDto(String fnr,
                                    Rolle rolle,
                                    NavnDto navn,
                                    LocalDate fødselsdato,
                                    LocalDate dødsdato,
                                    Språk språk,
                                    Kjønn kjønn,
                                    GeografiskTilknytningDto geografiskTilknytning,
                                    List<FamilierelasjonDto> familierelasjoner,
                                    List<StatsborgerskapDto> statsborgerskap,
                                    List<SivilstandDto> sivilstand,
                                    List<PersonstatusDto> personstatus,
                                    List<MedlemskapDto> medlemskap,
                                    AdresserDto adresser,
                                    boolean erSkjermet) {
}
