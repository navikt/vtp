package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import io.qameta.allure.Description;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v1.ObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;


@Tag("foreldrepenger")
public class Uttak extends ForeldrepengerTestBase {
    // Testcaser

    @Test
    @DisplayName("Testcase for koblet sak")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. 100% dekningsgrad.")
    public void testcase_morOgFarSøkerEtterFødsel_kantTilKantsøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(10).plusDays(1);

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId,morAktørId,
                fødselsdato, LocalDate.now(), FordelingErketyper.fordelingFarHappycaseKobletMedMorHappycase(fødselsdato));
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

    }

    @Test
    @DisplayName("Mor fødsel med ett arbeidsforhold, med gradering")
    @Description("Mor søker fødsel med ett arbeidsforhold, en periode med gradering")
    public void testcase_enArbeidstaker_medGradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        // Oppretter fordeling med gradering
        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        LocalDate graderingFom = fødselsdato.plusWeeks(6);
        LocalDate graderingTom = fødselsdato.plusWeeks(12);
        Gradering gradering = new Gradering();
        gradering.setArbeidsforholdSomSkalGraderes(true);
        gradering.setArbeidtidProsent(55);
        gradering.setErArbeidstaker(true);
        gradering.setVirksomhetsnummer(orgnr);
        FordelingErketyper.addStønadskontotype(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, gradering);
        FordelingErketyper.addPeriode(graderingFom, graderingTom, gradering);
        perioder.add(gradering);

        // sender inn søknad
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Testcase mor - fødsel ")
    @Description("Tom for dager")
    public void testcase_enArbeidstaker_tomForDager() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(23).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(23), fødselsdato.plusWeeks(33).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(33), fødselsdato.plusWeeks(34).minusDays(1)));

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Mor fødsel med Arena")
    @Description("Mor søker fødsel med arena")
    public void testcase_morSøkerFødsel_medArena() throws Exception {
        TestscenarioDto testscenario = opprettScenario("46");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        // Må fortsette behandling fra behandlingsmeny for å få opp at FP er innvilget og behandlingen er avsluttet (refresh 1-2 ganger)

    }

    @Test
    @DisplayName("Testcase mor -  fødsel ")
    @Description("har ikke barn")
    public void testcase_enArbeidstaker_fødsl_ikkeBarn() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoSøker = fødselsdato;

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fpStartdatoSøker, fpStartdatoSøker.plusWeeks(10)));

        ForeldrepengesoknadBuilder søknadSøknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadSøknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerSøker = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoSøker);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerSøker, testscenario, saksnummerMor);
    }



}
