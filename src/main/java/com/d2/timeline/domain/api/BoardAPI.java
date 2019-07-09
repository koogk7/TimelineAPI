package com.d2.timeline.domain.api;

import com.d2.timeline.domain.dto.BoardDTO;
import com.d2.timeline.domain.service.BoardService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.d2.timeline.domain.Constant.SwaggerBoardConstant.*;
import static com.d2.timeline.domain.Constant.SwaggerPageConstant.*;


@Api(value = "Board for API")
@RestController
@RequestMapping(value = "api/board")
public class BoardAPI {

    private static final Logger logger = LoggerFactory.getLogger(BoardAPI.class);

    @Autowired
    BoardService boardService;

    @PostMapping(value = "")
    @ApiOperation(value = "게시물 작성")
    public String writeBoard(@ApiParam(value = BOARD_DESC) BoardDTO board){
        logger.info("Entry saveBoard, board: " + board.getContentText());
        return boardService.saveBoard(board);
    }

    @GetMapping(value = "/{"+ BOARD_ID_NAME + "}")
    @ApiOperation(value = "게시물 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = BOARD_ID_NAME, value = BOARD_ID_DESC,
                    required = true, paramType = "path")
    })
    public BoardDTO loadBoardByBoardId(@PathVariable(BOARD_ID_NAME) Long boardId){
        logger.info("Entry getBoardListByWriter, boardId : " + boardId.toString());
        return boardService.findByBoardId(boardId);
    }

    @GetMapping(value = "/")
    @ApiOperation(value = "작성자 게시물목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = WRITER_NAME, value = WRITER_DESC, required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = PAGE_NAME, value = PAGE_DESC, defaultValue = DEFAULT_PAGE,
                    dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = SIZE_NAME, value = SIZE_DESC, defaultValue = DEFAULT_PAGE_SIZE,
                    dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = SORT_NAME, value = SORT_DESC, allowMultiple = true,
                    dataType = "string", paramType = "query")
    })
    public List<BoardDTO> loadBoardListByWriter(@RequestParam(value = WRITER_NAME) Long writerId,
                                                Pageable pageable){
        logger.info("Entry getBoardListByWriter, writerId : " + writerId.toString());
        return boardService.findByWriter(writerId, pageable);
    }
}
