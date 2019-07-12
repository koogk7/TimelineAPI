package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardUpdateDTO;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

import static com.d2.timeline.domain.Constant.BoardConstant.*;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepository;

    public HttpStatus saveBoard(BoardReadDTO newBoardReadDTO){
        Board newBoard = newBoardReadDTO.transBoard();
        boardRepository.save(newBoard);
        return HttpStatus.CREATED;
    }

    public String updateBoard(Long userId, Long boardId, BoardUpdateDTO updateBoardDTO){
        //Todo 유저 검증확인하는 로직 필요
        Board updateBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoResultException(ERROR_MSG));

        updateBoard = updateBoardDTO.transBoard(updateBoard);
        boardRepository.save(updateBoard);
        return OK_MSG;
    }

    public String deleteBoard(Long deleteBoard){
        boolean exist = boardRepository.existsById(deleteBoard);
        if(!exist)
            return ERROR_MSG; //Todo 예외를 던져야함

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

}