package com.d2.timeline.domain.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.dto.UserRelationDTO;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.*;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.*;

@Service
public class RelationService {

    private static final Logger logger = LoggerFactory.getLogger(MemberFindService.class);


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRelationRepository userRelationRepo;

    public UserRelation createRelation(Member master, Member slave, RelationState state){

        Optional<UserRelation> maybeUserRelation = userRelationRepo.findByMasterAndSlave(master, slave);
        UserRelation userRelation = maybeUserRelation.orElseGet(()->{
           return UserRelation.builder()
                   .master(master)
                   .slave(slave)
                   .state(state)
                   .build();
        });

        userRelation.setState(state);
        userRelationRepo.save(userRelation);
        return userRelation;
    }

    public String deleteRelation(Member master, Member slave){

        boolean exist = userRelationRepo.existsByMasterAndSlave(master, slave);

        if(!exist) {
            return ERROR_NOT_EXIST;
        }

        userRelationRepo.deleteByMasterAndSlave(master, slave);
        return OK_MSG_RELATION;
    }

    public String updateState(Member master, Member slave, RelationState state){

        Optional<UserRelation> maybeUserRelation = userRelationRepo.findByMasterAndSlave(master, slave);
        UserRelation userRelation  = maybeUserRelation.orElseGet(
                ()-> createRelation(master, slave, state));

        userRelation.setState(state);
        userRelationRepo.save(userRelation);

        return STATE_CHANGE_OK_MSG;
    }

    public String followRequest(UserRelationDTO userRelationDTO){
        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        createRelation(master, slave, RelationState.REQUEST);
        return FOLLOW_REQUEST_OK_MSG;
    }

    public String responseForFollowRequest(UserRelationDTO userRelationDTO, boolean allow){
        Member requester = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member respondent = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        boolean exist = userRelationRepo.existsByMasterAndSlaveAndState(requester, respondent, RelationState.REQUEST);

        if(!exist) {
            return ERROR_NOT_EXIST;
        }

        if(!allow){
            deleteRelation(requester, respondent);
            return FOLLOW_RESPONSE_NO_MSG;
        }

        updateState(requester, respondent, RelationState.FOLLOW);

        return FOLLOW_RESPONSE_OK_MSG;
    }

    public String unfollow(UserRelationDTO userRelationDTO){
        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        UserRelation userRelation = userRelationRepo.findByMasterAndSlave(master, slave).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        if(userRelation.getState() != RelationState.FOLLOW){
            return ERROR_BAD_REQUEST;
        }

        deleteRelation(master, slave);
        return OK_MSG_RELATION;
    }

    public String block(UserRelationDTO userRelationDTO){

        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        updateState(master, slave, RelationState.BLOCK);
        updateState(slave, master, RelationState.BLOCKED);
        return OK_MSG_RELATION;

    }

    public String unblock(UserRelationDTO userRelationDTO){

        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        UserRelation userRelation = userRelationRepo.findByMasterAndSlave(master, slave).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        UserRelation reverseRelation = userRelationRepo.findByMasterAndSlave(slave, master).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        boolean canUnblockState = (userRelation.getState() == RelationState.BLOCK)
                && (reverseRelation.getState() == RelationState.BLOCKED);
        if(!canUnblockState){
            return ERROR_BAD_REQUEST;
        }

        deleteRelation(master, slave);
        deleteRelation(slave, master);

        return OK_MSG_RELATION;
    }


    public Page<MemberDTO> showRelationList(Long memberId, RelationState state, Pageable pageable){
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        if(state == RelationState.FOLLOWED){
            return showFollowerList(memberId, pageable);
        }
        Page<Member> relationMemberList = userRelationRepo.findSlaveByMasterAndState(member, state, pageable);

        return relationMemberList.map(MemberDTO::new);
    }

    public Page<MemberDTO> showFollowerList(Long memberId, Pageable pageable){
        RelationState state = RelationState.FOLLOW;
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Page<Member> relationMemberList = userRelationRepo.findMasterBySlaveAndState(member, state, pageable);

        return relationMemberList.map(MemberDTO::new);
    }





}
