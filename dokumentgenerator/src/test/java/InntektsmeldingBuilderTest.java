import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingErketype;
import org.junit.Assert;
import org.junit.Test;

public class InntektsmeldingBuilderTest{

    @Test
    public void inntektsmeldingGeneratorTest(){
        InntektsmeldingErketype inntektsmeldingErketype = new InntektsmeldingErketype();

        String xml =  inntektsmeldingErketype.standardInntektsmelding("123123", "13213123");

        Assert.assertTrue(xml.toLowerCase().contains("virksomhetsnummer"));
    }

}
