package de.tkonsta.pdf417reader.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormController {

    @GetMapping("/")
    public String pdfForm() {
        return "form";
    }

}
