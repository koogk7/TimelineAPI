package com.d2.timeline.domain.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.*;

public class RelationService {

    private static final Logger logger = LoggerFactory.getLogger(MemberFindService.class);

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRelationRepository userRelationRepo;

    public String followRequest(UserRelation userRelation){
        UserRelation relation = userRelationRepo.save(userRelation);
        return REQUEST_FOLLOW_OK_MSG;
    }

    public String changeState(Member master, Member slave, RelationState state){
        Optional<UserRelation> maybeUserRelation = userRelationRepo.findByMasterAndSlave(master, slave);
        UserRelation userRelation  = maybeUserRelation.orElseGet(()-> {
            return UserRelation.builder().id(-1L).build();
        });

        if(userRelation.getId() == -1){
            return STATE_CHANGE_ERROR_MSG;
        }

        userRelation.setState(state);
        userRelationRepo.save(userRelation);

        return STATE_CHANGE_OK_MSG;
    }

//    public String unblock(Member master, Member slave){
//        //unblock 동작 후에는 관계가 맺어진 row를 삭제해야한다.
//        //1->2 (BLOCK), 2->1 (BLOCKED) 둘다 삭제 해야함.
//    }

    public List<MemberDTO> showRelationList(Member master, RelationState state){

        List<MemberDTO> memberDTOList = new ArrayList<>();
        List<Member> relationMemberList = userRelationRepo.findByMasterAndState(master, state).orElseGet(()->{
            logger.info("관계가 존재하지 않습니다.");
            return Collections.emptyList();
        });

        relationMemberList.forEach(x -> memberDTOList.add(new MemberDTO(x)));

        return memberDTOList;
    }

    //TODO
//    public List<MemberDTO> showFollwerList(Member slave){
//
//    }
}
