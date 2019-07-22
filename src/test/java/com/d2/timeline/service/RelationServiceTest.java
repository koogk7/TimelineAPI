package com.d2.timeline.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.service.RelationService;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.apache.catalina.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import org.powermock.reflect.internal.WhiteboxImpl;


public class RelationServiceTest extends MockTest {

    @InjectMocks
    private RelationService relationService;

//    RelationService privateRelationService;

    @Mock
    private UserRelationRepository userRelationRepo;

    private Member master;
    private Member slave;
    private UserRelation userRelation;
    private RelationState relationState;

    @Before
    public void setUp() throws Exception {
        relationService = new RelationService();
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
    public void Create_관계생성_성공() throws Exception {
        //given
//        Method createRelation = relationService.getClass().getDeclaredMethod("createRelation", Member.class, Member.class, RelationState.class);
        given(userRelationRepo.save(userRelation)).willReturn(userRelation);
        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));

        //when
        final UserRelation returnRelation = WhiteboxImpl.invokeMethod(relationService, "createRelation", master, slave, relationState);

        //then
        assertEquals(userRelation, returnRelation);

    }

    @Test
    public void Delete_관계삭제_성공() throws Exception {
        //given
//        Method deleteRelation = relationService.getClass().getDeclaredMethod("deleteRelation", Member.class, Member.class);
        given(userRelationRepo.existsByMasterAndSlave(master, slave)).willReturn(true);

        //when
        final String returnMessage = WhiteboxImpl.invokeMethod(relationService, "deleteRelation", master, slave);

        //then
        assertEquals("OK", returnMessage);
    }
//
//    @Test
//    public void Update_관계변경_성공(){
//        final RelationState changeState = RelationState.BLOCK;
//        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));
//
//        final String returnString = relationService.updateRelation(master, slave, changeState);
//
//        assertEquals(STATE_CHANGE_OK_MSG, returnString);
//    }


    //TODO 팔로우 요청, 팔로우요청 수락, 팔로우요청 거절, 차단, 차단해제, followerList출력 test code작성해야함
/*
    @Test
    public void 차단_성공(){

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

    }

 */
}