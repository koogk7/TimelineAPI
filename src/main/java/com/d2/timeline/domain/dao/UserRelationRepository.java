package com.d2.timeline.domain.dao;


import com.d2.timeline.domain.common.RelationState;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.UserRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {
    @Override
    Optional<UserRelation> findById(Long aLong);

    Optional<UserRelation> findByMasterAndSlave(Member master, Member slave);

    @Query("select r.slave from UserRelation r where r.master = (:master) and r.state = (:state)")
    Page<Member> findSlaveByMasterAndState(@Param("master")Member master, @Param("state")RelationState state, Pageable pageable);

    @Query("select r.master from UserRelation r where r.slave = (:slave) and r.state = (:state)")
    Page<Member> findMasterBySlaveAndState(@Param("slave")Member slave, @Param("state")RelationState state, Pageable pageable);

    @Query("select r.state from UserRelation  r where r.master = (:master) and r.slave = (:slave)")
    RelationState findStateByMasterAndSlave(@Param("master")Member master, @Param("slave")Member slave);

    boolean existsByMasterAndSlave(Member master, Member slave);
    boolean existsByMasterAndSlaveAndState(Member master, Member slave, RelationState state);

    @Transactional
    void deleteByMasterAndSlave(Member master, Member slave);
}
