package com.d2.timeline.domain.vo;

import com.d2.timeline.domain.common.BaseEntity;
import com.d2.timeline.domain.common.RelationState;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_relation_tb")
public class UserRelation extends BaseEntity {

    //TODO cascadeType.REMOVE -> 부모에 설정해야함(여기서는  Member.java)
    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "master_FK", nullable = false)
    private Member master;

    @ManyToOne//(cascade = CascadeType.REMOVE)
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

    @Override
    public boolean equals(Object obj) {
        UserRelation relationObj = (UserRelation)obj;
        return (this.getId() == relationObj.getId());
    }
}
