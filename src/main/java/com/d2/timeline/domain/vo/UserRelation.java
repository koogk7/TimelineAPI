package com.d2.timeline.domain.vo;

import com.d2.timeline.domain.common.BaseEntity;
import com.d2.timeline.domain.common.RelationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "user_relation_tb")
public class UserRelation extends BaseEntity {

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "master_FK", nullable = false)
    private Member master;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "slave_FK", nullable = false)
    private Member slave;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Column(name = "state")
    private RelationState state;

    @Builder
    public UserRelation(Long id, Member master, Member slave, RelationState state){
        super(id);
        this.master = master;
        this.slave = slave;
        this.state = state;
    }
}
