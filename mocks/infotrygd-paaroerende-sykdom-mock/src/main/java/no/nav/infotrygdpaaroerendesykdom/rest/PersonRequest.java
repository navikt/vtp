package no.nav.infotrygdpaaroerendesykdom.rest;

import java.time.LocalDate;
import java.util.List;

public record PersonRequest(LocalDate fom, LocalDate tom, List<String> fnr) {
}
