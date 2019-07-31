package com.d2.timeline.domain.common;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private static final Logger logger =LoggerFactory.getLogger(S3Uploader.class);
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }


    private String putS3(File uploadFile, String fileName){
        logger.info(amazonS3Client.getRegion().toString());
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }

    private void removeNewFile(File targetFile) throws NoSuchFileException, DirectoryNotEmptyException,IOException{
        if(targetFile.delete()){
            log.info("파일이 삭제되었습니다.");
        } else{
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        logger.info(convertFile.toString());
        if(convertFile.createNewFile()){
            FileOutputStream fos = new FileOutputStream(convertFile);
            try{
                fos.write(file.getBytes());
            }catch(Exception e){
                Writer writer= new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                String s = writer.toString();
                logger.info(s);
            }finally {
                fos.close();
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

}
