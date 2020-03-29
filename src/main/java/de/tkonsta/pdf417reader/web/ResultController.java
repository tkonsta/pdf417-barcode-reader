package de.tkonsta.pdf417reader.web;

import de.tkonsta.pdf417reader.Pdf417ReaderService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ResultController {

    final Pdf417ReaderService pdf417ReaderService;

    public ResultController(@Autowired  Pdf417ReaderService pdf417ReaderService) {
        this.pdf417ReaderService = pdf417ReaderService;
    }

    @PostMapping("/result")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) {

        final Map<String, Object> model = new HashMap<>();

        if (file.isEmpty()) {
            return getInvalidFileModelAndView(model);
        }

        PDDocument pdfDocument;
        try {
            String barcodeFromPdf = pdf417ReaderService.readPdf417BarcodeFromPdf(file.getBytes());
            model.put("result", barcodeFromPdf);
            return new ModelAndView("result", model);
        } catch (Exception e) {
            return getInvalidFileModelAndView(model);
        }
    }

    private ModelAndView getInvalidFileModelAndView(Map<String, Object> model) {
        model.put("invalidFile", Boolean.TRUE);
        return new ModelAndView("form", model);
    }
}
