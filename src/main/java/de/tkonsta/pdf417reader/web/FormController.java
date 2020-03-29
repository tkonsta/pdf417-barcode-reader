package de.tkonsta.pdf417reader.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FormController {

    @GetMapping("/")
    public ModelAndView pdfForm() {
        final Map<String, Object> model = new HashMap<>();
        model.put("invalidFile", Boolean.FALSE);
        return new ModelAndView("form", model);
    }

}
