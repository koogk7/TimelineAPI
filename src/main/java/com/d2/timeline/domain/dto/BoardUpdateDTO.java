package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDTO {
    private String contextText;
    private String contentImg;

    public Board transBoard(Board board) {
        board.setContentText(this.contextText);
        board.setContentImg(this.contentImg);
        return board;
    }

    @Builder
    public BoardUpdateDTO(String contentText, String contentImg){
        this.contextText = contentText;
        this.contentImg = contentImg;
    }
}
