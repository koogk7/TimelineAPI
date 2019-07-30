package com.d2.timeline.domain.common;

public enum RelationState {
    FOLLOW, REQUEST, BLOCK, BLOCKED, FOLLOWED, REQUESTED, NONE
}
//FOLLOWED, REQUESTED, NONE 는 실제로 데이터베이스에 state로 존재하지 않는다
// 리스트 조회시 API를 간단하게 하도록 하기 위해서 추가 하였다.