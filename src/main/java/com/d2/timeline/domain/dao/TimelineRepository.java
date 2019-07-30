package com.d2.timeline.domain.dao;

import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.Timeline;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, Long> {

    Page<Timeline> findByReceiver(Member receiver, Pageable pageable);
}
