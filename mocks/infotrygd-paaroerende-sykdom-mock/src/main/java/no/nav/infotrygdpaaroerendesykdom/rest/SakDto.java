package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;



public record SakDto(Kodeverdi behandlingstema ,
                     LocalDate iverksatt ,
                     LocalDate opphoerFom ,
                     LocalDate registrert ,
                     Kodeverdi resultat ,
                     String sakId ,
                     Kodeverdi status ,
                     Kodeverdi tema ,
                     Kodeverdi type ,
                     LocalDate vedtatt) {

}
