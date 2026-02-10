package no.nav.infotrygdpaaroerendesykdom.rest;

import java.util.List;



public record SakResult(List<SakDto> saker, List<SakDto> vedtak) {
}
