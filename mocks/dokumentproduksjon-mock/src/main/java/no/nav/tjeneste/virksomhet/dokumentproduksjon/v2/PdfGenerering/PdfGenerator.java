package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PdfGenerator implements AutoCloseable {

    private final ClassLoader classLoader = PdfGenerator.class.getClassLoader();
    private static final String FONT = "fonts/OpenSans-Regular.ttf";

    final PDDocument doc;
    final String[] pdfcontent;
    private PDFont font;
    private PDPageContentStream content = null;
    private int textRenderingLineStartY = 0;
    private int textRenderingLineStartX = 0;
    private int textRenderingLineEndX;
    private int textRenderingLineEndY;
    private int textRenderingLineCurrentY;
    private final int fontSize;
    private int fontHeight;


    public PdfGenerator(PDDocument doc, String message, int fontSize) {
        this.pdfcontent = message.replaceAll("\t", "  ").split(System.getProperty("line.separator"));
        this.doc = doc;
        this.fontSize = fontSize;
        loadAndSetFont();
    }


    public void renderText() throws IOException {
        newPage();
        for (String strTemp : pdfcontent) {
            List<String> lines = autowrappingLongLines(strTemp);
            for (String line: lines) {
                renderLine(line);
            }
        }
    }


    private List<String> autowrappingLongLines(String text) throws IOException {
        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int indexOfNextSpace = text.indexOf(' ', lastSpace + 1);
            if (indexOfNextSpace < 0)
                indexOfNextSpace = text.length();
            String subString = text.substring(0, indexOfNextSpace);
            float sizeOfSubstring = fontSize * font.getStringWidth(subString) / 1000;
            if (sizeOfSubstring > textRenderingLineEndX) {
                if (lastSpace < 0)
                    lastSpace = indexOfNextSpace;
                subString = text.substring(0, lastSpace);
                lines.add(subString);
                text = text.substring(lastSpace).trim();
                lastSpace = -1;
            } else if (indexOfNextSpace == text.length()) {
                lines.add(text);
                text = "";
            } else {
                lastSpace = indexOfNextSpace;
            }
        }
        return lines;
    }


    private void renderLine(String info) throws IOException {
        if (content == null || textRenderingLineCurrentY < textRenderingLineEndY)
            newPage();
        textRenderingLineCurrentY -=fontHeight;
        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(textRenderingLineStartX, textRenderingLineCurrentY);
        content.showText(info);
        content.endText();
    }


    private void newPage() throws IOException {
        close();
        PDPage page = new PDPage();
        doc.addPage(page);
        content = new PDPageContentStream(doc, page);
        setPdfPageProperties(page);
    }


    private void setPdfPageProperties(PDPage page) throws IOException {
        content.setNonStrokingColor(Color.BLACK);
        PDRectangle mediabox = page.getMediaBox();
        int margin = 2*fontHeight;
        textRenderingLineStartY = (int) (mediabox.getUpperRightY()) - margin;
        textRenderingLineEndY = (int) (mediabox.getLowerLeftX()) + 2 * margin;
        textRenderingLineStartX = (int) (mediabox.getLowerLeftX()) + 2 * margin;
        textRenderingLineEndX = (int) (mediabox.getWidth()) - textRenderingLineStartX - 2 * margin;
        textRenderingLineCurrentY = textRenderingLineStartY;
    }


    private void loadAndSetFont() {
        try (InputStream fontInputStream = classLoader.getResourceAsStream(FONT)){
            this.font = PDType0Font.load(doc, fontInputStream);
            double tempFontHeightDouble = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
            this.fontHeight = (int) Math.round(tempFontHeightDouble);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
