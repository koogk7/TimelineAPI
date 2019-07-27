package com.d2.timeline.domain.dto;


import com.d2.timeline.domain.vo.UserRelation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRelationDTO {

    private Long masterId;
    private Long slaveId;

    public UserRelationDTO(UserRelation userRelation){

        this.masterId = userRelation.getMaster().getId();
        this.slaveId = userRelation.getSlave().getId();
    }

    @Builder
    public UserRelationDTO(Long masterId, Long slaveId){

        this.masterId = masterId;
        this.slaveId = slaveId;
    }
}
