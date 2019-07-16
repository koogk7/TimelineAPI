package com.d2.timeline.domain.dto;

import com.d2.timeline.domain.vo.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardWriteDTO {
    private String contentText;
    private String contentImg;

    public Board transBoard(Board board) {
        board.setContentText(this.contentText);
        board.setContentImg(this.contentImg);
        return board;
    }

    @Builder
    public BoardWriteDTO(String contentText, String contentImg){
        this.contentText = contentText;
        this.contentImg = contentImg;
    }
}
