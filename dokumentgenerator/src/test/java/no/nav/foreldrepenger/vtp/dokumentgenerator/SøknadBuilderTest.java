package no.nav.foreldrepenger.vtp.dokumentgenerator;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.SøknadBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.EngangstønadYtelseBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.ForeldrepengerYtelseBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.ytelse.SvangerskapspengerYtelseBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.ArbeidsforholdErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.MedlemskapErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.TilretteleggingsErketyper;
import no.nav.vedtak.felles.xml.soeknad.engangsstoenad.v3.Engangsstønad;
import no.nav.vedtak.felles.xml.soeknad.foreldrepenger.v3.Foreldrepenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Svangerskapspenger;
import no.nav.vedtak.felles.xml.soeknad.svangerskapspenger.v1.Tilrettelegging;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;

import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper.fordelingHappyCase;
import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.SoekersRelasjonErketyper.fødsel;
import static no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.SoekersRelasjonErketyper.termin;
import static org.assertj.core.api.Assertions.assertThatCode;

public class SøknadBuilderTest {

    @Test
    public void foreldrepengersøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        LocalDate terminDato = LocalDate.now().plusWeeks(3);
        Foreldrepenger foreldrepenger = new ForeldrepengerYtelseBuilder(
                termin(1, terminDato),
                fordelingHappyCase(terminDato,SøkersRolle.MOR))
                .build();
        SøknadBuilder soeknad = new SøknadBuilder(foreldrepenger, "123", SøkersRolle.MOR);

        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();
    }
    @Test
    public void svangerskappengersøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Tilrettelegging tilrettelegging = TilretteleggingsErketyper.ingenTilrettelegging(
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                ArbeidsforholdErketyper.virksomhet("11111111"));
        Svangerskapspenger svangerskapspenger = new SvangerskapspengerYtelseBuilder(
                    LocalDate.now().plusWeeks(4),
                    MedlemskapErketyper.medlemskapNorge(),
                    Collections.singletonList(tilrettelegging)
                    )
                .build();

        SøknadBuilder soeknad = new SøknadBuilder(svangerskapspenger, "123", SøkersRolle.MOR);

        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();

    }



    @Test
    public void engangsstønadsøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        LocalDate fødselsDato = LocalDate.now().minusMonths(1);

        Engangsstønad engangsstønad = new EngangstønadYtelseBuilder(fødsel(1, fødselsDato))
                .build();
        SøknadBuilder soeknad = new SøknadBuilder(engangsstønad, "123112312312", SøkersRolle.MEDMOR);

        assertThatCode(() -> soeknad.build()).doesNotThrowAnyException();

    }

}
