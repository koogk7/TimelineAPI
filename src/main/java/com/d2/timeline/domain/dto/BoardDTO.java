package com.d2.timeline.domain.dto;


import com.d2.timeline.domain.vo.Board;
import lombok.Getter;

@Getter
public class BoardDTO {
    private Long id;
    private String contentText;
    private String contentImg;
    private Long writerIdx;
    private String writerNickName;
    private String writerImg;

    public BoardDTO(Board board){
        this.id = board.getId();
        if(this.id == -1) {
            return;
        }
        this.contentText = board.getContentText();
        this.contentImg = board.getContentImg();
        this.writerIdx = board.getWriter().getId();
        this.writerNickName = board.getWriter().getNickname();
        this.writerImg = board.getWriter().getProfileImg();
    }
}
