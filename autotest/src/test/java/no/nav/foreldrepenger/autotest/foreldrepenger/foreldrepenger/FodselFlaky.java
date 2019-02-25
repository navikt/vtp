package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import no.nav.foreldrepenger.autotest.base.ForeldrepengerTestBase;
import org.junit.jupiter.api.Tag;

@Tag("flaky")
public class FodselFlaky extends ForeldrepengerTestBase {
    @Test
    @DisplayName("Mor søker gradering og utsettelse. Med to arbeidsforhold. Uten avvikende inntektsmelding")
    public void morSøkerGraderingOgUtsettelseMedToArbeidsforhold_utenAvvikendeInntektsmeldinger() throws Exception {

        TestscenarioDto testscenario = opprettScenario("58");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        Fordeling fordeling = new ObjectFactory().createFordeling();
        fordeling.setAnnenForelderErInformert(true);
        List<LukketPeriodeMedVedlegg> perioder = fordeling.getPerioder();
        LocalDate startdatoForeldrePenger = fødselsdato.minusWeeks(3);
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, startdatoForeldrePenger, fødselsdato.minusDays(1)));
        perioder.add(FordelingErketyper.uttaksperiode(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE, fødselsdato, fødselsdato.plusWeeks(10)));
        String gradetArbeidsgiver = "979191138";
        LocalDate graderingFom = fødselsdato.plusWeeks(10).plusDays(1);
        LocalDate graderingTom = fødselsdato.plusWeeks(12);
        BigDecimal arbeidstidsprosent = BigDecimal.TEN;
        perioder.add(FordelingErketyper.graderingPeriode(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE, graderingFom, graderingTom, gradetArbeidsgiver,
                arbeidstidsprosent.doubleValue()));
        LocalDate utsettelseFom = fødselsdato.plusWeeks(12).plusDays(1);
        LocalDate utsettelseTom = fødselsdato.plusWeeks(14);
        perioder.add(FordelingErketyper.utsettelsePeriode(FordelingErketyper.UTSETTELSETYPE_ARBEID, utsettelseFom, utsettelseTom));
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(søkerAktørIdent, fordeling, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);

        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startdatoForeldrePenger);

        for (InntektsmeldingBuilder im : inntektsmeldinger) {
            im.addUtsettelseperiode(FordelingErketyper.UTSETTELSETYPE_ARBEID, utsettelseFom, utsettelseTom);
            if (im.getArbeidsgiver().getVirksomhetsnummer().equals(gradetArbeidsgiver)) {
                im.addGradertperiode(arbeidstidsprosent, graderingFom, graderingTom);
            }
        }

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        //hackForÅKommeForbiØkonomi(saksnummer);

        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(5);
        for (UttakResultatPeriode periode : uttaksperioder) {
            assertThat(periode.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
            assertThat(periode.getPeriodeResultatÅrsak().kode).isNotEqualTo("-");
            assertThat(periode.getAktiviteter()).hasSize(2);
            for (UttakResultatPeriodeAktivitet aktivitet : periode.getAktiviteter()) {
                assertThat(aktivitet.getArbeidsgiver().getVirksomhet()).isTrue();
                assertThat(aktivitet.getArbeidsgiver().getAktørId()).isNull();
                assertThat(aktivitet.getArbeidsgiver().getNavn()).isNotNull();
                assertThat(aktivitet.getArbeidsgiver().getNavn()).isNotEmpty();
                assertThat(aktivitet.getUttakArbeidType().kode).isEqualTo("ORDINÆRT_ARBEID");
                List<Arbeidsforhold> arbeidsforholdFraScenario = testscenario.getScenariodata().getArbeidsforholdModell().getArbeidsforhold();
                assertThat(aktivitet.getArbeidsgiver().getIdentifikator()).isIn(arbeidsforholdFraScenario.get(0).getArbeidsgiverOrgnr(),
                        arbeidsforholdFraScenario.get(1).getArbeidsgiverOrgnr());
            }
        }
        UttakResultatPeriode fpff = uttaksperioder.get(0);
        assertThat(fpff.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(fpff.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        assertThat(fpff.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(fpff.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        UttakResultatPeriode mødrekvoteFørste6Ukene = uttaksperioder.get(1);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteFørste6Ukene.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode mødrekvoteEtterUke6 = uttaksperioder.get(2);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getTrekkdager()).isGreaterThan(0);
        assertThat(mødrekvoteEtterUke6.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        UttakResultatPeriode gradering = uttaksperioder.get(3);
        assertThat(gradering.getGraderingAvslagÅrsak().kode).isEqualTo("-");
        assertThat(gradering.getGraderingInnvilget()).isTrue();
        assertThat(gradering.getGradertArbeidsprosent()).isEqualTo(arbeidstidsprosent);
        assertThat(gradering.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        assertThat(gradering.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FELLESPERIODE);
        UttakResultatPeriodeAktivitet gradertAktivitet = finnAktivitetForArbeidsgiver(gradering, gradetArbeidsgiver);
        assertThat(gradertAktivitet.getTrekkdager()).isGreaterThan(0);
        assertThat(gradertAktivitet.getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100).subtract(arbeidstidsprosent));
        assertThat(gradertAktivitet.getProsentArbeid()).isEqualTo(arbeidstidsprosent);
        UttakResultatPeriode utsettelse = uttaksperioder.get(4);
        assertThat(utsettelse.getUtsettelseType().kode).isEqualTo(UTSETTELSETYPE_ARBEID);
        assertThat(utsettelse.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
        assertThat(utsettelse.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);
        assertThat(utsettelse.getAktiviteter().get(1).getUtbetalingsgrad()).isEqualTo(BigDecimal.ZERO);
        assertThat(utsettelse.getAktiviteter().get(1).getTrekkdager()).isEqualTo(0);
        assertThat(utsettelse.getAktiviteter().get(1).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE);

        verifiserLikhet(saksbehandler.valgtBehandling.hentBehandlingsresultat(), "Innvilget");
        verifiserLikhet(saksbehandler.getBehandlingsstatus(), "AVSLU");
        debugLoggHistorikkinnslag(saksbehandler.historikkInnslag);
        saksbehandler.ventTilHistorikkinnslag("Brev sendt");
    }

    //@Disabled("Disabler til bug fikset i fpsak") <- vi disabler vel ikke tester fordi de finner en bug
    @DisplayName("Mor søker fødsel alene - kun en har rett? todo bedre beskrivelse")
    @Test
    public void morSøkerFødselAleneomsorgKunEnHarRett() throws Exception {

        TestscenarioDto testscenario = opprettScenario("102");

        String søkerAktørIdent = testscenario.getPersonopplysninger().getSøkerAktørIdent();
        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakMorAleneomsorg(søkerAktørIdent, fødselsdato);
        fordel.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        LocalDate startdatoForeldrePenger = fødselsdato.minusWeeks(3);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startdatoForeldrePenger);

        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);
        debugLoggBehandling(saksbehandler.valgtBehandling);
        saksbehandler.erLoggetInnMedRolle(Aktoer.Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        saksbehandler.ventTilAvsluttetSak();

        //verifiserer uttak
        List<UttakResultatPeriode> uttaksperioder = saksbehandler.valgtBehandling.hentUttaksperioder();
        assertThat(uttaksperioder).hasSize(4);

        UttakResultatPeriode fpff = uttaksperioder.get(0);
        assertThat(fpff.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(fpff.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(fpff.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(fpff.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        UttakResultatPeriode foreldrepengerFørste6Ukene = uttaksperioder.get(1);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(foreldrepengerFørste6Ukene.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        UttakResultatPeriode foreldrepengerEtterUke6 = uttaksperioder.get(2);
        assertThat(foreldrepengerFørste6Ukene.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getTrekkdager()).isEqualTo(200);
        assertThat(foreldrepengerEtterUke6.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);
        //Periode søkt mer enn 49 uker er avslått automatisk
        UttakResultatPeriode periodeMerEnn49Uker = uttaksperioder.get(3);
        assertThat(periodeMerEnn49Uker.getPeriodeResultatType().kode).isEqualTo("AVSLÅTT");
        assertThat(periodeMerEnn49Uker.getPeriodeResultatÅrsak().kode).isEqualTo("4002");
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getUtbetalingsgrad()).isEqualTo(BigDecimal.valueOf(0));
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getTrekkdager()).isEqualTo(0);
        assertThat(periodeMerEnn49Uker.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER);

}
