package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dto.BoardDTO;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.BoardConstant.*;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ModelMapper modelMapper;

    public HttpStatus saveBoard(BoardDTO newBoardDTO){
        Board newBoard = newBoardDTO.transBoard();
        //Todo 게시물 저장 실패 시 예외처리 필요
        boardRepository.save(newBoard);
        return HttpStatus.CREATED;
    }

    public HttpStatus updateBoard(BoardDTO updateBoard){
        boolean exist = boardRepository.existsById(updateBoard.getId());
        if(!exist)
            return HttpStatus.BAD_REQUEST; // Todo 이 메시지가 맞나?
        boardRepository.save(updateBoard.transBoard());
        return HttpStatus.OK;
    }

    public HttpStatus deleteBoard(BoardDTO deleteBoard){
        boolean exist = boardRepository.existsById(deleteBoard.getId());
        if(!exist)
            return HttpStatus.BAD_REQUEST; // Todo 이 메시지가 맞나?
        boardRepository.delete(deleteBoard.transBoard());
        return HttpStatus.OK;
    }

    public BoardDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseGet(()->{
            logger.error(ID_ERROR_MSG);
            return Board.builder().id(-1L).build();
        });
        return new BoardDTO(targetBoard);
    }

    public Page<BoardDTO> findByWriter(Long writerId, Pageable pageable){
        Page<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable);
        return resultBoardList.map(BoardDTO::new);
    }

}