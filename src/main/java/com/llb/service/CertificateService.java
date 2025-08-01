package com.llb.service;

import com.llb.model.Certificate;
import com.llb.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository repository;

    public Certificate saveFile(MultipartFile file) throws IOException {
        Certificate doc = new Certificate();
        doc.setName(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setData(file.getBytes());
        return repository.save(doc);
    }

    public List<Certificate> getAllCertificates() {
        return repository.findAll();
    }

    public Certificate getCertificate(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }


}
