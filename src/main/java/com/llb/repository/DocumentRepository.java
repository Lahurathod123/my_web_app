package com.llb.repository;

import com.llb.dto.DocumentListDTO;
import com.llb.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findTop10ByOrderByIdDesc();
    @Query("SELECT new com.llb.dto.DocumentListDTO(d.id, d.name) FROM Document d ORDER BY d.id DESC")
    List<DocumentListDTO> findTop10DocumentListDTO();
}
