package com.d2.timeline.dao;

import com.d2.timeline.domain.dao.TimelineRedisRepository;
import com.d2.timeline.domain.vo.TimelineRedis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class TimelineRedisTest{

    @Autowired
    TimelineRedisRepository timelineRedisRepository;

    @Test
    @Transactional
    public void 기동_등록_조회기능(){
        Long id = 3L;
        LocalDateTime refreshTime = LocalDateTime.now();
        TimelineRedis timelineRedis = TimelineRedis.builder()
                .id(id)
                .user_FK(1L)
                .board_FK(1L)
                .refreshTime(refreshTime)
                .build();

        timelineRedisRepository.save(timelineRedis);

        TimelineRedis saved = timelineRedisRepository.findById(id).orElseGet(() -> {
            return TimelineRedis.builder().id(-1L).build();
        });

        assertEquals(id, saved.getId());
        assertEquals(refreshTime, saved.getRefreshTime());

    }

}
