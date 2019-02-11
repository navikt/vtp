package no.nav.foreldrepenger.autotest.spberegning;

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
import no.nav.foreldrepenger.autotest.aktoerer.fordel.Fordel;
import no.nav.foreldrepenger.autotest.aktoerer.spberegning.Saksbehandler;
import no.nav.foreldrepenger.autotest.klienter.vtp.testscenario.TestscenarioKlient;
import no.nav.foreldrepenger.autotest.util.http.HttpSession;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingBuilder;
import no.nav.foreldrepenger.fpmock2.dokumentgenerator.inntektsmelding.erketyper.InntektsmeldingErketype;
import no.nav.foreldrepenger.fpmock2.server.api.scenario.TestscenarioDto;
import no.nav.foreldrepenger.fpmock2.testmodell.inntektytelse.inntektkomponent.Inntektsperiode;
import no.nav.inntektsmelding.xml.kodeliste._20180702.YtelseKodeliste;
import no.nav.inntektsmelding.xml.kodeliste._20180702.ÅrsakInnsendingKodeliste;

public class SpberegningTestBase extends TestBase{

    /*
     * Aktører
     */
    protected Saksbehandler saksbehandler;
    protected Fordel fordel;
    /*
     * VTP
     */
    protected TestscenarioKlient testscenarioKlient;
    protected InntektsmeldingErketype inntektsmeldingErketype;

    @BeforeEach
    void setUp() throws Exception{
        saksbehandler = new Saksbehandler();
        fordel = new Fordel();

        testscenarioKlient = new TestscenarioKlient(new HttpSession());
        inntektsmeldingErketype = new InntektsmeldingErketype();
    }

    protected TestscenarioDto opprettScenario(String id) throws IOException {
        return testscenarioKlient.opprettTestscenario(id);
    }

    protected String opprettSak(TestscenarioDto testscenarioDto, String fagområde) throws IOException{
        return fordel.opprettSak(testscenarioDto, fagområde);
    }

    protected List<InntektsmeldingBuilder> makeInntektsmeldingFromTestscenario(TestscenarioDto testscenario, LocalDate startDatoForeldrepenger) {
        return makeInntektsmeldingFromTestscenario(testscenario, YtelseKodeliste.FORELDREPENGER, startDatoForeldrepenger);
    }
    
    protected List<InntektsmeldingBuilder> makeInntektsmeldingFromTestscenario(TestscenarioDto testscenario, YtelseKodeliste ytelse, LocalDate startDatoForeldrepenger) {
        return makeInntektsmeldingFromTestscenario(testscenario, "", ytelse, startDatoForeldrepenger);
    }
    
    protected List<InntektsmeldingBuilder> makeInntektsmeldingFromTestscenario(TestscenarioDto testscenario, String arbeidsforholdId, YtelseKodeliste ytelse, LocalDate startDatoForeldrepenger) {

        List<InntektsmeldingBuilder> inntektsmeldinger = new ArrayList<>();
        String fnr = testscenario.getPersonopplysninger().getSøkerIdent();

        for (Inntektsperiode periode : testscenario.getScenariodata().getInntektskomponentModell().getInntektsperioder()) {
            String orgnummer = periode.getOrgnr();
            Integer belop = periode.getBeløp();

            inntektsmeldinger.add(inntektsmeldingGrunnlag(belop, fnr, orgnummer, arbeidsforholdId, ytelse, ÅrsakInnsendingKodeliste.NY));
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
    
    protected InntektsmeldingBuilder inntektsmeldingGrunnlag(Integer beløp, String fnr, String orgnummer, String arbeidsforholdId, YtelseKodeliste ytelse, ÅrsakInnsendingKodeliste årsak) {
        InntektsmeldingBuilder builder = new InntektsmeldingBuilder(UUID.randomUUID().toString().substring(0, 7),
                ytelse,
                årsak,
                fnr);
        builder.setArbeidsgiver(InntektsmeldingBuilder.createArbeidsgiver(orgnummer, "41925090"));
        builder.setAvsendersystem(InntektsmeldingBuilder.createAvsendersystem("FS22","1.0"));
        builder.setArbeidsforhold(InntektsmeldingBuilder.createArbeidsforhold(
                arbeidsforholdId,
                null,
                new BigDecimal(beløp),
                new ArrayList<>(),          //UtsettelseAvForeldrepenger
                new ArrayList<>(),          //GraderingIForeldrepenger
                new ArrayList<>()));        //avtaltFerieListe

        return builder;
    }

//    protected InntektsmeldingBuilder lagInntektsmeldingBuilderFraInntektsperiode(Integer beløp, String fnr, String orgnummer, LocalDate startDatoForeldrepenger) {
//        return inntektsmeldingGrunnlag(beløp, fnr, orgnummer, "", YtelseKodeliste.FORELDREPENGER, ÅrsakInnsendingKodeliste.NY);
//    }

}
