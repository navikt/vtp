package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.KontrollerManueltOpprettetRevurdering;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.overstyr.OverstyrMedlemskapsvilkaaret;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;



@Tag("smoke")
@Tag("foreldrepenger")
public class Revurdering extends ForeldrepengerTestBase {

    @Test
    public void opprettRevurderingManuelt() throws Exception {

        TestscenarioDto testscenario = opprettScenario("50"); // 51?

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        saksbehandler.ikkeVentPåStatus = false;
        saksbehandler.opprettBehandlingRevurdering(saksbehandler.kodeverk.BehandlingÅrsakType.getKode("RE-MDL"));


        overstyrer.erLoggetInnMedRolle(Rolle.OVERSTYRER);
        overstyrer.hentFagsak(saksnummer);
        overstyrer.velgBehandling(overstyrer.kodeverk.BehandlingType.getKode("Revurdering"));
        OverstyrMedlemskapsvilkaaret overstyrMedlemskapsvilkaaret = new OverstyrMedlemskapsvilkaaret(overstyrer.valgtFagsak,overstyrer.valgtBehandling);
        overstyrMedlemskapsvilkaaret.avvis(overstyrer.kodeverk.Avslagsårsak.get("FP_VK_2").getKode("Søker er ikke medlem"));
        overstyrMedlemskapsvilkaaret.setBegrunnelse("avvist");
        overstyrer.overstyr(overstyrMedlemskapsvilkaaret);
        overstyrer.bekreftAksjonspunktBekreftelse(KontrollerManueltOpprettetRevurdering.class);
        overstyrer.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        verifiserLikhet(overstyrer.valgtBehandling.behandlingsresultat.toString(), "OPPHØR", "Behandlingsresultat");

        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        beslutter.hentFagsak(saksnummer);
        beslutter.velgBehandling(beslutter.kodeverk.BehandlingType.getKode("Revurdering"));

        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
                .godkjennAksjonspunkt(beslutter.hentAksjonspunkt(AksjonspunktKoder.OVERSTYRING_AV_MEDLEMSKAPSVILKÅRET));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();
        verifiserLikhet(beslutter.valgtBehandling.behandlingsresultat.toString(), "OPPHØR", "Behandlingsresultat");
        verifiserLikhet(beslutter.valgtBehandling.status.kode, "AVSLU", "Behandlingsstatus");

    }

    @Test
    public void endringssøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50"); // 51?

        // Førstegangssøknad
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        Long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        saksbehandler.ikkeVentPåStatus = false;
        verifiserLikhet(saksbehandler.behandlinger.size(), 1, "Antall behandlinger");
        verifiserLikhet(saksbehandler.valgtBehandling.type.kode, "BT-002", "Behandlingstype");

        // Endringssøknad
        ForeldrepengesoknadBuilder søknadE = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorEndring(søkerAktørIdent, fpStartdato, saksnummer.toString());
        fordel. erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        Long saksnummerE = fordel.sendInnEndringssøknad(søknadE.buildEndring(), testscenario, DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerE);
        verifiserLikhet(saksbehandler.behandlinger.get(1).type.kode, "BT-004", "Behandlingstype");
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));

        // TODO (MV): avslutt behandlingen når økonomisteg er fikset

    }

    @Test
    public void revurderingViaInntektsmelding() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, fpStartdato);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        saksbehandler.ikkeVentPåStatus = false;

        // Inntektsmelding
        TestscenarioDto testscenarioEndret = opprettScenario("47");
        List<InntektsmeldingBuilder> inntektsmeldingerEndret = makeInntektsmeldingFromTestscenario(testscenarioEndret, fpStartdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerEndret, testscenarioEndret, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiserLikhet(saksbehandler.behandlinger.get(1).type.kode, "BT-004", "Behandlingstype");
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakManueltBekreftelse.class);
    }


}
