package com.d2.timeline.domain.dao;

import com.d2.timeline.domain.vo.TimelineRedis;
import org.springframework.data.repository.CrudRepository;


public interface TimelineRedisRepository extends CrudRepository<TimelineRedis, Long> {
}
