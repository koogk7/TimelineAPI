package com.d2.timeline.domain.service;

import com.d2.timeline.domain.common.S3Uploader;
import com.d2.timeline.domain.dao.BoardRepository;
import com.d2.timeline.domain.dao.MemberRepository;
import com.d2.timeline.domain.dao.TimelineRepository;
import com.d2.timeline.domain.dto.BoardReadDTO;
import com.d2.timeline.domain.exception.EntityNotFoundException;
import com.d2.timeline.domain.exception.UnmatchedWriterException;
import com.d2.timeline.domain.vo.Board;
import com.d2.timeline.domain.vo.Member;
import com.d2.timeline.domain.vo.Timeline;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.d2.timeline.domain.Constant.BoardConstant.*;

@RequiredArgsConstructor
@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardService.class);

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final TimelineRepository timelineRepository;
    private final S3Uploader s3Uploader;

    public Page<BoardReadDTO> loadTimelineFromOrigin(String email, Pageable pageable){
        logger.info("Entry getTimlinePage");
        Page<Timeline> timeLines = getTimeline(email,pageable);
        return transTimelineToBoardReadDTO(timeLines);
    }

    public Page<Timeline> getTimeline(String email, Pageable pageable){
        logger.info("Entry getTimeline");
        Member member = memberRepository.findByEmail(email).orElseThrow(
                ()-> new EntityNotFoundException("올바른 요청자가 아닙니다."));

        return timelineRepository.findByReceiver(member, pageable);
    }


    public String saveBoard(String writerEmail, String content, MultipartFile img){
        Member writer =  memberRepository.findByEmail(writerEmail).
                orElseThrow(()-> new EntityNotFoundException("작성자가 존재하지 않습니다."));
        String img_url = "";

        try {
            img_url = s3Uploader.upload(img, "board");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Board newBoard = Board.builder()
                .writer(writer)
                .contentText(content)
                .contentImg(img_url)
                .build();

        boardRepository.save(newBoard);

        Timeline timeline =Timeline.builder()
                .receiver(writer)
                .board(newBoard)
                .build();

        timelineRepository.save(timeline);
        return OK_MSG;
    }

    public String updateBoard(String requestEmail, Long boardId, String boardContent, MultipartFile boardImg){
        Board updateBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new EntityNotFoundException("올바른 게시물 아닙니다."));
        String imgUrl = "";
        validateRequest(requestEmail, updateBoard);

        try {
            imgUrl = s3Uploader.upload(boardImg, "board");
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateBoard.setContentText(boardContent);
        updateBoard.setContentImg(imgUrl);

        boardRepository.save(updateBoard);
        return OK_MSG;
    }

    public String deleteBoard(String requestEmail, Long deleteBoard){
        Board board = boardRepository.findById(deleteBoard).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 게시물입니다."));

        validateRequest(requestEmail, board);
        boardRepository.deleteById(deleteBoard);
        return OK_MSG;
    }

    public BoardReadDTO findByBoardId(Long boardId){
        Board targetBoard = boardRepository.findById(boardId).orElseThrow(
                ()-> new EntityNotFoundException("존재하지 않는 게시물입니다."));
        return new BoardReadDTO(targetBoard);
    }

    public Page<BoardReadDTO> findByWriter(Long writerId, Pageable pageable){
        Page<Board> resultBoardList = boardRepository.findByWriterUid(writerId, pageable);
        return resultBoardList.map(BoardReadDTO::new);
    }

    public Page<BoardReadDTO> transTimelineToBoardReadDTO(Page<Timeline> timelinePage)
    {
        logger.info("Entry transTimelineToBoardReadDTO");
        timelinePage.forEach(timeline -> logger.info(timeline.toString()));
        return timelinePage.map(timeline -> new BoardReadDTO(timeline.getBoard()));
    }

    private void validateRequest(String request, Board board){
        String writerEmail = board.getWriter().getEmail();
        if(!request.equals(writerEmail))
            throw new UnmatchedWriterException("작성자가 아닙니다");
    }


}