package com.d2.timeline.domain.dto;


import com.d2.timeline.domain.vo.UserRelation;
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
}
