package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.foreldrepenger.FpsakTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.FatterVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.ForesloVedtakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerBosattBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaUttakBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.AksjonspunktKoder;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.Behandling;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

public class MorOgFarSammen extends ForeldrepengerTestBase{

    @Test
    public void farOgMorSøkerFødselMedEttArbeidsforholdOverlappendePeriode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("80");
        
        long saksnummer = behandleSøknadForMor(testscenario);
        behandleSøknadForFar(testscenario);
        
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilSakHarBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
        
        
    }
    
    @Test
    public void farOgMorSøkerFødselMedEttArbeidsforholdUtenOverlappendePeriode() throws Exception {
        TestscenarioDto testscenario = opprettScenario("81");
        
        long saksnummer = behandleSøknadForMor(testscenario);
        behandleSøknadForFar(testscenario);
        
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilSakHarBehandling(saksbehandler.kodeverk.BehandlingType.getKode("Revurdering"));
    }
    
    public long behandleSøknadForMor(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(søkerAktørid, annenPartAktørid, startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(inntekter.get(0), fnr,
                startDatoForeldrepenger, orgnr, Optional.empty(), Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, testscenario, saksnummer);
                
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.ikkeVentPåStatus = true;
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ikkeVentPåStatus = false;
        saksbehandler.ventOgGodkjennØkonomioppdrag();
        
        return saksnummer;
    }
    
    public void behandleSøknadForFar(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String annenPartIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(3);
       
        
        System.out.println("søkerAktørid: " + søkerAktørid);
        System.out.println("annenPartAktørid: " + annenPartAktørid);
        System.out.println("Fødselsdato: " + fødselsdato);
        System.out.println("startDatoForeldrepenger: " + startDatoForeldrepenger);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMorMedOverlapp(søkerAktørid, annenPartAktørid, startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                fnr,
                fødselsdato.plusWeeks(3),
                orgnr,
                Optional.empty(),
                Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, testscenario.getPersonopplysninger().getAnnenpartIdent(), saksnummer);
        
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
        
        Behandling behandling = saksbehandler.valgtBehandling;
    }
    
    public void behandleSøknadForFarUtenOverlapp(TestscenarioDto testscenario) throws Exception {
        String søkerAktørid = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String annenPartAktørid = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String annenPartIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate startDatoForeldrepenger = fødselsdato.plusWeeks(3);
       
        
        System.out.println("søkerAktørid: " + søkerAktørid);
        System.out.println("annenPartAktørid: " + annenPartAktørid);
        System.out.println("Fødselsdato: " + fødselsdato);
        System.out.println("startDatoForeldrepenger: " + startDatoForeldrepenger);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMorUtenOverlapp(søkerAktørid, annenPartAktørid, startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerAktørid, søkerIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        String fnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        
        List<Integer> inntekter = sorterteInntektsbeløp(testscenario);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        
        InntektsmeldingBuilder inntektsmelding = lagInntektsmeldingBuilder(
                inntekter.get(0),
                fnr,
                fødselsdato.plusWeeks(3),
                orgnr,
                Optional.empty(),
                Optional.empty());
        fordel.sendInnInntektsmelding(inntektsmelding, søkerAktørid, testscenario.getPersonopplysninger().getAnnenpartIdent(), saksnummer);
        
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
        
        Behandling behandling = saksbehandler.valgtBehandling;
        System.out.println("Done");
    }
    
}
