package com.d2.timeline.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.TimelineRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardWriteDTO;
import com.d2.timeline.domain.exception.EntityNotFoundException;
import com.d2.timeline.domain.exception.UnmatchedWriterException;
import com.d2.timeline.domain.service.BoardService;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.Timeline;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

public class BoardServiceTest extends MockTest{
    final static Long boardId = 1L;
    final static String authorEmail = "master@test.com";


    @InjectMocks
    private BoardService boardService;

    @Mock
    private TimelineRepository timelineRepository;

    @Mock
    private BoardRepository boardRepository;

    private Board board;
    private Member author;
    private BoardWriteDTO boardUpdateDTO;
    MultipartFile multipartFile;

    @Before
    public void setUp() throws Exception {
        author = Member.builder()
                .id(1L)
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

        multipartFile = new MockMultipartFile("test", "test".getBytes());
        boardUpdateDTO = BoardWriteDTO.builder()
                .contentText(board.getContentText())
                .contentImg(multipartFile)
                .build();
    }

    @Test(expected = EntityNotFoundException.class)
    public void 게시물수정_실패_존재하지않는게시물(){
        //given
        given(boardRepository.findById(board.getId())).willReturn(Optional.empty());

        //when
        final String returnMessage = boardService.updateBoard(authorEmail, boardId,
                boardUpdateDTO.getContentText(), boardUpdateDTO.getContentImg());

    }

    @Test(expected = UnmatchedWriterException.class)
    public void 게시물수정_실패_권한없음(){
        //given
        given(boardRepository.findById(board.getId())).willReturn(Optional.of(board));

        //when
        final String returnMessage = boardService.updateBoard("failTest@test.com", boardId, boardUpdateDTO.getContentText(),
                boardUpdateDTO.getContentImg());

    }

    @Test
    public void 타임라인_성공_테스트(){
        //given
        Page<BoardReadDTO> boardReadDTOPage;
        List<Timeline> timelineList = new ArrayList<>();
        Timeline timeline = Timeline.builder()
                .id(1L)
                .board(board)
                .receiver(author)
                .build();
        timelineList.add(timeline);
        timelineList.add(timeline);
        timelineList.add(timeline);
        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<Timeline> timelinePage = new PageImpl<Timeline>(timelineList, PageRequest.of(0, 5),10);
        given(timelineRepository.findByReceiver(author, pageRequest)).willReturn(timelinePage);

        //when
        boardReadDTOPage = boardService.loadTimelineFromOrigin(author.getEmail(), pageRequest);

        //then
        assertThat(boardReadDTOPage, contains(
                hasProperty("id", is(1L)),
                hasProperty("id", is(1L)),
                hasProperty("id", is(1L))
        ));
    }
}
