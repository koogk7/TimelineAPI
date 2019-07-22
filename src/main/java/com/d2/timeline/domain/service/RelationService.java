package com.d2.timeline.domain.service;

import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.UserRelationRepository;
import com.d2.timeline.domain.dto.MemberDTO;
import com.d2.timeline.domain.dto.UserRelationDTO;
import com.d2.timeline.domain.exception.NotExistRelationException;
import com.d2.timeline.domain.exception.UnmatchedRequestorException;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.*;

import static com.d2.timeline.domain.Constant.RelationServiceConstant.*;

@RequiredArgsConstructor
@Service
public class RelationService {

    private static final Logger logger = LoggerFactory.getLogger(MemberFindService.class);


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UserRelationRepository userRelationRepo;

    private UserRelation createRelation(Member master, Member slave, RelationState state){

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

    private String deleteRelation(Member master, Member slave){

        boolean exist = userRelationRepo.existsByMasterAndSlave(master, slave);

        isExist(exist);
        userRelationRepo.deleteByMasterAndSlave(master, slave);
        return OK_MSG_RELATION;
    }

    private String updateRelation(Member master, Member slave, RelationState state){

        Optional<UserRelation> maybeUserRelation = userRelationRepo.findByMasterAndSlave(master, slave);
        UserRelation userRelation  = maybeUserRelation.orElseGet(
                ()-> createRelation(master, slave, state));

        userRelation.setState(state);
        userRelationRepo.save(userRelation);

        return STATE_CHANGE_OK_MSG;
    }

    public String followRequest(String requestEmail, UserRelationDTO userRelationDTO){
        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        validateMember(requestEmail, master);

        createRelation(master, slave, RelationState.REQUEST);
        return FOLLOW_REQUEST_OK_MSG;
    }

    public String responseForFollowRequest(String requestEmail,UserRelationDTO userRelationDTO, boolean allow){
        Member requester = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member respondent = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        boolean exist = userRelationRepo.existsByMasterAndSlaveAndState(requester, respondent, RelationState.REQUEST);
        isExist(exist);

        if(!allow){
            deleteRelation(requester, respondent);
            return FOLLOW_RESPONSE_NO_MSG;
        }
        validateMember(requestEmail, respondent);

        updateRelation(requester, respondent, RelationState.FOLLOW);

        return FOLLOW_RESPONSE_OK_MSG;
    }

    public String unfollow(String requestEmail,UserRelationDTO userRelationDTO){
        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        UserRelation userRelation = userRelationRepo.findByMasterAndSlave(master, slave).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        boolean existFollow = (userRelation.getState() == RelationState.FOLLOW);

        isExist(existFollow);
        validateMember(requestEmail, master);

        deleteRelation(master, slave);
        return OK_MSG_RELATION;
    }

    public String block(String requestEmail, UserRelationDTO userRelationDTO){

        Member master = (memberRepository.findById(userRelationDTO.getMasterId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(userRelationDTO.getSlaveId())).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));

        validateMember(requestEmail, master);

        updateRelation(master, slave, RelationState.BLOCK);
        updateRelation(slave, master, RelationState.BLOCKED);
        return OK_MSG_RELATION;

    }

    public String unblock(String requestEmail, UserRelationDTO userRelationDTO){

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
        isExist(canUnblockState);
        validateMember(requestEmail, master);

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


    private void validateMember(String request, Member member){
        String requestorEmail = member.getEmail();
        if(!request.equals(requestorEmail)){
            throw new UnmatchedRequestorException("권한이 없는 계정입니다.");
        }
    }

    private void isExist(boolean exist){
        if(!exist){
            throw new NotExistRelationException("존재하지 않는 관계입니다.");

        }
    }

    public RelationState verifyRelation(Long masterId, Long slaveId){

        Member master = (memberRepository.findById(masterId)).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        Member slave = (memberRepository.findById(slaveId)).orElseThrow(
                ()-> new NoResultException(ERROR_NOT_EXIST));
        boolean existRelation = userRelationRepo.existsByMasterAndSlave(master, slave);
;
        if(!existRelation){
            return RelationState.NONE;
        }
        return userRelationRepo.findStateByMasterAndSlave(master, slave);
    }
}
