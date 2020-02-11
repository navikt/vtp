import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DokumentGeneratingTest {

    private static final String OUTPUT_PDF = "statiskBrev.pdf";
    PDDocument dokument = new PDDocument();

    String textForTesting = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
            "<brevdata xmlns:ns2=\"http://nav.no/tjeneste/virksomhet/dokumentproduksjon/v2\">\n" +
            "\t<felles>\n" +
            "\t\t<spraakkode>NB</spraakkode>\n" +
            "\t\t<fagsaksnummer>130142901</fagsaksnummer>\n" +
            "\t\t<signerendeSaksbehandler>\n" +
            "\t\t\t<signerendeSaksbehandlerNavn>saksbeh</signerendeSaksbehandlerNavn>\n" +
            "\t\t</signerendeSaksbehandler>\n" +
            "\t\t<automatiskBehandlet>false</automatiskBehandlet>\n" +
            "\t\t<sakspart>\n" +
            "\t\t\t<sakspartId>13079443659</sakspartId>\n" +
            "\t\t\t<sakspartTypeKode>PERSON</sakspartTypeKode>\n" +
            "\t\t\t<sakspartNavn>HUND INGUNN</sakspartNavn>\n" +
            "\t\t</sakspart>\n" +
            "\t\t<signerendeBeslutter>\n" +
            "\t\t\t<signerendeBeslutterNavn>beslut</signerendeBeslutterNavn>\n" +
            "\t\t\t<geografiskEnhet>N/A</geografiskEnhet>\n" +
            "\t\t</signerendeBeslutter>\n" +
            "\t\t<mottaker>\n" +
            "\t\t\t<mottakerId>13079443659</mottakerId>\n" +
            "\t\t\t<mottakerTypeKode>PERSON</mottakerTypeKode>\n" +
            "\t\t\t<mottakerNavn>HUND INGUNN</mottakerNavn>\n" +
            "\t\t\t<mottakerAdresse>\n" +
            "\t\t\t\t<adresselinje1>Fjordlandet 10 B</adresselinje1>\n" +
            "\t\t\t\t<postNr>2500</postNr>\n" +
            "\t\t\t\t<poststed>TYNSET</poststed>\n" +
            "\t\t\t\t<land>NORGE</land>\n" +
            "\t\t\t</mottakerAdresse>\n" +
            "\t\t</mottaker>\n" +
            "\t\t<navnAvsenderEnhet>NAV Familie- og pensjonsytelser</navnAvsenderEnhet>\n" +
            "\t\t<kontaktInformasjon>\n" +
            "\t\t\t<kontaktTelefonnummer>55553333</kontaktTelefonnummer>\n" +
            "\t\t\t<returadresse>\n" +
            "\t\t\t\t<navEnhetsNavn>NAV Familie- og pensjonsytelser</navEnhetsNavn>\n" +
            "\t\t\t\t<adresselinje>Postboks 6600 Etterstad</adresselinje>\n" +
            "\t\t\t\t<postNr>0607</postNr>\n" +
            "\t\t\t\t<poststed>OSLO</poststed>\n" +
            "\t\t\t</returadresse>\n" +
            "\t\t\t<postadresse>\n" +
            "\t\t\t\t<navEnhetsNavn>NAV Familie- og pensjonsytelser</navEnhetsNavn>\n" +
            "\t\t\t\t<adresselinje>Postboks 6600 Etterstad</adresselinje>\n" +
            "\t\t\t\t<postNr>0607</postNr>\n" +
            "\t\t\t\t<poststed>OSLO</poststed>\n" +
            "\t\t\t</postadresse>\n" +
            "\t\t</kontaktInformasjon>\n" +
            "\t\t<dokumentDato>2020-01-30</dokumentDato>\n" +
            "\t</felles>\n" +
            "\t<fag>\n" +
            "\t\t<behandlingsType>FOERSTEGANGSBEHANDLING</behandlingsType>\n" +
            "\t\t<relasjonskode>MOR</relasjonskode>\n" +
            "\t\t<behandlingsResultat>INNVILGET</behandlingsResultat>\n" +
            "\t\t<mottattDato>2020-01-30</mottattDato>\n" +
            "\t\t<dekningsgrad>100</dekningsgrad>\n" +
            "\t\t<bruttoBeregningsgrunnlag>180000</bruttoBeregningsgrunnlag>\n" +
            "\t\t<dagsats>692</dagsats>\n" +
            "\t\t<månedsbeløp>14993</månedsbeløp>\n" +
            "\t\t<stønadsperiodeFom>2020-01-07</stønadsperiodeFom>\n" +
            "\t\t<stønadsperiodeTom>2020-08-31</stønadsperiodeTom>\n" +
            "\t\t<totalBrukerAndel>0</totalBrukerAndel>\n" +
            "\t\t<totalArbeidsgiverAndel>1</totalArbeidsgiverAndel>\n" +
            "\t\t<antallArbeidsgivere>1</antallArbeidsgivere>\n" +
            "\t\t<annenForelderHarRett>true</annenForelderHarRett>\n" +
            "\t\t<annenForelderHarRettVurdert>IKKE_VURDERT</annenForelderHarRettVurdert>\n" +
            "\t\t<aleneomsorg>IKKE_VURDERT</aleneomsorg>\n" +
            "\t\t<ikkeOmsorg>false</ikkeOmsorg>\n" +
            "\t\t<antallBarn>1</antallBarn>\n" +
            "\t\t<barnErFødt>true</barnErFødt>\n" +
            "\t\t<fødselsHendelse>false</fødselsHendelse>\n" +
            "\t\t<gjelderFoedsel>true</gjelderFoedsel>\n" +
            "\t\t<dagerTaptFørTermin>0</dagerTaptFørTermin>\n" +
            "\t\t<antallPerioder>1</antallPerioder>\n" +
            "\t\t<antallInnvilget>1</antallInnvilget>\n" +
            "\t\t<antallAvslag>0</antallAvslag>\n" +
            "\t\t<prematurDager>0</prematurDager>\n" +
            "\t\t<besteBeregning>false</besteBeregning>\n" +
            "\t\t<periodeListe>\n" +
            "\t\t\t<periode>\n" +
            "\t\t\t\t<innvilget>true</innvilget>\n" +
            "\t\t\t\t<årsak>2006</årsak>\n" +
            "\t\t\t\t<periodeFom>2020-01-07</periodeFom>\n" +
            "\t\t\t\t<periodeTom>2020-08-31</periodeTom>\n" +
            "\t\t\t\t<periodeDagsats>692</periodeDagsats>\n" +
            "\t\t\t\t<antallTapteDager>0</antallTapteDager>\n" +
            "\t\t\t\t<arbeidsforholdListe>\n" +
            "\t\t\t\t\t<arbeidsforhold>\n" +
            "\t\t\t\t\t\t<arbeidsgiverNavn>BEDRIFT AS</arbeidsgiverNavn>\n" +
            "\t\t\t\t\t\t<aktivitetDagsats>692</aktivitetDagsats>\n" +
            "\t\t\t\t\t\t<gradering>false</gradering>\n" +
            "\t\t\t\t\t\t<uttaksgrad>100</uttaksgrad>\n" +
            "\t\t\t\t\t\t<prosentArbeid>0</prosentArbeid>\n" +
            "\t\t\t\t\t\t<stillingsprosent>100</stillingsprosent>\n" +
            "\t\t\t\t\t\t<utbetalingsgrad>100</utbetalingsgrad>\n" +
            "\t\t\t\t\t</arbeidsforhold>\n" +
            "\t\t\t\t</arbeidsforholdListe>\n" +
            "\t\t\t</periode>\n" +
            "\t\t</periodeListe>\n" +
            "\t\t<disponibleDager>0</disponibleDager>\n" +
            "\t\t<disponibleFellesDager>0</disponibleFellesDager>\n" +
            "\t\t<sisteDagAvSistePeriode>2020-08-31</sisteDagAvSistePeriode>\n" +
            "\t\t<seksG>599148</seksG>\n" +
            "\t\t<inntektOverSeksG>false</inntektOverSeksG>\n" +
            "\t\t<klageFristUker>6</klageFristUker>\n" +
            "\t\t<lovhjemmel>\n" +
            "\t\t\t<vurdering>§§ 14-9, 14-10 og 14-12</vurdering>\n" +
            "\t\t\t<beregning>§§ 14-7 og 8-30</beregning>\n" +
            "\t\t</lovhjemmel>\n" +
            "\t\t<konsekvensForYtelse>INGEN_ENDRING</konsekvensForYtelse>\n" +
            "\t\t<antallBeregningsgrunnlagRegeler>1</antallBeregningsgrunnlagRegeler>\n" +
            "\t\t<beregningsgrunnlagRegelListe>\n" +
            "\t\t\t<beregningsgrunnlagRegel>\n" +
            "\t\t\t\t<regelStatus>ARBEIDSTAKER</regelStatus>\n" +
            "\t\t\t\t<antallArbeidsgivereIBeregningUtenEtterlønnSluttpakke>1</antallArbeidsgivereIBeregningUtenEtterlønnSluttpakke>\n" +
            "\t\t\t\t<SNNyoppstartet>false</SNNyoppstartet>\n" +
            "\t\t\t\t<andelListe>\n" +
            "\t\t\t\t\t<andel>\n" +
            "\t\t\t\t\t\t<status>ARBEIDSTAKER</status>\n" +
            "\t\t\t\t\t\t<arbeidsgiverNavn>BEDRIFT AS</arbeidsgiverNavn>\n" +
            "\t\t\t\t\t\t<dagsats>692</dagsats>\n" +
            "\t\t\t\t\t\t<månedsinntekt>15000</månedsinntekt>\n" +
            "\t\t\t\t\t\t<årsinntekt>180000</årsinntekt>\n" +
            "\t\t\t\t\t\t<etterlønnSluttpakke>false</etterlønnSluttpakke>\n" +
            "\t\t\t\t\t</andel>\n" +
            "\t\t\t\t</andelListe>\n" +
            "\t\t\t</beregningsgrunnlagRegel>\n" +
            "\t\t</beregningsgrunnlagRegelListe>\n" +
            "\t\t<forMyeUtbetalt>INGEN</forMyeUtbetalt>\n" +
            "\t\t<inntektMottattArbgiver>false</inntektMottattArbgiver>\n" +
            "\t</fag>\n" +
            "</brevdata>";


//    @Test
//    public void testNewLibrary() throws IOException {
//        String filename = OUTPUT_PDF;
//
//
//        Document document = new Document();
//        PdfWriter.getInstance(document, new FileOutputStream(filename));
//        document.open();
//
//        BaseFont courier = BaseFont.createFont();
//        com.lowagie.text.Font font = new com.lowagie.text.Font(courier, 7);
//        document.add(new Paragraph(textForTesting, font));
//
//        document.close();
//    }





    @Test
    public void PdfRenderingTestTest() throws IOException {
        PDDocument doc = new PDDocument();
        PdfGenerator renderer = new PdfGenerator(doc, textForTesting);
        renderer.renderText(60);
        renderer.close();
        doc.save(new FileOutputStream(OUTPUT_PDF));
        doc.close();

        byte[] bytes = pdfToByte(OUTPUT_PDF);

    }



    private byte[] pdfToByte(String filePath) {
        try {
            Path pdfPath = Paths.get(filePath);
            byte[] pdf = Files.readAllBytes(pdfPath);
            return pdf;
        } catch (IOException e) {
            String message = "Noe gikk galt med når pdfen skulle konverters til byte array: " + e.getMessage();
            System.out.println(message);
            return null;
        }
    }

}
