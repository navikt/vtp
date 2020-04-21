package no.nav.tjeneste.virksomhet.dokumentproduksjon.v2.PdfGenerering;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PdfGeneratorUtil {

    private final ClassLoader classLoader = PdfGeneratorUtil.class.getClassLoader();
    private static final String FONT_FILE = "fonts/OpenSans-Regular.ttf";
    private static final String OUTPUT_PDF = "statiskBrev.pdf";
    private static final int FONT_SIZE = 8;

    private PDFont font;
    private PDDocument doc = null;
    private PDPageContentStream content = null;
    private int textRenderingLineStartY;
    private int textRenderingLineStartX;
    private int textRenderingLineEndX;
    private int textRenderingLineEndY;
    private int textRenderingLineCurrentY;
    private int fontHeight;

    public PdfGeneratorUtil() {
        loadAndSetFont();
    }


    public byte[] genererPdfByteArrayFraString(String brev) {
        try {
            if (doc == null) {
                loadAndSetFont();
            }
            String[] pdfcontent = brev.replaceAll("\t", "  ").split(System.getProperty("line.separator"));
            renderText(pdfcontent);
            saveAndClosePdf();
            Path pdfPath = Paths.get(OUTPUT_PDF);
            return Files.readAllBytes(pdfPath);
        } catch (IOException e) {
            throw new IllegalStateException("Kunne ikke generere PDF", e);
        }
    }


    private void renderText(String[] pdfcontent) throws IOException {
        newPage();
        for (String strTemp : pdfcontent) {
            List<String> lines = autowrappingLongLines(strTemp);
            int numberOfLines = lines.size();
            for (int i = 0; i < numberOfLines; i++) {
                String line = lines.get(i);
                setCharacterSpacingForLinje(line, i == (numberOfLines - 1));
                renderLine(line);
            }
        }
        close();
    }


    private List<String> autowrappingLongLines(String text) throws IOException {
        List<String> lines = new ArrayList<>();
        int lastSpace = -1;
        while (text.length() > 0) {
            int indexOfNextSpace = text.indexOf(' ', lastSpace + 1);
            if (indexOfNextSpace < 0)
                indexOfNextSpace = text.length();
            String subString = text.substring(0, indexOfNextSpace);
            float sizeOfSubstring = FONT_SIZE * font.getStringWidth(subString) / 1000;
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
        textRenderingLineCurrentY -= fontHeight;
        content.beginText();
        content.setFont(font, FONT_SIZE);
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

    private void setCharacterSpacingForLinje(String linje, boolean setCharSpacingToZero) throws IOException {
        float charSpacing = 0;
        if (linje.length() > 0 && !setCharSpacingToZero) {
            float breddeTilLinje = FONT_SIZE * font.getStringWidth(linje) / 1000;
            float plassTilOvers = textRenderingLineEndX - breddeTilLinje;
            if (plassTilOvers > 0) {
                charSpacing = plassTilOvers / (linje.length() - 1);
            }
        }
        content.setCharacterSpacing(charSpacing);
    }


    private void setPdfPageProperties(PDPage page) throws IOException {
        content.setNonStrokingColor(Color.BLACK);
        PDRectangle mediabox = page.getMediaBox();
        int margin = 2 * fontHeight;
        textRenderingLineStartY = (int) (mediabox.getUpperRightY()) - margin;
        textRenderingLineEndY = (int) (mediabox.getLowerLeftX()) + 2 * margin;
        textRenderingLineStartX = (int) (mediabox.getLowerLeftX()) + 2 * margin;
        textRenderingLineEndX = (int) (mediabox.getWidth()) - textRenderingLineStartX - 2 * margin;
        textRenderingLineCurrentY = textRenderingLineStartY;
    }


    private void loadAndSetFont() {
        try (InputStream fontInputStream = classLoader.getResourceAsStream(FONT_FILE)) {
            this.doc = new PDDocument();
            this.font = PDType0Font.load(doc, fontInputStream);
            double tempFontHeightDouble = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * FONT_SIZE;
            this.fontHeight = (int) Math.round(tempFontHeightDouble);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveAndClosePdf() throws IOException {
        if (doc != null) {
            doc.save(new File(OUTPUT_PDF));
            doc.close();
            doc = null;
        }
    }


    private void close() throws IOException {
        if (content != null) {
            content.close();
            content = null;
        }
    }
}
