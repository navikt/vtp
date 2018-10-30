package no.nav.foreldrepenger.autotest.foreldrepenger;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;

import no.nav.foreldrepenger.autotest.TestBase;
import no.nav.foreldrepenger.autotest.TestScenarioTestBase;
import no.nav.foreldrepenger.autotest.aktoerer.fordel.Fordel;
import no.nav.foreldrepenger.autotest.aktoerer.foreldrepenger.Saksbehandler;
import no.nav.foreldrepenger.autotest.klienter.fpsak.kodeverk.dto.Kodeverk;
import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.foreldrepengesoknad.erketyper.ForeldrepengesoknadXmlErketyper;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingErketype;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;

public class FpsakTestBase extends TestScenarioTestBase{

    /*
     * Aktører
     */
    protected Fordel fordel;
    protected Saksbehandler saksbehandler;
    protected Saksbehandler overstyrer;
    protected Saksbehandler beslutter;
    protected Saksbehandler klagebehandler;

    /*
     * VTP
     */
    protected ForeldrepengesoknadXmlErketyper foreldrepengeSøknadErketyper;
    protected InntektsmeldingErketype inntektsmeldingErketype;


    @BeforeEach
    void setUp() throws Exception{
        fordel = new Fordel();
        saksbehandler = new Saksbehandler();
        overstyrer = new Saksbehandler();
        beslutter = new Saksbehandler();
        klagebehandler = new Saksbehandler();
        
        foreldrepengeSøknadErketyper = new ForeldrepengesoknadXmlErketyper();
        inntektsmeldingErketype = new InntektsmeldingErketype();
    }

    protected Kodeverk hentKodeverk() {
        if(saksbehandler != null && saksbehandler.kodeverk != null) {
            return saksbehandler.kodeverk;
        }
        return null;
    }

    protected List<InntektsmeldingBuilder> makeInntektsmeldingFromTestscenario(TestscenarioDto testscenario, LocalDate startDatoForeldrepenger) {

        List<InntektsmeldingBuilder> inntektsmeldinger = new ArrayList<>();
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        for (Inntektsperiode periode : testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioder()) {
            String orgnummer = periode.getOrgnr();
            Integer belop = periode.getBeløp();

            inntektsmeldinger.add(lagInntektsmeldingBuilderFraInntektsperiode(belop, fnr, orgnummer, startDatoForeldrepenger));
        }

        return inntektsmeldinger;
    }

    private List<Inntektsperiode> hentGjeldendeInntektsperioder(List<Inntektsperiode> perioder){
        Map<String, Inntektsperiode> periodeMap = new HashMap<>();

        for (Inntektsperiode periode : perioder) {

            if(periodeMap.get(periode.getOrgnr()) == null || periode.getTom().isAfter(periodeMap.get(periode.getOrgnr()).getTom())) {
                periodeMap.put(periode.getOrgnr(), periode);
            }
        }

        return new ArrayList<>(periodeMap.values());
    }

    protected InntektsmeldingBuilder lagInntektsmeldingBuilderFraInntektsperiode(Integer beløp, String fnr, String orgnummer, LocalDate startDatoForeldrepenger) {
        InntektsmeldingBuilder builder = new InntektsmeldingBuilder(UUID.randomUUID().toString().substring(0, 7),
                YtelseKodeliste.FORELDREPENGER,
                ÅrsakInnsendingKodeliste.NY.NY,
                fnr,
                startDatoForeldrepenger);
        builder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(orgnummer, "41925090"));
        builder.setAvsendersystem(InntektsmeldingBuilder.createAvsendersystem("FS22","1.0"));
        builder.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                "", //TODO arbeidsforhold id
                null,
                new BigDecimal(beløp),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()));

        return builder;
    }

}
