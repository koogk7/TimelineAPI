package com.d2.timeline.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.service.RelationService;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

public class RelationServiceTest extends MockTest {

    @InjectMocks
    private RelationService relationService;

    @Mock
    private UserRelationRepository userRelationRepo;
    private Member master;
    private Member slave;
    private UserRelation userRelation;
    private RelationState relationState;

    @Before
    public void setUp() throws Exception {
        master = Member.builder().id(1L).build();
        slave = Member.builder().id(2L).build();
        relationState = RelationState.REQUEST;
        userRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(relationState)
                .build();
    }

    @Test
    public void 팔로우요청_성공(){
        //given
//        given(userRelationRepo.save(userRelation)).willReturn(userRelation);

        //when
//        final String returnMessage = relationService.followRequest(userRelation);

        //then
//        assertEquals(FOLLOW_REQUEST_OK_MSG, returnMessage);
    }
    @Test
    public void 팔로우요청수락_성공(){
        //given
        given(userRelationRepo.save(userRelation)).willReturn(userRelation);
        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));

        //when
        final String returnMessage = relationService.responseForFollowRequest(master, slave, true);

        //then
        assertEquals(FOLLOW_RESPONSE_OK_MSG, returnMessage);
    }

    @Test
    public void 팔로우요청거절_성공(){
        //given
        given(userRelationRepo.save(userRelation)).willReturn(userRelation);
        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));

        //when
        final String returnMessage = relationService.responseForFollowRequest(master, slave, false);

        //then
        assertEquals(FOLLOW_RESPONSE_NO_MSG, returnMessage);
    }

    @Test
    public void 관계상태변경_성공() {
        //given
        final RelationState changeState = RelationState.BLOCK;
        given(userRelationRepo.save(userRelation)).willReturn(userRelation);
        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));

        //when
        final String returnMessage = relationService.updateState(master, slave, changeState);

        //then
        assertEquals(STATE_CHANGE_OK_MSG, returnMessage);

    }

    @Test
    public void 관계삭제_성공(){
        final UserRelation forwardRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(RelationState.BLOCK)
                .build();
    }

    @Test
    public void 차단해제_성공(){
        final UserRelation forwardRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(RelationState.BLOCK)
                .build();
        final UserRelation reverseRelation = UserRelation.builder()
                .id(1L)
                .master(slave)
                .slave(master)
                .state(RelationState.BLOCKED)
                .build();

//        given(userRelationRepo.findByMasterAndSlave(master, slave))



    }

    @Test
    public void 관계목록출력_성공() {
        //given
        final List<Member> relationMemberList = new ArrayList<>();
        relationMemberList.add(slave);
        final List<MemberDTO> memberDTOList = new ArrayList<>();
        relationMemberList.forEach(x -> memberDTOList.add(new MemberDTO(x)));

        given(userRelationRepo.findSlaveByMasterAndState(master, relationState)).willReturn(Optional.of(relationMemberList));


        //when
        final List<MemberDTO> returnMemberDTOList = relationService.showRelationList(master, relationState);

        //then
        assertArrayEquals(memberDTOList.toArray(), returnMemberDTOList.toArray());
    }

}