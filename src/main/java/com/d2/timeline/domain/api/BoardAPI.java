package com.d2.timeline.domain.api;

import com.d2.timeline.domain.common.AuthenticationHelper;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.dto.BoardWriteDTO;
import com.d2.timeline.domain.service.BoardService;
import com.d2.timeline.domain.common.ResponseHelper;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.d2.timeline.domain.Constant.SwaggerBoardConstant.*;
import static com.d2.timeline.domain.Constant.SwaggerPageConstant.*;


@Api(value = "Board for API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/boards")
public class BoardAPI {

    private static final Logger logger = LoggerFactory.getLogger(BoardAPI.class);

    private final BoardService boardService;
    private final AuthenticationHelper authHelper;

    @ApiOperation(value = "게시물 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping(value = "")
    public String writeBoard(@RequestBody @Valid BoardWriteDTO board){
        logger.info("Entry saveBoard, board: " + board.getContentText());
        String writerEmail = authHelper.getEmailFormToken();
        return boardService.saveBoard(writerEmail, board);
    }

    @ApiOperation(value = "게시물 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping(value = "/{"+ BOARD_ID_NAME + "}")
    public ResponseEntity<?> updateBoard(@PathVariable(BOARD_ID_NAME) @NotNull Long boardId,
                                         @RequestBody @Valid BoardWriteDTO boardUpdateDTO){
        logger.info("Entry updateBoard, boardId : " + boardId.toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = boardService.updateBoard(requestEmail, boardId, boardUpdateDTO);
        return ResponseEntity.ok(msg);
    }


    @ApiOperation(value = "게시물 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping(value = "/{"+ BOARD_ID_NAME + "}")
    public ResponseEntity<?> deleteBoard(@PathVariable(BOARD_ID_NAME) Long boardId){
        logger.info("Entry deleteBoard, boardId : " + boardId.toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = boardService.deleteBoard(requestEmail,boardId);
        return ResponseEntity.ok(msg);
    }

    @GetMapping(value = "/{"+ BOARD_ID_NAME + "}")
    @ApiOperation(value = "게시물 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = BOARD_ID_NAME, value = BOARD_ID_DESC,
                    required = true, paramType = "path")
    })
    public ResponseEntity<BoardReadDTO> loadBoardByBoardId(@PathVariable(BOARD_ID_NAME) Long boardId){
        logger.info("Entry getBoardListByWriter, boardId : " + boardId.toString());
        return ResponseEntity.ok(boardService.findByBoardId(boardId));
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
    public ResponseEntity<?> loadBoardListByWriter(@RequestParam(value = WRITER_NAME) Long writerId,
                                                                              Pageable pageable,
                                                                              PagedResourcesAssembler assembler){
        logger.info("Entry getBoardListByWriter, writerId : " + writerId.toString());
        Page<BoardReadDTO> boards = boardService.findByWriter(writerId, pageable);
        return ResponseEntity.ok(assembler.toResource(boards));
    }
}
