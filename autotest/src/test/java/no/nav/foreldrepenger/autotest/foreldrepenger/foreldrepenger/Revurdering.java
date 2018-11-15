package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderManglendeFodselBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarBrukerHarGyldigPeriodeBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.avklarfakta.AvklarFaktaTillegsopplysningerBekreftelse;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("foreldrepenger")
public class Revurdering extends ForeldrepengerTestBase {

    @Test
    public void opprettRevurderingManuelt() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, LocalDate.now().minusMonths(1));
        InntektsmeldingBuilder inntektsmeldingsBuilder = inntektsmeldinger.get(0);
        //inntektsmeldingsBuilder.addGradertperiode(100, InntektsmeldingBuilder.createPeriode(LocalDate.now().plusWeeks(3), LocalDate.now().plusWeeks(5)));
        LocalDate startDatoForeldrepenger = LocalDate.now();
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        fordel.sendInnInntektsmelding(inntektsmeldingsBuilder, testscenario, saksnummer);
        System.out.println("Saksnummer: " + saksnummer);
        opprettForstegangsbehandling(saksnummer);

    }

    private void opprettForstegangsbehandling(long saksnummer) throws Exception {
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiser(saksbehandler.valgtBehandling != null);
        // tilleggsopplysninger: bekreft "jeg har lest dette" bekreftedeAksjonspunker type:5009
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaTillegsopplysningerBekreftelse.class);
        // fakta om fødsel: dokumentasjon foreligger. fyll inn dato og antall barn født. begrunn endringene. bekreftedeAksjonspunkt type 5027
        saksbehandler.hentAksjonspunktbekreftelse(VurderManglendeFodselBekreftelse.class).bekreftDokumentasjonForeligger(1,LocalDate.now());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderManglendeFodselBekreftelse.class);
        // fakta om medlemsskap: periode med medlemsskap, begrunn, bekreft og godkjenn. type 5021
        saksbehandler.hentAksjonspunktbekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class)
                .setVurdering(saksbehandler.kodeverk.MedlemskapManuellVurderingType.getKode("MEDLEM"))
                .setBegrunnelse("Medlemskap gyldig.");
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarBrukerHarGyldigPeriodeBekreftelse.class);


        /**** Her stopper det enn så lenge
        //Beregning. vurdering (minst 3 tegn). fastsatt inntekt(PrAndelList) (600000). bekreft type 5038
        saksbehandler.hentAksjonspunktbekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class)
                .leggTilInntekt(600000)
                .setBegrunnelse("Inntekt beregnet");
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderBeregnetInntektsAvvikBekreftelse.class);
        // fakta omsorg. vurdering. Søker har aleneomsorg. bekreft. type 5060. aleneomsorg:true, begrunnelse: "blabla"
        saksbehandler.hentAksjonspunktbekreftelse(AvklarFaktaAleneomsorgBekreftelse.class).setBegrunnelse("Mor har aleneomsorg.");
        //legg til kodeverk
        saksbehandler.bekreftAksjonspunktBekreftelse(AvklarFaktaAleneomsorgBekreftelse.class);
        //uttak. før fødsel: mødrekvoten. etter fødsel: foreldrepenger.
        String s = "";
        //vedtak. fritekst brev. Til godkjenning. bekreftedeAksjonspunkt 5015
         *****/



    }
}
