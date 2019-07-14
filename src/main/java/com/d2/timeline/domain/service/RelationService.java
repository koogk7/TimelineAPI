package com.d2.timeline.domain.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
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

    public UserRelation createRelation(Member master, Member slave, RelationState state){
        UserRelation userRelation = UserRelation.builder()
                .master(master)
                .slave(slave)
                .state(state)
                .build();

        return userRelation;
    }

    public String deleteRelation(Member master, Member slave){
        boolean exist = userRelationRepo.existsByMasterAndSlave(master, slave);

        if(!exist) {
            return "NOT EXIST";
        }

        userRelationRepo.deleteByMasterAndSlave(master, slave);
        return "OK";
    }

    public String updateState(Member master, Member slave, RelationState state){

        Optional<UserRelation> maybeUserRelation = userRelationRepo.findByMasterAndSlave(master, slave);
        UserRelation userRelation  = maybeUserRelation.orElseGet(()->createRelation(master, slave, state));

        if(userRelation.getId() == -1){
            return STATE_CHANGE_ERROR_MSG;
        }

        userRelation.setState(state);
        userRelationRepo.save(userRelation);

        return STATE_CHANGE_OK_MSG;
    }

    public String followRequest(Member master, Member slave){
        createRelation(master, slave, RelationState.REQUEST);
        return FOLLOW_REQUEST_OK_MSG;
    }

    public String responseForFollowRequest(Member requester, Member respondent, boolean allow){

        if(!allow){
            deleteRelation(requester, respondent);
            return FOLLOW_RESPONSE_NO_MSG;
        }

        updateState(requester, respondent, RelationState.FOLLOW);

        return FOLLOW_RESPONSE_OK_MSG;
    }

    public String block(Member master, Member slave){

        updateState(master, slave, RelationState.BLOCK);
        updateState(slave, master, RelationState.BLOCKED);
        return "OK";

    }

    public String unblock(Member master, Member slave){

        deleteRelation(master, slave);
        deleteRelation(slave, master);

        return "OK";
    }

    public List<MemberDTO> showRelationList(Member member, RelationState state){

        List<MemberDTO> memberDTOList = new ArrayList<>();
        List<Member> relationMemberList = userRelationRepo.findSlaveByMasterAndState(member, state).orElseGet(()->{
            logger.info(RELATION_NO_EXIST_MSG);
            return Collections.emptyList();
        });

        relationMemberList.forEach(x -> memberDTOList.add(new MemberDTO(x)));

        return memberDTOList;
    }

    public List<MemberDTO> showFollowerList(Member member){

        List<MemberDTO> memberDTOList = new ArrayList<>();
        List<Member> relationMemberList = userRelationRepo.findMasterBySlaveAndState(member, RelationState.FOLLOW).orElseGet(()->{
            logger.info(RELATION_NO_EXIST_MSG);
            return Collections.emptyList();
        });

        relationMemberList.forEach(x -> memberDTOList.add(new MemberDTO(x)));

        return memberDTOList;
    }




}
