package com.d2.timeline.global;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public class AbstractEntity {
    @Id
    @Column(name = "idx_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_DT")
    private LocalDateTime createdDT;

    @CreationTimestamp
    @Column(name = "updated_DT")
    private LocalDateTime updatedDT;
}
