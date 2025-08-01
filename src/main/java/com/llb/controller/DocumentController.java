package com.llb.controller;

import com.llb.dto.DocumentListDTO;
import com.llb.model.Certificate;
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
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload"; // This should match your Thymeleaf template name: upload.html
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            Document saved = service.saveFile(file);
            model.addAttribute("doc", saved);
            return "view"; // Thymeleaf template
        } catch (Exception e) {
            model.addAttribute("message", "Failed to upload file: " + e.getMessage());
            return "upload"; // Or custom error page
        }
    }

    @GetMapping("/documents")
    public String showAllDocuments(Model model) {
        List<Document> allDocs = service.getTop10Documents();  // Calls service
        model.addAttribute("documents", allDocs);
        return "document";  // Renders document.html
    }

    @GetMapping("/gamer-documents")
    public String showGamerDocuments(Model model) {
        List<DocumentListDTO> allDocs = service.getTop10DocumentListDTO();
        model.addAttribute("documents", allDocs);
        return "gamer-documents";
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable Long id) {
        Document doc = service.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + doc.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(doc.getData());
    }

    @GetMapping("/contact")
    public String showContactPage(Model model) {
        return "contact";
    }


}
