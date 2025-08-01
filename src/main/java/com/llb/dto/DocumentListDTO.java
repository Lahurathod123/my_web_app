package com.llb.dto;

public class DocumentListDTO {
    private Long id;
    private String name;

    public DocumentListDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}