package com.valanse.valanse.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


// TODO interface 구현 필요

@Service
@Slf4j
public class FileUploadUtil {

    @Value("${spring.path.image-upload}")
    private String imageUploadPath;

    public String saveFile(MultipartFile file) throws IOException {
        try {
            // 고유한 파일 이름 생성
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadDir = new File(imageUploadPath);

            // 디렉터리가 존재하는지 확인하고 없으면 생성
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 파일을 저장할 전체 경로 생성
            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);

            // 경로 반환
            return dest.getAbsolutePath();
        } catch (Exception e) {
            log.error("Error occurred while saving file", e);
            throw e;
        }

    }
}
