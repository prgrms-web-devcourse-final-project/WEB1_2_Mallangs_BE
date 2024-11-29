package com.mallangs.domain.member.controller;

import com.mallangs.domain.member.util.UploadUtil;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.mallangs.global.exception.ErrorCode.UNSUPPORTED_FILE_TYPE;


@Log4j2
@RestController
//@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RequestMapping("/api/member/file")
public class MemberFileController {

    private final UploadUtil uploadUtil;

    @DeleteMapping("/{profileImage}")
    public ResponseEntity<?> fileDelete(@PathVariable String profileImage) {
        log.info("--- fileDelete() : " + profileImage);
        uploadUtil.deleteFile(profileImage);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    @Operation(summary = "프로필 사진 등록", description = "회원 가입 시 프로필 사진을 등록할 때 사용하는 API")
    public ResponseEntity<String> uploadFile(@RequestParam("profileImage") MultipartFile profileImage) {
        log.info("--- uploadFile() : " + profileImage);

        //업로드 파일이 없는 경우
        if (profileImage == null) {
            throw new MallangsCustomException(ErrorCode.NOT_FOUND_PROFILE_IMAGE);   //UploadNotSupportedException 예외 발생 시키기 - 메시지 : 업로드 파일이 없습니다.
        }
        log.info("------------------------------");
        log.info("name : " + profileImage.getName());
        log.info("origin name : " + profileImage.getOriginalFilename());
        log.info("type : " + profileImage.getContentType());

        checkFileExt(Objects.requireNonNull(profileImage.getOriginalFilename()));
        return ResponseEntity.ok(uploadUtil.upload(profileImage));
    }

    //업로드 파일 확장자 체크
    public void checkFileExt(String imageName) throws MallangsCustomException {
        String ext = imageName.substring(imageName.lastIndexOf(".") + 1);    //2.이미지 파일 확장자
        String regExp = "^(jpg|jpeg|JPG|JPEG|png|PNG|gif|GIF|bmp|BMP)";

        //업로드 파일의 확장자가 위에 해당하지 않는 경우 예외
        if (!ext.matches(regExp)) {
            throw new MallangsCustomException(UNSUPPORTED_FILE_TYPE);
        }
    }
}
