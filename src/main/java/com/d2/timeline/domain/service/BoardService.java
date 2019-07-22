package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardWriteDTO;
import com.d2.timeline.domain.exception.EntityNotFoundException;
import com.d2.timeline.domain.exception.UnmatchedRequestorException;
import com.d2.timeline.domain.exception.UnmatchedWriterException;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

import static com.d2.timeline.domain.Constant.BoardConstant.*;

@RequiredArgsConstructor
@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public String saveBoard(String writerEmail, BoardWriteDTO newBoardDTO){
        Member writer =  memberRepository.findByEmail(writerEmail).
                orElseThrow(()-> new NoResultException(ERROR_MSG));
        Board newBoard = Board.builder()
                .writer(writer)
                .build();
        newBoard = newBoardDTO.transBoard(newBoard);
        boardRepository.save(newBoard);
        return OK_MSG;
    }

    public String updateBoard(String requestEmail, Long boardId, BoardWriteDTO updateBoardDTO){
        Board updateBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new UnmatchedWriterException("작성자가 아닙니다."));

        validateRequest(requestEmail, updateBoard);
        updateBoard = updateBoardDTO.transBoard(updateBoard);
        boardRepository.save(updateBoard);
        return OK_MSG;
    }

    public String deleteBoard(String requestEmail, Long deleteBoard){
        Board board = boardRepository.findById(deleteBoard).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        validateRequest(requestEmail, board);
        boardRepository.deleteById(deleteBoard);
        return OK_MSG;
    }

    public BoardReadDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 게시물입니다."));
        return new BoardReadDTO(targetBoard);
    }

    public Page<BoardReadDTO> findByWriter(Long writerId, Pageable pageable){
        Page<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable);
        return resultBoardList.map(BoardReadDTO::new);
    }

    /*
    public Page<BoardReadDTO> loadFollowingBoardList(String requestEmail, Long masterId, Pageable pageable){
        Member master = memberRepository.findById(masterId).orElseThrow(
                ()-> new NoResultException(ERROR_MSG));
        checkLoadAuthority(requestEmail, master);

        //TODO database (userRelation - board) join 해서 구현하려다가 구조 아직 안정해서 말았음
        Page<Board> resultBoardList ;
        return resultBoardList.map(BoardReadDTO::new);
    }
    */
    private void checkLoadAuthority(String request, Member member){
        String requestorEmail = member.getEmail();
        if(!request.equals(requestorEmail))
            throw new UnmatchedRequestorException("조회를 요청 할 권한이 없습니다.");
    }

    private void validateRequest(String request, Board board){
        String writerEmail = board.getWriter().getEmail();
        if(!request.equals(writerEmail))
            throw new UnmatchedWriterException("작성자가 아닙니다");
    }


}