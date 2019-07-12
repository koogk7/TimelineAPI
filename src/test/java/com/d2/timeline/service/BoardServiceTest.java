package com.d2.timeline.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardUpdateDTO;
import com.d2.timeline.domain.service.BoardService;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;

import static com.d2.timeline.domain.Constant.BoardConstant.ERROR_MSG;
import static com.d2.timeline.domain.Constant.BoardConstant.OK_MSG;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class BoardServiceTest extends MockTest{
    final static Long boardId = 1L;
    final static Long authorId = 1L;
    @InjectMocks
    private BoardService boardService;

    @Mock
    private BoardRepository boardRepository;

    private Board board;
    private Member author;
    private BoardUpdateDTO boardUpdateDTO;

    @Before
    public void setUp() throws Exception {
        author = Member.builder()
                .id(authorId)
                .email("test@naver.com")
                .password("123")
                .nickname("테서트")
                .profileImg("None")
                .build();

        board = Board.builder()
                .id(boardId)
                .writer(author)
                .contentImg("None")
                .contentText("Testing...")
                .build();

        BoardUpdateDTO boardUpdateDTO = BoardUpdateDTO.builder()
                .contentText(board.getContentText())
                .contentImg(board.getContentImg())
                .build();
    }

    @Test
    public void 유저정보수정_성공_테스트(){
        //given
        given(boardRepository.existsById(boardId)).willReturn(true);
        //when
        final String returnMessage = boardService.updateBoard(authorId, boardId, boardUpdateDTO);
        //then
        assertEquals(OK_MSG, returnMessage);
    }

    @Test
    public void 유저정보수정_실패_테스트(){
        //given
        given(boardRepository.existsById(-1L)).willReturn(false);
        //when
        final String returnMessage = boardService.updateBoard(authorId, boardId, boardUpdateDTO);
        //then
        assertEquals(ERROR_MSG, returnMessage);
    }

    @Test
    public void 유저정보삭제_성공_테스트(){
        //given
        given(boardRepository.existsById(boardId)).willReturn(true);
        //when
        final String returnMessage = boardService.deleteBoard(boardId);
        //then
        assertEquals(OK_MSG, returnMessage);
    }

    @Test
    public void 유저정보삭제_실패_테스트(){
        //given
        given(boardRepository.existsById(boardId)).willReturn(false);
        //when
        final String returnMessage = boardService.deleteBoard(boardId);
        //then
        assertEquals(ERROR_MSG, returnMessage);
    }
}
