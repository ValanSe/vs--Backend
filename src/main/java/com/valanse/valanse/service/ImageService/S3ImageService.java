package com.valanse.valanse.service.ImageService;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.valanse.valanse.exception.S3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ImageService {

    private final AmazonS3 amazonS3;
//    private Set<String> uploadedFileNames = new HashSet<>();
//    private Set<Long> uploadedFileSizes = new HashSet<>();

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    @Value("${cloud.aws.cloud-front}")
    private String cloudFront;


    // 단일 이미지 저장
    public String uploadImage(MultipartFile file) {

        String randomFilename = generateRandomFilename(file);
        log.info("File upload started: " + randomFilename);

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
            log.info("File upload completed: " + randomFilename);

            return cloudFront + randomFilename;


        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new S3Exception("Amazon S3 error while uploading file", e);
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new S3Exception("AWS SDK client error while uploading file", e);
        } catch (IOException e) {
            log.error("IO error while uploading file: " + e.getMessage());
            throw new S3Exception("IO error while uploading file", e);
        }
    }

    // 파일 삭제
    public void deleteImage(String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket)) {
            throw new S3Exception("Invalid bucket name in URL");
        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new S3Exception("File does not exist in S3");
        }

        try {
            amazonS3.deleteObject(bucket, objectKey);
            log.info("File delete complete: " + objectKey);
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while deleting file: " + e.getMessage());
            throw new S3Exception("Amazon S3 error while deleting file", e);
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while deleting file: " + e.getMessage());
            throw new S3Exception("AWS SDK client error while deleting file", e);
        }
    }


//    // 요청에 중복되는 파일 여부 확인
//    // TODO 입력되는 사진이 동일한 파일이라면 하나만 저장하도록 처리하는게 좋다..
//    private boolean isDuplicate(MultipartFile multipartFile) {
//        String fileName = multipartFile.getOriginalFilename();
//        Long fileSize = multipartFile.getSize();
//
//        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
//            return true;
//        }
//
//        uploadedFileNames.add(fileName);
//        uploadedFileSizes.add(fileSize);
//
//        return false;
//    }
//
//    private void clear() {
//        uploadedFileNames.clear();
//        uploadedFileSizes.clear();
//    }

    // 파일명 생성 (파일명 중복 방지)
    // 랜덤파일명 생성 (파일명 중복 방지)
    private String generateRandomFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        String randomFilename = UUID.randomUUID() + "." + fileExtension;
        return randomFilename;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        try {
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg");

            if (!allowedExtensions.contains(fileExtension)) {
                log.error("Invalid file extension: " + fileExtension);
                throw new S3Exception("Invalid file extension: " + fileExtension);
            }

            log.info("File extension is valid: " + fileExtension);
            return fileExtension;

        } catch (StringIndexOutOfBoundsException e) {
            log.error("Error while extracting file extension: " + e.getMessage());
            throw new S3Exception("Error while extracting file extension", e);
        }
    }
}
