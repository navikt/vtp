package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2;

import no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering.PdfGeneratorUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.assertj.core.api.Assertions.assertThat;

public class PdfGeneratorUtilTest {

    private static final String INPUT_XML = "brevxml.txt";
    private static final String INPUT_TEXT = "paragraph.txt";
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
            byte[] byteArray = renderer.genererPdfByteArrayFraString(inputString);
            var doc = PDDocument.load(byteArray);
            int numberOfPages = doc.getNumberOfPages();
            assertThat(numberOfPages).isEqualTo(2);
        }
    }

    @Test
    public void PdfGeneratorLongParagraphTest() throws Exception {
        try (var inputStream = classLoader.getResourceAsStream(INPUT_TEXT)) {
            String inputString = hentStringFraInputStream(inputStream);
            byte[] byteArray = renderer.genererPdfByteArrayFraString(inputString);
            var doc = PDDocument.load(byteArray);
            int numberOfPages = doc.getNumberOfPages();
            assertThat(numberOfPages).isGreaterThan(1);
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
