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
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.dokument.modell.koder.DokumenttypeId;

@Tag("foreldrepenger")
public class Fodsel extends ForeldrepengerTestBase{

    @Test
    public void morSøkerFødselMedEttArbeidsforhold() throws Exception {
        TestscenarioDto testscenario = opprettScenario("50");

        LocalDate startDatoForeldrepenger = LocalDate.now().minusDays(2).minusWeeks(3);
        List<InntektsmeldingBuilder> inntektsmeldinger = makeInntektsmeldingFromTestscenario(testscenario, startDatoForeldrepenger);
        ForeldrepengesoknadBuilder søknad = foreldrepengeSøknadErketyper.fodselfunnetstedUttakKunMor(testscenario.getPersonopplysninger().getSøkerAktørIdent(), startDatoForeldrepenger);

        fordel.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        long saksnummer = fordel.sendInnSøknad(søknad.build(), testscenario, DokumenttypeId.FOEDSELSSOKNAD_FORELDREPENGER);
        for (InntektsmeldingBuilder builder : inntektsmeldinger) {
            fordel.sendInnInntektsmelding(builder, testscenario, saksnummer);
        }
        
        saksbehandler.erLoggetInnMedRolle(Rolle.SAKSBEHANDLER);
        saksbehandler.hentFagsak(saksnummer);
        verifiserUttak();
        
        //verifiser historikkinnslag
        
        //Fatte vedtak
        
        //verifiser økonomi?
        
        //verifiser brev
    }

    private void verifiserUttak() {
        List<UttakResultatPeriode> perioder = saksbehandler.valgtBehandling.hentUttaksperioder()
                .stream()
                .sorted(Comparator.comparing(UttakResultatPeriode::getFom))
                .collect(Collectors.toList());
        assertThat(perioder).hasSize(3);
        verifiserUttaksperiode(perioder.get(0), STØNADSKONTOTYPE_FORELDREPENGER_FØR_FØDSEL);
        verifiserUttaksperiode(perioder.get(1), STØNADSKONTOTYPE_MØDREKVOTE);
        verifiserUttaksperiode(perioder.get(2), STØNADSKONTOTYPE_MØDREKVOTE);
    }

    private void verifiserUttaksperiode(UttakResultatPeriode uttakResultatPeriode, String stønadskontotype) {
        assertThat(uttakResultatPeriode.getAktiviteter()).hasSize(1);
        assertThat(uttakResultatPeriode.getPeriodeResultatType().kode).isEqualTo("INNVILGET");
        assertThat(uttakResultatPeriode.getUtsettelseType().kode).isEqualTo("-");
        assertThat(uttakResultatPeriode.getAktiviteter().get(0).getStønadskontoType().kode).isEqualTo(stønadskontotype);
        assertThat(uttakResultatPeriode.getAktiviteter().get(0).getTrekkdager()).isGreaterThan(0);
        assertThat(uttakResultatPeriode.getAktiviteter().get(0).getUtbetalingsgrad()).isGreaterThan(BigDecimal.ZERO);
    }
}
