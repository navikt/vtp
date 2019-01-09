package no.nav.foreldrepenger.autotest.klienter.vtp.expect;

import java.io.IOException;

import no.nav.foreldrepenger.autotest.klienter.vtp.VTPKlient;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectRequestDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectResultDto;
import no.nav.foreldrepenger.autotest.klienter.vtp.expect.dto.ExpectTokenDto;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class ExpectKlient extends VTPKlient{
    
    private static final String EXPECT_URL = "/expect";
    private static final String CREATE_EXPECT_URL = EXPECT_URL + "/createExpectation";
    private static final String CHECK_EXPECT_URL = EXPECT_URL + "/checkExpectation";

    public ExpectKlient(HttpSession session) {
        super(session);
    }
    
    public ExpectTokenDto createExpectation(ExpectRequestDto request) throws IOException {
        String url = hentRestRotUrl() + CREATE_EXPECT_URL;
        return postOgHentJson(url, request, ExpectTokenDto.class, StatusRange.STATUS_SUCCESS);
    }
    
    public ExpectResultDto checkExpectation(ExpectTokenDto request) throws IOException {
        String url = hentRestRotUrl() + CHECK_EXPECT_URL;
        return postOgHentJson(url, request, ExpectResultDto.class, StatusRange.STATUS_SUCCESS);
    }
}
