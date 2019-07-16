package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardWriteDTO;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
        //Todo 유저 검증확인하는 로직 필요
        Board updateBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoResultException(ERROR_MSG));

        if(!isWriter(requestEmail, updateBoard))
            return ERROR_MSG;

        updateBoard = updateBoardDTO.transBoard(updateBoard);
        boardRepository.save(updateBoard);
        return OK_MSG;
    }

    public String deleteBoard(String requestEmail, Long deleteBoard){
        Board board = boardRepository.findById(deleteBoard).orElseThrow(
                ()-> new NoResultException(ERROR_MSG));

        if(!isWriter(requestEmail, board))
            return ERROR_MSG;

        boardRepository.deleteById(deleteBoard);
        return OK_MSG;
    }

    public BoardReadDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseGet(()->{
            logger.error(ERROR_MSG);
            return Board.builder().id(-1L).build();
        });
        return new BoardReadDTO(targetBoard);
    }

    public Page<BoardReadDTO> findByWriter(Long writerId, Pageable pageable){
        Page<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable);
        return resultBoardList.map(BoardReadDTO::new);
    }

    public boolean isWriter(String request, Board board){
        String writerEmail = board.getWriter().getEmail();
        return request.equals(writerEmail);
    }
}