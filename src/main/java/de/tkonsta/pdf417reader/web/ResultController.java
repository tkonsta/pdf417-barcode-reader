package de.tkonsta.pdf417reader.web;

import de.tkonsta.pdf417reader.Pdf417ReaderService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResultController {

    final Pdf417ReaderService pdf417ReaderService;

    public ResultController(@Autowired Pdf417ReaderService pdf417ReaderService) {
        this.pdf417ReaderService = pdf417ReaderService;
    }

    @PostMapping("/result")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) {

        final Map<String, Object> model = new HashMap<>();

        if (file.isEmpty()) {
            model.put("invalidFile", Boolean.TRUE);
        } else {
            try {
                String barcodeFromPdf = pdf417ReaderService.readPdf417BarcodeFromPdf(file.getBytes());
                if (StringUtils.isEmpty(barcodeFromPdf)) {
                    model.put("noBarcodeContent", Boolean.TRUE);
                } else {
                    model.put("content", barcodeFromPdf);
                }
            } catch (Exception e) {
                model.put("invalidFile", Boolean.TRUE);
            }
        }

        return new ModelAndView("result", model);
    }
}
