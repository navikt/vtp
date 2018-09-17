import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.app.ForeldrepengesoknadXmlGenerator;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.SoeknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.dto.erketyper.ForeldrepengeSoknadDto;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.integrasjon.felles.ws.DateUtil;
import no.nav.vedtak.felles.xml.soeknad.v1.Soeknad;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import java.time.LocalDate;

public class ForeldrepengerBuilderTest {

    @Test
    public void foreldrepengerKontraktBuilderTest() throws DatatypeConfigurationException {
        ForeldrepengesoknadXmlErketyper fpx = new ForeldrepengesoknadXmlErketyper();

        Soeknad soeknad = fpx.termindatoUttakKunMor("123");

        String xml = ForeldrepengesoknadBuilder.tilXML(soeknad);
        String s = "";
    }
}
