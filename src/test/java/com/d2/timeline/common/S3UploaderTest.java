package com.d2.timeline.common;

import com.d2.timeline.domain.common.S3Uploader;
import com.d2.timeline.service.MockTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class S3UploaderTest {

    @Autowired
    S3Uploader s3Uploader;

    @Test
    public void convert_테스트() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fileDir = "/tmp";
        String fileName = "import_target.xls";
        Optional<File> rst = null;
        // given
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName.getBytes());
        Method method = s3Uploader.getClass().getDeclaredMethod("convert", MultipartFile.class);
        method.setAccessible(true);

        //when
        rst = (Optional)method.invoke(method, multipartFile);
        System.out.println(rst.get().getName());

    }


}
