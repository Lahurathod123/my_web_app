package com.llb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 100000)
    private byte[] data;
}
