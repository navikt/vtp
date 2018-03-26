package no.nav.tjeneste.virksomhet.arbeidsforhold.v3;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.Arbeidsforhold;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.NorskIdent;
import no.nav.tjeneste.virksomhet.arbeidsforhold.v3.informasjon.arbeidsforhold.ObjectFactory;

public class ArbeidsforholdGeneratorTest {

    private ObjectFactory objectFactory = new ObjectFactory();
    private static final String ID = "03108940181";

    @Test
    public void skal_hente_arbeidsforhold(){
        LocalDate fom = LocalDate.of(2017, 1, 1);
        LocalDate tom = LocalDate.of(2017, 12, 31);
        NorskIdent ident = objectFactory.createNorskIdent();
        ident.setIdent(ID);
        List<Arbeidsforhold> arbeidsforhold = new ArbeidsforholdGenerator().hentArbeidsforhold(ident, fom, tom);

        assertThat(arbeidsforhold).isNotEmpty();
    }
}
