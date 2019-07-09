package com.d2.timeline.domain.api;

import com.d2.timeline.domain.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimelineAPI {
    @Autowired
    BoardService boardService;
}
