package no.nav.foreldrepenger.autotest.foreldrepenger.foreldrepenger;

import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL;
import static no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import no.nav.foreldrepenger.autotest.aktoerer.Aktoer.Rolle;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriode;
import no.nav.foreldrepenger.autotest.klienter.fpsak.behandlinger.dto.behandling.uttak.UttakResultatPeriodeAktivitet;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("foreldrepenger")
public class Fodsel extends ForeldrepengerTestBase {

    @Test
    public void morSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        //verifiserer uttak
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder()
                .stream()
                .sorted(Comparator.comparing(UttakResultatPeriode::getFom))
                .collect(Collectors.toList());
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, 1);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, 1);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, 1);

        //verifiser historikkinnslag
        
        //Fatte vedtak
        
        //verifiser økonomi?
        
        //verifiser brev
    }

    @Test
    public void morSøkerFødselMedToArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("56");

        LocalDate fødselsdato = testscenario.getPersonopplysninger().getFødselsdato();

        LocalDate startDatoForeldrepenger = fødselsdato.minusWeeks(3);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        fordel.sendInnInntektsmeldinger(inntektsmeldinger, testscenario, saksnummer);

        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);

        //verifiserer uttak
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder()
                .stream()
                .sorted(Comparator.comparing(UttakResultatPeriode::getFom))
                .collect(Collectors.toList());
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL, 2);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE, 2);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE, 2);

        //verifiser historikkinnslag

        //Fatte vedtak

        //verifiser økonomi?

        //verifiser brev
    }

    private void verifiserUttaksperiode(UttakResultatPeriode uttakResultatPeriode, String stønadskontotype, int antallAktiviteter) {
        assertThat(uttakResultatPeriode.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(uttakResultatPeriode.getUtsettelseType().kode).isEqualTo("-");

        assertThat(uttakResultatPeriode.getAktiviteter()).hasSize(antallAktiviteter);
        for (UttakResultatPeriodeAktivitet aktivitet : uttakResultatPeriode.getAktiviteter()) {
            assertThat(aktivitet.getStønadskontoType().kode).isEqualTo(stønadskontotype);
            assertThat(aktivitet.getTrekkdager()).isGreaterThan(0);
            assertThat(aktivitet.getUtbetalingsgrad()).isGreaterThan(BigDecimal.ZERO);
        }
    }
}
