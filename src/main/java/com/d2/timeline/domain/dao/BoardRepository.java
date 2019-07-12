package com.d2.timeline.domain.dao;

import com.d2.timeline.domain.vo.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    Optional<Board> findById(Long id);

    @Query("select b from Board b where b.writer.id = (:uid)")
    Page<Board> findByWriterUid(@Param("uid") Long uid, Pageable pageable);

    @Query("select b from Board b where b.writer.id in (:uids)")
    Optional<List<Board>> findByWriterUidIn(@Param("uids") List<Object> uids);

    boolean existsById(Long id);
}
