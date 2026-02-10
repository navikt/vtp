package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;
import java.util.List;



public record PaaroerendeSykdom(String foedselsnummerSoeker,
                                List<Arbeidsforhold> arbeidsforhold,
                                Kodeverdi arbeidskategori,
                                Kodeverdi behandlingstema,
                                LocalDate foedselsdatoPleietrengende,
                                String foedselsnummerPleietrengende,
                                LocalDate identdato,
                                LocalDate iverksatt,
                                LocalDate opphoerFom,
                                Periode periode,
                                LocalDate registrert,
                                String saksbehandlerId,
                                Kodeverdi status,
                                Kodeverdi tema,
                                List<Vedtak> vedtak) {


}
