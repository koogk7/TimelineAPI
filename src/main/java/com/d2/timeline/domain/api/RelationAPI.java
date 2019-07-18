package com.d2.timeline.domain.api;


import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.common.ResponseHelper;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.dto.UserRelationDTO;
import com.d2.timeline.domain.service.RelationService;

import com.d2.timeline.domain.vo.UserRelation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import java.util.List;

import static com.d2.timeline.domain.Constant.SwaggerBoardConstant.DEFAULT_PAGE_SIZE;
import static com.d2.timeline.domain.Constant.SwaggerPageConstant.*;
import static com.d2.timeline.domain.Constant.SwaggerRelationConstant.*;

@Api(value = "UserRelation for API")
@RestController
@RequestMapping(value = "api/relations")
public class RelationAPI {

    private static final Logger logger = LoggerFactory.getLogger(BoardAPI.class);

    @Autowired
    RelationService relationService;

    @Autowired
    ResponseHelper helper;

    @PostMapping(value = "request")
    @ApiOperation(value = "팔로우 요청")
    public ResponseEntity<?> followRequest(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry followRequest, masterId : " + userRelationDTO.getMasterId().toString());
        String msg = relationService.followRequest(userRelationDTO);
        return helper.getResEntity(msg);
    }

    @PutMapping(value = "response/{" + RESPONSE_BOOLEAN + "}")
    @ApiOperation(value = "팔로우 요청 응답")
    public ResponseEntity<?> responseFollowing(@PathVariable(RESPONSE_BOOLEAN) boolean allow,
                                               @RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry responseFollowing, requesterId : " + userRelationDTO.getMasterId().toString()
                + ", repondentId : " + userRelationDTO.getSlaveId().toString() );
        String msg = relationService.responseForFollowRequest(userRelationDTO, allow);
        return helper.getResEntity(msg);
    }

    @DeleteMapping(value = "")
    @ApiOperation("팔로우 해제")
    public ResponseEntity<?> unfollow(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry unfollow, masterId : " + userRelationDTO.getMasterId().toString());
        String msg = relationService.unfollow(userRelationDTO);
        return helper.getResEntity(msg);
    }

    @PutMapping(value = "block")
    @ApiOperation(value = "관계변경 : 차단")
    public ResponseEntity<?> block(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry block, masterId : " + userRelationDTO.getMasterId().toString());
        String msg = relationService.block(userRelationDTO);
        return helper.getResEntity(msg);
    }

    @DeleteMapping(value = "unblock")
    @ApiOperation(value = "관계변경 : 차단해제")
    public ResponseEntity<?> unblock(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry unblock, masterId : " + userRelationDTO.getMasterId().toString());
        String msg = relationService.unblock(userRelationDTO);
        return helper.getResEntity(msg);
    }

    //TODO State종류가 뭐뭐있는지 swagger에 알려줘야함
    @GetMapping(value = "/{" + MEMBER_ID + "}/{" + RELATION_STATE + "}")
    @ApiOperation(value = "관계리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = PAGE_NAME, value = PAGE_DESC, defaultValue = DEFAULT_PAGE,
                    dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = SIZE_NAME, value = SIZE_DESC, defaultValue = DEFAULT_PAGE_SIZE,
                    dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = SORT_NAME, value = SORT_DESC, allowMultiple = true,
                    dataType = "string", paramType = "query")
    })
    public ResponseEntity<?> loadRelationListByState(@PathParam(MEMBER_ID) Long memberId,
                                                     @PathParam(RELATION_STATE) RelationState state,
                                                     Pageable pageable,
                                                     PagedResourcesAssembler assembler){

        logger.info("Entry loadRelationListByState, memberId : " + memberId.toString());
        Page<MemberDTO> members = relationService.showRelationList(memberId, state, pageable);
        return ResponseEntity.ok(assembler.toResource(members));
    }



}
