package com.llb.controller;

import com.llb.model.Certificate;
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

    // Show all uploaded certificates
    @GetMapping("/certificate")
    public String viewCertificates(Model model) {
        List<Certificate> certificates = certificateService.getAllCertificates();
        model.addAttribute("certificates", certificates);
        return "viewCertificates"; // maps to viewCertificates.html
    }

    // Upload a new certificate
    @PostMapping("/upload-certificate")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Certificate saved = certificateService.saveFile(file);
            model.addAttribute("doc", saved);
            return "view"; // you can customize this template
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
            return "upload"; // or redirect to an error page
        }
    }

    // Stream certificate PDF by ID with mobile detection
    @GetMapping("/certificate/view/{id}")
    public ResponseEntity<byte[]> viewCertificate(
            @PathVariable Long id,
            @RequestHeader(value = "User-Agent", defaultValue = "") String userAgent) {

        Certificate cert = certificateService.getCertificate(id);
        if (cert == null || cert.getData() == null) {
            return ResponseEntity.notFound().build();
        }

        // Basic mobile detection
        boolean isMobile = userAgent.toLowerCase().contains("mobile")
                || userAgent.toLowerCase().contains("android")
                || userAgent.toLowerCase().contains("iphone")
                || userAgent.toLowerCase().contains("ipad");

        String disposition = isMobile
                ? "attachment; filename=\"" + cert.getName() + "\""
                : "inline; filename=\"" + cert.getName() + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .contentType(MediaType.APPLICATION_PDF)
                .body(cert.getData());
    }
}
