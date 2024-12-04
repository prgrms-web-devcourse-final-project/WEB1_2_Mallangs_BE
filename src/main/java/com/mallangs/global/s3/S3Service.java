package com.mallangs.global.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mallangs.global.exception.ErrorCode;
import com.mallangs.global.exception.MallangsCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * S3에 이미지 업로드 하기
     */
        public String uploadFile(MultipartFile file, String storedFilename) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            try {
                amazonS3.putObject(bucket, storedFilename, file.getInputStream(), metadata);
                return amazonS3.getUrl(bucket, storedFilename).toString();
            } catch (IOException e) {
                throw new MallangsCustomException(ErrorCode.IMAGE_PROCESSING_ERROR);
            }
        }

        public void deleteFile(String storedFilename) {
            amazonS3.deleteObject(bucket, storedFilename);
        }
}