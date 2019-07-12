package com.d2.timeline.domain.dto;


import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.d2.timeline.domain.Constant.SwaggerBoardConstant.BOARD_DESC;

@Getter
@NoArgsConstructor
public class BoardReadDTO{
    private Long id;
    private String contentText;
    private String contentImg;
    private Long writerId;
    private String writerNickName;
    private String writerImg;
    private LocalDateTime createdDT;
    private LocalDateTime updatedDT;


    public BoardReadDTO(Board board){
        this.id = board.getId();
        if(this.id == -1) {
            return;
        }
        this.contentText = board.getContentText();
        this.contentImg = board.getContentImg();
        this.writerId = board.getWriter().getId();
        this.writerNickName = board.getWriter().getNickname();
        this.writerImg = board.getWriter().getProfileImg();
        this.createdDT = board.getCreatedDT();
    }

    public Board transBoard(){
        Member writer = Member.builder().id(this.writerId).build();

        return Board.builder()
                .id(this.id)
                .contentText(this.contentText)
                .contentImg(this.contentImg)
                .writer(writer)
                .createdDT(this.createdDT)
                .updatedDT(this.updatedDT)
                .build();
    }
}
