package com.llb.service;

import com.llb.dto.DocumentListDTO;
import com.llb.model.Document;
import com.llb.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository repository;

    public Document saveFile(MultipartFile file) throws IOException {
        Document doc = new Document();
        doc.setName(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setData(file.getBytes());
        return repository.save(doc);
    }

    public Document getFile(Long id) {
        return repository.findById(id).orElse(null);
    }
    public List<Document> getTop10Documents() {
        return repository.findTop10ByOrderByIdDesc();
    }
//    public List<DocumentSummary> getTop10Summary() {
//        return repository.findTop10Summary();
//    }

    public List<DocumentListDTO> getTop10DocumentListDTO() {
        return repository.findTop10DocumentListDTO();
    }
}
