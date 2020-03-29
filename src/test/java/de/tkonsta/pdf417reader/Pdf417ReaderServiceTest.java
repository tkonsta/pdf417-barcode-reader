package de.tkonsta.pdf417reader;

import ch.qos.logback.classic.Level;
import com.google.zxing.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Pdf417ReaderServiceTest {

    Pdf417ReaderService service = new Pdf417ReaderService();

    @BeforeEach
    void before() {
        // disable all the debug logging from fontbox and pdfbox
        Logger logger = LoggerFactory.getLogger("org.apache.fontbox");
        ((ch.qos.logback.classic.Logger) logger).setLevel(Level.OFF);
        logger = LoggerFactory.getLogger("org.apache.pdfbox");
        ((ch.qos.logback.classic.Logger) logger).setLevel(Level.OFF);
    }

    @Test
    void readPdf417BarcodeFromPdf() {
        Path pdf = Path.of("src", "test", "resources", "pdf417-macro.pdf");
        String resultFromService = service.readPdf417BarcodeFromPdfFile(pdf);
        assertTrue(resultFromService.startsWith("barcode 1"));
        assertTrue(resultFromService.endsWith("barcode 2 end"));
    }

    @Test
    void readPdf417BarcodeFromPdf_not_a_pdf() {
        Path pdf = Path.of("src", "test", "resources", "pdf417-macro.gif");
        assertThrows(IllegalArgumentException.class, () -> service.readPdf417BarcodeFromPdfFile(pdf));
    }

    @Test
    void readPdf417BarcodeFromImage() throws IOException {
        Path image = Path.of("src", "test", "resources", "pdf417-macro.gif");
        String resultFromService = service.readPdf417BarcodeFromImage(image);
        assertTrue(resultFromService.startsWith("barcode 1"));
        assertTrue(resultFromService.endsWith("barcode 2 end"));
    }
    @Test
    void readPdf417BarcodeFromImage_not_an_image() {
        Path image = Path.of("src", "test", "resources", "pdf417-macro.pdf");
        assertThrows(IllegalArgumentException.class, () -> service.readPdf417BarcodeFromImage(image));
    }
}
