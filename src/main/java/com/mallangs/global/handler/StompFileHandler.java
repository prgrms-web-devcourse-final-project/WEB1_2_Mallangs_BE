//package com.mallangs.global.handler;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.stereotype.Controller;
//
//import java.io.ByteArrayInputStream;
//
//@Controller
//public class StompFileHandler {
//
//    private final AmazonS3 amazonS3;
//    private final String bucketName = "your-bucket-name";
//
//    public StompFileHandler(AmazonS3 amazonS3) {
//        this.amazonS3 = amazonS3;
//    }
//
//    @MessageMapping("/upload")
//    public void handleBinaryMessage(byte[] fileData) {
//        try {
//            // S3 저장
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(fileData.length);
//
//            // S3에 업로드
//            String fileName = "uploaded-image-" + System.currentTimeMillis() + ".png";
//            amazonS3.putObject(bucketName, fileName, inputStream, metadata);
//
//            System.out.println("File uploaded successfully to S3: " + fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
