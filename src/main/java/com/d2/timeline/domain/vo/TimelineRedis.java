package com.d2.timeline.domain.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@RedisHash("timeline")
public class TimelineRedis implements Serializable {

    @Id
    private Long id;
    private Long user_FK;
    private Long board_FK;
    private LocalDateTime refreshTime;

    public void refresh(Long user_FK, Long board_FK, LocalDateTime refreshTime){
        if(refreshTime.isAfter(this.refreshTime)){
            this.user_FK = user_FK;
            this.board_FK = board_FK;
            this.refreshTime = refreshTime;
        }
    }

}
