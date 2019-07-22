package com.d2.timeline.domain.api;


import com.d2.timeline.domain.common.AuthenticationHelper;
import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.dto.UserRelationDTO;
import com.d2.timeline.domain.service.RelationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

import static com.d2.timeline.domain.Constant.SwaggerBoardConstant.DEFAULT_PAGE_SIZE;
import static com.d2.timeline.domain.Constant.SwaggerPageConstant.*;
import static com.d2.timeline.domain.Constant.SwaggerRelationConstant.*;

@Api(value = "UserRelation for API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/relations")
public class RelationAPI {

    private static final Logger logger = LoggerFactory.getLogger(BoardAPI.class);

    private final RelationService relationService;
    private final AuthenticationHelper authHelper;

    @PostMapping(value = "follow/request")
    @ApiOperation(value = "팔로우 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> followRequest(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry followRequest, masterId : " + userRelationDTO.getMasterId().toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = relationService.followRequest(requestEmail, userRelationDTO);
        return ResponseEntity.ok(msg);
    }

    @PutMapping(value = "follow/response")
    @ApiOperation(value = "팔로우 요청 응답")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> responseFollowing(boolean allow,
                                               @RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry responseFollowing, requesterId : " + userRelationDTO.getMasterId().toString()
                + ", repondentId : " + userRelationDTO.getSlaveId().toString() );
        String requestEmail = authHelper.getEmailFormToken();
        String msg = relationService.responseForFollowRequest(requestEmail, userRelationDTO, allow);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping(value = "")
    @ApiOperation("팔로우 해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> unfollow(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry unfollow, masterId : " + userRelationDTO.getMasterId().toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = relationService.unfollow(requestEmail, userRelationDTO);
        return ResponseEntity.ok(msg);
    }

    @PutMapping(value = "block")
    @ApiOperation(value = "관계변경 : 차단")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> block(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry block, masterId : " + userRelationDTO.getMasterId().toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = relationService.block(requestEmail, userRelationDTO);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping(value = "unblock")
    @ApiOperation(value = "관계변경 : 차단해제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token",
                    required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> unblock(@RequestBody UserRelationDTO userRelationDTO){

        logger.info("Entry unblock, masterId : " + userRelationDTO.getMasterId().toString());
        String requestEmail = authHelper.getEmailFormToken();
        String msg = relationService.unblock(requestEmail, userRelationDTO);
        return ResponseEntity.ok(msg);
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

    @GetMapping(value = "/{" + MASTER_ID + "}/{" + SLAVE_ID + "}")
    @ApiOperation(value = "관계 조회")
    public ResponseEntity<?> loadRelationState(@PathParam(MASTER_ID) Long masterId,
                                               @PathParam(SLAVE_ID) Long slaveId){

        logger.info("Entry loadRelationState, masterId : " + masterId);
        return ResponseEntity.ok(relationService.verifyRelation(masterId, slaveId));
    }




}
