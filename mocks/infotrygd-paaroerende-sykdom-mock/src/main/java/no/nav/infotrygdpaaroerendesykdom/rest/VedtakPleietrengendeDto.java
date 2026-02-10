package no.nav.infotrygdpaaroerendesykdom.rest;

import java.util.List;


public record VedtakPleietrengendeDto(String soekerFnr, List<SakDto> vedtak) {

}
