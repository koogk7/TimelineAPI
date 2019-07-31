package com.d2.timeline.domain.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.d2.timeline.domain.Constant.BoardConstant.OK_MSG;
import static com.d2.timeline.domain.Constant.RelationServiceConstant.OK_MSG_RELATION;

@Component
public class ResponseHelper {
    public ResponseEntity<?> getResEntity(String msg){
        ResponseEntity<?> resEntity = null;

        if(msg.equals(OK_MSG))
            resEntity =  new ResponseEntity<>(HttpStatus.OK);
        if(msg.equals(OK_MSG_RELATION))
            resEntity =  new ResponseEntity<>(HttpStatus.OK);
        else
            resEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return resEntity;
    }
}
