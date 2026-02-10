package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;


public record RammevedtakDto(LocalDate date, String tekst, LocalDate fom, LocalDate tom) {
}
