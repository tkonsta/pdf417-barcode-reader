package de.tkonsta.pdf417reader;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.*;

public class Pdf417ReaderService {

    public String readPdf417BarcodeFromPdf(Path pdfFile) {
        StringWriter result = new StringWriter();
        PDDocument pdfDocument;
        try {
            pdfDocument = PDDocument.load(pdfFile.toFile());
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            int pageCounter = 0;
            for (PDPage page : pdfDocument.getPages()) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(pageCounter++, 300, ImageType.RGB);
                result.append(readPdf417BarcodeFromImage(bim));
            }
            pdfDocument.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("error while reading pdf");
        } catch (NotFoundException e) {
            // do nothing => this page just does not contain barcodes
        }
        return result.toString();
    }

    public String readPdf417BarcodeFromImage(Path imageFile) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(imageFile.toFile());
        if (image == null) {
            throw new IllegalArgumentException("image not readable");
        }
        return readPdf417BarcodeFromImage(image);
    }

    private String readPdf417BarcodeFromImage(BufferedImage image) throws NotFoundException {
        StringWriter result = new StringWriter();
        MultipleBarcodeReader barcodeReader = new PDF417Reader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        //hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        List<Result> results = new ArrayList<>();
        results.addAll(Arrays.asList(barcodeReader.decodeMultiple(bitmap, hints)));
        results.sort(Comparator.comparingInt((Result r) -> getMeta(r).getSegmentIndex()));
        results.forEach(r -> result.append(r.getText()));
        return result.toString();
    }

    private static PDF417ResultMetadata getMeta(Result result) {
        return result.getResultMetadata() == null ? null : (PDF417ResultMetadata) result.getResultMetadata().get(
                ResultMetadataType.PDF417_EXTRA_METADATA);
    }
}
