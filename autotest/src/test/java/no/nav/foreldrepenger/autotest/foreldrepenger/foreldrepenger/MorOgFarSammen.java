package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FastsettUttaksperioderManueltBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerBosattBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Tag("smoke")
@Tag("foreldrepenger")
public class MorOgFarSammen extends ForeldrepengerTestBase{

    @Test
    public void farOgMorSøkerFødselMedEttArbeidsforholdOverlappendePeriode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("80");
        
        long saksnummerMor = behandleSøknadForMor(testscenario);
        long saksnummerFar = behandleSøknadForFar(testscenario);
        
        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(saksbehandler.harBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering")), "Mor har ikke fått revurdering.");

        /*
        * TODO: Fortsett når økonomi er klar til å koble førstegangsbeh og revurdering. Nå: "Fant ikke forrige oppdrag"
        * saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
        * verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(),"FORELDREPENGER_ENDRET", "Behandlingsresultat");
        * // saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakManueltBekreftelse.class);
        *
        * saksbehandler.hentFagsak(saksnummerFar);
        * saksbehandler.harIkkeBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
        * */
        
    }
    
    @Test
    public void farOgMorSøkerFødselMedEttArbeidsforholdUtenOverlappendePeriode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("81");
        
        long saksnummerMor = behandleSøknadForMor(testscenario);
        long saksnummerFar = behandleSøknadForFarUtenOverlapp(testscenario);
        
        saksbehandler.hentFagsak(saksnummerMor);
        verifiser(saksbehandler.harBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering")), "Saken har fått revurdering");
        saksbehandler.velgBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
        verifiserLikhet(saksbehandler.valgtBehandling.behandlingsresultat.toString(), "INGEN_ENDRING", "Behandlingsresultat.");
        verifiserLikhet(saksbehandler.valgtBehandling.status.navn, "Avsluttet", "Behandlingsstatus");

        saksbehandler.hentFagsak(saksnummerFar);
        verifiser(saksbehandler.harIkkeBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering")), "Far har fått revurdering han ikke skal ha");

    }
    
    public long behandleSøknadForMor(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        
        Fordeling fordeling = FordelingErketyper.fordelingMorHappyCase(fødselsdato);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(
                søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                startDatoForeldrepenger.plusWeeks(2),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(inntekter.get(0), søkerIdent,
                startDatoForeldrepenger, orgnr, Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);
                
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ikkeVentPåStatus = false;
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        
        return saksnummer;
    }
    
    public long behandleSøknadForFar(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(3);
       
        
        System.out.println("søkerAktørid: " + søkerAktørid);
        System.out.println("annenPartAktørid: " + annenPartAktørid);
        System.out.println("Fødselsdato: " + fødselsdato);
        System.out.println("startDatoForeldrepenger: " + startDatoForeldrepenger);

        
        Fordeling fordeling = fordeling(uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                fødselsdato.plusWeeks(3),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                søkerIdent,
                startDatoForeldrepenger,
                orgnr,
                Optional.empty(),
                Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, søkerIdent, saksnummer);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerBosattBekreftelse.class)
            .bekreftBrukerErBosatt();
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerBosattBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaUttakBekreftelse.class)
            .godkjennPeriode(startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2),
                saksbehandler.kodeverk.UttakPeriodeVurderingType.getKode("PERIODE_OK"));
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaUttakBekreftelse.class);
        
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        verifiserLikhet(perioder.size(), 1);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        
        beslutter.hentFagsak(saksnummer);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_FAKTA_UTTAK))
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_OM_ER_BOSATT));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        return saksnummer;
    }
    
    public long behandleSøknadForFarUtenOverlapp(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(10).plusDays(1);

        Fordeling fordeling = fordeling(uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, startDatoForeldrepenger, startDatoForeldrepenger.plusWeeks(2)));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(søkerAktørid,
                annenPartAktørid,
                fødselsdato,
                fødselsdato.plusWeeks(3),
                fordeling);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                søkerIdent,
                startDatoForeldrepenger,
                orgnr,
                Optional.empty(),
                Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, søkerIdent, saksnummer);
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        
        verifiser(saksbehandler.sakErKobletTilAnnenpart(), "Saken er ikke koblet til en annen behandling");
        
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerBosattBekreftelse.class)
            .bekreftBrukerErBosatt();
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerBosattBekreftelse.class);
        
        saksbehandler.hentAksjonspunktbekreftelse(FastsettUttaksperioderManueltBekreftelse.class)
            .godkjennAllePerioder();
        saksbehandler.bekreftAksjonspunktBekreftelse(FastsettUttaksperioderManueltBekreftelse.class);
        
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        verifiserLikhet(perioder.size(), 1);

        saksbehandler.bekreftAksjonspunktBekreftelse(ForesloVedtakBekreftelse.class);
        
        beslutter.erLoggetInnMedRolle(Rolle.BESLUTTER);
        
        beslutter.hentFagsak(saksnummer);
        beslutter.hentAksjonspunktbekreftelse(FatterVedtakBekreftelse.class)
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.FASTSETT_UTTAKPERIODER))
            .godkjennAksjonspunkt(saksbehandler.hentAksjonspunkt(AksjonspunktKoder.AVKLAR_OM_ER_BOSATT));
        beslutter.fattVedtakOgGodkjennØkonomioppdrag();

        System.out.println("Done");
        return saksnummer;
    }
    
}
