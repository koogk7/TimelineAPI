package com.d2.timeline.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.dto.UserRelationDTO;
import com.d2.timeline.domain.exception.NotExistRelationException;
import com.d2.timeline.domain.exception.UnmatchedRequestorException;
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
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.NoResultException;
import javax.validation.constraints.Null;


@WebAppConfiguration
public class RelationServiceTest extends MockTest {

    @InjectMocks
    private RelationService relationService;

    @Mock
    private UserRelationRepository userRelationRepo;

    @Mock
    private MemberRepository memberRepository;

    private Member master;
    private Member slave;
    private String masterEmail;
    private String slaveEmail;
    private UserRelation requestRelation;
    private UserRelation followRelation;
    private RelationState relationState;
    private UserRelationDTO userRelationDTO;
    private MemberDTO memberDTO;

    @Before
    public void setUp() throws Exception {
        masterEmail = "test1@test.com";
        slaveEmail = "test2@test.com";

        master = Member.builder()
                .id(1L)
                .email(masterEmail)
                .build();
        slave = Member.builder()
                .id(2L)
                .email(slaveEmail)
                .build();
        relationState = RelationState.REQUEST;
        requestRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(relationState)
                .build();
        followRelation = UserRelation.builder()
                .id(2L)
                .master(master)
                .slave(slave)
                .state(RelationState.FOLLOW)
                .build();
        userRelationDTO = UserRelationDTO.builder()
                .masterId(master.getId())
                .slaveId(slave.getId())
                .build();

    }

    @Test
    public void 팔로우요청_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master, slave)).willReturn(Optional.of(requestRelation));


        //when
        final String returnMessage = relationService.followRequest(masterEmail, userRelationDTO);

        //then
        assertEquals(FOLLOW_REQUEST_OK_MSG, returnMessage);
    }

    @Test(expected = UnmatchedRequestorException.class)
    public void 팔로우요청_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));

        //when
        final String returnMessage = relationService.followRequest("test3@test.com", userRelationDTO);

        //then
    }

    @Test
    public void 팔로우요청_수락_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.existsByMasterAndSlaveAndState(master, slave, relationState)).willReturn(true);

        //when
        final String returnMessage = relationService.responseForFollowRequest(slaveEmail, userRelationDTO, true);

        //then
        assertEquals(FOLLOW_RESPONSE_OK_MSG, returnMessage);
    }
    @Test
    public void 팔로우요청_거절_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.existsByMasterAndSlaveAndState(master, slave, relationState)).willReturn(true);
        given(userRelationRepo.existsByMasterAndSlave(master, slave)).willReturn(true);

        //when
        final String returnMessage = relationService.responseForFollowRequest(slaveEmail, userRelationDTO, false);

        //then
        assertEquals(FOLLOW_RESPONSE_NO_MSG, returnMessage);

    }
    @Test(expected = UnmatchedRequestorException.class)
    public void 팔로우요청_응답_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));

        //when
        final String returnMessage = relationService.responseForFollowRequest("test3@test.com", userRelationDTO, true);

        //then
    }

    @Test
    public void 언팔로우_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.of(followRelation));
        given(userRelationRepo.existsByMasterAndSlave(master, slave)).willReturn(true);

        //when
        final String returnMessage = relationService.unfollow(masterEmail, userRelationDTO);

        //then
        assertEquals(OK_MSG_RELATION, returnMessage);
    }

    @Test(expected = UnmatchedRequestorException.class)
    public void 언팔로우_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.of(followRelation));

        //when
        final String returnMessage = relationService.unfollow("test3@test.com", userRelationDTO);

        //then
    }

    @Test
    public void 차단_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.empty());

        //when
        final String returnMessage = relationService.block(masterEmail, userRelationDTO);

        //then
        assertEquals(OK_MSG_RELATION, returnMessage);
    }

    @Test(expected = UnmatchedRequestorException.class)
    public void 차단_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));

        //when
        final String returnMessage = relationService.block("test3@test.com", userRelationDTO);
    }

    @Test
    public void 차단해제_성공(){//OK
        //given
        final UserRelation forwardRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(RelationState.BLOCK)
                .build();
        final UserRelation reverseRelation = UserRelation.builder()
                .id(2L)
                .master(slave)
                .slave(master)
                .state(RelationState.BLOCKED)
                .build();

        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.of(forwardRelation));
        given(userRelationRepo.findByMasterAndSlave(slave,master)).willReturn(Optional.of(reverseRelation));
        given(userRelationRepo.existsByMasterAndSlave(master,slave)).willReturn(true);
        given(userRelationRepo.existsByMasterAndSlave(slave,master)).willReturn(true);

        //when
        final String returnMessage = relationService.unblock(masterEmail, userRelationDTO);

        //then
        assertEquals(OK_MSG_RELATION, returnMessage);
    }

    @Test(expected = NotExistRelationException.class)
    public void 차단관계아닐때_차단해제_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.of(requestRelation));
        given(userRelationRepo.findByMasterAndSlave(slave,master)).willReturn(Optional.of(followRelation));

        //when
        final String returnMessage = relationService.unblock(masterEmail, userRelationDTO);

        //then
    }

    @Test(expected = UnmatchedRequestorException.class)
    public void 서비스요청자불일치_차단해제_실패(){//OK
        final UserRelation forwardRelation = UserRelation.builder()
                .id(1L)
                .master(master)
                .slave(slave)
                .state(RelationState.BLOCK)
                .build();
        final UserRelation reverseRelation = UserRelation.builder()
                .id(2L)
                .master(slave)
                .slave(master)
                .state(RelationState.BLOCKED)
                .build();

        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.findByMasterAndSlave(master,slave)).willReturn(Optional.of(forwardRelation));
        given(userRelationRepo.findByMasterAndSlave(slave,master)).willReturn(Optional.of(reverseRelation));

        //when
        final String returnMessage = relationService.unblock("test3@test.com", userRelationDTO);

        //then
    }

    @Test
    public void 관계리스트_출력_성공(){//OK
        //given
        Page<MemberDTO> memberDTOPage;
        List<Member> memberList = new ArrayList<>();

        Member member = Member.builder()
                .id(3L)
                .email("test3@test.com")
                .build();

        memberList.add(member);
        memberList.add(slave);

        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<Member> memberPage = new PageImpl<Member>(memberList, PageRequest.of(0, 5),10);
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(userRelationRepo.findSlaveByMasterAndState(master, RelationState.FOLLOW, pageRequest)).willReturn(memberPage);

        //when
        memberDTOPage = relationService.showRelationList(master.getId(), RelationState.FOLLOW, pageRequest);

        //then
        assertThat(memberDTOPage, contains(
                hasProperty("id", is(3L)),
                hasProperty("id", is(2L))
        ));
    }

    @Test
    public void 팔로워_관계리스트_출력_성공(){//OK
        //given
        Page<MemberDTO> memberDTOPage;
        List<Member> memberList = new ArrayList<>();

        Member member = Member.builder()
                .id(3L)
                .email("test3@test.com")
                .build();

        memberList.add(member);
        memberList.add(slave);

        PageRequest pageRequest = PageRequest.of(0, 5);

        Page<Member> memberPage = new PageImpl<Member>(memberList, PageRequest.of(0, 5),10);
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(userRelationRepo.findMasterBySlaveAndState(master, RelationState.FOLLOW, pageRequest)).willReturn(memberPage);

        //when
        memberDTOPage = relationService.showRelationList(master.getId(), RelationState.FOLLOWED, pageRequest);

        //then
        assertThat(memberDTOPage, contains(
                hasProperty("id", is(3L)),
                hasProperty("id", is(2L))
        ));
    }

    @Test(expected = NoResultException.class)
    public void 관계리스트_출력_실패(){//OK
        //given
        Page<MemberDTO> memberDTOPage;
        PageRequest pageRequest = PageRequest.of(0, 5);
        given(memberRepository.findById(master.getId())).willReturn(Optional.empty());

        //when
        memberDTOPage = relationService.showRelationList(master.getId(), relationState, pageRequest);

        //then
    }

    @Test
    public void 관계확인_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.existsByMasterAndSlave(master, slave)).willReturn(true);
        given(userRelationRepo.findStateByMasterAndSlave(master, slave)).willReturn(RelationState.FOLLOW);

        //when
        RelationState returnState = relationService.verifyRelation(master.getId(), slave.getId());

        //then
        assertEquals(RelationState.FOLLOW, returnState);
    }

    @Test
    public void 없는관계확인_성공(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.of(master));
        given(memberRepository.findById(slave.getId())).willReturn(Optional.of(slave));
        given(userRelationRepo.existsByMasterAndSlave(master, slave)).willReturn(false);

        //when
        RelationState returnState = relationService.verifyRelation(master.getId(), slave.getId());

        //then
        assertEquals(RelationState.NONE, returnState);
    }

    @Test(expected = NoResultException.class)
    public void 관계확인_실패(){//OK
        //given
        given(memberRepository.findById(master.getId())).willReturn(Optional.empty());

        //when
        RelationState relationState = relationService.verifyRelation(master.getId(), slave.getId());

        //then
    }

}