package com.d2.timeline.domain.dao;


import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    @Override
    Optional<UserRelation> findById(Long aLong);

    Optional<UserRelation> findByMasterAndState(Member master, RelationState state);
}
