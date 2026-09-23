package com.karainc.dailytalk.domain.utils.s3;


import com.karainc.dailytalk.domain.utils.s3.dto.NDto;
import com.karainc.dailytalk.domain.utils.s3.dto.redto;
import com.karainc.dailytalk.domain.utils.s3.dto.testDto;
import com.karainc.dailytalk.domain.utils.s3.service.S3UploadService;
import com.karainc.dailytalk.domain.utils.smtp.controller.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class s3Controller {

    private final S3UploadService s3UploadService;

    // 이미지 테스트용이라 테스트할때만 필요함
    @PostMapping
    public ResultDto uploadImage(@RequestPart MultipartFile newImage,
                                 @RequestPart String image){
        System.out.println("서버로 뭐들어왔니?");
        System.out.println(newImage.getOriginalFilename());
        String url;
        if(newImage.isEmpty()){
            url ="온거 없음";
        }else {
            url=s3UploadService.uploadImage(newImage,newImage.getOriginalFilename());
        }
        ResultDto resultDto = new ResultDto();
        resultDto.setStatus("200");
        resultDto.setMessage("업로드 성공");
        resultDto.setData(url);
        return resultDto;
    }

    @PostMapping("/test")
    public String uploadImage1(@RequestPart MultipartFile newImage,
                               @RequestPart testDto testDto){
        return newImage.getOriginalFilename()+testDto.getCome();
    }

    @PostMapping("/test-folder")
    public String uploadtest(@RequestParam(value = "image" ,required = false) MultipartFile image,
                             @RequestParam(value = "path") String path){
        String url;
        String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));

        // 오늘 날짜를 포함한 새로운 파일 이름 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String newFileName = dateFormat.format(new Date()) + fileExtension;

        url = s3UploadService.uploadImage(image,path+"/"+newFileName);
        return url;
    }

    @DeleteMapping("/d")
    public void testDelete(@RequestParam String key){
        s3UploadService.deleteFolder(key);
    }



}
