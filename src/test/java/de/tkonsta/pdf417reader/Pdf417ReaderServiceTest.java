package de.tkonsta.pdf417reader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Level;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

class Pdf417ReaderServiceTest {

    final Pdf417ReaderService service = new Pdf417ReaderService();

    @BeforeEach
    void before() {
        // disable all the debug logging from fontbox and pdfbox
        Logger logger = LoggerFactory.getLogger("org.apache.fontbox");
        ((ch.qos.logback.classic.Logger) logger).setLevel(Level.OFF);
        logger = LoggerFactory.getLogger("org.apache.pdfbox");
        ((ch.qos.logback.classic.Logger) logger).setLevel(Level.OFF);
    }

    @Test
    void readPdf417BarcodeFromPdf() throws IOException {
        Path pdf = Path.of("src", "test", "resources", "pdf417-macro.pdf");
        byte[] bytes = FileCopyUtils.copyToByteArray(pdf.toFile());
        String resultFromService = service.readPdf417BarcodeFromPdf(bytes);
        assertTrue(resultFromService.startsWith("barcode 1"));
        assertTrue(resultFromService.endsWith("barcode 2 end"));
    }

    @Test
    void readPdf417BarcodeFromPdf_not_a_pdf() throws IOException {
        Path pdf = Path.of("src", "test", "resources", "pdf417-macro.gif");
        byte[] bytes = FileCopyUtils.copyToByteArray(pdf.toFile());
        assertThrows(IllegalArgumentException.class, () -> service.readPdf417BarcodeFromPdf(bytes));
    }

    @Test
    void readPdf417BarcodeFromImage() throws IOException {
        Path imageFile = Path.of("src", "test", "resources", "pdf417-macro.gif");
        BufferedImage image = ImageIO.read(imageFile.toFile());
        String resultFromService = service.readPdf417BarcodeFromImage(image);
        assertTrue(resultFromService.startsWith("barcode 1"));
        assertTrue(resultFromService.endsWith("barcode 2 end"));
    }

    @Test
    void readPdf417BarcodeFromImage_not_an_image() throws IOException {
        Path imageFile = Path.of("src", "test", "resources", "pdf417-macro.pdf");
        BufferedImage image = ImageIO.read(imageFile.toFile());
        assertThrows(NullPointerException.class, () -> service.readPdf417BarcodeFromImage(image));
    }
}
