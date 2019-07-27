package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.TimelineRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardWriteDTO;
import com.d2.timeline.domain.exception.EntityNotFoundException;
import com.d2.timeline.domain.exception.UnmatchedRequestorException;
import com.d2.timeline.domain.exception.UnmatchedWriterException;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.Timeline;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.d2.timeline.domain.Constant.BoardConstant.*;

@RequiredArgsConstructor
@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final TimelineRepository timelineRepository;

    public Page<BoardReadDTO> loadTimeline(Member member, Pageable pageable){
        Page<Timeline> timeLines = timelineRepository.findByReciver(member, pageable);
        return timeLines.map(timeline -> new BoardReadDTO(timeline.getBoard()));
    }

    public String saveBoard(String writerEmail, BoardWriteDTO newBoardDTO){
        Member writer =  memberRepository.findByEmail(writerEmail).
                orElseThrow(()-> new EntityNotFoundException(ERROR_NOT_EXIST));
        Board newBoard = Board.builder()
                .writer(writer)
                .build();
        newBoard = newBoardDTO.transBoard(newBoard);
        boardRepository.save(newBoard);
        return OK_MSG;
    }

    public String updateBoard(String requestEmail, Long boardId, BoardWriteDTO updateBoardDTO){
        Board updateBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new EntityNotFoundException(ERROR_NOT_EXIST));

        validateRequest(requestEmail, updateBoard);
        updateBoard = updateBoardDTO.transBoard(updateBoard);
        boardRepository.save(updateBoard);
        return OK_MSG;
    }

    public String deleteBoard(String requestEmail, Long deleteBoard){
        Board board = boardRepository.findById(deleteBoard).orElseThrow(
                ()-> new EntityNotFoundException(ERROR_NOT_EXIST));

        validateRequest(requestEmail, board);
        boardRepository.deleteById(deleteBoard);
        return OK_MSG;
    }

    public BoardReadDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new EntityNotFoundException(ERROR_NOT_EXIST));
        return new BoardReadDTO(targetBoard);
    }

    public Page<BoardReadDTO> findByWriter(Long writerId, Pageable pageable){
        Page<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable);
        return resultBoardList.map(BoardReadDTO::new);
    }

    private void checkLoadAuthority(String request, Member member){
        String requestorEmail = member.getEmail();
        if(!request.equals(requestorEmail))
            throw new UnmatchedRequestorException(ERROR_BAD_REQUEST);
    }

    private void validateRequest(String request, Board board){
        String writerEmail = board.getWriter().getEmail();
        if(!request.equals(writerEmail))
            throw new UnmatchedWriterException(ERROR_BAD_REQUEST);
    }


}