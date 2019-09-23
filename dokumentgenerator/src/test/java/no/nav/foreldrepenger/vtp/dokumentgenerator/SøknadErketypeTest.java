package no.nav.foreldrepenger.vtp.dokumentgenerator;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.SøkersRolle;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.builders.SøknadBuilder;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.FordelingErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.SøknadErketyper;
import no.nav.vedtak.felles.xml.soeknad.uttak.v3.Fordeling;
import no.nav.vedtak.felles.xml.soeknad.v3.Soeknad;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatCode;

public class SøknadErketypeTest {

    @Test
    public void foreldrepengerKontraktBuilderTest() throws Exception {
        SøknadErketyper fpx = new SøknadErketyper();

        Soeknad soeknad = fpx.foreldrepengesøknadTerminErketype("123", SøkersRolle.MOR, 1, LocalDate.now()).build();
        String xml = SøknadBuilder.tilXML(soeknad);
        String s = "";
    }

    @Test
    public void endringssøknadKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        Fordeling fordeling = FordelingErketyper.fordelingHappyCase(LocalDate.now().minusWeeks(3),SøkersRolle.FAR);
        SøknadBuilder søknad = SøknadErketyper.endringssøknadErketype(
                "123123", SøkersRolle.FAR,
                fordeling, "123123");

        assertThatCode(() -> søknad.build()).doesNotThrowAnyException();

    }
    @Test
    public void foreldrepengerTerminKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        SøknadBuilder søknad = SøknadErketyper.foreldrepengesøknadTerminErketype(
                "123123", SøkersRolle.MOR,
                1, LocalDate.now().plusWeeks(3));

        assertThatCode(() -> søknad.build()).doesNotThrowAnyException();
    }
    @Test
    public void foreldrepengerFødselKasterIngenExceptionHvisIkkeAlleNødvendigeFelterErSatt() {
        SøknadBuilder søknad = SøknadErketyper.foreldrepengesøknadFødselErketype(
                "123123", SøkersRolle.MEDMOR,
                1, LocalDate.now().plusWeeks(3));

        assertThatCode(() -> søknad.build()).doesNotThrowAnyException();

    }
}
