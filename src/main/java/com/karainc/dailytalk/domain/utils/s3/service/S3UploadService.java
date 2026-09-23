package com.karainc.dailytalk.domain.utils.s3.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${s3.base-url}")
    private String s3baseUrl;

    private final S3Client s3Client;

    public String uploadImage(MultipartFile imageFile, String key) {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
        try {
            RequestBody requestBody = RequestBody.fromInputStream(imageFile.getInputStream(), imageFile.getSize());
            PutObjectResponse response = s3Client.putObject(request, requestBody);
            String imageUrl = generateImageUrl(key);
            return imageUrl;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 전송 실패", e);
        }
    }

    private String generateImageUrl(String key) {
        // S3에서 업로드된 파일의 주소 생성
        String imageUrl = s3baseUrl + key;
        return imageUrl;
    }


    public void deleteFolder( String folderKey) {
        // 폴더 내의 모든 객체를 삭제

        try{
            listObjects(bucketName, folderKey).forEach(objectKey -> {
                s3Client.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(objectKey)
                        .build());
                System.out.println("Deleted object: " + objectKey);
            });

            // 폴더 삭제
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(folderKey)
                    .build());
            System.out.println("Deleted folder: " + folderKey);
        }catch (Exception e){
            return;
        }
    }

    private List<String> listObjects(String bucketName, String folderKey) {
        // 폴더 내의 객체 키 목록을 가져오기
        ListObjectsV2Response listObjectsResponse = s3Client.listObjectsV2(ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(folderKey)
                .build());

        return listObjectsResponse.contents().stream()
                .map(s3Object -> s3Object.key())
                .collect(Collectors.toList());
    }





}
