package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.autotest.util.AllureHelper.debugLoggBehandling;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FEDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_ARBEID;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.UTSETTELSETYPE_LOVBESTEMT_FERIE;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.addPeriode;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.addStønadskontotype;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.fordelingFarHappycaseKobletMedMorHappycase;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengerYtelseNorskBorger;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekerErketyper.morSoeker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer;
import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderFaktaOmBeregningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.aksjonspunktbekreftelse.VurderPerioderOpptjeningBekreftelse;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.beregning.FaktaOmBeregningTilfelle;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.builders.UttaksperiodeBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.RettigheterErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.SoekersRelasjonErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.kontrakter.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelderMedNorskIdent;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Rettigheter;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.Termin;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.UkjentForelder;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Utsettelsesaarsaker;
import no.nav.vedtak.felles.xml.soeknad.kodeverk.v3.Uttaksperiodetyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Gradering;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.ObjectFactory;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Utsettelsesperiode;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Virksomhet;

@Tag("utvikling")
@Tag("foreldrepenger")
public class Uttak extends ForeldrepengerTestBase {
    // Testcaser
    @Test
    @DisplayName("Mor automatisk førstegangssøknad fødsel")
    @Description("Mor førstegangssøknad på fødsel")
    public void testcase_mor_fødsel() throws Exception {
        TestscenarioDto testscenario = opprettScenario("75");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(8).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(13).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakKunMor(morAktørId, fordelingMor, SoekersRelasjonErketyper.fødsel(1,fødselsdato) );
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
    }
    @Test
    @DisplayName("Mor automatisk førstegangssøknad termin")
    @Description("Mor førstegangssøknad på termin")
    public void testcase_mor_termin() throws Exception {
        TestscenarioDto testscenario = opprettScenario("74");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate termindato = LocalDate.now().plusWeeks(4);
        LocalDate fpStartdatoMor = termindato.minusWeeks(3);
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, termindato.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato, termindato.plusWeeks(8).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, termindato.plusWeeks(8), termindato.plusWeeks(13).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakKunMor(morAktørId, fordelingMor, SoekersRelasjonErketyper.søkerTermin(1,termindato) );
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
    }
    @Test
    @DisplayName("Mor automatisk førstegangssøknad termin")
    @Description("Mor førstegangssøknad på termin")
    public void testcase_mor_fødsel_utenBarnTPS() throws Exception {
        TestscenarioDto testscenario = opprettScenario("74");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(4);
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(8).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(13).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakKunMor(morAktørId, fordelingMor, SoekersRelasjonErketyper.fødsel(1,fødselsdato) );
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
    }
    @Test
    @DisplayName("Testcase alenefar - søker med manglende periode")
    @Description("Alenefar søker med manglende perioder i starten")
    public void testcase_alenefar_fødsel_manglerPerioder() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(9);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER, fpStartDatoFar, fpStartDatoFar.plusWeeks(6).minusDays(1)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now(), fordeling);
        Rettigheter rettigheter = RettigheterErketyper.harAleneOmsorgOgEnerett();
        Termin fødsel = SoekersRelasjonErketyper.søkerTermin(fødselsdato);
        AnnenForelderMedNorskIdent annenpart = new AnnenForelderMedNorskIdent();
        annenpart.setAktoerId(morAktørId);

        søknadFar.withForeldrepengerYtelse(foreldrepengerYtelseNorskBorger(rettigheter, fødsel, fordeling, annenpart));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
    }
    @Test
    @DisplayName("Testcase for koblet sak")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. 100% dekningsgrad.")
    public void testcase_morOgFar_etterFødsel_kantTilKant() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(10).plusDays(1);

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId,
                fødselsdato, LocalDate.now(), fordelingFarHappycaseKobletMedMorHappycase(fødselsdato));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
    }
    @Test
    @DisplayName("Testcase for koblet sak med overlappende perioder, far mister dager")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. 100% dekningsgrad.")
    public void testcase_morOgFar_etterFødsel_overlapp_misterDager() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();

        LocalDate familiehendelse = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = familiehendelse.minusWeeks(3);
        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordeling.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, familiehendelse.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familiehendelse, familiehendelse.plusWeeks(6).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familiehendelse.plusWeeks(6), familiehendelse.plusWeeks(22).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakMedFordeling(morAktørId, farAktørId, fordeling, SoekersRelasjonErketyper.fødsel(1, familiehendelse));

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = familiehendelse.plusWeeks(6);
        Fordeling fordelingFar = new ObjectFactory().createFordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderFar = fordelingFar.getPerioder();
        perioderFar.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, familiehendelse.plusWeeks(6), familiehendelse.plusWeeks(10).minusDays(1)));
//        perioderFar.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familiehendelse.plusWeeks(20), familiehendelse.plusWeeks(30).minusDays(1)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.uttakMedFordeling(farAktørId, morAktørId, fordelingFar, SoekersRelasjonErketyper.fødsel(1, familiehendelse));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
//        saksbehandler.ventTilBehandlingsstatus("AVSLU");
    }
    @Test
    @DisplayName("Testcase for koblet sak med AP i uttak")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. 100% dekningsgrad. Med AP i uttak")
    public void testcase_morOgFar_etterFødsel_kantTilKant_APfar() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(10).plusDays(1);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fpStartDatoFar, fpStartDatoFar.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fpStartDatoFar.plusWeeks(6), fpStartDatoFar.plusWeeks(10)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now(), fordeling);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        //saksbehandler.ventTilBehandlingsstatus("AVSLU");

    }
    @Test
    @DisplayName("Testcase for koblet sak hvor far stjeler dager")
    @Description("Mor og far søker etter fødsel med ett arbeidsforhold hver. Far stjeler dager.")
    public void testcase_morOgFar_etterFødsel_overlapp() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fpStartDatoFar = fødselsdato.plusWeeks(6).plusDays(1);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fpStartDatoFar, fpStartDatoFar.plusWeeks(1).minusDays(1)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now(), fordeling);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);

        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        //saksbehandler.ventTilBehandlingsstatus("AVSLU");
    }
    @Test
    @DisplayName("Mor fødsel med ett arbeidsforhold, med gradering")
    @Description("Mor søker fødsel med ett arbeidsforhold, en periode med gradering")
    public void testcase_mor_fødsel_AT_gradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("75");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
//        perioder.add(graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(12).minusDays(1), 47, true, orgnr));
//        perioder.add(graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(12),fødselsdato.plusWeeks(18).minusDays(1), 33, true, orgnr));
        for (int i = 0 ; i<10 ; i++){
            perioder.add(graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(6).plusDays(i),fødselsdato.plusWeeks(6).plusDays(i), 33, true, orgnr));
        }
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(6).plusDays(10), fødselsdato.plusWeeks(30).minusDays(1)));

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakKunMor(morAktørId, fordeling, SoekersRelasjonErketyper.fødsel(1, fødselsdato));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
//        inntektsmeldingerMor.get(0).addGradertperiode(BigDecimal.valueOf(47), fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(12).minusDays(1));
//        inntektsmeldingerMor.get(0).addGradertperiode(BigDecimal.valueOf(33), fødselsdato.plusWeeks(12),fødselsdato.plusWeeks(18).minusDays(1));
        for (int i = 0 ; i<10 ; i++){
            inntektsmeldingerMor.get(0).addGradertperiode(BigDecimal.valueOf(33), fødselsdato.plusWeeks(6).plusDays(i),fødselsdato.plusWeeks(6).plusDays(i));
        }
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }
    @Test
    @DisplayName("Testcase mor tom for dager med gradering")
    @Description("Mor går tom for stønadsdager i en graderingsperiode")
    public void testcase_mor_fødsel_AT_gradering_tomForDager() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        // Oppretter fordeling med gradering
        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(22).minusDays(1)));
        LocalDate graderingFom = fødselsdato.plusWeeks(22);
        LocalDate graderingTom = fødselsdato.plusWeeks(50);
        Gradering gradering = new Gradering();
        gradering.setArbeidsforholdSomSkalGraderes(true);
        gradering.setArbeidtidProsent(20);
        gradering.setErArbeidstaker(true);
        Virksomhet virksomhet = new Virksomhet();
        virksomhet.setIdentifikator(orgnr);
        gradering.setArbeidsgiver(virksomhet);
        addStønadskontotype(STØNADSKONTOTYPE_MØDREKVOTE, gradering);
        addPeriode(graderingFom, graderingTom, gradering);
        perioder.add(gradering);

        // sender inn søknad
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        inntektsmeldingerMor.get(0).addGradertperiode(BigDecimal.valueOf(20), graderingFom, graderingTom);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }
    @Test
    @DisplayName("Testcase mor tom for dager")
    @Description("Mor går tom for dager, søker mer av kontoen senere også")
    public void testcase_mor_fødsel_tomForDager() throws Exception {

        TestscenarioDto testscenario = opprettScenario("50");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(23).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(23), fødselsdato.plusWeeks(33).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(33), fødselsdato.plusWeeks(34).minusDays(1)));

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }
    @Test
    @DisplayName("Testcase mor - søker med manglende perioder")
    @Description("Mor søker med manglende perioder i starten")
    public void testcase_mor_fødsel_manglerPerioder() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(7)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(10), fødselsdato.plusWeeks(11)));


        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }
    @Test
    @DisplayName("Mor søker på termin men barn finnes, test for flytting av perioder")
    @Description("Mor sender førstegangssøknad på termin, kan brukes for å teste flytting")
    public void testcase_mor_fødsel_flyttingFødsel() throws Exception {
        //TODO FIKSE på periodene og legge til IM med utsettelse
        TestscenarioDto testscenario = opprettScenario("78");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate termindato = fødselsdato.plusWeeks(1);
        LocalDate fpStartdatoMor = termindato.minusWeeks(3);

        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, termindato.minusWeeks(3), termindato.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato, termindato.plusWeeks(2)));
        perioderMor.add(new UttaksperiodeBuilder()
                .medTidsperiode(termindato.plusWeeks(2).plusDays(1), termindato.plusWeeks(9))
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .build());
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato.plusWeeks(9).plusDays(1), termindato.plusWeeks(23)));

        LocalDate utsettelseFOM = termindato.plusWeeks(23).plusDays(1);
        LocalDate utsettelseTOM = termindato.plusWeeks(27);
        Utsettelsesperiode utsettelse = utsettelseSøknadBuilder(utsettelseFOM,utsettelseTOM, UTSETTELSETYPE_ARBEID, false);
        perioderMor.add(utsettelse);
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, termindato.plusWeeks(27).plusDays(1), termindato.plusWeeks(30).minusDays(1)));
        perioderMor.add(new UttaksperiodeBuilder()
                .medTidsperiode(termindato.plusWeeks(30),termindato.plusWeeks(50))
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medFlerbarnsdager(true)
                .build());
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakMedFordeling(morAktørId, fordelingMor, SoekersRelasjonErketyper.søkerTermin( 2,termindato) );
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }
    @Test
    @DisplayName("Mor og fra søker om samtidig uttak med overlapp")
    @Description("Mor søker om samtidig uttak i førstegangssøknaden og far søker overlapp i denne perioden")
    public void testcase_morOgFar_samtidigUttak_overlappendePerioder() throws Exception {
        TestscenarioDto testscenario = opprettScenario("85");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String farFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();

        LocalDate familiehendelse = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = familiehendelse.minusWeeks(3);
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        LocalDate samtidigUttakFOM = familiehendelse.plusWeeks(6);
        LocalDate samtidigUttakTOM = familiehendelse.plusWeeks(14).minusDays(1);
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, familiehendelse.minusWeeks(3), familiehendelse.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familiehendelse, familiehendelse.plusWeeks(6).minusDays(1)));
        perioderMor.add(new UttaksperiodeBuilder()
                .medTidsperiode(samtidigUttakFOM, samtidigUttakTOM)
                .medStønadskontoType(STØNADSKONTOTYPE_MØDREKVOTE)
                .medSamtidigUttak(true, BigDecimal.valueOf(50))
                .build());
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familiehendelse.plusWeeks(14), familiehendelse.plusWeeks(20).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.uttakMedFordeling(morAktørId, farAktørId, fordelingMor, SoekersRelasjonErketyper.søkerTermin( 2,familiehendelse) );
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        LocalDate fpStartDatoFar = samtidigUttakFOM;
        Fordeling fordelingFar = new ObjectFactory().createFordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderFar = fordelingFar.getPerioder();
        perioderFar.add(new UttaksperiodeBuilder()
                .medTidsperiode(samtidigUttakFOM, samtidigUttakTOM.plusWeeks(3))
                .medStønadskontoType(STØNADSKONTOTYPE_FELLESPERIODE)
                .medSamtidigUttak(true, BigDecimal.valueOf(100))
                .medFlerbarnsdager(true)
                .build());
        perioderFar.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, familiehendelse.plusWeeks(20), familiehendelse.plusWeeks(21).minusDays(1)));
        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.uttakMedFordeling(farAktørId, morAktørId, fordelingFar, SoekersRelasjonErketyper.fødsel( 2,familiehendelse));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farFnr, fpStartDatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farFnr, saksnummerFar);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");

    }

    @Test
    @DisplayName("Mor fødsel med Arena")
    @Description("Mor søker fødsel med arena")
    public void testcase_mor_fødsel_ARENA() throws Exception {
        TestscenarioDto testscenario = opprettScenario("46");

        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        // Må fortsette behandling fra behandlingsmeny for å få opp at FP er innvilget og behandlingen er avsluttet (refresh 1-2 ganger)

    }

    @Test
    @DisplayName("Testcase mor uten barn søker fødsel ")
    @Description("Mor har ikke barn registrert i TPS og søker på fødsel som skal ha skjedd 3 uker før.")
    public void testcase_mor_fødsel_ikkeBarn() throws Exception {
        TestscenarioDto testscenario = opprettScenario("55");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoSøker = fødselsdato;

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fpStartdatoSøker, fpStartdatoSøker.plusWeeks(15).minusDays(1)));

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerSøker = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoSøker);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerSøker, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Mor søker med ukjent annen foreldre")
    @Description("AnnenForelder, kjent eller ikke kjent")
    public void testcase_mor_farIkkeKjent() throws Exception {
        TestscenarioDto testscenario = opprettScenario("83");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String annenpartAktørIdent = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoSøker = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(false);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoSøker, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(15).minusDays(1)));

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.uttakMedFordeling(søkerAktørIdent,new UkjentForelder(),fordeling, RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg(), SoekersRelasjonErketyper.fødsel( 1,fødselsdato));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerSøker = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoSøker);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerSøker, testscenario, saksnummerMor);

    }
    @Test
    @DisplayName("Mor søker om aleneomsorg")
    @Description("Mor søker fult uttak med foreldrepenger")
    public void testcase_mor_aleneomsorg() throws Exception {
        TestscenarioDto testscenario = opprettScenario("83");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String annenAktørIdent = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String søkerIdent = søkerAktørIdent;
        LocalDate fødselsdato = LocalDate.now().plusWeeks(4);
        LocalDate fpStartdatoSøker = fødselsdato.minusWeeks(3);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoSøker, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER, fødselsdato, fødselsdato.plusWeeks(46).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(46), fødselsdato.plusWeeks(50).minusDays(1)));

        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarAleneomsorgForBarnet(true);
        rettigheter.setHarAnnenForelderRett(false);
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.uttakMedFordeling(annenAktørIdent, new UkjentForelder(), fordeling, rettigheter, SoekersRelasjonErketyper.søkerTermin( 1,fødselsdato));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerIdent, søkerFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerSøker = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoSøker);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerSøker, søkerIdent, søkerFnr, saksnummer);
    }
    @Test
    @DisplayName("Far mor ikke rett")
    @Description("Far søker om foreldrepenger og mor har ikke rett til foreldrepenger")
    public void testcase_far_morIkkeRett() throws Exception {
        TestscenarioDto testscenario = opprettScenario("83");

        String søkerIdent = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        String søkerFnr = testscenario.getPersonopplysninger().getAnnenpartIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(4);
        LocalDate fpStartdatoSøker = fødselsdato.plusWeeks(6);

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER, fpStartdatoSøker, fødselsdato.plusWeeks(46).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(46), fødselsdato.plusWeeks(50).minusDays(1)));

        Rettigheter rettigheter = new Rettigheter();
        rettigheter.setHarAleneomsorgForBarnet(true);
        rettigheter.setHarAnnenForelderRett(false);
        rettigheter.setHarOmsorgForBarnetIPeriodene(true);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.uttakMedFordeling(søkerIdent, new UkjentForelder(), fordeling, rettigheter, SoekersRelasjonErketyper.søkerTermin( 1,fødselsdato));
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), søkerIdent, søkerFnr, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerSøker = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoSøker);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerSøker, søkerIdent, søkerFnr, saksnummer);
    }

    @Test
    @DisplayName("Mor fødsel med arbeidsforhold og frilans. AP i Fakta om uttak")
    @Description("Mor søker fødsel med ett arbeidsforhold og frilans. Vurder fakta om beregning. Avvik i beregning")
    public void testcase_mor_fødsel_ATFL_medAP() throws Exception {
        TestscenarioDto testscenario = opprettScenario("59");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = fødselsdato.minusWeeks(3);
        String orgNr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();

        int inntektPerMåned = 20_000;
        int overstyrtInntekt = 500_000;
        int overstyrtFrilanserInntekt = 500_000;
        BigDecimal refusjon = BigDecimal.valueOf(overstyrtInntekt + overstyrtFrilanserInntekt);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedFrilans(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNr, Optional.empty(), Optional.of(refusjon), Optional.empty());

        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);
    }
    @Test
    @DisplayName("Mor automatisk førstegangssøknad med endringssøknad")
    @Description("Mor sender førstegangssøknad på termin, automatsik vurdert og mor sender endringssøknad")
    public void testcase_mor_termin_endringssøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("74");
        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate familieHendelse = LocalDate.now().plusMonths(1);
        LocalDate fpStartdatoMor = familieHendelse.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        // Fordeling og søknad mor
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, familieHendelse.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse, familieHendelse.plusWeeks(8).minusDays(1)));
        perioderMor.add(FordelingErketyper.oppholdsperiode(OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER, familieHendelse.plusWeeks(8), familieHendelse.plusWeeks(10).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse.plusWeeks(10), familieHendelse.plusWeeks(14).minusDays(1)));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.termindatoUttakKunMor(morAktørId, fordelingMor, familieHendelse );
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");
        // Endring - fordelig og søknad mor
        Long saksnummerMorL = saksnummerMor;
        String saksnummerMorS = saksnummerMorL.toString();
        Fordeling fordelingMorEndring = new ObjectFactory().createFordeling();
        fordelingMorEndring.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMorEndring = fordelingMorEndring.getPerioder();
        perioderMorEndring.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familieHendelse.plusWeeks(14), familieHendelse.plusWeeks(16).minusDays(1)));
        LocalDate graderingFom = familieHendelse.plusWeeks(6);
        LocalDate graderingTom = familieHendelse.plusWeeks(14).minusDays(1);
        Gradering gradering = graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, graderingFom, graderingTom, 60, true, orgnr);
        perioderMorEndring.add(gradering);
        ForeldrepengesoknadBuilder søknadMorEndring = foreldrepengeSøknadErketyper.fodselfunnetstedKunMorEndring(morAktørId, fordelingMorEndring, saksnummerMorS);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        Long saksnummerMorE = fordel.sendInnSøknad(søknadMorEndring.build(), morAktørId, morIdent, DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMorE);
        saksbehandler.ventTilSakHarBehandling("Revurdering");
        saksbehandler.velgBehandling("Revurdering");
    }

    @Test
    @DisplayName("Koblet sak med oppholdsperioder og endringssøknad")
    @Description("Mor søker (med oppholdsperioder for far). Far søker (med oppholdsperioder for mor). Berørt sak på begge. " +
            "Endringssøknad fra mor for å sjekke endringsstartpunkt. Endringssøknad fra far for å sjekke endringsstartpunkt." +
            "Blir opprettet veldig mange revurderinger. En del gå automatisk gjennom og vrient å holde styr på rekkefølge.")
    public void testcase_kobletSak_endringssøknad() throws Exception {
        TestscenarioDto testscenario = opprettScenario("82");

        String morIdent = testscenario.getPersonopplysninger().getSøkerIdent();
        String farIdent = testscenario.getPersonopplysninger().getAnnenpartIdent();
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String farAktørId = testscenario.getPersonopplysninger().getAnnenPartAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        LocalDate fpStartdatoFar = fødselsdato.plusWeeks(8);

        // Fordeling og søknad mor
        Fordeling fordelingMor = new ObjectFactory().createFordeling();
        fordelingMor.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMor = fordelingMor.getPerioder();
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(8).minusDays(1)));
        perioderMor.add(FordelingErketyper.oppholdsperiode(OPPHOLDSTYPE_FEDREKVOTE_ANNEN_FORELDER, fødselsdato.plusWeeks(8), fødselsdato.plusWeeks(10).minusDays(1)));
        perioderMor.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(10), fødselsdato.plusWeeks(14).minusDays(1)));

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedMorMedFar(morAktørId, farAktørId, fødselsdato, LocalDate.now().minusWeeks(1), fordelingMor);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMor);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        // Fordeling og søknad far
        Fordeling fordelingFar = new ObjectFactory().createFordeling();
        fordelingFar.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderFar = fordelingFar.getPerioder();
        perioderFar.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fpStartdatoFar, fpStartdatoFar.plusWeeks(2).minusDays(1)));
        perioderFar.add(FordelingErketyper.oppholdsperiode(OPPHOLDSTYPE_MØDREKVOTE_ANNEN_FORELDER, fødselsdato.plusWeeks(10), fødselsdato.plusWeeks(14).minusDays(1)));
        perioderFar.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(14), fødselsdato.plusWeeks(20).minusDays(1)));

        ForeldrepengesoknadBuilder søknadFar = foreldrepengeSøknadErketyper.fodselfunnetstedFarMedMor(farAktørId, morAktørId, fødselsdato, LocalDate.now().minusWeeks(1), fordelingFar);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummerFar = fordel.sendInnSøknad(søknadFar.build(), farAktørId, farIdent, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerFar = makeInntektsmeldingFromTestscenarioMedIdent(testscenario, farIdent, fpStartdatoFar, true);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerFar, farAktørId, farIdent, saksnummerFar);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerFar);
        saksbehandler.velgBehandling("Førstegangsbehandling");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        // Endring - fordelig og søknad mor
        Fordeling fordelingMorEndring = new ObjectFactory().createFordeling();
        fordelingMorEndring.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderMorEndring = fordelingMorEndring.getPerioder();
        perioderMorEndring.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato.plusWeeks(9), fødselsdato.plusWeeks(10).minusDays(1)));
        Long saksnummerMorL = saksnummerMor;
        String saksnummerMorS = saksnummerMorL.toString();
        ForeldrepengesoknadBuilder søknadMorEndring = foreldrepengeSøknadErketyper.fodselfunnetstedKunMorEndring(morAktørId, fordelingMorEndring, saksnummerMorS);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        Long saksnummerMorE = fordel.sendInnSøknad(søknadMorEndring.build(), morAktørId, morIdent, DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD, saksnummerMor);
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummerMorE);
        saksbehandler.ventTilSakHarBehandling("Revurdering");
        saksbehandler.velgBehandling("Revurdering");
        saksbehandler.ventTilBehandlingsstatus("AVSLU");

        // Endring - fordeling og søknad far
        Fordeling fordelingFarEndring = new ObjectFactory().createFordeling();
        fordelingFarEndring.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioderFarEndring = fordelingFarEndring.getPerioder();
        perioderFarEndring.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FEDREKVOTE, fødselsdato.plusWeeks(11), fødselsdato.plusWeeks(13).minusDays(1)));
        Long saksnummerFarL = saksnummerFar;
        String saksnummerFarS = saksnummerFarL.toString();
        ForeldrepengesoknadBuilder søknadFarEndring = foreldrepengeSøknadErketyper.fodselfunnetstedKunMorEndring(farAktørId, fordelingFarEndring, saksnummerFarS);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        Long saksnummerFarE = fordel.sendInnSøknad(søknadFarEndring.build(), farAktørId, farIdent, DokumenttypeId.FORELDREPENGER_ENDRING_SØKNAD, saksnummerFar);

    }

    @Test
    @DisplayName("Utsettelse ")
    @Description("-")
    public void testcase_uttsettelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("49");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoMor = fødselsdato.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(6).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, fødselsdato.plusWeeks(6), fødselsdato.plusWeeks(10).minusDays(1)));
        LocalDate utsettelseFom = fødselsdato.plusWeeks(10);
        LocalDate utsettelseTom = fødselsdato.plusWeeks(15);
        perioder.add(utsettelseSøknadBuilder(utsettelseFom, utsettelseTom, UTSETTELSETYPE_ARBEID, true));
        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(morAktørId, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldingerMor = makeInntektsmeldingFromTestscenario(testscenario, fpStartdatoMor);
        inntektsmeldingerMor.get(0).addUtsettelseperiode(UTSETTELSETYPE_ARBEID, utsettelseFom, utsettelseTom);
        fordel.sendInnInntektsmeldinger(inntektsmeldingerMor, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Testcase mor termin med gradering i uke 7")
    @Description("Mor søker på termin, graderer i uke 7")
    public void testcase_mor_termin_AT_gradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("74");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String fnrMor = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate familieHendelse = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoMor = familieHendelse.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Integer beløp = testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioderSplittMånedlig().get(0).getBeløp();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, familieHendelse.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse, familieHendelse.plusWeeks(6).minusDays(1)));
        LocalDate graderingFom = familieHendelse.plusWeeks(6);
        LocalDate graderingTom = familieHendelse.plusWeeks(14).minusDays(1);
        Gradering gradering = graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, graderingFom, graderingTom, 80, true, orgnr);
        perioder.add(gradering);
        LocalDate graderingFom2 = familieHendelse.plusWeeks(14);
        LocalDate graderingTom2 = familieHendelse.plusWeeks(16).minusDays(1);
        Gradering gradering2 = graderingSøknadBuilder(STØNADSKONTOTYPE_FELLESPERIODE, graderingFom2, graderingTom2, 20, true, orgnr);
        perioder.add(gradering2);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.termindatoUttakKunMor(morAktørId, fordeling, familieHendelse);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingerMor = lagInntektsmeldingBuilder(beløp, fnrMor, fpStartdatoMor, orgnr, Optional.empty(), Optional.empty(), Optional.empty());
        inntektsmeldingerMor.addGradertperiode(BigDecimal.valueOf(80), graderingFom, graderingTom);
        inntektsmeldingerMor.addGradertperiode(BigDecimal.valueOf(20), graderingFom2, graderingTom2);
        fordel.sendInnInntektsmelding(inntektsmeldingerMor, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Testcase mor termin med gradering og utsettelse")
    @Description("Mor søker på termin, gradering og utsettelse")
    public void testcase_mor_termin_AT_gradering_utsettelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("74");
        String morAktørId = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        String fnrMor = testscenario.getPersonopplysninger().getSøkerIdent();
        LocalDate familieHendelse = LocalDate.now().minusWeeks(3);
        LocalDate fpStartdatoMor = familieHendelse.minusWeeks(3);
        String orgnr = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Integer beløp = testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioderSplittMånedlig().get(0).getBeløp();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdatoMor, familieHendelse.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse, familieHendelse.plusWeeks(10).minusDays(1)));
        LocalDate graderingFom = familieHendelse.plusWeeks(10);
        LocalDate graderingTom = familieHendelse.plusWeeks(14).minusDays(1);
        Gradering gradering = graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE, graderingFom, graderingTom, 80, true, orgnr);
        perioder.add(gradering);
        LocalDate utsettelseFOM = familieHendelse.plusWeeks(14);
        LocalDate utsettelseTOM = familieHendelse.plusWeeks(40);
        Utsettelsesperiode utsettelse = utsettelseSøknadBuilder(utsettelseFOM,utsettelseTOM, UTSETTELSETYPE_ARBEID, true);
        perioder.add(utsettelse);

        ForeldrepengesoknadBuilder søknadMor = foreldrepengeSøknadErketyper.termindatoUttakKunMor(morAktørId, fordeling, familieHendelse);
        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummerMor = fordel.sendInnSøknad(søknadMor.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingerMor = lagInntektsmeldingBuilder(beløp, fnrMor, fpStartdatoMor, orgnr, Optional.empty(), Optional.empty(), Optional.empty());
        inntektsmeldingerMor.addGradertperiode(BigDecimal.valueOf(80), graderingFom, graderingTom);
        inntektsmeldingerMor.addUtsettelseperiode(UTSETTELSETYPE_ARBEID,utsettelseFOM,utsettelseTOM);
        fordel.sendInnInntektsmelding(inntektsmeldingerMor, testscenario, saksnummerMor);
    }

    @Test
    @DisplayName("Mor Fødsel ATFL med graderte perioder")
    @Description("Mor søker på fødsel, har status ATFL og søker med minst en gradert periode")
    public void testcase_mor_fødsel_ATFL_gradering() throws Exception {
        TestscenarioDto testscenario = opprettScenario("59");

        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate familieHendelse = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = familieHendelse.minusWeeks(3);
        String orgNrAT = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Integer inntektPerMåned = testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioderSplittMånedlig().get(0).getBeløp();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdato, familieHendelse.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse, familieHendelse.plusWeeks(6).minusDays(1)));
        LocalDate graderingFOM = familieHendelse.plusWeeks(6);
        LocalDate graderingTOM = familieHendelse.plusWeeks(14).minusDays(1);
        Gradering gradering = graderingSøknadBuilder(STØNADSKONTOTYPE_MØDREKVOTE,graderingFOM,graderingTOM,80,true, orgNrAT);
        perioder.add(gradering);
        LocalDate gradering2FOM = familieHendelse.plusWeeks(14);
        LocalDate gradering2TOM = familieHendelse.plusWeeks(16).minusDays(1);
        Gradering gradering2 = new Gradering();
        graderingSøknadBuilder(gradering2,STØNADSKONTOTYPE_FELLESPERIODE,gradering2FOM,gradering2TOM,63,false);
        perioder.add(gradering2);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedFrilans(fordeling,søkerAktørIdent, familieHendelse);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNrAT, Optional.empty(), Optional.empty(), Optional.empty());
        inntektsmeldingBuilder.addGradertperiode(BigDecimal.valueOf(80), graderingFOM, graderingTOM);
        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");
        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilMottarYtelse(false, Collections.emptyList());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);
    }
    @Test
    @DisplayName("Mor Fødsel ATFL med utsettelse perioder")
    @Description("Mor søker på fødsel, har status ATFL og søker med minst en gradert periode")
    public void testcase_mor_fødsel_ATFL_utsettelse() throws Exception {
        TestscenarioDto testscenario = opprettScenario("59");
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();
        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate familieHendelse = testscenario.getPersonopplysninger().getFødselsdato();
        LocalDate fpStartdato = familieHendelse.minusWeeks(3);
        String orgNrAT = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold().get(0).getArbeidsgiverOrgnr();
        Integer inntektPerMåned = testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioderSplittMånedlig().get(0).getBeløp();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, fpStartdato, familieHendelse.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_MØDREKVOTE, familieHendelse, familieHendelse.plusWeeks(15).minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(STØNADSKONTOTYPE_FELLESPERIODE, familieHendelse.plusWeeks(15), familieHendelse.plusWeeks(30).minusDays(1)));
        LocalDate utsettelseFOM = familieHendelse.plusWeeks(30);
        LocalDate utsettelseTOM = familieHendelse.plusWeeks(35).plusDays(1);
        Utsettelsesperiode utsettelse = utsettelseSøknadBuilder(utsettelseFOM, utsettelseTOM, UTSETTELSETYPE_ARBEID, false);
        perioder.add(utsettelse);

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMorMedFrilans(fordeling,søkerAktørIdent, familieHendelse);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        InntektsmeldingBuilder inntektsmeldingBuilder = lagInntektsmeldingBuilder(inntektPerMåned, fnr, fpStartdato,
                orgNrAT, Optional.empty(), Optional.empty(), Optional.empty());
        inntektsmeldingBuilder.addUtsettelseperiode(UTSETTELSETYPE_ARBEID,utsettelseFOM,utsettelseTOM);

        fordel.sendInnInntektsmelding(inntektsmeldingBuilder, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilHistorikkinnslag("Vedlegg mottatt");
        saksbehandler.hentAksjonspunktbekreftelse(VurderPerioderOpptjeningBekreftelse.class)
                .godkjennAllOpptjening();
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderPerioderOpptjeningBekreftelse.class);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.hentAksjonspunktbekreftelse(VurderFaktaOmBeregningBekreftelse.class)
                .leggTilFaktaOmBeregningTilfeller(FaktaOmBeregningTilfelle.VURDER_MOTTAR_YTELSE.kode)
                .leggTilMottarYtelse(false, Collections.emptyList());
        saksbehandler.bekreftAksjonspunktBekreftelse(VurderFaktaOmBeregningBekreftelse.class);
    }

    private Gradering graderingSøknadBuilder(Gradering gradering, String STØNADSKONTOTYPE, LocalDate graderingFom, LocalDate graderingTom, Integer arbeidtidProsent, boolean setErArbeidstaker){
        gradering.setArbeidsforholdSomSkalGraderes(true);
        gradering.setArbeidtidProsent(arbeidtidProsent);
        addStønadskontotype(STØNADSKONTOTYPE, gradering);
        addPeriode(graderingFom, graderingTom, gradering);
        gradering.setErArbeidstaker(setErArbeidstaker);

        return gradering;
    }
    private Gradering graderingSøknadBuilder(String STØNADSKONTOTYPE, LocalDate graderingFom, LocalDate graderingTom, Integer arbeidtidProsent, boolean setErArbeidstaker, String orgNr){
        Gradering gradering = new Gradering();
        Virksomhet virksomhet = new Virksomhet();
        virksomhet.setIdentifikator(orgNr);
        gradering.setArbeidsgiver(virksomhet);
        return graderingSøknadBuilder(gradering,STØNADSKONTOTYPE, graderingFom,graderingTom, arbeidtidProsent, setErArbeidstaker);
    }
    private Utsettelsesperiode utsettelseSøknadBuilder(LocalDate utsettelseFom, LocalDate utsettelseTom, String UTSETTELSETYPE, boolean setErArbeidstaker){
        Utsettelsesperiode utsettelse = new Utsettelsesperiode();
        Utsettelsesaarsaker årsak = new Utsettelsesaarsaker();
        årsak.setKode(UTSETTELSETYPE);
        utsettelse.setAarsak(årsak);
        utsettelse.setErArbeidstaker(setErArbeidstaker);
        addPeriode(utsettelseFom, utsettelseTom, utsettelse);
        return utsettelse;
    }
}