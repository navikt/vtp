package no.nav.foreldrepenger.vtp.dokumentgenerator;

import java.time.LocalDate;

import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.Test;

import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.vtp.dokumentgenerator.foreldrepengesoknad.soeknad.ForeldrepengesoknadBuilder;
import no.nav.vedtak.felles.xml.soeknad.v3.Soeknad;

public class ForeldrepengerBuilderTest {

    @Test
    public void foreldrepengerKontraktBuilderTest() throws DatatypeConfigurationException {
        ForeldrepengesoknadXmlErketyper fpx = new ForeldrepengesoknadXmlErketyper();

        Soeknad soeknad = fpx.termindatoUttakKunMor("123", LocalDate.now()).build();

        String xml = ForeldrepengesoknadBuilder.tilXML(soeknad);

        String s = "";
    }
}
