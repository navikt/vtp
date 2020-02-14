package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;

public class PdfGenerator implements AutoCloseable {

    private final ClassLoader classLoader = PdfGenerator.class.getClassLoader();
    private static final String FONT = "fonts/OpenSans-Bold.ttf";

    final PDDocument doc;
    final String[] pdfcontent;
    private PDFont font;
    private PDPageContentStream content = null;
    private int textRenderingLineY = 0;

    public PdfGenerator(PDDocument doc, String message) {
        this.pdfcontent = message.replaceAll("\t", "  ").split(System.getProperty("line.separator"));
        this.doc = doc;
        loadAndSetFont();
    }

    public void renderText(int marginwidth) throws IOException {
        for (String strTemp : pdfcontent) {
            renderText(strTemp, 60);
        }
    }

    private void renderText(String info, int marginwidth) throws IOException {

            if (content == null || textRenderingLineY < 20)
                newPage();

            textRenderingLineY-=10;

            content.beginText();
            content.setFont(font, 8);
            content.newLineAtOffset(marginwidth, textRenderingLineY);
            content.showText(info);
            content.endText();
    }

    private void loadAndSetFont() {
        try (InputStream fontInputStream = classLoader.getResourceAsStream(FONT);){
            font = PDType0Font.load(doc, fontInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void newPage() throws IOException {
        close();
        PDPage page = new PDPage();
        doc.addPage(page);
        content = new PDPageContentStream(doc, page);
        content.setNonStrokingColor(Color.BLACK);
        textRenderingLineY = 768;
    }

    @Override
    public void close() throws IOException {
        if (content != null)
        {
            content.close();
            content = null;
        }
    }
}
