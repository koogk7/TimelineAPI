package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dto.BoardDTO;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    BoardRepository boardRepository;

    public String saveBoard(BoardDTO newBoardDTO){
        Board newBoard = newBoardDTO.transBoard();
        //Todo 게시물 저장 실패 시 예외처리 필요
        boardRepository.save(newBoard);
        return SAVE_OK_MSG;
    }

    public BoardDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseGet(()->{
            logger.error(ID_ERROR_MSG);
            return Board.builder().id(-1L).build();
        });
        return new BoardDTO(targetBoard);
    }

    public List<BoardDTO> findByWriter(Long writerId, Pageable pageable){
        List<BoardDTO> boardDTOList = new ArrayList<>();
        List<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable).orElseGet(()->{
            logger.info(NO_EXIST_MSG);
            return Collections.emptyList();
        });

        resultBoardList.forEach(x -> boardDTOList.add(new BoardDTO(x)));

        return boardDTOList;
    }

//    public List<BoardDTO> loadTimeLine(Long memberId, Pageable pageable){
//    }

}