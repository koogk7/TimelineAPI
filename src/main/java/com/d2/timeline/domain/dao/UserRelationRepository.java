package com.d2.timeline.domain.dao;


import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    @Override
    Optional<UserRelation> findById(Long aLong);

    Optional<UserRelation> findByMasterAndSlave(Member master, Member slave);

    @Query("select r.slave from UserRelation r where r.master = (:master) and r.state = (:state)")
    Optional<List<Member>> findByMasterAndState(@Param("master")Member master, @Param("state")RelationState state);

    @Query("select r.master from UserRelation r where r.slave = (:slave) and r.state = (:state)")
    List<Member> findBySlaveAndState(@Param("slave")Member slave, @Param("state")RelationState state);
}
