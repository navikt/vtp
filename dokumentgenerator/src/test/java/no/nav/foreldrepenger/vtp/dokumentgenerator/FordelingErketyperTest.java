package no.nav.foreldrepenger.vtp.dokumentgenerator;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.OverføringÅrsak;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.SøknadBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.ForeldrepengerYtelseBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.SoekersRelasjonErketyper;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.LukketPeriodeMedVedlegg;
import org.junit.Test;

import java.time.LocalDate;

import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.generiskFordeling;
import static org.assertj.core.api.Assertions.assertThatCode;

public class FordelingErketyperTest {
    @Test
    public void testAvoverføringsperiode() throws Exception {
        LukketPeriodeMedVedlegg testPeriode = FordelingErketyper.overføringsperiode(
                OverføringÅrsak.SYKDOM_ANNEN_FORELDER,
                FordelingErketyper.STØNADSKONTOTYPE_MØDREKVOTE,
                LocalDate.now().minusWeeks(1),
                LocalDate.now().plusWeeks(2));
        Fordeling fordeling = generiskFordeling(testPeriode);
        Foreldrepenger foreldrepenger = new ForeldrepengerYtelseBuilder(
                SoekersRelasjonErketyper.fødsel(1, LocalDate.now().minusMonths(1)),
                fordeling)
                .build();
        SøknadBuilder søknad = new SøknadBuilder(
                foreldrepenger, "123", SøkersRolle.MOR
        );
        assertThatCode(() -> søknad.build()).doesNotThrowAnyException();
    }
}
