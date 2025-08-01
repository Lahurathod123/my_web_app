package com.llb.controller;

import com.llb.model.Certificate;
import com.llb.model.Document;
import com.llb.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping("/certificate")
    public String viewCertificates(Model model) {
        List<Certificate> certificates = certificateService.getAllCertificates();
        model.addAttribute("certificates", certificates);
        return "viewCertificates"; // Thymeleaf HTML page: view-certificates.html
    }

    @PostMapping("/upload-certificate")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Certificate saved = certificateService.saveFile(file);
            model.addAttribute("doc", saved);
            return "view"; // Thymeleaf template
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
            return "upload"; // Or custom error page
        }
    }
    @GetMapping("/certificate/view/{id}")
    public ResponseEntity<byte[]> viewCertificate(@PathVariable Long id) {
        Certificate cert = certificateService.getCertificate(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + cert.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(cert.getData());
    }

}
