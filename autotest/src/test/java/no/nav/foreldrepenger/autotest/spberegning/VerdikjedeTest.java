package no.nav.foreldrepenger.autotest.spberegning;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.spberegning.SpBeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.BeregningKlient;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForeslaaDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.ForslagDto;
import no.nav.foreldrepenger.autotest.klienter.spberegning.beregning.dto.beregning.BeregningDto;
import no.nav.foreldrepenger.autotest.util.http.HttpsSession;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("spberegning")
public class VerdikjedeTest extends SpberegningTestBase {
    BeregningKlient klient;

    @Test
    public void grunnleggendeSykepenger() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");
        
        saksbehandler.erLoggetInnMedRolle("Saksbehandler");
        saksbehandler.foreslåBeregning(testscenario, "123123");

        verifiser(saksbehandler.beregning.getTema().kode.equals("SYK"));
    }

}
