package com.d2.timeline.dao;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class BoardRepositoryTest extends RepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void 게시물저장_테스트(){

        // given
        Member writer = Member.builder().id(1L).build();
        Board board = Board.builder()
                .contentText("Text")
                .contentImg("Img")
//                .writer(writer)
                .build();
        //when
        Board afterSave = boardRepository.save(board);

        //then
        assertEquals(afterSave.getContentText(), "Text");
        assertEquals(afterSave.getCreatedDT().toLocalDate(), LocalDate.now());

    }
}
