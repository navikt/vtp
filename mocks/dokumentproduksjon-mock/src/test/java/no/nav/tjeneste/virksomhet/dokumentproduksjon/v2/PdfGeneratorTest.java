package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PdfGeneratorTest {

    private static final String INPUT_XML = "brevxml.txt";
    private final ClassLoader classLoader = PdfGeneratorTest.class.getClassLoader();

    @Test
    public void PdfGeneratorTest() {

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
