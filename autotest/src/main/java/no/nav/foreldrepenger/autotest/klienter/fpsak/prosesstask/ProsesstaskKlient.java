package no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.FpsakKlient;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.ProsessTaskListItemDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.ProsesstaskDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.ProsesstaskResultatDto;
import no.nav.foreldrepenger.autotest.klienter.fpsak.prosesstask.dto.SokeFilterDto;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.autotest.util.http.rest.StatusRange;

public class ProsesstaskKlient  extends FpsakKlient{

    private static String PROSESSTASK_URL = "/prosesstask";
    private static String PROSESSTASK_LIST_URL = PROSESSTASK_URL + "/list";
    private static String PROSESSTASK_LAUNCH_URL = PROSESSTASK_URL + "/launch"; 
    
    public ProsesstaskKlient(HttpSession session) {
        super(session);
    }
    
    public List<ProsessTaskListItemDto> list(SokeFilterDto sokeFilter) throws IOException {
        String url = hentRestRotUrl() + PROSESSTASK_LIST_URL;
        return postOgHentJson(url, sokeFilter, hentObjectMapper().getTypeFactory().constructCollectionType(ArrayList.class, ProsessTaskListItemDto.class), StatusRange.STATUS_SUCCESS);
    }
    
    public ProsesstaskResultatDto launch(ProsesstaskDto prosessTask) throws IOException {
        String url = hentRestRotUrl() + PROSESSTASK_LAUNCH_URL;
        return postOgHentJson(url, prosessTask, ProsesstaskResultatDto.class, StatusRange.STATUS_SUCCESS);
    }
}
