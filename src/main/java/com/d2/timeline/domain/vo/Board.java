package com.d2.timeline.domain.vo;

import com.d2.timeline.domain.common.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="board_tb")
public class Board extends BaseEntity {

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_FK", nullable = false)
    private Member writer;

    @Setter
    @Column(name = "content_text")
    @ApiModelProperty(name = "게시물 내용")
    private String contentText;

    @Setter
    @Column(name = "content_img")
    private String contentImg;

    @Builder
    public Board(Long id, Member writer, String contentText, String contentImg){
        super(id);
        this.writer = writer;
        this.contentText = contentText;
        this.contentImg = contentImg;
    }


}
