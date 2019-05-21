package no.nav.foreldrepenger.autotest.klienter.vtp.tpsFeed;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.qameta.allure.Step;
import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;
import no.nav.foreldrepenger.fpmock2.kontrakter.PersonhendelseDto;

public class TpsFeedKlient extends VTPKlient {

    private static final String TPS_FEED_URL = "/feed";
    private static final Logger LOG = LoggerFactory.getLogger(TpsFeedKlient.class);

    public TpsFeedKlient(HttpSession session){super(session);}

    @Step("Legger til hendelse i tps-feed")
    public void leggTilHendelse(PersonhendelseDto personhendelseDto) throws IOException {
        String url = hentRestRotUrl() + String.format(TPS_FEED_URL);
        LOG.info("TPS hendelse lagt til av type: " + personhendelseDto.getType());
        postOgHentJson(url, personhendelseDto, Object.class, StatusRange.STATUS_SUCCESS);
    }
}
