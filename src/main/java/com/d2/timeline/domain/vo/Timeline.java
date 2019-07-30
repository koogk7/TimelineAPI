package com.d2.timeline.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timeline_tb")
public class Timeline implements Serializable {
    @Id
    @Column(name = "idx_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userFk")
    private Member receiver;

    @ManyToOne
    @JoinColumn(name = "boardFk")
    private Board board;

}
