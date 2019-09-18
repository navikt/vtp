package no.nav.foreldrepenger.vtp.dokumentgenerator;

import java.time.LocalDate;
import java.util.Collections;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.*;
import no.nav.vedtak.felles.xml.soeknad.endringssoeknad.v3.Endringssoeknad;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.AnnenForelder;
import no.nav.vedtak.felles.xml.soeknad.felles.v3.UkjentForelder;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;
import org.junit.Test;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;

import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.foreldrepengeYtelseNorskBorgerINorgeTerminMor;
import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengeYtelseErketyper.standardDekningsgrader;
import static org.assertj.core.api.Assertions.*;

public class ForeldrepengesoknadBuilderTest {

    @Test
    public void foreldrepengersøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(LocalDate.now().plusWeeks(3)));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorHappyCase(LocalDate.now().plusWeeks(3))
        );

        ForeldrepengesoknadBuilder soeknad = new ForeldrepengesoknadBuilder()
                .withSoeker("123", "MOR")
                .withForeldrepengerYtelse(foreldrepenger);
        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();
    }
    @Test
    public void foreldrepengersøknadKasterExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Foreldrepenger foreldrepenger = new Foreldrepenger();
        foreldrepenger.setDekningsgrad(standardDekningsgrader());
        foreldrepenger.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        foreldrepenger.setRettigheter(RettigheterErketyper.beggeForeldreRettIkkeAleneomsorg());
        foreldrepenger.setRelasjonTilBarnet(SoekersRelasjonErketyper.søkerTermin(LocalDate.now().plusWeeks(3)));
        foreldrepenger.setFordeling(FordelingErketyper.fordelingMorHappyCase(LocalDate.now().plusWeeks(3))
        );
        ForeldrepengesoknadBuilder soeknad = new ForeldrepengesoknadBuilder()
                .withForeldrepengerYtelse(foreldrepenger);
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> soeknad.build());
    }

    @Test
    public void svangerskappengersøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Tilrettelegging tilrettelegging = TilretteleggingsErketyper.ingenTilrettelegging(
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                ArbeidsforholdErketyper.virksomhet("11111111"));
        Svangerskapspenger svangerskapspenger = SvangerskapspengerYtelseErketyper.svangerskapspenger(
                LocalDate.now().plusWeeks(4),
                MedlemskapErketyper.medlemskapNorge(),
                Collections.singletonList(tilrettelegging));

        ForeldrepengesoknadBuilder soeknad = new ForeldrepengesoknadBuilder()
                .withSoeker("123", "MOR")
                .withSvangerskapspengeYtelse(svangerskapspenger);
        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();

    }

    @Test
    public void endringssøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Endringssoeknad endringssoeknad = new Endringssoeknad();
        endringssoeknad.setSaksnummer("123123");
        endringssoeknad.setFordeling(FordelingErketyper.fordelingFarHappyCase(LocalDate.now().minusWeeks(3)));

        ForeldrepengesoknadBuilder soeknad = new ForeldrepengesoknadBuilder()
                .withSoeker("123123", "FAR")
                .withEndringssoeknadYtelse(endringssoeknad);
        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();

    }

    @Test
    public void engangsstønadsøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Engangsstønad engangsstønad = new Engangsstønad();
        AnnenForelder annenForelder = new UkjentForelder();
        engangsstønad.setAnnenForelder(annenForelder);
        engangsstønad.setMedlemskap(MedlemskapErketyper.medlemskapNorge());
        engangsstønad.setSoekersRelasjonTilBarnet(SoekersRelasjonErketyper.søkerFødselEtterFødsel());

        ForeldrepengesoknadBuilder soeknad = new ForeldrepengesoknadBuilder()
                .withSoeker("123112312312", "MEDMOR")
                .withEngangsstoenadYtelse(engangsstønad);
        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();

    }

}
