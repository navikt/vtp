package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;
import java.util.List;

import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrMedlemskapsvilkaaret;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("foreldrepenger")
public class Revurdering extends ForeldrepengerTestBase {

    @Test
    public void opprettRevurderingManuelt() throws Exception {

        TestscenarioDto testscenario = opprettScenario("50"); // 51

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        // TODO (MV) : Jobb videre herfra
        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.ikkeVentPåStatus = true;
        overstyrer.hentFagsak(saksnummer);
        overstyrer.ventOgGodkjennØkonomioppdrag();
        overstyrer.ikkeVentPåStatus = false;
        overstyrer.opprettBehandlingRevurdering(overstyrer.kodeverk.BehandlingÅrsakType.getKode("RE-MDL"));
        overstyrer.velgBehandling(overstyrer.kodeverk.BehandlingType.getKode("Revurdering"));
        overstyrer.aksjonspunktBekreftelse(OverstyrMedlemskapsvilkaaret.class) //stopper her
                .avvis(overstyrer.kodeverk.IkkeOppfyltÅrsak.getKode("1021"));
        overstyrer.bekreftAksjonspunktBekreftelse(OverstyrMedlemskapsvilkaaret.class);
        /*
        * kode: 6005
        * avslagskode: "1021" (=utvandret) (1020= er ikke medlem)
        * begrunnelse: "blabla"
        * erVilkarOk: false
        * */
        overstyrer.bekreftAksjonspunktBekreftelse(FatterVedtakBekreftelse.class);

    }


}
