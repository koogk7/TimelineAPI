package com.d2.timeline.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.service.RelationService;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.REQUEST_FOLLOW_OK_MSG;
import static com.d2.timeline.domain.Constant.RelationServiceConstant.STATE_CHANGE_OK_MSG;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

public class RelationServiceTest extends MockTest {

    @InjectMocks
    private RelationService relationService;

    @Mock
    private UserRelationRepository userRelationRepo;
    private Member master;
    private Member slave;
    private UserRelation userRelation;

    @Before
    public void setUp() throws Exception {
        master = Member.builder().id(1L).build();
        slave = Member.builder().id(2L).build();
        userRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(RelationState.REQUEST)
                .build();
    }

    @Test
    public void 친구요청_성공(){
        //given
        given(userRelationRepo.save(userRelation)).willReturn(userRelation);

        //when
        final String returnMessage = relationService.followRequest(userRelation);

        //then
        assertEquals(REQUEST_FOLLOW_OK_MSG, returnMessage);
    }

    @Test
    public void 관계상태변경_성공() {
        //given
        final RelationState changeState = RelationState.FOLLOW;

        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(userRelation));

        //when
        final String returnMessage = relationService.changeState(master, slave, changeState);

        //then
        assertEquals(STATE_CHANGE_OK_MSG, returnMessage);

    }

    @Test
    public void 관계목록출력_성공() {
        //given
        final RelationState state = RelationState.FOLLOW;
        given(userRelationRepo.findByMasterAndState(master, state))
    }
}