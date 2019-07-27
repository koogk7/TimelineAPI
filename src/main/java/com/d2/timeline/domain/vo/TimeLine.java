package com.d2.timeline.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timeline_tb")
public class Timeline {
    @Id
    @Column(name = "idx_PK")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_FK")
    private Member reciver;

    @ManyToOne
    @JoinColumn(name = "board_FK")
    private Board board;
}
