package com.d2.timeline.dao;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BoardRepositoryTest extends RepositoryTest{

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;
    final static Long targetBoardId = 1L;
    final static String updateContent = "Updated";

    private Board board;
    private Member author;

    @Before
    public void setUp() throws Exception {
        author = Member.builder()
                .email("test@naver.com")
                .password("123")
                .nickname("테서트")
                .profileImg("None")
                .build();
        author = memberRepository.save(author);

        board = Board.builder()
                .writer(author)
                .contentImg("None")
                .contentText("Testing...")
                .build();
        board = boardRepository.save(board);
    }

    @Test
    public void 게시물저장_테스트(){
        //given
        Board beforeBoard = Board.builder()
                .writer(author)
                .contentImg("None")
                .contentText("Testing...")
                .build();

        //when
        Board afterSave = boardRepository.save(beforeBoard);

        //then
        assertEquals(afterSave.getContentText(), "Testing...");
        assertEquals(afterSave.getCreatedDT().toLocalDate(), LocalDate.now());
    }

    @Test
    public void 게시물수정_테스트(){
        //when
        board.setContentText(updateContent);
        board = boardRepository.save(board);

        //then
        assertEquals(updateContent, board.getContentText());
        assertEquals(LocalDate.now(), board.getUpdatedDT().toLocalDate());
    }

    @Test
    public void 게시물삭제_테스트(){
        //when
        boardRepository.delete(board);
        Long expectId = -1L;
        Board afterBoard = boardRepository.findById(board.getId()).orElseGet(
                ()-> Board.builder().id(expectId).build());

        //then
        assertEquals(expectId, afterBoard.getId());
    }
}
