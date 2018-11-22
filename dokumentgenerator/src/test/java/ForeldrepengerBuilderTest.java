import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;

public class ForeldrepengerBuilderTest {

    @Test
    public void foreldrepengerKontraktBuilderTest() throws DatatypeConfigurationException {
        ForeldrepengesoknadXmlErketyper fpx = new ForeldrepengesoknadXmlErketyper();

        Soeknad soeknad = fpx.termindatoUttakKunMor("123", LocalDate.now()).build();

        String xml = ForeldrepengesoknadBuilder.tilXML(soeknad);

        String s = "";
    }
}
