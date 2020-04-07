package de.tkonsta.pdf417reader;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.pdf417.PDF417ResultMetadata;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Pdf417ReaderService {

    private static final Logger LOG = LoggerFactory.getLogger(Pdf417ReaderService.class);

    public String readPdf417BarcodeFromPdf(byte[] pdfData) {
        StringWriter result = new StringWriter();
        try {
            checkIfValidPdfOrThrowException(pdfData);
            PDDocument pdfDocument = PDDocument.load(pdfData);
            PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
            int pageCount = pdfDocument.getPages().getCount();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300, ImageType.RGB);
                result.append(readPdf417BarcodeFromImage(bim));
            }
            pdfDocument.close();
            return result.toString();
        } catch (IOException e) {
            LOG.debug("error while reading pdf", e);
            throw new IllegalArgumentException("error while reading pdf");
        }
    }

    public String readPdf417BarcodeFromImage(BufferedImage image) {
        StringWriter result = new StringWriter();
        MultipleBarcodeReader barcodeReader = new PDF417Reader();
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.FALSE);
        List<Result> results = new ArrayList<>();
        try {
            results.addAll(Arrays.asList(barcodeReader.decodeMultiple(bitmap, hints)));
        } catch (NotFoundException e) {
            LOG.debug("no barcodes were found on given image");
        }
        results.sort(Comparator.comparingInt((Result r) -> Objects.requireNonNull(getMeta(r)).getSegmentIndex()));
        results.forEach(r -> result.append(r.getText()));
        return result.toString();
    }

    private static PDF417ResultMetadata getMeta(Result result) {
        return result.getResultMetadata() == null ? null : (PDF417ResultMetadata) result.getResultMetadata().get(
            ResultMetadataType.PDF417_EXTRA_METADATA);
    }

    private void checkIfValidPdfOrThrowException(byte[] pdfData) throws IOException {
        RandomAccessBuffer randomAccessBuffer = new RandomAccessBuffer(pdfData);
        PDFParser parser = new PDFParser(randomAccessBuffer);
        parser.setLenient(false);
        parser.parse();
    }
}
