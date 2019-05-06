package no.nav.foreldrepenger.autotest.domain.foreldrepenger;

import org.w3c.dom.Document;

public class InnvilgelseForeldrepenger000061 extends BrevMalXml{

    public InnvilgelseForeldrepenger000061(Document root) {
        super(root);
    }
    
    public static InnvilgelseForeldrepenger000061 fromString(String text) throws Exception {
        return fromString(text, InnvilgelseForeldrepenger000061.class);
    }
    
    public static InnvilgelseForeldrepenger000061 fromFile(String path) throws Exception {
        return fromFile(path, InnvilgelseForeldrepenger000061.class);
    }
    
    public static InnvilgelseForeldrepenger000061 fromResource(String path) throws Exception  {
        return fromResource(path, InnvilgelseForeldrepenger000061.class);
    }

    @Override
    public boolean isComparable(BrevMalXml otherXml) {
        boolean result = true;
        
        result = result && isComparableNode("fag behandlingsType", otherXml);
        result = result && isComparableNode("fag sokersNavn", otherXml);
        result = result && isComparableNode("fag personstatus", otherXml);
        result = result && isComparableNode("fag relasjonskode", otherXml);
        result = result && isComparableNode("fag behandlingsResultat", otherXml);
        result = result && isComparableNode("fag overbetaling", otherXml);
//        result = result && isComparableNode("fag mottattDato", otherXml);                  //Må valideres som true/false siden dato varierer
        result = result && isComparableNode("fag dekningsgrad", otherXml);
        result = result && isComparableNode("fag bruttoBeregningsgrunnlag", otherXml);
        result = result && isComparableNode("fag dagsats", otherXml);
        result = result && isComparableNode("fag månedsbeløp", otherXml);
//        result = result && isComparableNode("fag stønadsperiodeFom", otherXml);             //Må valideres som true/false siden dato varierer
//        result = result && isComparableNode("fag stønadsperiodeTom", otherXml);             //Må valideres som true/false siden dato varierer
        result = result && isComparableNode("fag totalBrukerAndel", otherXml);
        result = result && isComparableNode("fag totalArbeidsgiverAndel", otherXml);
        result = result && isComparableNode("fag antallArbeidsgivere", otherXml);
        result = result && isComparableNode("fag annenForelderHarRett", otherXml);
        result = result && isComparableNode("fag aleneomsorg", otherXml);
        result = result && isComparableNode("fag ikkeOmsorg", otherXml);
        result = result && isComparableNode("fag antallBarn", otherXml);
        result = result && isComparableNode("fag barnErFødt", otherXml);
        result = result && isComparableNode("fag fødselsHendelse", otherXml);
        result = result && isComparableNode("fag gjelderFoedsel", otherXml);
        result = result && isComparableNode("fag dagerTaptFørTermin", otherXml);
        result = result && isComparableNode("fag antallPerioder", otherXml);
        result = result && isComparableNode("fag antallInnvilget", otherXml);
        result = result && isComparableNode("fag antallAvslag", otherXml);
        result = result && isComparableNode("fag graderingFinnes", otherXml);
//        result = result && isComparableNode("fag sisteDagIFellesPeriode", otherXml);        //Må valideres som true/false siden dato varierer
//        result = result && isComparableNode("fag sisteUtbetalingsdag", otherXml);           //Må valideres som true/false siden dato varierer
//        result = result && isComparableNode("fag periodeListe", otherXml);                  //Periodeliste har ukjent antall perioder
        result = result && isComparableNode("fag disponibleDager", otherXml);
        result = result && isComparableNode("fag disponibleFellesDager", otherXml);
        result = result && isComparableNode("fag sisteDagAvSistePeriode", otherXml);
//        result = result && isComparableNode("fag seksG", otherXml);                         //Må valideres som true/false siden 6G vil endres fra år til år
        result = result && isComparableNode("fag inntektOverSeksG", otherXml);
        result = result && isComparableNode("fag klageFristUker", otherXml);
        result = result && isComparableNode("fag lovhjemmel vurdering", otherXml);
        result = result && isComparableNode("fag lovhjemmel beregning", otherXml);
        result = result && isComparableNode("fag konsekvensForYtelse", otherXml);
        result = result && isComparableNode("fag antallBeregningsgrunnlagRegeler", otherXml);
//        result = result && isComparableNode("fag beregningsgrunnlagRegelListe", otherXml);  //Regelliste har ukjent antall regler
        result = result && isComparableNode("fag forMyeUtbetalt", otherXml);
        result = result && isComparableNode("fag inntektMottattArbgiver", otherXml);
        return result;
    }
}
