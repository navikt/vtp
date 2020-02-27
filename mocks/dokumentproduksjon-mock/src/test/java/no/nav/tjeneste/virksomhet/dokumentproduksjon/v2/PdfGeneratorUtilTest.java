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
    private static final int fontSize = 8;
    private final ClassLoader classLoader = PdfGeneratorUtilTest.class.getClassLoader();
    PdfGeneratorUtil renderer;

    @BeforeAll
    public void setup(){
        PdfGeneratorUtil renderer = new PdfGeneratorUtil();
    }

    @Test
    public void PdfGeneratorXmlTest() {
        try (InputStream inputStream = classLoader.getResourceAsStream(INPUT_XML)){
            String inputString = readFromInputStream(inputStream);

            byte[] bytes = renderer.genererPdfByteArrayFraString(inputString);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void PdfGeneratorLongParagraphTest() {
        try (InputStream inputStream = classLoader.getResourceAsStream(INPUT_TEXT)){
            String inputString = readFromInputStream(inputStream);

            PDDocument doc = new PDDocument();
            PdfGeneratorUtil renderer = new PdfGeneratorUtil(doc, inputString, fontSize);
            renderer.renderText();
            renderer.close();
            int numberOfPages = doc.getNumberOfPages();
            doc.close();

            assertThat(numberOfPages).isGreaterThan(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
