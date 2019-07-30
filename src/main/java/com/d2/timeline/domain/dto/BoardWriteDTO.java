package com.d2.timeline.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class BoardWriteDTO {

    @NotNull
    @ApiModelProperty(value = "본문 내용")
    private String contentText;

    @NotNull
    @ApiModelProperty(value = "본문 사진")
    private MultipartFile contentImg;

    @Builder
    public BoardWriteDTO(String contentText, MultipartFile contentImg){
        this.contentText = contentText;
        this.contentImg = contentImg;
    }
}
