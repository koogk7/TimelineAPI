package com.d2.timeline.domain.service;

import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dto.BoardDTO;
import com.d2.timeline.domain.vo.Board;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    @Autowired
    BoardRepository boardRepository;

    public String saveBoard(Board newBoard){
        Board board = boardRepository.save(newBoard);
        return "게시물 작성에 성공했습니다.";
    }

    public BoardDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseGet(()->{
            logger.error("존재하지 않는 게시물 번호입니다.");
            return Board.builder().id(-1L).build();
        });
        return new BoardDTO(targetBoard);
    }

    public List<BoardDTO> findByWriter(Long writerId, Pageable pageable){
        List<BoardDTO> boardDTOList = new ArrayList<>();
        List<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable).orElseGet(()->{
            logger.info("작성한 게시물이 없습니다.");
            return Collections.emptyList();
        });

        resultBoardList.forEach(x -> boardDTOList.add(new BoardDTO(x)));

        return boardDTOList;
    }

}