package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGeneratorUtil;

public class PdfGeneratorUtilTest {

    private static final String INPUT_XML = "brevxml.txt";
    private static final String INPUT_TEXT = "paragraph.txt";
    private static final String OUTPUT_PDF = "statiskBrev.pdf";
    private static PdfGeneratorUtil renderer;
    private final ClassLoader classLoader = PdfGeneratorUtilTest.class.getClassLoader();

    @BeforeAll
    public static void setup() {
        renderer = new PdfGeneratorUtil();
    }

    @Test
    public void PdfGeneratorXmlTest() throws Exception {
        try (var inputStream = classLoader.getResourceAsStream(INPUT_XML)) {
            String inputString = hentStringFraInputStream(inputStream);
            renderer.genererPdfByteArrayFraString(inputString);
            try (var fis = new FileInputStream(OUTPUT_PDF); var doc = PDDocument.load(fis)) {
                int numberOfPages = doc.getNumberOfPages();
                assertThat(numberOfPages).isEqualTo(2);
            } catch (IOException e) {
                throw new IllegalStateException("Kunne ikke laste inn " + OUTPUT_PDF, e);
            }
        }
    }

    @Test
    public void PdfGeneratorLongParagraphTest() {
        try (var inputStream = classLoader.getResourceAsStream(INPUT_TEXT)) {
            String inputString = hentStringFraInputStream(inputStream);
            renderer.genererPdfByteArrayFraString(inputString);
            try (var fis = new FileInputStream(OUTPUT_PDF); var doc = PDDocument.load(fis)) {
                int numberOfPages = doc.getNumberOfPages();
                assertThat(numberOfPages).isGreaterThan(1);
            } catch (IOException e) {
                throw new IllegalStateException("Kunne ikke laste inn " + OUTPUT_PDF, e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String hentStringFraInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append(System.getProperty("line.separator"));
            }
        }
        return resultStringBuilder.toString();
    }
}
