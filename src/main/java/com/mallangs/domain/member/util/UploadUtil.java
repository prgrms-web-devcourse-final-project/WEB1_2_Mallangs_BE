package com.mallangs.domain.member.util;

import com.mallangs.domain.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class UploadUtil {

    @Value("${edu.example.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempDir = new File(uploadPath);
        //업로드 디렉토리 생성
        if (!tempDir.exists()) {
            log.info("--- tempDir : " + tempDir);
            tempDir.mkdir();
        }

        uploadPath = tempDir.getAbsolutePath();
        log.info("--- getPath() : " + tempDir.getPath());
        log.info("--- uploadPath : " + uploadPath);
        log.info("-------------------------------------");
    }

    //File 업로드
    public String upload(MultipartFile file) {

        String uuid = UUID.randomUUID().toString();
        String saveFilename = uuid + "_" + file.getOriginalFilename();
        String savePath = uploadPath + File.separator;

        try {
            //파일 업로드
            file.transferTo(new File(savePath + saveFilename));

            //썸네일 파일 생성
            Thumbnails.of(new File(savePath + saveFilename))
                    .size(150, 150)
                    .toFile(savePath + "s_" + saveFilename);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "/uploads/" + saveFilename;
    }

    //업로드 파일 삭제
    public void deleteFile(String filename) {
        File file = new File(uploadPath + File.separator + filename);
        File thumbFile = new File(uploadPath + File.separator + "s_" + filename);

        try {
            if (file.exists()) file.delete();
            if (thumbFile.exists()) thumbFile.delete();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
