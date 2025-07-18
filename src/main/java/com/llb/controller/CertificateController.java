package com.llb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CertificateController {

    @GetMapping("/certificates")
    public String certificates() {
        return "redirect:/certificate.html";
    }
}
