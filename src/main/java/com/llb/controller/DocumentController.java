package com.llb.controller;

import com.llb.dto.DocumentListDTO;
import com.llb.model.Document;
import com.llb.service.CertificateService;
import com.llb.service.DocumentService;
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
public class DocumentController {

    @Autowired
    private DocumentService service;

    @Autowired
    private CertificateService certService;

    // Show upload form
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // Thymeleaf template
    }

    // Handle upload
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Document saved = service.saveFile(file);
            model.addAttribute("doc", saved);
            return "view"; // shows uploaded document details
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
            return "upload";
        }
    }

    // List top 10 recent documents
    @GetMapping("/documents")
    public String showAllDocuments(Model model) {
        List<Document> allDocs = service.getTop10Documents();
        model.addAttribute("documents", allDocs);
        return "document";
    }

    // List top 10 documents with DTO for gamer view
    @GetMapping("/gamer-documents")
    public String showGamerDocuments(Model model) {
        List<DocumentListDTO> allDocs = service.getTop10DocumentListDTO();
        model.addAttribute("documents", allDocs);
        return "gamer-documents";
    }

    // View PDF inline in browser/mobile
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable Long id) {
        Document doc = service.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + doc.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(doc.getData());
    }

    // Contact page
    @GetMapping("/contact")
    public String showContactPage(Model model) {
        return "contact";
    }
}
