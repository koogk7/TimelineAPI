package com.d2.timeline.domain.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
public class BaseEntity {
    @Id
    @Column(name = "idx_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_DT")
    private LocalDateTime createdDT;

    @UpdateTimestamp
    @Column(name = "updated_DT")
    private LocalDateTime updatedDT;

    public BaseEntity(Long id, LocalDateTime createdDT,
                      LocalDateTime updatedDT){
        this.id = id;
        this.createdDT = createdDT;
        this.updatedDT = updatedDT;
    }

    public BaseEntity(Long id){
        this.id = id;
    }

}
