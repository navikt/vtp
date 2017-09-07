package no.nav.tjeneste.virksomhet.inntekt.v3;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.AktoerId;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ArbeidsInntektIdent;
import no.nav.tjeneste.virksomhet.inntekt.v3.informasjon.inntekt.ObjectFactory;

public class InntektGeneratorTest {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String ID = "08088049846";

    @Test
    public void skal_hente_inntekter(){
        LocalDate fom = LocalDate.of(2017, 1, 1);
        LocalDate tom = LocalDate.of(2017, 12, 31);
        AktoerId aktoerId = objectFactory.createAktoerId();
        aktoerId.setAktoerId(ID);
        List<ArbeidsInntektIdent> inntekter = new InntektGenerator().hentInntekter(aktoerId, fom, tom);

        assertThat(inntekter).isNotEmpty();
    }
}
